package com.raillylinker.module_api_sample.services

import com.raillylinker.module_api_sample.controllers.C6Service1TkV1TestController
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity

interface C6Service1TkV1TestService {
    // <공개 메소드 공간>
    fun api1SendEmailTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api1SendEmailTestInputVo
    )


    ////
    fun api2SendHtmlEmailTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api2SendHtmlEmailTestInputVo
    )


    ////
    fun api3NaverSmsSample(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api3NaverSmsSampleInputVo
    )


    ////
    fun api3Dot1NaverAlimTalkSample(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api3Dot1NaverAlimTalkSampleInputVo
    )


    ////
    fun api4ReadExcelFileSample(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api4ReadExcelFileSampleInputVo
    ): C6Service1TkV1TestController.Api4ReadExcelFileSampleOutputVo?


    ////
    fun api5WriteExcelFileSample(httpServletResponse: HttpServletResponse)


    ////
    fun api6HtmlToPdfSample(
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>?


    ////
    fun api6Dot1MultipartHtmlToPdfSample(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api6Dot1MultipartHtmlToPdfSampleInputVo,
        controllerBasicMapping: String?
    ): ResponseEntity<Resource>?


    ////
    fun api6Dot2DownloadFontFile(
        httpServletResponse: HttpServletResponse,
        fileName: String
    ): ResponseEntity<Resource>?


    ////
    fun api7SendKafkaTopicMessageTest(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api7SendKafkaTopicMessageTestInputVo
    )


    ////
    fun api8ProcessBuilderTest(
        httpServletResponse: HttpServletResponse,
        javaEnvironmentPath: String?
    ): C6Service1TkV1TestController.Api8ProcessBuilderTestOutputVo?


    ////
    fun api9CheckFontFileInnerName(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api9CheckFontFileInnerNameInputVo
    ): C6Service1TkV1TestController.Api9CheckFontFileInnerNameOutputVo?


    ////
    fun api10Aes256EncryptTest(
        httpServletResponse: HttpServletResponse,
        plainText: String,
        alg: C6Service1TkV1TestController.Api10Aes256EncryptTestCryptoAlgEnum,
        initializationVector: String,
        encryptionKey: String
    ): C6Service1TkV1TestController.Api10Aes256EncryptTestOutputVo?


    ////
    fun api11Aes256DecryptTest(
        httpServletResponse: HttpServletResponse,
        encryptedText: String,
        alg: C6Service1TkV1TestController.Api11Aes256DecryptTestCryptoAlgEnum,
        initializationVector: String,
        encryptionKey: String
    ): C6Service1TkV1TestController.Api11Aes256DecryptTestOutputVo?


    ////
    fun api12JsoupTest(httpServletResponse: HttpServletResponse, fix: Boolean): String?
}