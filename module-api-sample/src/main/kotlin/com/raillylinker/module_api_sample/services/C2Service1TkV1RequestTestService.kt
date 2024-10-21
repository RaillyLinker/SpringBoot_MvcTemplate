package com.raillylinker.module_api_sample.services

import com.raillylinker.module_api_sample.controllers.C2Service1TkV1RequestTestController
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

interface C2Service1TkV1RequestTestService {
    // <공개 메소드 공간>
    // (기본 요청 테스트 API)
    fun api1BasicRequestTest(httpServletResponse: HttpServletResponse): String?


    ////
    // (요청 Redirect 테스트)
    fun api2RedirectTest(httpServletResponse: HttpServletResponse): ModelAndView?


    ////
    // (요청 Forward 테스트)
    fun api3ForwardTest(httpServletResponse: HttpServletResponse): ModelAndView?


    ////
    // (Get 요청 테스트 (Query Parameter))
    fun api4GetRequestTest(
        httpServletResponse: HttpServletResponse,
        queryParamString: String,
        queryParamStringNullable: String?,
        queryParamInt: Int,
        queryParamIntNullable: Int?,
        queryParamDouble: Double,
        queryParamDoubleNullable: Double?,
        queryParamBoolean: Boolean,
        queryParamBooleanNullable: Boolean?,
        queryParamStringList: List<String>,
        queryParamStringListNullable: List<String>?
    ): C2Service1TkV1RequestTestController.Api4GetRequestTestOutputVo?


    ////
    fun api5GetRequestTestWithPathParam(
        httpServletResponse: HttpServletResponse,
        pathParamInt: Int
    ): C2Service1TkV1RequestTestController.Api5GetRequestTestWithPathParamOutputVo?


    ////
    fun api6PostRequestTestWithApplicationJsonTypeRequestBody(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api6PostRequestTestWithApplicationJsonTypeRequestBodyInputVo
    ): C2Service1TkV1RequestTestController.Api6PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo?


    ////
    fun api6Dot1PostRequestTestWithApplicationJsonTypeRequestBody2(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api6Dot1PostRequestTestWithApplicationJsonTypeRequestBody2InputVo
    ): C2Service1TkV1RequestTestController.Api6Dot1PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo?


    ////
    fun api6Dot2PostRequestTestWithNoInputAndOutput(
        httpServletResponse: HttpServletResponse
    )


    ////
    fun api7PostRequestTestWithFormTypeRequestBody(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api7PostRequestTestWithFormTypeRequestBodyInputVo
    ): C2Service1TkV1RequestTestController.Api7PostRequestTestWithFormTypeRequestBodyOutputVo?


    ////
    fun api8PostRequestTestWithMultipartFormTypeRequestBody(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api8PostRequestTestWithMultipartFormTypeRequestBodyInputVo
    ): C2Service1TkV1RequestTestController.Api8PostRequestTestWithMultipartFormTypeRequestBodyOutputVo?


    ////
    fun api9PostRequestTestWithMultipartFormTypeRequestBody2(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api9PostRequestTestWithMultipartFormTypeRequestBody2InputVo
    ): C2Service1TkV1RequestTestController.Api9PostRequestTestWithMultipartFormTypeRequestBody2OutputVo?


    ////
    fun api10PostRequestTestWithMultipartFormTypeRequestBody3(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api10PostRequestTestWithMultipartFormTypeRequestBody3InputVo
    ): C2Service1TkV1RequestTestController.Api10PostRequestTestWithMultipartFormTypeRequestBody3OutputVo?


    ////
    fun api11GenerateErrorTest(httpServletResponse: HttpServletResponse)

    ////
    fun api12ReturnResultCodeThroughHeaders(
        httpServletResponse: HttpServletResponse,
        errorType: C2Service1TkV1RequestTestController.Api12ReturnResultCodeThroughHeadersErrorTypeEnum?
    )


    ////
    fun api13ResponseDelayTest(httpServletResponse: HttpServletResponse, delayTimeSec: Long)


    ////
    fun api14ReturnTextStringTest(httpServletResponse: HttpServletResponse): String?


    ////
    fun api15ReturnTextHtmlTest(httpServletResponse: HttpServletResponse): ModelAndView?


    ////
    fun api16ReturnByteDataTest(httpServletResponse: HttpServletResponse): Resource?


    ////
    fun api17VideoStreamingTest(
        videoHeight: C2Service1TkV1RequestTestController.Api17VideoStreamingTestVideoHeight,
        httpServletResponse: HttpServletResponse
    ): Resource?


    ////
    fun api18AudioStreamingTest(httpServletResponse: HttpServletResponse): Resource?


    ////
    fun api19AsynchronousResponseTest(httpServletResponse: HttpServletResponse): DeferredResult<C2Service1TkV1RequestTestController.Api19AsynchronousResponseTestOutputVo>?


    ////
    fun api20SseTestSubscribe(httpServletResponse: HttpServletResponse, lastSseEventId: String?): SseEmitter?


    ////
    fun api21SseTestEventTrigger(httpServletResponse: HttpServletResponse)


    ////
    fun api22EmptyListRequestTest(
        httpServletResponse: HttpServletResponse,
        stringList: List<String>,
        inputVo: C2Service1TkV1RequestTestController.Api22EmptyListRequestTestInputVo
    ): C2Service1TkV1RequestTestController.Api22EmptyListRequestTestOutputVo?
}