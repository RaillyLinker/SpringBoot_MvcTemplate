package com.raillylinker.module_idp_common.custom_objects

import java.awt.Color
import java.awt.Font
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.*
import java.nio.file.Files
import javax.imageio.ImageIO

// [이미지 처리 유틸]
object ImageProcessUtil {
    // (움직이지 않는 정적 이미지 리사이징 및 리포멧 함수)
    fun resizeImage(
        imageBytes: ByteArray,
        resizeWidth: Int,
        resizeHeight: Int,
        imageTypeEnum: ResizeImageTypeEnum
    ): ByteArray {
        val imageType = imageTypeEnum.typeStr
        val bufferedResizedImage = BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_INT_RGB)
        val resultByteArray: ByteArray
        imageBytes.inputStream().use { imageInputStream ->
            bufferedResizedImage.createGraphics().drawImage(
                ImageIO.read(imageInputStream)
                    .getScaledInstance(resizeWidth, resizeHeight, BufferedImage.SCALE_SMOOTH),
                0,
                0,
                null
            )
            ByteArrayOutputStream().use { outputStream ->
                ImageIO.write(bufferedResizedImage, imageType, outputStream)
                resultByteArray = outputStream.toByteArray()
            }
        }
        return resultByteArray
    }

    enum class ResizeImageTypeEnum(val typeStr: String) {
        JPG("jpg"),
        PNG("png"),
        BMP("bmp"),
        GIF("gif")
    }

    // (Gif 를 이미지 리스트로 분리)
    fun gifToImageList(inputStream: InputStream): ArrayList<GifUtil.GifFrame> {
        return GifUtil.decodeGif(inputStream)
    }

    // (이미지 리스트를 Gif 로 병합)
    fun imageListToGif(gifFrameList: ArrayList<GifUtil.GifFrame>, outputStream: OutputStream) {
        GifUtil.encodeGif(gifFrameList, outputStream, 2, false)
    }

    fun resizeGifImage(inputStream: InputStream, newWidth: Int, newHeight: Int): ByteArray {
        val frameList = GifUtil.decodeGif(inputStream)

        val resizedFrameList = ArrayList<GifUtil.GifFrame>()
        for (frame in frameList) {
            // 이미지 리사이징
            val resizedImage: Image =
                frame.frameBufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)

            // 리사이징된 이미지를 버퍼 이미지로 변환
            val resultBufferedImage = BufferedImage(newWidth, newHeight, frame.frameBufferedImage.type)
            val g2d = resultBufferedImage.createGraphics()
            g2d.drawImage(resizedImage, 0, 0, null)
            g2d.dispose()

            resizedFrameList.add(
                GifUtil.GifFrame(
                    resultBufferedImage,
                    frame.frameDelay
                )
            )
        }

        // 임시 파일 생성
        val tempFile = File.createTempFile("resized_", ".gif")

        try {
            FileOutputStream(tempFile).use { fileOutputStream ->
                GifUtil.encodeGif(resizedFrameList, fileOutputStream, 2, false)
            }
            return Files.readAllBytes(tempFile.toPath())
        } finally {
            // 임시 파일 삭제
            tempFile.delete()
        }
    }

    // (문자열을 투명 배경 서명 이미지로 변경하는 함수)
    fun createSignatureImage(
        // 서명화할 텍스트
        text: String,
        // 사인 이미지 사이즈
        signImageWidth: Int,
        signImageHeight: Int,
        // 사인 텍스트 색상
        signColor: Color,
        // 사인 텍스트 폰트
        signFont: Font
    ): BufferedImage {
        // 투명한 배경의 BufferedImage 생성
        val bufferedImage = BufferedImage(signImageWidth, signImageHeight, BufferedImage.TYPE_INT_ARGB)
        val g2d = bufferedImage.createGraphics()

        // 안티앨리어싱 설정
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // 배경을 투명하게 설정
        g2d.color = Color(0, 0, 0, 0)
        g2d.fillRect(0, 0, signImageWidth, signImageHeight)

        // 서명 텍스트 설정
        g2d.color = signColor
        g2d.font = signFont

        // 텍스트의 크기를 계산하여 중앙에 배치
        val fontMetrics = g2d.fontMetrics
        val stringBounds = fontMetrics.getStringBounds(text, g2d)
        val x = (signImageWidth - stringBounds.width.toInt()) / 2
        val y = (signImageHeight - stringBounds.height.toInt()) / 2 + fontMetrics.ascent

        // 텍스트 그리기
        g2d.drawString(text, x, y)

        // 리소스 해제
        g2d.dispose()

        return bufferedImage
    }
}