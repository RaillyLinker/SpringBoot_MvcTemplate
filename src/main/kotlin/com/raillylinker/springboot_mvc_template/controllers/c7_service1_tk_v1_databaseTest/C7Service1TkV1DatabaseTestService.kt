package com.raillylinker.springboot_mvc_template.controllers.c7_service1_tk_v1_databaseTest

import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.database_configs.Database1Config
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_NativeRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Template_FkTestOneToManyChildRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Template_FkTestParentRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Template_TestsRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Template_TestData
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class C7Service1TkV1DatabaseTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // (Database1 Repository)
    private val database1NativeRepository: Database1_NativeRepository,
    private val database1TemplateTestRepository: Database1_Template_TestsRepository,
    private val database1TemplateFkTestParentRepository: Database1_Template_FkTestParentRepository,
    private val database1TemplateFkTestOneToManyChildRepository: Database1_Template_FkTestOneToManyChildRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: C7Service1TkV1DatabaseTestController.Api1InputVo
    ): C7Service1TkV1DatabaseTestController.Api1OutputVo? {
        val result = database1TemplateTestRepository.save(
            Database1_Template_TestData(inputVo.content, (0..99999999).random())
        )

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api1OutputVo(
            result.uid!!,
            result.content,
            result.randomNum,
            result.rowCreateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
            result.rowUpdateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api2(httpServletResponse: HttpServletResponse) {
        database1TemplateTestRepository.deleteAll()

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api3(httpServletResponse: HttpServletResponse, index: Long) {
        database1TemplateTestRepository.deleteById(index)

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api4OutputVo? {
        val resultEntityList = database1TemplateTestRepository.findAll()

        val testEntityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api4OutputVo.TestEntityVo>()
        for (resultEntity in resultEntityList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api4OutputVo.TestEntityVo(
                    resultEntity.uid!!,
                    resultEntity.content,
                    resultEntity.randomNum,
                    resultEntity.rowCreateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    resultEntity.rowUpdateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api4OutputVo(
            testEntityVoList
        )
    }


    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        num: Int
    ): C7Service1TkV1DatabaseTestController.Api5OutputVo? {
        val foundEntityList = database1NativeRepository.forC7N5(num)

        val testEntityVoList =
            ArrayList<C7Service1TkV1DatabaseTestController.Api5OutputVo.TestEntityVo>()

        for (entity in foundEntityList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api5OutputVo.TestEntityVo(
                    entity.uid,
                    entity.content,
                    entity.randomNum,
                    entity.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    entity.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    entity.distance
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api5OutputVo(
            testEntityVoList
        )
    }


    ////
    fun api6(
        httpServletResponse: HttpServletResponse,
        dateString: String
    ): C7Service1TkV1DatabaseTestController.Api6OutputVo? {
        val foundEntityList = database1NativeRepository.forC7N6(dateString)

        val testEntityVoList =
            ArrayList<C7Service1TkV1DatabaseTestController.Api6OutputVo.TestEntityVo>()

        for (entity in foundEntityList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api6OutputVo.TestEntityVo(
                    entity.uid,
                    entity.content,
                    entity.randomNum,
                    entity.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    entity.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    entity.timeDiffSec
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api6OutputVo(
            testEntityVoList
        )
    }


    ////
    fun api7(
        httpServletResponse: HttpServletResponse,
        page: Int,
        pageElementsCount: Int
    ): C7Service1TkV1DatabaseTestController.Api7OutputVo? {
        val pageable: Pageable = PageRequest.of(page - 1, pageElementsCount)
        val entityList = database1TemplateTestRepository.findAllByRowDeleteDateStrOrderByRowCreateDate(
            "-",
            pageable
        )

        val testEntityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api7OutputVo.TestEntityVo>()
        for (entity in entityList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api7OutputVo.TestEntityVo(
                    entity.uid!!,
                    entity.content,
                    entity.randomNum,
                    entity.rowCreateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    entity.rowUpdateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api7OutputVo(
            entityList.totalElements,
            testEntityVoList
        )
    }


    ////
    fun api8(
        httpServletResponse: HttpServletResponse,
        page: Int,
        pageElementsCount: Int,
        num: Int
    ): C7Service1TkV1DatabaseTestController.Api8OutputVo? {
        val pageable: Pageable = PageRequest.of(page - 1, pageElementsCount)
        val voList = database1NativeRepository.forC7N8(
            num,
            pageable
        )

        val testEntityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api8OutputVo.TestEntityVo>()
        for (vo in voList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api8OutputVo.TestEntityVo(
                    vo.uid,
                    vo.content,
                    vo.randomNum,
                    vo.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    vo.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    vo.distance
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api8OutputVo(
            voList.totalElements,
            testEntityVoList
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api9(
        httpServletResponse: HttpServletResponse,
        testTableUid: Long,
        inputVo: C7Service1TkV1DatabaseTestController.Api9InputVo
    ): C7Service1TkV1DatabaseTestController.Api9OutputVo? {
        val oldEntity = database1TemplateTestRepository.findById(testTableUid)

        if (oldEntity.isEmpty || oldEntity.get().rowDeleteDateStr != "-") {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val testObject = oldEntity.get()
        testObject.content = inputVo.content

        val result = database1TemplateTestRepository.save(
            testObject
        )

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api9OutputVo(
            result.uid!!,
            result.content,
            result.randomNum,
            result.rowCreateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
            result.rowUpdateDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api10(
        httpServletResponse: HttpServletResponse,
        testTableUid: Long,
        inputVo: C7Service1TkV1DatabaseTestController.Api10InputVo
    ) {
        // !! 아래는 네이티브 쿼리로 수정하는 예시를 보인 것으로,
        // 이 경우에는 @UpdateTimestamp, @Version 기능이 자동 적용 되지 않습니다.
        // 고로 수정문은 jpa 를 사용하길 권장합니다. !!
        val testEntity = database1TemplateTestRepository.findById(testTableUid)

        if (testEntity.isEmpty || testEntity.get().rowDeleteDateStr != "-") {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            // 트랜젝션 커밋
            return
        }

        database1NativeRepository.forC7N10(testTableUid, inputVo.content)

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api11(
        httpServletResponse: HttpServletResponse,
        page: Int,
        pageElementsCount: Int,
        searchKeyword: String
    ): C7Service1TkV1DatabaseTestController.Api11OutputVo? {
        val pageable: Pageable = PageRequest.of(page - 1, pageElementsCount)
        val voList = database1NativeRepository.forC7N11(
            searchKeyword,
            pageable
        )

        val testEntityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api11OutputVo.TestEntityVo>()
        for (vo in voList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api11OutputVo.TestEntityVo(
                    vo.uid,
                    vo.content,
                    vo.randomNum,
                    vo.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    vo.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api11OutputVo(
            voList.totalElements,
            testEntityVoList
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api12(
        httpServletResponse: HttpServletResponse
    ) {
        database1TemplateTestRepository.save(
            Database1_Template_TestData("error test", (0..99999999).random())
        )

        throw Exception("Transaction Rollback Test!")
    }


    ////
    fun api13(httpServletResponse: HttpServletResponse) {
        database1TemplateTestRepository.save(
            Database1_Template_TestData("error test", (0..99999999).random())
        )

        throw Exception("No Transaction Exception Test!")
    }


    ////
    fun api14(
        httpServletResponse: HttpServletResponse,
        lastItemUid: Long?,
        pageElementsCount: Int,
        num: Int
    ): C7Service1TkV1DatabaseTestController.Api14OutputVo? {
        val voList = database1NativeRepository.forC7N14(
            lastItemUid ?: -1,
            pageElementsCount,
            num
        )

        val count = database1TemplateTestRepository.countByRowDeleteDateStr("-")

        val testEntityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api14OutputVo.TestEntityVo>()
        for (vo in voList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api14OutputVo.TestEntityVo(
                    vo.uid,
                    vo.content,
                    vo.randomNum,
                    vo.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    vo.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    vo.distance
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api14OutputVo(count, testEntityVoList)
    }


    ////
    fun api15(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api15OutputVo? {
        val count = database1TemplateTestRepository.countByRowDeleteDateStr("-")

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api15OutputVo(count)
    }


    ////
    fun api16(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api16OutputVo? {
        val count = database1NativeRepository.forC7N16()

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api16OutputVo(count)
    }


    ////
    fun api17(
        httpServletResponse: HttpServletResponse,
        testTableUid: Long
    ): C7Service1TkV1DatabaseTestController.Api17OutputVo? {
        val entity = database1NativeRepository.forC7N17(testTableUid)

        if (entity == null) {
            httpServletResponse.status = HttpStatus.OK.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api17OutputVo(
            entity.uid,
            entity.content,
            entity.randomNum,
            entity.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
            entity.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
        )
    }
}