package com.raillylinker.module_api_sample.services

import com.raillylinker.module_api_sample.controllers.C4Service1TkV1FileTestController
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity

interface C4Service1TkV1FileTestService {
    // <공개 메소드 공간>
    fun api1UploadToServerTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C4Service1TkV1FileTestController.Api1UploadToServerTestInputVo
    ): C4Service1TkV1FileTestController.Api1UploadToServerTestOutputVo?


    ////
    fun api2FileDownloadTest(httpServletResponse: HttpServletResponse, fileName: String): ResponseEntity<Resource>?


    ////
    fun api3FilesToZipTest(httpServletResponse: HttpServletResponse)


    ////
    fun api3Dot1FolderToZipTest(httpServletResponse: HttpServletResponse)


    ////
    fun api4UnzipTest(httpServletResponse: HttpServletResponse)


    ////
    fun api5ForClientSideImageTest(
        httpServletResponse: HttpServletResponse,
        delayTimeSecond: Int
    ): ResponseEntity<Resource>?


    ////
    fun api6AwsS3UploadTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C4Service1TkV1FileTestController.Api6AwsS3UploadTestInputVo
    ): C4Service1TkV1FileTestController.Api6AwsS3UploadTestOutputVo?


    ////
    fun api7GetFileContentToStringTest(
        httpServletResponse: HttpServletResponse,
        uploadFileName: String
    ): C4Service1TkV1FileTestController.Api7GetFileContentToStringTestOutputVo?


    ////
    fun api8DeleteAwsS3FileTest(httpServletResponse: HttpServletResponse, deleteFileName: String)
}