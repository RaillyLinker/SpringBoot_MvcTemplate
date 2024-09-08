package com.raillylinker.springboot_mvc_template.controllers.c11_service1_tk_v1_mongoDbTest

import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.mongo_db_configs.Mdb1MainConfig
import com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mdb1_main.documents.Mdb1_Test
import com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mdb1_main.repositories.Mdb1_Test_Repository
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class C11Service1TkV1MongoDbTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,
    private val md1TestCollectionRepository: Mdb1_Test_Repository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @CustomTransactional([Mdb1MainConfig.TRANSACTION_NAME]) // ReplicaSet 환경이 아니면 에러가 납니다.
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: C11Service1TkV1MongoDbTestController.Api1InputVo
    ): C11Service1TkV1MongoDbTestController.Api1OutputVo? {
        val resultCollection = md1TestCollectionRepository.save(
            Mdb1_Test(
                inputVo.content,
                (0..99999999).random(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C11Service1TkV1MongoDbTestController.Api1OutputVo(
            resultCollection.uid!!.toString(),
            resultCollection.content,
            resultCollection.randomNum,
            resultCollection.rowCreateDate.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            resultCollection.rowUpdateDate.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        )
    }

    ////
    fun api2(httpServletResponse: HttpServletResponse) {
        md1TestCollectionRepository.deleteAll()

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }

    ////
    fun api3(httpServletResponse: HttpServletResponse, id: String) {
        md1TestCollectionRepository.deleteById(id)

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }

    ////
    fun api4(httpServletResponse: HttpServletResponse): C11Service1TkV1MongoDbTestController.Api4OutputVo? {
        val testCollectionList = md1TestCollectionRepository.findAll()

        val resultVoList: ArrayList<C11Service1TkV1MongoDbTestController.Api4OutputVo.TestEntityVo> = arrayListOf()

        for (testCollection in testCollectionList) {
            resultVoList.add(
                C11Service1TkV1MongoDbTestController.Api4OutputVo.TestEntityVo(
                    testCollection.uid!!.toString(),
                    testCollection.content,
                    testCollection.randomNum,
                    testCollection.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    testCollection.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C11Service1TkV1MongoDbTestController.Api4OutputVo(
            resultVoList
        )
    }


    @CustomTransactional([Mdb1MainConfig.TRANSACTION_NAME]) // ReplicaSet 환경이 아니면 에러가 납니다.
    fun api12(
        httpServletResponse: HttpServletResponse
    ) {
        md1TestCollectionRepository.save(
            Mdb1_Test(
                "test",
                (0..99999999).random(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

        throw Exception("Transaction Rollback Test!")

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    fun api13(
        httpServletResponse: HttpServletResponse
    ) {
        md1TestCollectionRepository.save(
            Mdb1_Test(
                "test",
                (0..99999999).random(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

        throw Exception("No Transaction Exception Test!")

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }
}