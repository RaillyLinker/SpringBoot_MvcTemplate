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
import org.springframework.util.StringUtils
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

        // 원본 파일명(with suffix)
        val multiPartFileNameString = StringUtils.cleanPath(inputVo.multipartImageFile.originalFilename!!)

        // 파일 확장자 구분 위치
        val fileExtensionSplitIdx = multiPartFileNameString.lastIndexOf('.')

        // 확장자가 없는 파일명
        val fileNameWithOutExtension: String
        // 확장자
        val fileExtension: String

        if (fileExtensionSplitIdx == -1) {
            fileNameWithOutExtension = multiPartFileNameString
            fileExtension = ""
        } else {
            fileNameWithOutExtension = multiPartFileNameString.substring(0, fileExtensionSplitIdx)
            fileExtension =
                multiPartFileNameString.substring(fileExtensionSplitIdx + 1, multiPartFileNameString.length)
        }

        if (fileExtension !in allowedExtensions) {
            httpServletResponse.status = HttpStatus.NO_CONTENT.value()
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val resultFileName = "${fileNameWithOutExtension}(${
            LocalDateTime.now().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        }).${inputVo.imageType.typeStr}"

        // 이미지 리사이징
        val resizedImage = ImageProcessUtilObject.resizeImage(
            inputVo.multipartImageFile.bytes,
            inputVo.resizingWidth,
            inputVo.resizingHeight,
            inputVo.imageType
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"$resultFileName\"")

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

        Files.newInputStream(gifFilePathObject).use { fileInputStream ->
            val frameSplit = ImageProcessUtilObject.gifToImageList(fileInputStream)

            // 요청 시간을 문자열로
            val timeString = LocalDateTime.now().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))

            // 파일 저장 디렉토리 경로
            val saveDirectoryPathString = "./by_product_files/test/$timeString"
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

        val saveDirectoryPathString = "./by_product_files/test"
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

        fileTargetPath.toFile().outputStream().use { fileOutputStream ->
            ImageProcessUtilObject.imageListToGif(
                gifFrameList,
                fileOutputStream
            )
        }

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
        val fileInputStream = inputVo.multipartImageFile.inputStream
        val resizedImageByteArray = ImageProcessUtilObject.resizeGifImage(
            fileInputStream,
            inputVo.resizingWidth,
            inputVo.resizingHeight
        )
        fileInputStream.close()

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
        // 서명 이미지 생성 및 저장
        val signBufferedImage = ImageProcessUtilObject.createSignatureImage(
            inputVo.signatureText,
            400,
            100,
            Color.BLACK,
            Font("Serif", Font.PLAIN, 48)
        )

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./by_product_files/test"
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

        // 사인 이미지를 파일로 저장
        ImageIO.write(signBufferedImage, "png", fileTargetPath.toFile())

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }
}