package com.raillylinker.springboot_mvc_template.controllers.c6_service1_tk_v1_test

import com.raillylinker.springboot_mvc_template.custom_dis.EmailSenderUtilDi
import com.raillylinker.springboot_mvc_template.custom_dis.NaverSmsUtilDi
import com.raillylinker.springboot_mvc_template.custom_objects.*
import jakarta.servlet.http.HttpServletResponse
import org.apache.fontbox.ttf.TTFParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class C6Service1TkV1TestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // 이메일 발송 유틸
    private val emailSenderUtilDi: EmailSenderUtilDi,
    // 네이버 메시지 발송 유틸
    private val naverSmsUtilDi: NaverSmsUtilDi,
    @Qualifier("kafkaProducer0") private val kafkaProducer0: KafkaTemplate<String, Any>,

    @Value("\${spring.boot.admin.client.instance.service-url}") private var serviceUrl: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse, inputVo: C6Service1TkV1TestController.Api1InputVo) {
        emailSenderUtilDi.sendMessageMail(
            inputVo.senderName,
            inputVo.receiverEmailAddressList.toTypedArray(),
            inputVo.carbonCopyEmailAddressList?.toTypedArray(),
            inputVo.subject,
            inputVo.message,
            null,
            inputVo.multipartFileList
        )

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse, inputVo: C6Service1TkV1TestController.Api2InputVo) {
        // CID 는 첨부파일을 보내는 것과 동일한 의미입니다.
        // 고로 전송시 서버 성능에 악영향을 끼칠 가능성이 크고, CID 처리도 번거로우므로, CDN 을 사용하고, CID 는 되도록 사용하지 마세요.
        emailSenderUtilDi.sendThymeLeafHtmlMail(
            inputVo.senderName,
            inputVo.receiverEmailAddressList.toTypedArray(),
            inputVo.carbonCopyEmailAddressList?.toTypedArray(),
            inputVo.subject,
            "template_c6_n2/html_email_sample",
            hashMapOf(
                Pair("message", inputVo.message)
            ),
            null,
            hashMapOf(
                "html_email_sample_css" to ClassPathResource("static/resource_c6_n2/html_email_sample.css"),
                "image_sample" to ClassPathResource("static/resource_c6_n2/image_sample.jpg")
            ),
            null,
            inputVo.multipartFileList
        )

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse, inputVo: C6Service1TkV1TestController.Api3InputVo) {
        val phoneNumberSplit = inputVo.phoneNumber.split(")") // ["82", "010-0000-0000"]

        // 국가 코드 (ex : 82)
        val countryCode = phoneNumberSplit[0]

        // 전화번호 (ex : "01000000000")
        val phoneNumber = (phoneNumberSplit[1].replace("-", "")).replace(" ", "")

        // SMS 전송
        naverSmsUtilDi.sendSms(
            NaverSmsUtilDi.SendSmsInputVo(
                countryCode,
                phoneNumber,
                inputVo.smsMessage
            )
        )

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api4(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api4InputVo
    ): C6Service1TkV1TestController.Api4OutputVo? {
        val excelData = ExcelFileUtilObject.readExcel(
            inputVo.excelFile.inputStream,
            inputVo.sheetIdx,
            inputVo.rowRangeStartIdx,
            inputVo.rowRangeEndIdx,
            inputVo.columnRangeIdxList,
            inputVo.minColumnLength
        )

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
        return C6Service1TkV1TestController.Api4OutputVo(
            excelData?.size ?: 0,
            excelData.toString()
        )
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse) {
        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 확장자 포함 파일명 생성
        val saveFileName = "temp_${timeString}.xlsx"

        // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
        val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()
        val file = fileTargetPath.toFile()

        val inputExcelSheetDataMap: HashMap<String, List<List<String>>> = hashMapOf()
        inputExcelSheetDataMap["testSheet1"] = listOf(
            listOf("1-1", "1-2", "1-3"),
            listOf("2-1", "2-2", "2-3"),
            listOf("3-1", "3-2", "3-3")
        )
        inputExcelSheetDataMap["testSheet2"] = listOf(
            listOf("1-1", "1-2"),
            listOf("2-1", "2-2")
        )

        ExcelFileUtilObject.writeExcel(file.outputStream(), inputExcelSheetDataMap)

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api6(
        httpServletResponse: HttpServletResponse
    ) {
        // thymeLeaf 엔진으로 파싱한 HTML String 가져오기
        // 여기서 가져온 HTML 내에 기입된 static resources 의 경로는 절대경로가 아님
        val htmlString = ThymeleafParserUtilObject.parseHtmlFileToHtmlString(
            "template_c6_n6/html_to_pdf_sample", // thymeLeaf Html 이름 (ModelAndView 의 사용과 동일)
            // thymeLeaf 에 전해줄 데이터 Map
            mapOf(
                "title" to "PDF 변환 테스트"
            )
        )

        // htmlString 을 PDF 로 변환하여 저장
        // XHTML 1.0(strict), CSS 2.1 (@page 의 size 는 가능)
        PdfGenerator.createPdfFileFromHtmlString(
            "./files/temp",
            "temp(${
                LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                )
            }).pdf",
            htmlString,
            arrayListOf(
                ClassPathResource("/static/resource_global/fonts/for_itext/NanumMyeongjo.ttf").url.toString()
            )
        )

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api6Dot1(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api6Dot1InputVo
    ): ResponseEntity<Resource>? {
        val savedFontFileNameMap: HashMap<String, String> = hashMapOf()
        val savedImgFileList: ArrayList<File> = arrayListOf()
        val savedImgFilePathMap: HashMap<String, String> = hashMapOf()

        // htmlString 을 PDF 로 변환하여 저장
        // XHTML 1.0(strict), CSS 2.1 (@page 의 size 는 가능)
        try {
            if (inputVo.fontFiles != null) {
                for (fontFile in inputVo.fontFiles) {
                    val multiPartFileNameString = StringUtils.cleanPath(fontFile.originalFilename!!)
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

                    if (fileExtension != "ttf") {
                        throw Exception("font File must be ttf")
                    }

                    val fontInputStream = fontFile.inputStream

                    val ttf = TTFParser().parse(fontInputStream)
                    val fontName: String = ttf.name
                    ttf.close()

                    val directoryPathStr = "src/main/resources/static/uploads/fonts"

                    val directoryPath = Paths.get(directoryPathStr)

                    if (!Files.exists(directoryPath)) {
                        Files.createDirectories(directoryPath)
                    }

                    val path: Path = Paths.get(directoryPathStr + File.separator + fontName + ".$fileExtension")

                    if (!Files.exists(path)) {
                        val bytes = fontFile.bytes
                        Files.write(path, bytes)
                    }

                    savedFontFileNameMap["$fileNameWithOutExtension.$fileExtension"] =
                        "${
                            if (serviceUrl.endsWith("/")) {
                                serviceUrl.dropLast(1)
                            } else {
                                serviceUrl
                            }
                        }/uploads/fonts/$fontName.$fileExtension"
                }
            }

            if (inputVo.imgFiles != null) {
                for (imgFile in inputVo.imgFiles) {
                    val multiPartFileNameString = StringUtils.cleanPath(imgFile.originalFilename!!)
                    val fileExtensionSplitIdx = multiPartFileNameString.lastIndexOf('.')

                    // 확장자가 없는 파일명
                    // 확장자
                    val fileExtension: String = if (fileExtensionSplitIdx == -1) {
                        ""
                    } else {
                        multiPartFileNameString.substring(fileExtensionSplitIdx + 1, multiPartFileNameString.length)
                    }
                    val tempFile: File = Files.createTempFile(null, ".$fileExtension").toFile()
                    imgFile.transferTo(tempFile)

                    savedImgFileList.add(tempFile)
                    savedImgFilePathMap[multiPartFileNameString] = tempFile.toString()
                }
            }

            val pdfByteArray = PdfGenerator.createPdfByteArrayFromHtmlString(
                String(inputVo.htmlFile.bytes, Charsets.UTF_8),
                null,
                savedFontFileNameMap,
                savedImgFilePathMap
            )

            httpServletResponse.status = HttpStatus.OK.value()
            httpServletResponse.setHeader("api-result-code", "0")
            return ResponseEntity<Resource>(
                InputStreamResource(pdfByteArray.inputStream()),
                HttpHeaders().apply {
                    this.contentDisposition = ContentDisposition.builder("attachment")
                        .filename(
                            "result(${
                                LocalDateTime.now().format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                                )
                            }).pdf", StandardCharsets.UTF_8
                        )
                        .build()
                    this.add(HttpHeaders.CONTENT_TYPE, "application/pdf")
                },
                HttpStatus.OK
            )
        } catch (e: Exception) {
            httpServletResponse.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            httpServletResponse.setHeader("api-msg", e.message)
            e.printStackTrace()
            return null
        } finally {
            for (imgFile in savedImgFileList) {
                val result = imgFile.delete()
                println("delete $imgFile : $result")
            }
        }
    }


    ////
    fun api7(httpServletResponse: HttpServletResponse, inputVo: C6Service1TkV1TestController.Api7InputVo) {
        // kafkaProducer1 에 토픽 메세지 발행
        kafkaProducer0.send(inputVo.topic, inputVo.message)

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
    }

    ////
    fun api8(
        httpServletResponse: HttpServletResponse,
        javaEnvironmentPath: String?
    ): C6Service1TkV1TestController.Api8OutputVo? {
        val javaEnv = javaEnvironmentPath ?: "java"

        // JAR 파일 실행 명령어 설정
        val javaJarPb = ProcessBuilder(javaEnv, "-jar", "./samples/JarExample/Counter.jar")
        javaJarPb.directory(File(".")) // 현재 작업 디렉토리 설정

        // 프로세스 시작
        val javaJarProcess = javaJarPb.start()

        // 프로세스의 출력 스트림 가져오기
        val inputStream: InputStream = javaJarProcess.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Read the result from the JAR execution
        val result = reader.readLine()?.toLong() ?: 0

        // 프로세스 종료 대기
        val exitCode = javaJarProcess.waitFor()
        println("Exit Code: $exitCode")

        // 자원 해제
        reader.close()
        inputStream.close()

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
        return C6Service1TkV1TestController.Api8OutputVo(
            result
        )
    }


    ////
    fun api9(
        httpServletResponse: HttpServletResponse,
        inputVo: C6Service1TkV1TestController.Api9InputVo
    ): C6Service1TkV1TestController.Api9OutputVo? {
        // MultipartFile에서 InputStream을 얻어옴
        val fontInputStream = inputVo.fontFile.inputStream

        val parser = TTFParser()
        val ttf = parser.parse(fontInputStream)
        val fontName: String = ttf.name
        ttf.close()

        httpServletResponse.status = HttpStatus.OK.value()
        httpServletResponse.setHeader("api-result-code", "0")
        return C6Service1TkV1TestController.Api9OutputVo(
            fontName
        )
    }
}