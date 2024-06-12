package com.raillylinker.springboot_mvc_template.controllers.c1

import com.raillylinker.springboot_mvc_template.ApplicationRuntimeConfigs
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Tag(name = "root APIs", description = "C1 : Root 경로에 대한 API 컨트롤러")
@Controller
class C1Controller(
    private val service: C1Service
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 홈페이지",
        description = "루트 홈페이지를 반환합니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            )
        ]
    )
    @GetMapping(
        path = ["", "/"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return service.api1(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N2 : 범용 런타임 설정 파일 데이터 반영",
        description = "파일로 저장되는 범용 런타임 설정 데이터를 프로젝트에 반영하고 현재 적용된 값을 가져옵니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            )
        ]
    )
    @GetMapping(
        path = ["/runtime-config"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ApplicationRuntimeConfigs.RuntimeConfigFile {
        return service.api2(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N2.1 : service1 런타임 설정 데이터 반영",
        description = "service1 데이터베이스에 저장되는 런타임 설정 데이터를 프로젝트에 반영하고 현재 적용된 값을 가져옵니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            )
        ]
    )
    @GetMapping(
        path = ["/service1-runtime-config"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api2Dot1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ApplicationRuntimeConfigs.RuntimeConfigDbService1 {
        return service.api2Dot1(httpServletResponse)
    }
}