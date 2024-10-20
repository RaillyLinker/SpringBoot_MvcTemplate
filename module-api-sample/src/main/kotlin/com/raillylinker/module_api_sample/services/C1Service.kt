package com.raillylinker.module_api_sample.services

import com.raillylinker.module_api_sample.controllers.C1Controller
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.ModelAndView

interface C1Service {
    // <공개 메소드 공간>
    fun api1GetRoot(
        httpServletResponse: HttpServletResponse
    ): ModelAndView?


    ////
    fun api2SelectAllProjectRuntimeConfigsRedisKeyValue(httpServletResponse: HttpServletResponse): C1Controller.Api2SelectAllProjectRuntimeConfigsRedisKeyValueOutputVo?


    ////
    fun api3InsertProjectRuntimeConfigActuatorAllowIpList(
        httpServletResponse: HttpServletResponse,
        inputVo: C1Controller.Api3InsertProjectRuntimeConfigActuatorAllowIpListInputVo
    )


    ////
    fun api4InsertProjectRuntimeConfigLoggingDenyIpList(
        httpServletResponse: HttpServletResponse,
        inputVo: C1Controller.Api4InsertProjectRuntimeConfigLoggingDenyIpListInputVo
    )
}