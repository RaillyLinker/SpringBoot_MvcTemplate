package com.raillylinker.springboot_mvc_template.custom_objects

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.*
import java.nio.file.Files
import javax.imageio.ImageIO

object ImageProcessUtilObject {
    // (움직이지 않는 정적 이미지 리사이징 및 리포멧 함수)
    fun resizeImage(
        imageBytes: ByteArray,
        resizeWidth: Int,
        resizeHeight: Int,
        imageTypeEnum: ResizeImageTypeEnum
    ): ByteArray {
        val imageType = imageTypeEnum.typeStr
        val bufferedResizedImage = BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_INT_RGB)
        bufferedResizedImage.createGraphics().drawImage(
            ImageIO.read(imageBytes.inputStream())
                .getScaledInstance(resizeWidth, resizeHeight, BufferedImage.SCALE_SMOOTH),
            0,
            0,
            null
        )
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedResizedImage, imageType, outputStream)
        return outputStream.toByteArray()
    }

    enum class ResizeImageTypeEnum(val typeStr: String) {
        JPG("jpg"),
        PNG("png"),
        BMP("bmp"),
        GIF("gif")
    }

    // (Gif 를 이미지 리스트로 분리)
    fun gifToImageList(inputStream: InputStream): ArrayList<GifUtilObject.GifFrame> {
        return GifUtilObject.decodeGif(inputStream)
    }

    // (이미지 리스트를 Gif 로 병합)
    fun imageListToGif(gifFrameList: ArrayList<GifUtilObject.GifFrame>, outputStream: OutputStream) {
        GifUtilObject.encodeGif(gifFrameList, outputStream, 2, false)
    }

    fun resizeGifImage(inputStream: InputStream, newWidth: Int, newHeight: Int): ByteArray {
        val frameList = GifUtilObject.decodeGif(inputStream)

        val resizedFrameList = ArrayList<GifUtilObject.GifFrame>()
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
                GifUtilObject.GifFrame(
                    resultBufferedImage,
                    frame.frameDelay
                )
            )
        }

        // 임시 파일 생성
        val tempFile = File.createTempFile("resized_", ".gif")

        try {
            GifUtilObject.encodeGif(resizedFrameList, FileOutputStream(tempFile), 2, false)
            return Files.readAllBytes(tempFile.toPath())
        } finally {
            // 임시 파일 삭제
            tempFile.delete()
        }
    }
}