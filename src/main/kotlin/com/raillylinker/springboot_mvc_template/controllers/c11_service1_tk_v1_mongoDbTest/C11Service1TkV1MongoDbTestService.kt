package com.raillylinker.springboot_mvc_template.controllers.c11_service1_tk_v1_mongoDbTest

import com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mongoDb1.TestCollection
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Service

@Service
class C11Service1TkV1MongoDbTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    @Qualifier("mongoDb1") private val mongoDb1: MongoTemplate
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: C11Service1TkV1MongoDbTestController.Api1InputVo
    ): C11Service1TkV1MongoDbTestController.Api1OutputVo? {
        val resultCollection = mongoDb1.insert(
            TestCollection(
                inputVo.content,
                (0..99999999).random(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
        return C11Service1TkV1MongoDbTestController.Api1OutputVo(
            resultCollection.uid!!.toString(),
            resultCollection.content,
            resultCollection.randomNum,
            resultCollection.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
            resultCollection.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
        )
    }

    ////
    fun api2(httpServletResponse: HttpServletResponse) {
        mongoDb1.remove(Query(), "testCollection")

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }

    ////
    fun api3(httpServletResponse: HttpServletResponse, id: String) {
        mongoDb1.remove(Query(Criteria.where("_id").`is`(id)), "testCollection")

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }

    ////
    fun api4(httpServletResponse: HttpServletResponse): C11Service1TkV1MongoDbTestController.Api4OutputVo? {
        val testCollectionList = mongoDb1.findAll(TestCollection::class.java, "testCollection")

        val resultVoList: ArrayList<C11Service1TkV1MongoDbTestController.Api4OutputVo.TestEntityVo> = arrayListOf()

        for (testCollection in testCollectionList) {
            resultVoList.add(
                C11Service1TkV1MongoDbTestController.Api4OutputVo.TestEntityVo(
                    testCollection.uid!!.toString(),
                    testCollection.content,
                    testCollection.randomNum,
                    testCollection.rowCreateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                    testCollection.rowUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
        return C11Service1TkV1MongoDbTestController.Api4OutputVo(
            resultVoList
        )
    }
}