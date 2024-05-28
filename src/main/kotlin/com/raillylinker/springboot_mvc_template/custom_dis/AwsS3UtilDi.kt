package com.raillylinker.springboot_mvc_template.custom_dis

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3Object
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.charset.StandardCharsets

@Service
class AwsS3UtilDi(
    private val amazonS3Client: AmazonS3
) {
    // (S3 로 업로드하는 함수)
    fun upload(
        // S3 에 저장할 파일 객체
        multipartFile: MultipartFile,
        // S3 에 저장할 때 사용할 파일명 (ex : "test.png")
        fileSaveName: String,
        // 버킷 위치
        // S3 안에 가장 외곽 버킷은 직접 만들어 보안 설정 등을 거쳐야 합니다.
        // test 라는 버킷을 만들었고, 그 안에 my 디렉토리, 그 안에 bucket 디렉토리 안에 fileSaveName 이름의 파일을 저장하려면,
        // test/my/bucket 이라고 입력하면, 결과적으로 S3 -> test/my/bucket/test.png 라는 경로로 파일이 저장됩니다.
        bucketName: String
    ): String {
        val objMeta = ObjectMetadata()

        val extension = if (fileSaveName.contains(".")) {
            fileSaveName.substringAfterLast('.').lowercase()
        } else {
            "" // 확장자가 없는 경우 빈 문자열 반환
        }

        if (extension == "pdf") {
            objMeta.contentType = "application/pdf"
        }

        objMeta.contentLength = multipartFile.inputStream.available().toLong()
        amazonS3Client.putObject(bucketName, fileSaveName, multipartFile.inputStream, objMeta)

        // 업로드 된 파일의 다운로드 URL 이 반환됩니다.
        return amazonS3Client.getUrl(bucketName, fileSaveName).toString()
    }

    // (S3 로 업로드하는 함수)
    fun upload(
        // S3 에 저장할 파일 객체
        file: File,
        // S3 에 저장할 때 사용할 파일명 (ex : "test.png")
        fileSaveName: String,
        // 버킷 위치 (ex : test/my/bucket)
        bucketName: String
    ): String {
        amazonS3Client.putObject(bucketName, fileSaveName, file)

        // 업로드 된 파일의 다운로드 URL 반환
        return amazonS3Client.getUrl(bucketName, fileSaveName).toString()
    }

    // (S3 에 저장된 파일을 삭제하는 함수)
    fun delete(
        // 버킷 위치 (ex : test/my/bucket)
        bucketName: String,
        // 삭제할 파일명 (ex : "test.png")
        fileName: String
    ) {
        amazonS3Client.deleteObject(DeleteObjectRequest(bucketName, fileName))
    }

    // (S3 에 저장된 텍스트(Html 포함) 파일의 내용을 읽어오는 함수)
    fun getTextFileString(
        // 버킷 위치 (ex : test/my/bucket)
        bucketName: String,
        // 읽을 파일명 (ex : "test.png")
        fileName: String
    ): String {
        val s3Object: S3Object = amazonS3Client.getObject(bucketName, fileName)
        s3Object.objectContent.use { inputStream ->
            val testString = inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
            return testString
        }
    }
}