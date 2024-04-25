package com.raillylinker.springboot_mvc_template.controllers.c8_service1_tk_v1_redisTest

import com.raillylinker.springboot_mvc_template.annotations.CustomRedisTransactional
import com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis1.repositories.Redis1_TestRepository
import com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis1.tables.Redis1_Test
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class C8Service1TkV1RedisTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val redis1TestRepository: Redis1_TestRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @CustomRedisTransactional([Redis1_Test.TRANSACTION_NAME])
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: C8Service1TkV1RedisTestController.Api1InputVo
    ) {
        redis1TestRepository.saveKeyValue(
            inputVo.key,
            Redis1_Test(
                inputVo.content,
                Redis1_Test.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.InnerVo("testObject1", false),
                    Redis1_Test.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse, key: String): C8Service1TkV1RedisTestController.Api2OutputVo? {
        // 전체 조회 테스트
        val keyValue = redis1TestRepository.findKeyValue(key)

        if (keyValue == null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C8Service1TkV1RedisTestController.Api2OutputVo(
            Redis1_Test.TABLE_NAME,
            keyValue.key,
            keyValue.value.content,
            keyValue.expireTimeMs
        )
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse): C8Service1TkV1RedisTestController.Api3OutputVo? {
        // 전체 조회 테스트
        val keyValueList = redis1TestRepository.findAllKeyValues()

        val testEntityListVoList = ArrayList<C8Service1TkV1RedisTestController.Api3OutputVo.KeyValueVo>()
        for (keyValue in keyValueList) {
            testEntityListVoList.add(
                C8Service1TkV1RedisTestController.Api3OutputVo.KeyValueVo(
                    keyValue.key,
                    keyValue.value.content,
                    keyValue.expireTimeMs
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C8Service1TkV1RedisTestController.Api3OutputVo(
            Redis1_Test.TABLE_NAME,
            testEntityListVoList
        )
    }


    ////
    @CustomRedisTransactional([Redis1_Test.TRANSACTION_NAME])
    fun api4(httpServletResponse: HttpServletResponse, key: String) {
        val keyValue = redis1TestRepository.findKeyValue(key)

        if (keyValue == null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        redis1TestRepository.deleteKeyValue(key)

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    @CustomRedisTransactional([Redis1_Test.TRANSACTION_NAME])
    fun api5(httpServletResponse: HttpServletResponse) {
        redis1TestRepository.deleteAllKeyValues()

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    @CustomRedisTransactional([Redis1_Test.TRANSACTION_NAME])
    fun api6(httpServletResponse: HttpServletResponse, inputVo: C8Service1TkV1RedisTestController.Api6InputVo) {
        redis1TestRepository.saveKeyValue(
            inputVo.key,
            Redis1_Test(
                inputVo.content,
                Redis1_Test.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.InnerVo("testObject1", false),
                    Redis1_Test.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )
        throw RuntimeException("Test Exception for Redis Transaction Test")
    }


    ////
    fun api7(httpServletResponse: HttpServletResponse, inputVo: C8Service1TkV1RedisTestController.Api7InputVo) {
        redis1TestRepository.saveKeyValue(
            inputVo.key,
            Redis1_Test(
                inputVo.content,
                Redis1_Test.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.InnerVo("testObject1", false),
                    Redis1_Test.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )
        throw RuntimeException("Test Exception for Redis Non Transaction Test")
    }
}