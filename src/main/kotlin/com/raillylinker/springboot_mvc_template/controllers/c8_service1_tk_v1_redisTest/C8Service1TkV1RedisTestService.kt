package com.raillylinker.springboot_mvc_template.controllers.c8_service1_tk_v1_redisTest

import com.raillylinker.springboot_mvc_template.data_sources.memory_redis.redis1_main.Redis1_Test
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

/*
    Redis 는 주로 캐싱에 사용됩니다.
    이러한 특징에 기반하여, 본 프로젝트에서는 Redis 를 쉽고 편하게 사용하기 위하여 Key-Map 형식으로 래핑하여 사용하고 있으며,
    인 메모리 데이터 구조 저장소인 Redis 의 성능을 해치지 않기 위하여, Database 와는 달리 트랜젝션 처리를 따로 하지 않습니다.
    (Redis 는 애초에 고성능, 단순성을 위해 설계되었고, 롤백 기능을 지원하지 않으므로 일반적으로는 어플리케이션 레벨에서 처리합니다.)
    고로 Redis 에 값을 입력/삭제/수정하는 로직은, API 의 별도 다른 알고리즘이 모두 실행된 이후, "코드의 끝자락에서 한꺼번에 변경"하도록 처리하세요.
    그럼에도 트랜젝션 기능이 필요하다고 한다면,
    비동기 실행을 고려하여 Semaphore 등으로 락을 건 후, 기존 데이터를 백업한 후, 에러가 일어나면 복원하는 방식을 사용하면 됩니다.
 */
@Service
class C8Service1TkV1RedisTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val redis1Test: Redis1_Test
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1InsertRedisKeyValueTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C8Service1TkV1RedisTestController.Api1InsertRedisKeyValueTestInputVo
    ) {
        redis1Test.saveKeyValue(
            inputVo.key,
            Redis1_Test.ValueVo(
                inputVo.content,
                Redis1_Test.ValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.ValueVo.InnerVo("testObject1", false),
                    Redis1_Test.ValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api2SelectRedisValueSample(
        httpServletResponse: HttpServletResponse,
        key: String
    ): C8Service1TkV1RedisTestController.Api2SelectRedisValueSampleOutputVo? {
        // 전체 조회 테스트
        val keyValue = redis1Test.findKeyValue(key)

        if (keyValue == null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C8Service1TkV1RedisTestController.Api2SelectRedisValueSampleOutputVo(
            Redis1_Test.MAP_NAME,
            keyValue.key,
            keyValue.value.content,
            keyValue.expireTimeMs
        )
    }


    ////
    fun api3SelectAllRedisKeyValueSample(httpServletResponse: HttpServletResponse): C8Service1TkV1RedisTestController.Api3SelectAllRedisKeyValueSampleOutputVo? {
        // 전체 조회 테스트
        val keyValueList = redis1Test.findAllKeyValues()

        val testEntityListVoList =
            ArrayList<C8Service1TkV1RedisTestController.Api3SelectAllRedisKeyValueSampleOutputVo.KeyValueVo>()
        for (keyValue in keyValueList) {
            testEntityListVoList.add(
                C8Service1TkV1RedisTestController.Api3SelectAllRedisKeyValueSampleOutputVo.KeyValueVo(
                    keyValue.key,
                    keyValue.value.content,
                    keyValue.expireTimeMs
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C8Service1TkV1RedisTestController.Api3SelectAllRedisKeyValueSampleOutputVo(
            Redis1_Test.MAP_NAME,
            testEntityListVoList
        )
    }


    ////
    fun api4DeleteRedisKeySample(httpServletResponse: HttpServletResponse, key: String) {
        val keyValue = redis1Test.findKeyValue(key)

        if (keyValue == null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        redis1Test.deleteKeyValue(key)

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api5DeleteAllRedisKeySample(httpServletResponse: HttpServletResponse) {
        redis1Test.deleteAllKeyValues()

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api6RedisTransactionTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C8Service1TkV1RedisTestController.Api6RedisTransactionTestInputVo
    ) {
        // 트랜젝션 처리 = 기존 데이터 백업 후 에러 발생시 복구(비동기 처리도 필요합니다.)
        val redisBackup = redis1Test.findKeyValue(inputVo.key)

        try {
            redis1Test.saveKeyValue(
                inputVo.key,
                Redis1_Test.ValueVo(
                    inputVo.content,
                    Redis1_Test.ValueVo.InnerVo("testObject", true),
                    arrayListOf(
                        Redis1_Test.ValueVo.InnerVo("testObject1", false),
                        Redis1_Test.ValueVo.InnerVo("testObject2", true)
                    )
                ),
                inputVo.expirationMs
            )
            throw RuntimeException("Test Exception for Redis Transaction Test")
        } catch (e: Exception) {
            redis1Test.deleteKeyValue(inputVo.key)
            if (redisBackup != null) {
                redis1Test.saveKeyValue(redisBackup.key, redisBackup.value, redisBackup.expireTimeMs)
            }
            throw e
        }
    }


    ////
    fun api7RedisNonTransactionTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C8Service1TkV1RedisTestController.Api7RedisNonTransactionTestInputVo
    ) {
        redis1Test.saveKeyValue(
            inputVo.key,
            Redis1_Test.ValueVo(
                inputVo.content,
                Redis1_Test.ValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.ValueVo.InnerVo("testObject1", false),
                    Redis1_Test.ValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )
        throw RuntimeException("Test Exception for Redis Non Transaction Test")
    }
}