package com.raillylinker.module_api_sample.services

import com.raillylinker.module_api_sample.controllers.C10Service1TkV1MongoDbTestController
import jakarta.servlet.http.HttpServletResponse

interface C10Service1TkV1MongoDbTestService {
    // <공개 메소드 공간>
    fun api1InsertDocumentTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C10Service1TkV1MongoDbTestController.Api1InsertDocumentTestInputVo
    ): C10Service1TkV1MongoDbTestController.Api1InsertDocumentTestOutputVo?

    ////
    fun api2DeleteAllDocumentTest(httpServletResponse: HttpServletResponse)

    ////
    fun api3DeleteDocumentTest(httpServletResponse: HttpServletResponse, id: String)

    ////
    fun api4SelectAllDocumentsTest(httpServletResponse: HttpServletResponse): C10Service1TkV1MongoDbTestController.Api4SelectAllDocumentsTestOutputVo?


    fun api12TransactionRollbackTest(
        httpServletResponse: HttpServletResponse
    )


    fun api13NoTransactionRollbackTest(
        httpServletResponse: HttpServletResponse
    )
}