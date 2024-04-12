package com.raillylinker.springboot_mvc_template.controllers.c5_service1_tk_v1_mediaResourceProcess

import com.fasterxml.jackson.annotation.JsonProperty
import com.raillylinker.springboot_mvc_template.custom_objects.ImageProcessUtilObject
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

@Tag(name = "/service1/tk/v1/media-resource-process APIs", description = "C5 : 미디어 리소스(이미지, 비디오, 오디오 등...) 처리 API 컨트롤러")
@Controller
@RequestMapping("/service1/tk/v1/media-resource-process")
class C5Service1TkV1MediaResourceProcessController(
    private val service: C5Service1TkV1MediaResourceProcessService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 정적 이미지 파일(지원 타입은 description 에 후술)을 업로드 하여 리사이징 후 다운",
        description = "multipart File 로 받은 이미지 파일을 업로드 하여 리사이징 후 다운\n\n" +
                "지원 타입 : jpg, jpeg, bmp, png, gif(움직이지 않는 타입)\n\n" +
                "(api-result-code)\n\n" +
                "1 : 지원하는 파일이 아닙니다.\n\n"
    )
    @PostMapping(
        path = ["/resize-image"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api1InputVo
    ): ResponseEntity<Resource>? {
        return service.api1(inputVo, httpServletResponse)
    }

    data class Api1InputVo(
        @Schema(description = "업로드 이미지 파일", required = true)
        @JsonProperty("multipartImageFile")
        val multipartImageFile: MultipartFile,
        @Schema(description = "이미지 리사이징 너비", required = true, example = "300")
        @JsonProperty("resizingWidth")
        val resizingWidth: Int,
        @Schema(description = "이미지 리사이징 높이", required = true, example = "400")
        @JsonProperty("resizingHeight")
        val resizingHeight: Int,
        @Schema(description = "이미지 포멧", required = true, example = "BMP")
        @JsonProperty("imageType")
        val imageType: ImageProcessUtilObject.ResizeImageTypeEnum
    )


    ////
    @Operation(
        summary = "N2 : 서버에 저장된 움직이는 Gif 이미지 파일에서 프레임을 PNG 이미지 파일로 분리한 후 files/temps 폴더 안에 저장",
        description = "서버에 저장된 움직이는 Gif 이미지 파일에서 프레임을 PNG 이미지 파일로 분리한 후 files/temps 폴더 안에 저장\n\n" +
                "(api-result-code)\n\n"
    )
    @PostMapping(
        path = ["/split-animated-gif"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api2(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N3 : 서버에 저장된 움직이는 PNG 이미지 프레임들을 움직이는 Gif 파일로 병합 후 files/temps 폴더 안에 저장",
        description = "서버에 저장된 움직이는 PNG 이미지 프레임들을 움직이는 Gif 파일로 병합 후 files/temps 폴더 안에 저장\n\n" +
                "(api-result-code)\n\n"
    )
    @PostMapping(
        path = ["/merge-images-to-animated-gif"],
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
        summary = "N4 : 동적 GIF 이미지 파일을 업로드 하여 리사이징 후 다운",
        description = "multipart File 로 받은 움직이는 GIF 이미지 파일을 업로드 하여 리사이징 후 다운\n\n" +
                "(api-result-code)\n\n" +
                "1 : 지원하는 파일이 아닙니다.\n\n"
    )
    @PostMapping(
        path = ["/resize-gif-image"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api4InputVo
    ): ResponseEntity<Resource>? {
        return service.api4(inputVo, httpServletResponse)
    }

    data class Api4InputVo(
        @Schema(description = "업로드 이미지 파일", required = true)
        @JsonProperty("multipartImageFile")
        val multipartImageFile: MultipartFile,
        @Schema(description = "이미지 리사이징 너비", required = true, example = "300")
        @JsonProperty("resizingWidth")
        val resizingWidth: Int,
        @Schema(description = "이미지 리사이징 높이", required = true, example = "400")
        @JsonProperty("resizingHeight")
        val resizingHeight: Int
    )
}