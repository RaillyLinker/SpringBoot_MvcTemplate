package com.raillylinker.springboot_mvc_template.controllers.sc1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.security.Principal

@Tag(name = "/service1/sc/v1 APIs", description = "SC1 : Service1 웹 페이지에 대한 API 컨트롤러")
@Controller
@RequestMapping("/service1/sc/v1")
class SC1Controller(
    private val service: SC1Service
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 홈페이지",
        description = "루트 홈페이지 화면을 반환합니다.\n\n"
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
        path = ["/home"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(hidden = true)
        principal: Principal?
    ): ModelAndView? {
        return service.api1(httpServletResponse, session, principal)
    }


    ////
    @Operation(
        summary = "N2 : 인증 정보",
        description = "인증 정보 화면\n\n"
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
        path = ["/auth-info"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(hidden = true)
        principal: Principal?
    ): ModelAndView? {
        return service.api2(httpServletResponse, session, principal!!)
    }


    ////
    @Operation(
        summary = "N3 : 로그인 화면",
        description = "로그인 화면\n\n"
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
        path = ["/login"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(hidden = true)
        principal: Principal?,
        @Parameter(
            name = "fail",
            description = "인증 실패(not null 이라면 인증 정보가 잘못된 것입니다.)",
            example = ""
        )
        @RequestParam("fail")
        fail: String?,
        @Parameter(
            name = "logout",
            description = "로그아웃으로 진입(not null 이라면 로그아웃 하여 자동으로 진입한 것입니다.)",
            example = ""
        )
        @RequestParam("logout")
        logout: String?
    ): ModelAndView? {
        return service.api3(httpServletResponse, session, principal, fail, logout)
    }
}