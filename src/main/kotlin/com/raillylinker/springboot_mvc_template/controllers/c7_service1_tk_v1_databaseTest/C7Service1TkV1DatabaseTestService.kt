package com.raillylinker.springboot_mvc_template.controllers.c7_service1_tk_v1_databaseTest

import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.database_configs.Database0Config
import com.raillylinker.springboot_mvc_template.configurations.database_configs.Database1Config
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.repositories.Database0_Template_TestsRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.tables.Database0_Template_TestData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.*
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.*
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class C7Service1TkV1DatabaseTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // (Database Repository)
    private val database1NativeRepository: Database1_NativeRepository,
    private val database1TemplateTestRepository: Database1_Template_TestsRepository,
    private val database1TemplateFkTestParentRepository: Database1_Template_FkTestParentRepository,
    private val database1TemplateFkTestOneToManyChildRepository: Database1_Template_FkTestManyToOneChildRepository,
    private val database1TemplateLogicalDeleteUniqueDataRepository: Database1_Template_LogicalDeleteUniqueDataRepository,
    private val database1TemplateJustBooleanTestRepository: Database1_Template_JustBooleanTestRepository,

    private val database0TemplateTestRepository: Database0_Template_TestsRepository
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
            Database1_Template_TestData(
                inputVo.content,
                (0..99999999).random(),
                LocalDateTime.parse(inputVo.dateString, DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS"))
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api1OutputVo(
            result.uid!!,
            result.content,
            result.randomNum,
            result.testDatetime.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowCreateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowDeleteDateStr
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api2(httpServletResponse: HttpServletResponse, deleteLogically: Boolean) {
        if (deleteLogically) {
            val entityList = database1TemplateTestRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/")
            for (entity in entityList) {
                entity.rowDeleteDateStr =
                    LocalDateTime.now().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                database1TemplateTestRepository.save(entity)
            }
        } else {
            database1TemplateTestRepository.deleteAll()
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api3(httpServletResponse: HttpServletResponse, index: Long, deleteLogically: Boolean) {
        val entity = database1TemplateTestRepository.findByUidAndRowDeleteDateStr(index, "/")

        if (entity == null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (deleteLogically) {
            entity.rowDeleteDateStr =
                LocalDateTime.now().atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
            database1TemplateTestRepository.save(entity)
        } else {
            database1TemplateTestRepository.deleteById(index)
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api4OutputVo? {
        val resultEntityList =
            database1TemplateTestRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/")
        val entityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api4OutputVo.TestEntityVo>()
        for (resultEntity in resultEntityList) {
            entityVoList.add(
                C7Service1TkV1DatabaseTestController.Api4OutputVo.TestEntityVo(
                    resultEntity.uid!!,
                    resultEntity.content,
                    resultEntity.randomNum,
                    resultEntity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
                )
            )
        }

        val logicalDeleteEntityVoList =
            database1TemplateTestRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/")
        val logicalDeleteVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api4OutputVo.TestEntityVo>()
        for (resultEntity in logicalDeleteEntityVoList) {
            logicalDeleteVoList.add(
                C7Service1TkV1DatabaseTestController.Api4OutputVo.TestEntityVo(
                    resultEntity.uid!!,
                    resultEntity.content,
                    resultEntity.randomNum,
                    resultEntity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api4OutputVo(
            entityVoList,
            logicalDeleteVoList
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
                    entity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.distance
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
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
        val foundEntityList = database1NativeRepository.forC7N6(
            LocalDateTime.parse(
                dateString,
                DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS")
            )
        )

        val testEntityVoList =
            ArrayList<C7Service1TkV1DatabaseTestController.Api6OutputVo.TestEntityVo>()

        for (entity in foundEntityList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api6OutputVo.TestEntityVo(
                    entity.uid,
                    entity.content,
                    entity.randomNum,
                    entity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.timeDiffMicroSec
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
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
            "/",
            pageable
        )

        val testEntityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api7OutputVo.TestEntityVo>()
        for (entity in entityList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api7OutputVo.TestEntityVo(
                    entity.uid!!,
                    entity.content,
                    entity.randomNum,
                    entity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    entity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
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
                    vo.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.distance
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
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
        val oldEntity = database1TemplateTestRepository.findByUidAndRowDeleteDateStr(testTableUid, "/")

        if (oldEntity == null || oldEntity.rowDeleteDateStr != "/") {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        oldEntity.content = inputVo.content
        oldEntity.testDatetime =
            LocalDateTime.parse(inputVo.dateString, DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS"))

        val result = database1TemplateTestRepository.save(oldEntity)

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api9OutputVo(
            result.uid!!,
            result.content,
            result.randomNum,
            result.testDatetime.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowCreateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
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
        val testEntity = database1TemplateTestRepository.findByUidAndRowDeleteDateStr(testTableUid, "/")

        if (testEntity == null || testEntity.rowDeleteDateStr != "/") {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            // 트랜젝션 커밋
            return
        }

        database1NativeRepository.forC7N10(
            testTableUid,
            inputVo.content,
            LocalDateTime.parse(inputVo.dateString, DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS"))
        )

        httpServletResponse.setHeader("api-result-code", "")
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
                    vo.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
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
            Database1_Template_TestData(
                "error test",
                (0..99999999).random(),
                LocalDateTime.now()
            )
        )

        throw Exception("Transaction Rollback Test!")
    }


    ////
    fun api13(httpServletResponse: HttpServletResponse) {
        database1TemplateTestRepository.save(
            Database1_Template_TestData(
                "error test",
                (0..99999999).random(),
                LocalDateTime.now()
            )
        )

        throw Exception("No Transaction Exception Test!")
    }


    ////
    fun api14(
        httpServletResponse: HttpServletResponse,
        lastItemUid: Long?,
        pageElementsCount: Int
    ): C7Service1TkV1DatabaseTestController.Api14OutputVo? {
        // 중복 없는 페이징 쿼리를 사용합니다.
        val voList = database1NativeRepository.forC7N14(
            lastItemUid,
            pageElementsCount
        )

        // 전체 개수 카운팅은 따로 해주어야 합니다.
        val count = database1NativeRepository.forC7N14I1()

        val testEntityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api14OutputVo.TestEntityVo>()
        for (vo in voList) {
            testEntityVoList.add(
                C7Service1TkV1DatabaseTestController.Api14OutputVo.TestEntityVo(
                    vo.uid,
                    vo.content,
                    vo.randomNum,
                    vo.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api14OutputVo(count, testEntityVoList)
    }


    ////
    fun api15(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api15OutputVo? {
        val count = database1TemplateTestRepository.countByRowDeleteDateStr("/")

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api15OutputVo(count)
    }


    ////
    fun api16(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api16OutputVo? {
        val count = database1NativeRepository.forC7N16()

        httpServletResponse.setHeader("api-result-code", "")
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
            httpServletResponse.setHeader("api-result-code", "")
            httpServletResponse.status = HttpStatus.OK.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api17OutputVo(
            entity.uid,
            entity.content,
            entity.randomNum,
            entity.testDatetime.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            entity.rowCreateDate.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            entity.rowUpdateDate.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api18(
        httpServletResponse: HttpServletResponse,
        inputVo: C7Service1TkV1DatabaseTestController.Api18InputVo
    ): C7Service1TkV1DatabaseTestController.Api18OutputVo? {
        val result = database1TemplateLogicalDeleteUniqueDataRepository.save(
            Database1_Template_LogicalDeleteUniqueData(
                inputVo.uniqueValue
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api18OutputVo(
            result.uid!!,
            result.uniqueValue,
            result.rowCreateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowDeleteDateStr
        )
    }


    ////
    fun api19(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api19OutputVo? {
        val resultEntityList =
            database1TemplateLogicalDeleteUniqueDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/")
        val entityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api19OutputVo.TestEntityVo>()
        for (resultEntity in resultEntityList) {
            entityVoList.add(
                C7Service1TkV1DatabaseTestController.Api19OutputVo.TestEntityVo(
                    resultEntity.uid!!,
                    resultEntity.uniqueValue,
                    resultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
                )
            )
        }

        val logicalDeleteEntityVoList =
            database1TemplateLogicalDeleteUniqueDataRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/")
        val logicalDeleteVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api19OutputVo.TestEntityVo>()
        for (resultEntity in logicalDeleteEntityVoList) {
            logicalDeleteVoList.add(
                C7Service1TkV1DatabaseTestController.Api19OutputVo.TestEntityVo(
                    resultEntity.uid!!,
                    resultEntity.uniqueValue,
                    resultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api19OutputVo(
            entityVoList,
            logicalDeleteVoList
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api20(
        httpServletResponse: HttpServletResponse,
        testTableUid: Long,
        inputVo: C7Service1TkV1DatabaseTestController.Api20InputVo
    ): C7Service1TkV1DatabaseTestController.Api20OutputVo? {
        val oldEntity =
            database1TemplateLogicalDeleteUniqueDataRepository.findByUidAndRowDeleteDateStr(testTableUid, "/")

        if (oldEntity == null || oldEntity.rowDeleteDateStr != "/") {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val uniqueValueEntity =
            database1TemplateLogicalDeleteUniqueDataRepository.findByUniqueValueAndRowDeleteDateStr(
                inputVo.uniqueValue,
                "/"
            )

        if (uniqueValueEntity != null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }


        oldEntity.uniqueValue = inputVo.uniqueValue

        val result = database1TemplateLogicalDeleteUniqueDataRepository.save(oldEntity)

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api20OutputVo(
            result.uid!!,
            result.uniqueValue,
            result.rowCreateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api21(httpServletResponse: HttpServletResponse, index: Long) {
        val entity = database1TemplateLogicalDeleteUniqueDataRepository.findByUidAndRowDeleteDateStr(index, "/")

        if (entity == null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        entity.rowDeleteDateStr =
            LocalDateTime.now().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        database1TemplateLogicalDeleteUniqueDataRepository.save(entity)

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api22(
        httpServletResponse: HttpServletResponse,
        inputVo: C7Service1TkV1DatabaseTestController.Api22InputVo
    ): C7Service1TkV1DatabaseTestController.Api22OutputVo? {
        val result = database1TemplateFkTestParentRepository.save(
            Database1_Template_FkTestParent(
                inputVo.fkParentName
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api22OutputVo(
            result.uid!!,
            result.parentName,
            result.rowCreateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api23(
        httpServletResponse: HttpServletResponse,
        parentUid: Long,
        inputVo: C7Service1TkV1DatabaseTestController.Api23InputVo
    ): C7Service1TkV1DatabaseTestController.Api23OutputVo? {
        val parentEntityOpt = database1TemplateFkTestParentRepository.findById(parentUid)

        if (parentEntityOpt.isEmpty) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val parentEntity = parentEntityOpt.get()

        val result = database1TemplateFkTestOneToManyChildRepository.save(
            Database1_Template_FkTestManyToOneChild(
                inputVo.fkChildName,
                parentEntity
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api23OutputVo(
            result.uid!!,
            result.childName,
            result.fkTestParent.parentName,
            result.rowCreateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        )
    }


    ////
    fun api24(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api24OutputVo? {
        val resultEntityList =
            database1TemplateFkTestParentRepository.findAllByOrderByRowCreateDate()

        val entityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api24OutputVo.ParentEntityVo>()
        for (resultEntity in resultEntityList) {
            val childEntityVoList: ArrayList<C7Service1TkV1DatabaseTestController.Api24OutputVo.ParentEntityVo.ChildEntityVo> =
                arrayListOf()

            val childList =
                database1TemplateFkTestOneToManyChildRepository.findAllByFkTestParentOrderByRowCreateDate(resultEntity)

            for (childEntity in childList) {
                childEntityVoList.add(
                    C7Service1TkV1DatabaseTestController.Api24OutputVo.ParentEntityVo.ChildEntityVo(
                        childEntity.uid!!,
                        childEntity.childName,
                        childEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                        childEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                    )
                )
            }

            entityVoList.add(
                C7Service1TkV1DatabaseTestController.Api24OutputVo.ParentEntityVo(
                    resultEntity.uid!!,
                    resultEntity.parentName,
                    resultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    childEntityVoList
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api24OutputVo(
            entityVoList
        )
    }


    ////
    fun api24Dot1(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api24Dot1OutputVo? {
        val resultEntityList = database1NativeRepository.forC7N24Dot1()

        val entityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api24Dot1OutputVo.ChildEntityVo>()
        for (resultEntity in resultEntityList) {
            entityVoList.add(
                C7Service1TkV1DatabaseTestController.Api24Dot1OutputVo.ChildEntityVo(
                    resultEntity.childUid,
                    resultEntity.childName,
                    resultEntity.childCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.childUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.parentUid,
                    resultEntity.parentName
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api24Dot1OutputVo(
            entityVoList
        )
    }


    ////
    fun api25(
        httpServletResponse: HttpServletResponse,
        inputVal: Boolean
    ): C7Service1TkV1DatabaseTestController.Api25OutputVo? {
        // boolean 값을 갖고오기 위한 테스트 테이블이 존재하지 않는다면 하나 생성하기
        val justBooleanEntity = database1TemplateJustBooleanTestRepository.findAll()
        if (justBooleanEntity.isEmpty()) {
            database1TemplateJustBooleanTestRepository.save(
                Database1_Template_JustBooleanTest(
                    true
                )
            )
        }

        val resultEntity = database1NativeRepository.forC7N25(inputVal)

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api25OutputVo(
            // 쿼리문 내에서 True, False 로 반환하는 값은 Long 타입으로 받습니다.
            resultEntity.normalBoolValue == 1L,
            resultEntity.funcBoolValue == 1L,
            resultEntity.ifBoolValue == 1L,
            resultEntity.caseBoolValue == 1L,

            // 테이블 쿼리의 Boolean 값은 그대로 Boolean 타입으로 받습니다.
            resultEntity.tableColumnBoolValue
        )
    }


    ////
    fun api26(
        httpServletResponse: HttpServletResponse,
        searchKeyword: String
    ): C7Service1TkV1DatabaseTestController.Api26OutputVo? {
        // jpaRepository : Injection Safe
        val jpaRepositoryResultEntityList =
            database1TemplateTestRepository.findAllByContentOrderByRowCreateDate(
                searchKeyword
            )

        val jpaRepositoryResultList: ArrayList<C7Service1TkV1DatabaseTestController.Api26OutputVo.TestEntityVo> =
            arrayListOf()
        for (jpaRepositoryResultEntity in jpaRepositoryResultEntityList) {
            jpaRepositoryResultList.add(
                C7Service1TkV1DatabaseTestController.Api26OutputVo.TestEntityVo(
                    jpaRepositoryResultEntity.uid!!,
                    jpaRepositoryResultEntity.content,
                    jpaRepositoryResultEntity.randomNum,
                    jpaRepositoryResultEntity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    jpaRepositoryResultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    jpaRepositoryResultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                )
            )
        }

        // JPQL : Injection Safe
        val jpqlResultEntityList =
            database1TemplateTestRepository.findAllByContentOrderByRowCreateDateJpql(
                searchKeyword
            )

        val jpqlResultList: ArrayList<C7Service1TkV1DatabaseTestController.Api26OutputVo.TestEntityVo> =
            arrayListOf()
        for (jpqlEntity in jpqlResultEntityList) {
            jpqlResultList.add(
                C7Service1TkV1DatabaseTestController.Api26OutputVo.TestEntityVo(
                    jpqlEntity.uid!!,
                    jpqlEntity.content,
                    jpqlEntity.randomNum,
                    jpqlEntity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    jpqlEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    jpqlEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                )
            )
        }

        // NativeQuery : Injection Safe
        val nativeQueryResultEntityList =
            database1NativeRepository.forC7N26(
                searchKeyword
            )

        val nativeQueryResultList: ArrayList<C7Service1TkV1DatabaseTestController.Api26OutputVo.TestEntityVo> =
            arrayListOf()
        for (nativeQueryEntity in nativeQueryResultEntityList) {
            nativeQueryResultList.add(
                C7Service1TkV1DatabaseTestController.Api26OutputVo.TestEntityVo(
                    nativeQueryEntity.uid,
                    nativeQueryEntity.content,
                    nativeQueryEntity.randomNum,
                    nativeQueryEntity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    nativeQueryEntity.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    nativeQueryEntity.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                )
            )
        }

        /*
            결론 : 위 세 방식은 모두 SQL Injection 공격에서 안전합니다.
                셋 모두 쿼리문에 직접 값을 입력하는 것이 아니며, 매개변수로 먼저 받아서 JPA 를 경유하여 입력되므로,
                라이브러리가 자동으로 인젝션 공격을 막아주게 됩니다.
         */

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api26OutputVo(
            jpaRepositoryResultList,
            jpqlResultList,
            nativeQueryResultList
        )
    }


    ////
    fun api27(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api27OutputVo? {
        val resultEntityList = database1NativeRepository.forC7N27()

        val entityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api27OutputVo.ParentEntityVo>()
        for (resultEntity in resultEntityList) {
            entityVoList.add(
                C7Service1TkV1DatabaseTestController.Api27OutputVo.ParentEntityVo(
                    resultEntity.parentUid,
                    resultEntity.parentName,
                    resultEntity.parentCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.parentUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    if (resultEntity.childUid == null) {
                        null
                    } else {
                        C7Service1TkV1DatabaseTestController.Api27OutputVo.ParentEntityVo.ChildEntityVo(
                            resultEntity.childUid!!,
                            resultEntity.childName!!,
                            resultEntity.childCreateDate!!.atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.childUpdateDate!!.atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                        )
                    }
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api27OutputVo(
            entityVoList
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api28(httpServletResponse: HttpServletResponse, index: Long) {
        val entityOpt = database1TemplateFkTestOneToManyChildRepository.findById(index)

        if (entityOpt.isEmpty) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        database1TemplateFkTestOneToManyChildRepository.deleteById(index)

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api29(httpServletResponse: HttpServletResponse, index: Long) {
        val entityOpt = database1TemplateFkTestParentRepository.findById(index)

        if (entityOpt.isEmpty) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        database1TemplateFkTestParentRepository.deleteById(index)

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    @CustomTransactional([Database0Config.TRANSACTION_NAME])
    fun api30(
        httpServletResponse: HttpServletResponse,
        inputVo: C7Service1TkV1DatabaseTestController.Api30InputVo
    ): C7Service1TkV1DatabaseTestController.Api30OutputVo? {
        val result = database0TemplateTestRepository.save(
            Database0_Template_TestData(
                inputVo.content,
                (0..99999999).random(),
                LocalDateTime.parse(inputVo.dateString, DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS"))
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api30OutputVo(
            result.uid!!,
            result.content,
            result.randomNum,
            result.testDatetime.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowCreateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
            result.rowDeleteDateStr
        )
    }


    ////
    @CustomTransactional([Database0Config.TRANSACTION_NAME])
    fun api31(httpServletResponse: HttpServletResponse, index: Long, deleteLogically: Boolean) {
        val entity = database0TemplateTestRepository.findByUidAndRowDeleteDateStr(index, "/")

        if (entity == null) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (deleteLogically) {
            entity.rowDeleteDateStr =
                LocalDateTime.now().atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
            database0TemplateTestRepository.save(entity)
        } else {
            database0TemplateTestRepository.deleteById(index)
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api32(httpServletResponse: HttpServletResponse): C7Service1TkV1DatabaseTestController.Api32OutputVo? {
        val resultEntityList =
            database0TemplateTestRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/")
        val entityVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api32OutputVo.TestEntityVo>()
        for (resultEntity in resultEntityList) {
            entityVoList.add(
                C7Service1TkV1DatabaseTestController.Api32OutputVo.TestEntityVo(
                    resultEntity.uid!!,
                    resultEntity.content,
                    resultEntity.randomNum,
                    resultEntity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
                )
            )
        }

        val logicalDeleteEntityVoList =
            database0TemplateTestRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/")
        val logicalDeleteVoList = ArrayList<C7Service1TkV1DatabaseTestController.Api32OutputVo.TestEntityVo>()
        for (resultEntity in logicalDeleteEntityVoList) {
            logicalDeleteVoList.add(
                C7Service1TkV1DatabaseTestController.Api32OutputVo.TestEntityVo(
                    resultEntity.uid!!,
                    resultEntity.content,
                    resultEntity.randomNum,
                    resultEntity.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowCreateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate!!.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C7Service1TkV1DatabaseTestController.Api32OutputVo(
            entityVoList,
            logicalDeleteVoList
        )
    }


    ////
    @CustomTransactional([Database0Config.TRANSACTION_NAME])
    fun api33(
        httpServletResponse: HttpServletResponse
    ) {
        database0TemplateTestRepository.save(
            Database0_Template_TestData(
                "error test",
                (0..99999999).random(),
                LocalDateTime.now()
            )
        )

        throw Exception("Transaction Rollback Test!")
    }


    ////
    fun api34(httpServletResponse: HttpServletResponse) {
        database0TemplateTestRepository.save(
            Database0_Template_TestData(
                "error test",
                (0..99999999).random(),
                LocalDateTime.now()
            )
        )

        throw Exception("No Transaction Exception Test!")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api35(
        httpServletResponse: HttpServletResponse
    ) {
        val parentEntity = database1TemplateFkTestParentRepository.save(
            Database1_Template_FkTestParent(
                "transaction test"
            )
        )

        database1TemplateFkTestOneToManyChildRepository.save(
            Database1_Template_FkTestManyToOneChild(
                "transaction test1",
                parentEntity
            )
        )

        database1TemplateFkTestOneToManyChildRepository.save(
            Database1_Template_FkTestManyToOneChild(
                "transaction test2",
                parentEntity
            )
        )

        throw Exception("Transaction Rollback Test!")
    }


    ////
    fun api36(httpServletResponse: HttpServletResponse) {
        val parentEntity = database1TemplateFkTestParentRepository.save(
            Database1_Template_FkTestParent(
                "transaction test"
            )
        )

        database1TemplateFkTestOneToManyChildRepository.save(
            Database1_Template_FkTestManyToOneChild(
                "transaction test1",
                parentEntity
            )
        )

        database1TemplateFkTestOneToManyChildRepository.save(
            Database1_Template_FkTestManyToOneChild(
                "transaction test2",
                parentEntity
            )
        )

        throw Exception("No Transaction Exception Test!")
    }
}