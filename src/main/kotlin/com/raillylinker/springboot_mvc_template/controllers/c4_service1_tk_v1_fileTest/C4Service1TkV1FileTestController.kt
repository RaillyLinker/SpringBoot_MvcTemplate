package com.raillylinker.springboot_mvc_template.controllers.c4_service1_tk_v1_fileTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "/service1/tk/v1/file-test APIs", description = "C4 : 파일을 다루는 테스트 API 컨트롤러")
@Controller
@RequestMapping("/service1/tk/v1/file-test")
class C4Service1TkV1FileTestController(
    private val service: C4Service1TkV1FileTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : files/temp 폴더로 파일 업로드",
        description = "multipart File 을 하나 업로드하여 서버의 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n"
    )
    @PostMapping(
        path = ["/upload-to-temp"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        @RequestBody
        inputVo: Api1InputVo
    ): Api1OutputVo? {
        return service.api1(httpServletResponse, inputVo)
    }

    data class Api1InputVo(
        @Schema(description = "업로드 파일", required = true)
        @JsonProperty("multipartFile")
        val multipartFile: MultipartFile
    )

    data class Api1OutputVo(
        @Schema(
            description = "파일 다운로드 경로", required = true,
            example = "http://127.0.0.1:8080/service1/tk/v1/file-test/download-from-temp/file.txt"
        )
        @JsonProperty("fileDownloadFullUrl")
        val fileDownloadFullUrl: String
    )


    ////
    @Operation(
        summary = "N2 : files/temp 폴더에서 파일 다운받기",
        description = "업로드 API 를 사용하여 files/temp 로 업로드한 파일을 다운로드\n\n" +
                "(api-result-code)\n\n" +
                "1 : 파일이 존재하지 않습니다.\n\n"
    )
    @GetMapping(
        path = ["/download-from-temp/{fileName}"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "fileName", description = "files/temp 폴더 안의 파일명", example = "sample.txt")
        @PathVariable("fileName")
        fileName: String
    ): ResponseEntity<Resource>? {
        return service.api2(httpServletResponse, fileName)
    }


    ////
    @Operation(
        summary = "N3 : 파일 리스트 zip 압축 테스트",
        description = "파일들을 zip 타입으로 압축하여 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n"
    )
    @PostMapping(
        path = ["/zip-files"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api3(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N3.1 : 폴더 zip 압축 테스트",
        description = "폴더를 통째로 zip 타입으로 압축하여 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n"
    )
    @PostMapping(
        path = ["/zip-folder"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api3Dot1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api3Dot1(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N4 : zip 압축 파일 해제 테스트",
        description = "zip 압축 파일을 해제하여 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n"
    )
    @PostMapping(
        path = ["/unzip-file"],
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
        summary = "N5 : 클라이언트 이미지 표시 테스트용 API",
        description = "서버에서 이미지를 반환합니다. 클라이언트에서의 이미지 표시 시 PlaceHolder, Error 처리에 대응 할 수 있습니다.\n\n" +
                "(api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/client-image-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "delayTimeSecond", description = "이미지 파일 반환 대기 시간(0 은 바로, 음수는 에러 발생)", example = "0")
        @RequestParam("delayTimeSecond")
        delayTimeSecond: Int
    ): ResponseEntity<Resource>? {
        return service.api5(httpServletResponse, delayTimeSecond)
    }
}