package com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1

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

@Tag(name = "/main/sc/v1 APIs", description = "SC1 : main 웹 페이지에 대한 API 컨트롤러")
@Controller
@RequestMapping("/main/sc/v1")
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
        session: HttpSession
    ): ModelAndView? {
        return service.api1(httpServletResponse, session)
    }


    ////
    @Operation(
        summary = "N2 : 인증 정보 화면 <>",
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
        path = ["/member-info"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession
    ): ModelAndView? {
        return service.api2(httpServletResponse, session)
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
        @Parameter(
            name = "fail",
            description = "인증 실패(not null 이라면 인증 정보가 잘못된 것입니다.)",
            example = ""
        )
        @RequestParam("fail")
        fail: String?
    ): ModelAndView? {
        return service.api3(httpServletResponse, session, fail)
    }


    ////
    @Operation(
        summary = "N4 : 회원가입 화면",
        description = "회원가입 화면\n\n"
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
        path = ["/join"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "complete",
            description = "회원가입 완료(not null 이라면 회원가입이 완료된 것입니다.)",
            example = ""
        )
        @RequestParam("complete")
        complete: String?,
        @Parameter(
            name = "idExists",
            description = "가입된 아이디가 존재(not null 이라면 이미 가입된 아이디가 존재하는 것입니다.)",
            example = ""
        )
        @RequestParam("idExists")
        idExists: String?
    ): ModelAndView? {
        return service.api4(httpServletResponse, session, complete, idExists)
    }


    ////
    @Operation(
        summary = "N5 : 회원가입 진행",
        description = "회원가입 진행\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            )
        ]
    )
    @PostMapping(
        path = ["/join-process"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "accountId",
            description = "계정 아이디",
            example = "hongGilDong"
        )
        @RequestParam("accountId")
        accountId: String,
        @Parameter(
            name = "password",
            description = "계정 비밃번호",
            example = "tted21$"
        )
        @RequestParam("password")
        password: String
    ): ModelAndView? {
        return service.api5(httpServletResponse, session, accountId, password)
    }


    ////
    @Operation(
        summary = "N6 : 프로젝트 로그 확인 화면 <>",
        description = "프로젝트 로그 확인 화면\n\n"
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
        path = ["/project-logs"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_SERVER_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "currentPath",
            description = "폴더 현재 경로",
            example = "2023_12_11"
        )
        @RequestParam("currentPath")
        currentPath: String?
    ): ModelAndView? {
        return service.api6(httpServletResponse, session, currentPath)
    }


    ////
    @Operation(
        summary = "N7 : 프로젝트 로그 내용 화면 <>",
        description = "프로젝트 로그 내용 화면\n\n"
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
        path = ["/project-log-file"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_SERVER_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(name = "filePath", description = "파일 경로", example = "currentLog.log")
        @RequestParam("filePath")
        filePath: String
    ): ModelAndView? {
        return service.api7(httpServletResponse, session, filePath)
    }


    ////
    @Operation(
        summary = "N8 : 에러 화면",
        description = "에러 화면\n\n"
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
        path = ["/error"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun api8(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(name = "type", description = "에러 타입", example = "ACCESS_DENIED")
        @RequestParam("type")
        type: Api8ErrorTypeEnum
    ): ModelAndView? {
        return service.api8(httpServletResponse, session, type)
    }

    enum class Api8ErrorTypeEnum {
        ACCESS_DENIED
    }
}