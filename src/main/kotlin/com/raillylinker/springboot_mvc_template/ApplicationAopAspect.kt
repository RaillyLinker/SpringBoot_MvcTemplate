package com.raillylinker.springboot_mvc_template

import com.raillylinker.springboot_mvc_template.annotations.CustomRedisTransactional
import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.RedisConfig
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.ApplicationContext
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.util.concurrent.TimeUnit

// [AOP]
@Component
@Aspect
class ApplicationAopAspect(
    private val applicationContext: ApplicationContext,
    private val redisConfig: RedisConfig
) {
    companion object {
        // DB 트랜젝션용 어노테이션인 CustomTransactional 파일의 프로젝트 경로
        const val TRANSACTION_ANNOTATION_PATH =
            "@annotation(${ApplicationConstants.PACKAGE_NAME}.annotations.CustomTransactional)"

        // Redis 트랜젝션용 어노테이션인 CustomRedisTransactional 파일의 프로젝트 경로
        const val REDIS_TRANSACTION_ANNOTATION_PATH =
            "@annotation(${ApplicationConstants.PACKAGE_NAME}.annotations.CustomRedisTransactional)"
    }


    // ---------------------------------------------------------------------------------------------
    // <AOP 작성 공간>
    // (@CustomTransactional 를 입력한 함수 실행 전후에 JPA 트랜젝션 적용)
    @Around(TRANSACTION_ANNOTATION_PATH)
    fun aroundTransactionAnnotationFunction(joinPoint: ProceedingJoinPoint): Any? {
        val proceed: Any?

        // transactionManager and transactionStatus 리스트
        val transactionManagerAndTransactionStatusList =
            ArrayList<Pair<PlatformTransactionManager, TransactionStatus>>()

        try {
            // annotation 에 설정된 transaction 순차 실행 및 저장
            for (transactionManagerBeanName in ((joinPoint.signature as MethodSignature).method).getAnnotation(
                CustomTransactional::class.java
            ).transactionManagerBeanNameList) {
                // annotation 에 저장된 transactionManager Bean 이름으로 Bean 객체 가져오기
                val platformTransactionManager =
                    applicationContext.getBean(transactionManagerBeanName) as PlatformTransactionManager

                // transaction 시작 및 정보 저장
                transactionManagerAndTransactionStatusList.add(
                    Pair(
                        platformTransactionManager,
                        platformTransactionManager.getTransaction(DefaultTransactionDefinition())
                    )
                )
            }

            //// 함수 실행 전
            proceed = joinPoint.proceed() // 함수 실행
            //// 함수 실행 후

            // annotation 에 설정된 transaction commit 역순 실행 및 저장
            for (transactionManagerIdx in transactionManagerAndTransactionStatusList.size - 1 downTo 0) {
                val transactionManager = transactionManagerAndTransactionStatusList[transactionManagerIdx]
                transactionManager.first.commit(transactionManager.second)
            }
        } catch (e: Exception) {
            // annotation 에 설정된 transaction rollback 역순 실행 및 저장
            for (transactionManagerIdx in transactionManagerAndTransactionStatusList.size - 1 downTo 0) {
                val transactionManager = transactionManagerAndTransactionStatusList[transactionManagerIdx]
                transactionManager.first.rollback(transactionManager.second)
            }
            throw e
        }

        return proceed // 결과 리턴
    }


    ////
    // (@CustomRedisTransactional 를 입력한 함수 실행 전후에 JPA 트랜젝션 적용)
    @Around(REDIS_TRANSACTION_ANNOTATION_PATH)
    fun aroundRedisTransactionAnnotationFunction(joinPoint: ProceedingJoinPoint): Any? {
        val proceed: Any?

        // 백업 데이터
        val backUpTableVoList: ArrayList<RedisTableVo> = arrayListOf()

        try {
            // 어노테이션 파라미터 가져오기
            val redisTemplateBeanNameAndTableNameList =
                ((joinPoint.signature as MethodSignature).method).getAnnotation(
                    CustomRedisTransactional::class.java
                ).redisTemplateBeanNameAndTableNameList

            // annotation 에 설정된 table 내의 모든 정보 백업 작업
            for (redisTemplateBeanNameAndTableName in redisTemplateBeanNameAndTableNameList) {
                // redisTemplate 객체와 redis table 이름을 분리
                val redisTemplateBeanNameAndTableNameSplit = redisTemplateBeanNameAndTableName.split(":")
                // redisTemplate 객체
                val redisTemplate = redisConfig.redisTemplatesMap[redisTemplateBeanNameAndTableNameSplit[0].trim()]!!
                // redis table 이름
                val redisTableName = redisTemplateBeanNameAndTableNameSplit[1].trim()

                // redis Table 이름으로 시작되는 Key 를 모두 찾아서 백업
                val resultList = ArrayList<RedisTableVo.RedisKeyVo>()
                val keySet: Set<String> = redisTemplate.keys("${redisTableName}:*")
                for (innerKey in keySet) {
                    val redisValue = redisTemplate.opsForValue()[innerKey] ?: continue

                    resultList.add(
                        RedisTableVo.RedisKeyVo(
                            innerKey,
                            redisValue,
                            redisTemplate.getExpire(innerKey, TimeUnit.MILLISECONDS) // 남은 만료시간
                        )
                    )
                }

                backUpTableVoList.add(
                    RedisTableVo(
                        redisTemplate,
                        redisTableName,
                        resultList
                    )
                )
            }

            //// 함수 실행 전
            proceed = joinPoint.proceed() // 함수 실행
            //// 함수 실행 후
        } catch (e: Exception) {
            // Redis Table 데이터 복원하기
            for (backUpTableVo in backUpTableVoList) {
                // backup redis Table 이름으로 시작되는 Key 를 모두 찾아서 삭제
                val keySet: Set<String> = backUpTableVo.redisTemplate.keys("${backUpTableVo.tableName}:*")
                for (innerKey in keySet) {
                    // Key 삭제
                    backUpTableVo.redisTemplate.delete(innerKey)
                }

                // backup redis Table 정보 복원
                for (backUpTableRedisKey in backUpTableVo.redisKeyVoList) {
                    // Redis Value 저장
                    backUpTableVo.redisTemplate.opsForValue()[backUpTableRedisKey.innerKey] = backUpTableRedisKey.value
                    // Redis Key 에 대한 만료시간 설정
                    backUpTableVo.redisTemplate.expire(
                        backUpTableRedisKey.innerKey,
                        backUpTableRedisKey.expireTimeMs,
                        TimeUnit.MILLISECONDS
                    )
                }
            }
            throw e
        }

        return proceed // 결과 리턴
    }

    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class RedisTableVo(
        val redisTemplate: RedisTemplate<String, Any>,
        val tableName: String,
        val redisKeyVoList: ArrayList<RedisKeyVo>
    ) {
        data class RedisKeyVo(
            val innerKey: String,
            val value: Any,
            val expireTimeMs: Long
        )
    }
}