package com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

// 본 컨트롤러는 프로젝트 단위로 지원하는 웹 서비스 기능들을 모아둔 것이며, MVC 화면 생성 방식을 보여줍니다.
// RestAPI 와는 달리 API 문서를 Swagger 로 공개할 필요는 없기에 아래 @Hidden 어노테이션으로 숨김 처리를 했습니다.
// 그럼에도 Swagger 작성 방식으로 API 작성 방식을 고수하는 이유는,
// Swagger 작성 방식이 코드상으로 따로 주석을 달 필요 없이 체계적으로 API 를 설명하기 좋아서이며,
// RestAPI 와의 구분 없이 코드 형식에 통일성을 부여하여 코드 해석이 쉽도록 하기 위한 조치입니다.
@Hidden
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
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession
    ): ModelAndView? {
        return service.api1(httpServletRequest, httpServletResponse, session)
    }


    ////
    @Operation(
        summary = "N2 : 회원 정보 화면 <>",
        description = "회원 정보 화면\n\n"
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
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession
    ): ModelAndView? {
        return service.api2(httpServletRequest, httpServletResponse, session)
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
        httpServletRequest: HttpServletRequest,
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
        fail: String?,
        @Parameter(
            name = "expired",
            description = "세션 만료(not null 이라면 세션이 만료된 것입니다.)",
            example = ""
        )
        @RequestParam("expired")
        expired: String?,
        @Parameter(
            name = "duplicated",
            description = "동시 접속 금지(not null 이라면 동시 접속 금지 된 것입니다.)",
            example = ""
        )
        @RequestParam("duplicated")
        duplicated: String?,
        @Parameter(
            name = "changePassword",
            description = "비밀번호 변경(not null 이라면 비밀번호 변경 된 것입니다.)",
            example = ""
        )
        @RequestParam("changePassword")
        changePassword: String?,
        @Parameter(
            name = "lock",
            description = "계정 정지된 회원 고유번호(not null 이라면 계정이 정지 된 것이며, 값은 회원 고유번호를 뜻합니다.)",
            example = ""
        )
        @RequestParam("lock")
        lock: Long?
    ): ModelAndView? {
        return service.api3(
            httpServletRequest,
            httpServletResponse,
            session,
            fail,
            expired,
            duplicated,
            changePassword,
            lock
        )
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
        httpServletRequest: HttpServletRequest,
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
        return service.api4(httpServletRequest, httpServletResponse, session, complete, idExists)
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
        httpServletRequest: HttpServletRequest,
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
        return service.api5(httpServletRequest, httpServletResponse, session, accountId, password)
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
        httpServletRequest: HttpServletRequest,
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
        return service.api6(httpServletRequest, httpServletResponse, session, currentPath)
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
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(name = "filePath", description = "파일 경로", example = "currentLog.log")
        @RequestParam("filePath")
        filePath: String
    ): ModelAndView? {
        return service.api7(httpServletRequest, httpServletResponse, session, filePath)
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
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(name = "type", description = "에러 타입", example = "ACCESS_DENIED")
        @RequestParam("type")
        type: Api8ErrorTypeEnum
    ): ModelAndView? {
        return service.api8(httpServletRequest, httpServletResponse, session, type)
    }

    enum class Api8ErrorTypeEnum {
        ACCESS_DENIED
    }


    ////
    @Operation(
        summary = "N9 : 런타임 설정 수정 화면 <>",
        description = "런타임 설정 수정 화면\n\n"
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
        path = ["/runtime-config-editor"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_SERVER_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    fun api9(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "fail",
            description = "수정 실패(not null 이라면 수정 실패)",
            example = ""
        )
        @RequestParam("fail")
        fail: String?,
        @Parameter(
            name = "fail",
            description = "수정 성공(not null 이라면 수정 성공)",
            example = ""
        )
        @RequestParam("complete")
        complete: String?
    ): ModelAndView? {
        return service.api9(httpServletRequest, httpServletResponse, session, fail, complete)
    }


    ////
    @Operation(
        summary = "N10 : 런타임 설정 업데이트",
        description = "런타임 설정 업데이트\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"
            )
        ]
    )
    @PostMapping(
        path = ["/runtime-config"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_SERVER_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    fun api10(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "configJsonString",
            description = "설정 JsonString",
            example = "{}"
        )
        @RequestParam("configJsonString")
        configJsonString: String
    ): ModelAndView? {
        return service.api10(httpServletRequest, httpServletResponse, session, configJsonString)
    }

    ////
    @Operation(
        summary = "N11 : 아이디 변경",
        description = "아이디 변경\n\n"
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
        path = ["/change-id"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api11(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "complete",
            description = "아이디 변경 완료(not null 이라면 아이디 변경이 완료된 것입니다.)",
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
        return service.api11(httpServletRequest, httpServletResponse, session, complete, idExists)
    }


    ////
    @Operation(
        summary = "N12 : 아이디 변경 진행",
        description = "아이디 변경 진행\n\n"
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
        path = ["/change-id-process"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api12(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
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
        accountId: String
    ): ModelAndView? {
        return service.api12(httpServletRequest, httpServletResponse, session, accountId)
    }

    ////
    @Operation(
        summary = "N13 : 비밀번호 변경",
        description = "비밀번호 변경\n\n"
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
        path = ["/change-password"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api13(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "passwordNotMatch",
            description = "기존 비밀번호가 일치하지 않습니다.(not null 이라면 기존 비밀번호가 일치하지 않는 것입니다.)",
            example = ""
        )
        @RequestParam("passwordNotMatch")
        passwordNotMatch: String?
    ): ModelAndView? {
        return service.api13(httpServletRequest, httpServletResponse, session, passwordNotMatch)
    }


    ////
    @Operation(
        summary = "N14 : 비밀번호 변경 진행",
        description = "비밀번호 변경 진행\n\n"
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
        path = ["/change-password-process"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api14(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "oldPassword",
            description = "기존 비밀번호",
            example = "abcd1234"
        )
        @RequestParam("oldPassword")
        oldPassword: String,
        @Parameter(
            name = "newPassword",
            description = "새 비밀번호",
            example = "abcd1234"
        )
        @RequestParam("newPassword")
        newPassword: String
    ): ModelAndView? {
        return service.api14(httpServletRequest, httpServletResponse, session, oldPassword, newPassword)
    }

    ////
    @Operation(
        summary = "N15 : 회원 탈퇴",
        description = "회원 탈퇴\n\n"
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
        path = ["/withdrawal"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api15(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession
    ): ModelAndView? {
        return service.api15(httpServletRequest, httpServletResponse, session)
    }


    ////
    @Operation(
        summary = "N16 : 회원탈퇴 진행",
        description = "회원탈퇴 진행\n\n"
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
        path = ["/withdrawal-process"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api16(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession
    ): ModelAndView? {
        return service.api16(httpServletRequest, httpServletResponse, session)
    }

    ////
    @Operation(
        summary = "N17 : 회원 탈퇴 완료",
        description = "회원 탈퇴 완료\n\n"
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
        path = ["/good-bye"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun api17(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "accountId",
            description = "아이디",
            example = "abcd1234"
        )
        @RequestParam("accountId")
        accountId: String
    ): ModelAndView? {
        return service.api17(httpServletRequest, httpServletResponse, session, accountId)
    }

    ////
    @Operation(
        summary = "N18 : 멤버 비밀번호 변경",
        description = "멤버 비밀번호 변경\n\n"
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
        path = ["/member-password-change"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_SERVER_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    fun api18(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "complete",
            description = "비밀번호 변경 완료(not null 이라면 비밀번호 변경이 완료된 것입니다.)",
            example = ""
        )
        @RequestParam("complete")
        complete: String?,
        @Parameter(
            name = "memberNotFound",
            description = "멤버가 없습니다.(not null 이라면 멤버가 없는 것입니다.)",
            example = ""
        )
        @RequestParam("memberNotFound")
        memberNotFound: String?
    ): ModelAndView? {
        return service.api18(httpServletRequest, httpServletResponse, session, complete, memberNotFound)
    }


    ////
    @Operation(
        summary = "N19 : 멤버 비밀번호 변경 진행",
        description = "멤버 비밀번호 변경 진행\n\n"
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
        path = ["/member-password-change-process"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api19(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "memberUid",
            description = "멤버 고유번호",
            example = "1"
        )
        @RequestParam("memberUid")
        memberUid: Long,
        @Parameter(
            name = "newPassword",
            description = "새 비밀번호",
            example = "abcd1234"
        )
        @RequestParam("newPassword")
        newPassword: String
    ): ModelAndView? {
        return service.api19(httpServletRequest, httpServletResponse, session, memberUid, newPassword)
    }

    ////
    @Operation(
        summary = "N20 : 멤버 세션 만료 처리",
        description = "멤버 세션 만료 처리\n\n"
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
        path = ["/member-session-expire"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_SERVER_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    fun api20(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "complete",
            description = "비밀번호 변경 완료(not null 이라면 비밀번호 변경이 완료된 것입니다.)",
            example = ""
        )
        @RequestParam("complete")
        complete: String?,
        @Parameter(
            name = "memberNotFound",
            description = "멤버가 없습니다.(not null 이라면 멤버가 없는 것입니다.)",
            example = ""
        )
        @RequestParam("memberNotFound")
        memberNotFound: String?
    ): ModelAndView? {
        return service.api20(httpServletRequest, httpServletResponse, session, complete, memberNotFound)
    }


    ////
    @Operation(
        summary = "N21 : 멤버 세션 만료 처리 진행",
        description = "멤버 세션 만료 처리 진행\n\n"
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
        path = ["/member-session-expire-process"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    fun api21(
        @Parameter(hidden = true)
        httpServletRequest: HttpServletRequest,
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        session: HttpSession,
        @Parameter(
            name = "memberUid",
            description = "멤버 고유번호",
            example = "1"
        )
        @RequestParam("memberUid")
        memberUid: Long
    ): ModelAndView? {
        return service.api21(httpServletRequest, httpServletResponse, session, memberUid)
    }
}