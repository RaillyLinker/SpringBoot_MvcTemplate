package com.raillylinker.springboot_mvc_template.controllers.c1

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
    @Hidden
    @Operation(
        summary = "N1 : 홈페이지",
        description = "루트 홈페이지를 반환합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @GetMapping("", "/", consumes = [MediaType.ALL_VALUE], produces = [MediaType.TEXT_HTML_VALUE])
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return service.api1(httpServletResponse)
    }
}