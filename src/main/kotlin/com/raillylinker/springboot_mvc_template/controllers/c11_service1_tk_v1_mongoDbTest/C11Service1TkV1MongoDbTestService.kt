package com.raillylinker.springboot_mvc_template.controllers.c11_service1_tk_v1_mongoDbTest

import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.database_configs.Database1Config
import com.raillylinker.springboot_mvc_template.configurations.mongo_db_configs.MongoDb1Config
import com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mongoDb1.collections.TestCollection
import com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mongoDb1.repositories.TestCollectionRepository
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
    private val testCollectionRepository: TestCollectionRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @CustomTransactional([MongoDb1Config.TRANSACTION_NAME]) // ReplicaSet 환경이 아니면 에러가 납니다.
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: C11Service1TkV1MongoDbTestController.Api1InputVo
    ): C11Service1TkV1MongoDbTestController.Api1OutputVo? {
        val resultCollection = testCollectionRepository.save(
            TestCollection(
                inputVo.content,
                (0..99999999).random(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

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
        testCollectionRepository.deleteAll()

        httpServletResponse.status = HttpStatus.OK.value()
    }

    ////
    fun api3(httpServletResponse: HttpServletResponse, id: String) {
        testCollectionRepository.deleteById(id)

        httpServletResponse.status = HttpStatus.OK.value()
    }

    ////
    fun api4(httpServletResponse: HttpServletResponse): C11Service1TkV1MongoDbTestController.Api4OutputVo? {
        val testCollectionList = testCollectionRepository.findAll()

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

        httpServletResponse.status = HttpStatus.OK.value()
        return C11Service1TkV1MongoDbTestController.Api4OutputVo(
            resultVoList
        )
    }
}