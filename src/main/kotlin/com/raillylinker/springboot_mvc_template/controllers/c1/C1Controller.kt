package com.raillylinker.springboot_mvc_template.controllers.c1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
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
            ),
//            ApiResponse(
//                responseCode = "204",
//                content = [Content()],
//                description = "Response Body 가 없습니다.\n\n" +
//                        "Response Headers 를 확인하세요.",
//                headers = [
//                    Header(
//                        name = "api-result-code",
//                        description = "(Response Code 반환 원인) - Required\n\n" +
//                                "1 : 설명1\n\n" +
//                                "2 : 설명2\n\n",
//                        schema = Schema(type = "string")
//                    )
//                ]
//            ),
//            ApiResponse(
//                responseCode = "401",
//                content = [Content()],
//                description = "인증되지 않은 접근입니다.\n\n" +
//                        "Response Headers 를 확인하세요.",
//                headers = [
//                    Header(
//                        name = "api-result-code",
//                        description = SecurityConfig.AuthTokenFilterService1Tk.UNAUTHORIZED_CODE_FOR_SWAGGER_DESCRIPTION,
//                        schema = Schema(type = "string")
//                    )
//                ]
//            ),
//            ApiResponse(
//                responseCode = "403",
//                content = [Content()],
//                description = "인가되지 않은 접근입니다."
//            )
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
}