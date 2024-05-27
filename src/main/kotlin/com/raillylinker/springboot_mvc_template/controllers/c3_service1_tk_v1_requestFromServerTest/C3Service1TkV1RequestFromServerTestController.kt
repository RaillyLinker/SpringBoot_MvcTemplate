package com.raillylinker.springboot_mvc_template.controllers.c3_service1_tk_v1_requestFromServerTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Tag(name = "/service1/tk/v1/request-from-server-test APIs", description = "C3 : 서버에서 요청을 보내는 테스트 API 컨트롤러")
@Controller
@RequestMapping("/service1/tk/v1/request-from-server-test")
class C3Service1TkV1RequestFromServerTestController(
    private val service: C3Service1TkV1RequestFromServerTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 기본 요청 테스트",
        description = "기본적인 Get 메소드 요청 테스트입니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/request-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    @ResponseBody
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api1(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N2 : Redirect 테스트",
        description = "Redirect 되었을 때의 응답 테스트입니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/redirect-to-blank"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    @ResponseBody
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api2(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N3 : Forward 테스트",
        description = "Forward 되었을 때의 응답 테스트입니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/forward-to-blank"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    @ResponseBody
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api3(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N4 : Get 요청 테스트 (Query Parameter)",
        description = "Query 파라미터를 받는 Get 요청 테스트\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/get-request"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api4OutputVo? {
        return service.api4(httpServletResponse)
    }

    data class Api4OutputVo(
        @Schema(description = "입력한 String Query 파라미터", required = true, example = "testString")
        @JsonProperty("queryParamString")
        val queryParamString: String,
        @Schema(description = "입력한 String Nullable Query 파라미터", required = false, example = "testString")
        @JsonProperty("queryParamStringNullable")
        val queryParamStringNullable: String?,
        @Schema(description = "입력한 Int Query 파라미터", required = true, example = "1")
        @JsonProperty("queryParamInt")
        val queryParamInt: Int,
        @Schema(description = "입력한 Int Nullable Query 파라미터", required = false, example = "1")
        @JsonProperty("queryParamIntNullable")
        val queryParamIntNullable: Int?,
        @Schema(description = "입력한 Double Query 파라미터", required = true, example = "1.1")
        @JsonProperty("queryParamDouble")
        val queryParamDouble: Double,
        @Schema(description = "입력한 Double Nullable Query 파라미터", required = false, example = "1.1")
        @JsonProperty("queryParamDoubleNullable")
        val queryParamDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Query 파라미터", required = true, example = "true")
        @JsonProperty("queryParamBoolean")
        val queryParamBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Query 파라미터", required = false, example = "true")
        @JsonProperty("queryParamBooleanNullable")
        val queryParamBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Query 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("queryParamStringList")
        val queryParamStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Query 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("queryParamStringListNullable")
        val queryParamStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N5 : Get 요청 테스트 (Path Parameter)",
        description = "Path 파라미터를 받는 Get 요청 테스트\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/get-request-path-param"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api5OutputVo? {
        return service.api5(httpServletResponse)
    }

    data class Api5OutputVo(
        @Schema(description = "입력한 Int Path 파라미터", required = true, example = "1")
        @JsonProperty("pathParamInt")
        val pathParamInt: Int
    )

    ////
    @Operation(
        summary = "N6 : Post 요청 테스트 (Request Body, application/json)",
        description = "application/json 형식의 Request Body 를 받는 Post 요청 테스트\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/post-request-application-json"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api6OutputVo? {
        return service.api6(httpServletResponse)
    }

    data class Api6OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestBodyString")
        val requestBodyString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestBodyStringNullable")
        val requestBodyStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestBodyInt")
        val requestBodyInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestBodyIntNullable")
        val requestBodyIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestBodyDouble")
        val requestBodyDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestBodyDoubleNullable")
        val requestBodyDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestBodyBoolean")
        val requestBodyBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestBodyBooleanNullable")
        val requestBodyBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestBodyStringList")
        val requestBodyStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestBodyStringListNullable")
        val requestBodyStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N7 : Post 요청 테스트 (Request Body, x-www-form-urlencoded)",
        description = "x-www-form-urlencoded 형식의 Request Body 를 받는 Post 요청 테스트\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/post-request-x-www-form-urlencoded"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api7OutputVo? {
        return service.api7(httpServletResponse)
    }

    data class Api7OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N8 : Post 요청 테스트 (Request Body, multipart/form-data)",
        description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/post-request-multipart-form-data"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api8(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api8OutputVo? {
        return service.api8(httpServletResponse)
    }

    data class Api8OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N9 : Post 요청 테스트 (Request Body, multipart/form-data, MultipartFile List)",
        description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트\n\n" +
                "MultipartFile 파라미터를 List 로 받습니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/post-request-multipart-form-data2"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api9(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api9OutputVo? {
        return service.api9(httpServletResponse)
    }

    data class Api9OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N10 : Post 요청 테스트 (Request Body, multipart/form-data, with jsonString)",
        description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트\n\n" +
                "파일 외의 파라미터를 JsonString 형식으로 받습니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/post-request-multipart-form-data-json"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api10(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api10OutputVo? {
        return service.api10(httpServletResponse)
    }

    data class Api10OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N11 : 에러 발생 테스트",
        description = "요청시 에러가 발생했을 때의 테스트입니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/generate-error"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api11(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api11(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N12 : api-result-code 반환 테스트",
        description = "api-result-code 가 Response Header 로 반환되는 테스트입니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : 네트워크 에러\n\n" +
                                "2 : 서버 에러\n\n" +
                                "3 : errorType 을 A 로 보냈습니다.\n\n" +
                                "4 : errorType 을 B 로 보냈습니다.\n\n" +
                                "5 : errorType 을 C 로 보냈습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/api-result-code-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api12(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api12(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N13 : 응답 지연 발생 테스트",
        description = "요청을 보내어 인위적으로 응답이 지연 되었을 때를 테스트합니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/time-delay-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api13(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "delayTimeSec", description = "지연 시간(초)", example = "1")
        @RequestParam("delayTimeSec")
        delayTimeSec: Long
    ) {
        service.api13(httpServletResponse, delayTimeSec)
    }


    ////
    @Operation(
        summary = "N14 : text/string 형식 Response 받아오기",
        description = "text/string 형식 Response 를 받아옵니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/text-string-response"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    @ResponseBody
    fun api14(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api14(
            httpServletResponse
        )
    }


    ////
    @Operation(
        summary = "N15 : text/html 형식 Response 받아오기",
        description = "text/html 형식 Response 를 받아옵니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/text-html-response"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @ResponseBody
    fun api15(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api15(
            httpServletResponse
        )
    }


    ////
    @Operation(
        summary = "N16 : DeferredResult Get 요청 테스트",
        description = "결과 반환 지연 Get 메소드 요청 테스트\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/delayed-result-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api16(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api16OutputVo? {
        return service.api16(
            httpServletResponse
        )
    }

    data class Api16OutputVo(
        @Schema(description = "결과 메세지", required = true, example = "n 초 경과 후 반환했습니다.")
        @JsonProperty("resultMessage")
        val resultMessage: String
    )


    ////
    @Operation(
        summary = "N17 : SSE 구독 테스트",
        description = "SSE 구독 요청 테스트\n\n" +
                "SSE 를 구독하여 백그라운드에서 실행합니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/sse-subscribe"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api17(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api17(
            httpServletResponse
        )
    }


    ////
    @Operation(
        summary = "N18 : WebSocket 연결 테스트",
        description = "WebSocket 연결 요청 테스트\n\n" +
                "WebSocket 을 연결 하여 백그라운드에서 실행합니다.\n\n"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "정상 동작"
            ),
            ApiResponse(
                responseCode = "204",
                content = [Content()],
                description = "Response Body 가 없습니다.\n\n" +
                        "Response Headers 를 확인하세요.",
                headers = [
                    Header(
                        name = "api-result-code",
                        description = "(Response Code 반환 원인) - Required\n\n" +
                                "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
                                "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
                        schema = Schema(type = "string")
                    )
                ]
            ),
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
        path = ["/websocket-connect"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api18(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api18(
            httpServletResponse
        )
    }
}