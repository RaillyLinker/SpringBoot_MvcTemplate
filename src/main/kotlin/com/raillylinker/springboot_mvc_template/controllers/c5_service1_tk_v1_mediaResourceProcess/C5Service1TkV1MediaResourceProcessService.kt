package com.raillylinker.springboot_mvc_template.controllers.c5_service1_tk_v1_mediaResourceProcess

import com.raillylinker.springboot_mvc_template.custom_objects.GifUtilObject
import com.raillylinker.springboot_mvc_template.custom_objects.ImageProcessUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO

@Service
class C5Service1TkV1MediaResourceProcessService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        inputVo: C5Service1TkV1MediaResourceProcessController.Api1InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        // 이미지 파일의 확장자 확인
        val allowedExtensions = setOf("jpg", "jpeg", "bmp", "png", "gif")
        val fileName = inputVo.multipartImageFile.originalFilename
        val fileExtension = fileName?.split(".")?.lastOrNull()?.lowercase(Locale.getDefault())

        if (fileExtension !in allowedExtensions) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 이미지 리사이징
        val resizedImage = ImageProcessUtilObject.resizeImage(
            inputVo.multipartImageFile.bytes,
            inputVo.resizingWidth,
            inputVo.resizingHeight,
            inputVo.imageType
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"$fileName\"")

        return ResponseEntity<Resource>(
            ByteArrayResource(resizedImage),
            HttpStatus.OK
        )
    }


    ////
    fun api2(
        httpServletResponse: HttpServletResponse
    ) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        val gifFilePathObject =
            Paths.get("$projectRootAbsolutePathString/src/main/resources/static/resource_c5_n2/test.gif")

        val frameSplit = ImageProcessUtilObject.gifToImageList(Files.newInputStream(gifFilePathObject))

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp/$timeString"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 받은 파일 순회
        for (bufferedImageIndexedValue in frameSplit.withIndex()) {
            val bufferedImage = bufferedImageIndexedValue.value

            // 확장자 포함 파일명 생성
            val saveFileName = "${bufferedImageIndexedValue.index + 1}.png"

            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()

            // 파일 저장
            ImageIO.write(bufferedImage.frameBufferedImage, "png", fileTargetPath.toFile())
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val bufferedImageList = ArrayList<BufferedImage>()
        for (idx in 1..15) {
            val imageFilePathString =
                "$projectRootAbsolutePathString/src/main/resources/static/resource_c5_n3/gif_frame_images/${idx}.png"
            bufferedImageList.add(
                ImageIO.read(
                    Paths.get(imageFilePathString).toFile()
                )
            )
        }

        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)
        val resultFileName = "${
            LocalDateTime.now().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        }.gif"
        val fileTargetPath = saveDirectoryPath.resolve(resultFileName).normalize()

        val gifFrameList: ArrayList<GifUtilObject.GifFrame> = arrayListOf()
        for (bufferedImage in bufferedImageList) {
            gifFrameList.add(
                GifUtilObject.GifFrame(
                    bufferedImage,
                    30
                )
            )
        }

        ImageProcessUtilObject.imageListToGif(
            gifFrameList,
            fileTargetPath.toFile().outputStream()
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api4(
        inputVo: C5Service1TkV1MediaResourceProcessController.Api4InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        val contentType = inputVo.multipartImageFile.contentType

        val allowedContentTypes = setOf(
            "image/gif"
        )

        if (contentType !in allowedContentTypes) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))

        // 결과 파일의 확장자 포함 파일명 생성
        val resultFileName = "resized_${timeString}.gif"

        // 리사이징
        val resizedImageByteArray = ImageProcessUtilObject.resizeGifImage(
            inputVo.multipartImageFile.inputStream,
            inputVo.resizingWidth,
            inputVo.resizingHeight
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return ResponseEntity<Resource>(
            InputStreamResource(ByteArrayInputStream(resizedImageByteArray)),
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(resultFileName, StandardCharsets.UTF_8)
                    .build()
                this.add(
                    HttpHeaders.CONTENT_TYPE,
                    "image/gif"
                )
            },
            HttpStatus.OK
        )
    }


    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        inputVo: C5Service1TkV1MediaResourceProcessController.Api5InputVo
    ) {
        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 확장자 포함 파일명 생성
        val fileTargetPath = saveDirectoryPath.resolve(
            "signature_${
                LocalDateTime.now().atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
            }.png"
        ).normalize()

        // 서명 이미지 생성 및 저장
        ImageProcessUtilObject.createSignatureImage(
            inputVo.signatureText,
            fileTargetPath.toFile(),
            400,
            100,
            Color.BLACK,
            Font("Serif", Font.PLAIN, 48)
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }
}