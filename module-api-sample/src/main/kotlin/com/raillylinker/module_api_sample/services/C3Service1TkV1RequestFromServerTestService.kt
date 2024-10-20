package com.raillylinker.module_api_sample.services

import com.raillylinker.module_api_sample.controllers.C3Service1TkV1RequestFromServerTestController
import jakarta.servlet.http.HttpServletResponse

interface C3Service1TkV1RequestFromServerTestService {
    // <공개 메소드 공간>
    fun api1BasicRequestTest(httpServletResponse: HttpServletResponse): String?


    ////
    fun api2RedirectTest(httpServletResponse: HttpServletResponse): String?


    ////
    fun api3ForwardTest(httpServletResponse: HttpServletResponse): String?


    ////
    fun api4GetRequestTest(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api4GetRequestTestOutputVo?


    ////
    fun api5GetRequestTestWithPathParam(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api5GetRequestTestWithPathParamOutputVo?


    ////
    fun api6PostRequestTestWithApplicationJsonTypeRequestBody(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api6PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo?


    ////
    fun api7PostRequestTestWithFormTypeRequestBody(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api7PostRequestTestWithFormTypeRequestBodyOutputVo?


    ////
    fun api8PostRequestTestWithMultipartFormTypeRequestBody(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api8PostRequestTestWithMultipartFormTypeRequestBodyOutputVo?


    ////
    fun api9PostRequestTestWithMultipartFormTypeRequestBody2(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api9PostRequestTestWithMultipartFormTypeRequestBody2OutputVo?


    ////
    fun api10PostRequestTestWithMultipartFormTypeRequestBody3(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api10PostRequestTestWithMultipartFormTypeRequestBody3OutputVo?


    ////
    fun api11GenerateErrorTest(httpServletResponse: HttpServletResponse)


    ////
    fun api12ReturnResultCodeThroughHeaders(httpServletResponse: HttpServletResponse)


    ////
    fun api13ResponseDelayTest(httpServletResponse: HttpServletResponse, delayTimeSec: Long)


    ////
    fun api14ReturnTextStringTest(httpServletResponse: HttpServletResponse): String?


    ////
    fun api15ReturnTextHtmlTest(httpServletResponse: HttpServletResponse): String?


    ////
    fun api16AsynchronousResponseTest(httpServletResponse: HttpServletResponse): C3Service1TkV1RequestFromServerTestController.Api16AsynchronousResponseTestOutputVo?


    ////
//    fun api17SseSubscribeTest(httpServletResponse: HttpServletResponse)


//    ////
//    fun api18WebsocketConnectTest(httpServletResponse: HttpServletResponse)

//    data class Api18MessagePayloadVo(
//        val sender: String, // 송신자 (실제로는 JWT 로 보안 처리를 할 것)
//        val message: String
//    )
}