package com.raillylinker.springboot_mvc_template.controllers.c9_service1_tk_v1_mapCoordinateCalculation

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

// Google 맵과 비교하여 정상 동작 테스트를 하면 됩니다.
@Tag(name = "/service1/tk/v1/map-coordinate-calculation APIs", description = "C9 : 지도/좌표 계산에 대한 API 컨트롤러")
@Controller
@RequestMapping("/service1/tk/v1/map-coordinate-calculation")
class C9Service1TkV1MapCoordinateCalculationController(
    private val service: C9Service1TkV1MapCoordinateCalculationService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N0 : 테스트용 기본 좌표 리스트를 DB에 저장",
        description = "DB 내에 기존 좌표 데이터들을 모두 날려버리고, 테스트용 기본 좌표 리스트를 DB에 저장합니다.\n\n"
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
//                        description = SecurityConfig.AuthTokenFilterService1Tk.DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE,
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
    @PostMapping(
        path = ["/default-coordinate"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api0(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api0(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N1 : 두 좌표 사이의 거리를 반환",
        description = "함수를 사용하여 두 좌표 사이의 거리를 meter 단위로 반환하는 API\n\n"
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
//                        description = SecurityConfig.AuthTokenFilterService1Tk.DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE,
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
        path = ["/distance-meter-between-two-coordinate"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "latitude1", description = "위도1", example = "37.675683")
        @RequestParam("latitude1")
        latitude1: Double,
        @Parameter(name = "longitude1", description = "경도1", example = "126.761259")
        @RequestParam("longitude1")
        longitude1: Double,
        @Parameter(name = "latitude2", description = "위도2", example = "37.676563")
        @RequestParam("latitude2")
        latitude2: Double,
        @Parameter(name = "longitude2", description = "경도2", example = "126.764777")
        @RequestParam("longitude2")
        longitude2: Double
    ): Api1OutputVo? {
        return service.api1(httpServletResponse, latitude1, longitude1, latitude2, longitude2)
    }

    data class Api1OutputVo(
        @Schema(description = "좌표간 거리 (Meter)", required = true, example = "325.42")
        @JsonProperty("distanceMeter")
        val distanceMeter: Double
    )


    ////
    @Operation(
        summary = "N2 : 좌표들 사이의 중심 좌표를 반환",
        description = "함수를 사용하여 좌표들 사이의 중심 좌표를 반환하는 API\n\n"
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
//                        description = SecurityConfig.AuthTokenFilterService1Tk.DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE,
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
    @PostMapping(
        path = ["/for-center-coordinate"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api2InputVo
    ): Api2OutputVo? {
        return service.api2(httpServletResponse, inputVo)
    }

    data class Api2InputVo(
        @Schema(description = "좌표 리스트", required = true)
        @JsonProperty("coordinateList")
        val coordinateList: List<Coordinate>
    ) {
        @Schema(description = "좌표 데이터")
        data class Coordinate(
            @Schema(description = "중심좌표 위도", required = true, example = "37.676563")
            @JsonProperty("centerLatitude")
            val centerLatitude: Double,
            @Schema(description = "중심좌표 경도", required = true, example = "126.761259")
            @JsonProperty("centerLongitude")
            val centerLongitude: Double
        )
    }

    data class Api2OutputVo(
        @Schema(description = "중심좌표 위도", required = true, example = "37.676563")
        @JsonProperty("centerLatitude")
        val centerLatitude: Double,
        @Schema(description = "중심좌표 경도", required = true, example = "126.761259")
        @JsonProperty("centerLongitude")
        val centerLongitude: Double
    )


    ////
    @Operation(
        summary = "N3 : DB 의 좌표 테이블에 좌표 정보를 저장",
        description = "DB 의 좌표 테이블에 좌표 정보를 저장하는 API\n\n"
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
//                        description = SecurityConfig.AuthTokenFilterService1Tk.DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE,
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
    @PostMapping(
        path = ["/test-map/coordinate"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api3InputVo
    ): Api3OutputVo? {
        return service.api3(httpServletResponse, inputVo)
    }

    data class Api3InputVo(
        @Schema(description = "좌표 위도", required = true, example = "37.676563")
        @JsonProperty("latitude")
        val latitude: Double,
        @Schema(description = "좌표 경도", required = true, example = "126.761259")
        @JsonProperty("longitude")
        val longitude: Double
    )

    data class Api3OutputVo(
        @Schema(description = "DB 좌표 리스트", required = true)
        @JsonProperty("coordinateList")
        val coordinateList: List<Coordinate>,
        @Schema(description = "좌표 리스트들의 중심좌표", required = true)
        @JsonProperty("centerCoordinate")
        val centerCoordinate: Coordinate
    ) {
        @Schema(description = "좌표 데이터")
        data class Coordinate(
            @Schema(description = "중심좌표 위도", required = true, example = "37.676563")
            @JsonProperty("centerLatitude")
            val centerLatitude: Double,
            @Schema(description = "중심좌표 경도", required = true, example = "126.761259")
            @JsonProperty("centerLongitude")
            val centerLongitude: Double
        )
    }


    ////
    @Operation(
        summary = "N4 : DB 의 좌표 테이블의 모든 데이터 삭제",
        description = "DB 의 좌표 테이블의 모든 데이터 삭제 API\n\n"
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
//                        description = SecurityConfig.AuthTokenFilterService1Tk.DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE,
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
    @DeleteMapping(
        path = ["/test-map/coordinate/all"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api4(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N5 : DB 에 저장된 좌표들을 SQL 을 사용하여, 기준 좌표의 N Km 내의 결과만 필터",
        description = "기준 좌표를 입력하면 DB 에 저장된 좌표들과의 거리를 SQL 로 계산하여 N Km 내의 결과만 필터링 하여 리스트로 반환하는 API\n\n"
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
//                        description = SecurityConfig.AuthTokenFilterService1Tk.DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE,
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
        path = ["/test-map/coordinate-in-round"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "anchorLatitude", description = "기준 좌표 위도", example = "37.675683")
        @RequestParam("anchorLatitude")
        anchorLatitude: Double,
        @Parameter(name = "anchorLongitude", description = "기준 좌표 경도", example = "126.761259")
        @RequestParam("anchorLongitude")
        anchorLongitude: Double,
        @Parameter(
            name = "radiusKiloMeter",
            description = "기준 좌표를 중심으로 몇 Km 안까지의 결과를 가져올지",
            example = "12.54"
        )
        @RequestParam("radiusKiloMeter")
        radiusKiloMeter: Double
    ): Api5OutputVo? {
        return service.api5(httpServletResponse, anchorLatitude, anchorLongitude, radiusKiloMeter)
    }

    data class Api5OutputVo(
        @Schema(description = "결과 리스트", required = true)
        @JsonProperty("resultList")
        val resultList: List<CoordinateCalcResult>
    ) {
        @Schema(description = "결과 좌표")
        data class CoordinateCalcResult(
            @Schema(description = "고유번호", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "좌표 위도", required = true, example = "37.676563")
            @JsonProperty("latitude")
            val latitude: Double,
            @Schema(description = "좌표 경도", required = true, example = "126.761259")
            @JsonProperty("longitude")
            val longitude: Double,
            @Schema(description = "기준 좌표와의 거리 (Km)", required = true, example = "15.214")
            @JsonProperty("distanceMeter")
            val distanceKiloMeter: Double
        )
    }


    ////
    @Operation(
        summary = "N6 : DB 에 저장된 좌표들을 SQL 을 사용하여, 북서 좌표에서 남동 좌표까지의 사각 영역 안에 들어오는 좌표들만 필터링하여 반환",
        description = "북, 서, 남, 동 좌표를 입력하면 DB 에 저장된 좌표들 중 좌표 사각 영역 안에 들어오는 좌표를 필터링 하여 리스트로 반환하는 API\n\n"
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
//                        description = SecurityConfig.AuthTokenFilterService1Tk.DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE,
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
        path = ["/test-map/coordinate-in-box"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "northLatitude", description = "북위도", example = "37.771848")
        @RequestParam("northLatitude")
        northLatitude: Double,
        @Parameter(name = "eastLongitude", description = "동경도", example = "127.433549")
        @RequestParam("eastLongitude")
        eastLongitude: Double,
        @Parameter(name = "southLatitude", description = "남위도", example = "37.245683")
        @RequestParam("southLatitude")
        southLatitude: Double,
        @Parameter(name = "westLongitude", description = "서경도", example = "126.587602")
        @RequestParam("westLongitude")
        westLongitude: Double
    ): Api6OutputVo? {
        return service.api6(httpServletResponse, northLatitude, eastLongitude, southLatitude, westLongitude)
    }

    data class Api6OutputVo(
        @Schema(description = "결과 좌표 리스트", required = true)
        @JsonProperty("resultList")
        val resultList: List<CoordinateCalcResult>
    ) {
        @Schema(description = "결과 좌표")
        data class CoordinateCalcResult(
            @Schema(description = "고유번호", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "좌표 위도", required = true, example = "37.676563")
            @JsonProperty("latitude")
            val latitude: Double,
            @Schema(description = "좌표 경도", required = true, example = "126.761259")
            @JsonProperty("longitude")
            val longitude: Double
        )
    }


    ////
}