package com.raillylinker.springboot_mvc_template.controllers.c2_service1_tk_v1_requestTest

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raillylinker.springboot_mvc_template.custom_classes.SseEmitterWrapper
import com.raillylinker.springboot_mvc_template.custom_objects.SseEmitterUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.util.StringUtils
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class C2Service1TkV1RequestTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // (스레드 풀)
    private val executorService: ExecutorService = Executors.newCachedThreadPool()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): String? {
        httpServletResponse.status = HttpStatus.OK.value()
        return activeProfile
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "redirect:/service1/tk/v1/request-test"

        return mv
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "forward:/service1/tk/v1/request-test"

        return mv
    }


    ////
    fun api4(
        httpServletResponse: HttpServletResponse,
        queryParamString: String,
        queryParamStringNullable: String?,
        queryParamInt: Int,
        queryParamIntNullable: Int?,
        queryParamDouble: Double,
        queryParamDoubleNullable: Double?,
        queryParamBoolean: Boolean,
        queryParamBooleanNullable: Boolean?,
        queryParamStringList: List<String>,
        queryParamStringListNullable: List<String>?
    ): C2Service1TkV1RequestTestController.Api4OutputVo? {
        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api4OutputVo(
            queryParamString,
            queryParamStringNullable,
            queryParamInt,
            queryParamIntNullable,
            queryParamDouble,
            queryParamDoubleNullable,
            queryParamBoolean,
            queryParamBooleanNullable,
            queryParamStringList,
            queryParamStringListNullable
        )
    }


    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        pathParamInt: Int
    ): C2Service1TkV1RequestTestController.Api5OutputVo? {
        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api5OutputVo(pathParamInt)
    }


    ////
    fun api6(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api6InputVo
    ): C2Service1TkV1RequestTestController.Api6OutputVo? {
        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api6OutputVo(
            inputVo.requestBodyString,
            inputVo.requestBodyStringNullable,
            inputVo.requestBodyInt,
            inputVo.requestBodyIntNullable,
            inputVo.requestBodyDouble,
            inputVo.requestBodyDoubleNullable,
            inputVo.requestBodyBoolean,
            inputVo.requestBodyBooleanNullable,
            inputVo.requestBodyStringList,
            inputVo.requestBodyStringListNullable
        )
    }


    ////
    fun api7(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api7InputVo
    ): C2Service1TkV1RequestTestController.Api7OutputVo? {
        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api7OutputVo(
            inputVo.requestFormString,
            inputVo.requestFormStringNullable,
            inputVo.requestFormInt,
            inputVo.requestFormIntNullable,
            inputVo.requestFormDouble,
            inputVo.requestFormDoubleNullable,
            inputVo.requestFormBoolean,
            inputVo.requestFormBooleanNullable,
            inputVo.requestFormStringList,
            inputVo.requestFormStringListNullable
        )
    }


    ////
    fun api8(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api8InputVo
    ): C2Service1TkV1RequestTestController.Api8OutputVo? {
        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./files/temp").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 원본 파일명(with suffix)
        val multiPartFileNameString = StringUtils.cleanPath(inputVo.multipartFile.originalFilename!!)

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

        // multipartFile 을 targetPath 에 저장
        inputVo.multipartFile.transferTo(
            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            saveDirectoryPath.resolve(
                "${fileNameWithOutExtension}(${
                    LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                    )
                }).$fileExtension"
            ).normalize()
        )

        if (inputVo.multipartFileNullable != null) {
            // 원본 파일명(with suffix)
            val multiPartFileNullableNameString =
                StringUtils.cleanPath(inputVo.multipartFileNullable.originalFilename!!)

            // 파일 확장자 구분 위치
            val nullableFileExtensionSplitIdx = multiPartFileNullableNameString.lastIndexOf('.')

            // 확장자가 없는 파일명
            val nullableFileNameWithOutExtension: String
            // 확장자
            val nullableFileExtension: String

            if (nullableFileExtensionSplitIdx == -1) {
                nullableFileNameWithOutExtension = multiPartFileNullableNameString
                nullableFileExtension = ""
            } else {
                nullableFileNameWithOutExtension =
                    multiPartFileNullableNameString.substring(0, nullableFileExtensionSplitIdx)
                nullableFileExtension =
                    multiPartFileNullableNameString.substring(
                        nullableFileExtensionSplitIdx + 1,
                        multiPartFileNullableNameString.length
                    )
            }

            // multipartFile 을 targetPath 에 저장
            inputVo.multipartFileNullable.transferTo(
                // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                saveDirectoryPath.resolve(
                    "${nullableFileNameWithOutExtension}(${
                        LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                        )
                    }).$nullableFileExtension"
                ).normalize()
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api8OutputVo(
            inputVo.requestFormString,
            inputVo.requestFormStringNullable,
            inputVo.requestFormInt,
            inputVo.requestFormIntNullable,
            inputVo.requestFormDouble,
            inputVo.requestFormDoubleNullable,
            inputVo.requestFormBoolean,
            inputVo.requestFormBooleanNullable,
            inputVo.requestFormStringList,
            inputVo.requestFormStringListNullable
        )
    }


    ////
    fun api9(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api9InputVo
    ): C2Service1TkV1RequestTestController.Api9OutputVo? {
        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./files/temp").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        for (multipartFile in inputVo.multipartFileList) {
            // 원본 파일명(with suffix)
            val multiPartFileNameString = StringUtils.cleanPath(multipartFile.originalFilename!!)

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

            // multipartFile 을 targetPath 에 저장
            multipartFile.transferTo(
                // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                saveDirectoryPath.resolve(
                    "${fileNameWithOutExtension}(${
                        LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                        )
                    }).$fileExtension"
                ).normalize()
            )
        }

        if (inputVo.multipartFileNullableList != null) {
            for (multipartFileNullable in inputVo.multipartFileNullableList) {
                // 원본 파일명(with suffix)
                val multiPartFileNullableNameString =
                    StringUtils.cleanPath(multipartFileNullable.originalFilename!!)

                // 파일 확장자 구분 위치
                val nullableFileExtensionSplitIdx = multiPartFileNullableNameString.lastIndexOf('.')

                // 확장자가 없는 파일명
                val nullableFileNameWithOutExtension: String
                // 확장자
                val nullableFileExtension: String

                if (nullableFileExtensionSplitIdx == -1) {
                    nullableFileNameWithOutExtension = multiPartFileNullableNameString
                    nullableFileExtension = ""
                } else {
                    nullableFileNameWithOutExtension =
                        multiPartFileNullableNameString.substring(0, nullableFileExtensionSplitIdx)
                    nullableFileExtension =
                        multiPartFileNullableNameString.substring(
                            nullableFileExtensionSplitIdx + 1,
                            multiPartFileNullableNameString.length
                        )
                }

                // multipartFile 을 targetPath 에 저장
                multipartFileNullable.transferTo(
                    // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                    saveDirectoryPath.resolve(
                        "${nullableFileNameWithOutExtension}(${
                            LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                            )
                        }).$nullableFileExtension"
                    ).normalize()
                )
            }
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api9OutputVo(
            inputVo.requestFormString,
            inputVo.requestFormStringNullable,
            inputVo.requestFormInt,
            inputVo.requestFormIntNullable,
            inputVo.requestFormDouble,
            inputVo.requestFormDoubleNullable,
            inputVo.requestFormBoolean,
            inputVo.requestFormBooleanNullable,
            inputVo.requestFormStringList,
            inputVo.requestFormStringListNullable
        )
    }


    ////
    fun api10(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1RequestTestController.Api10InputVo
    ): C2Service1TkV1RequestTestController.Api10OutputVo? {
        // input Json String to Object
        val inputJsonObject = Gson().fromJson<C2Service1TkV1RequestTestController.Api10InputVo.InputJsonObject>(
            inputVo.jsonString, // 해석하려는 json 형식의 String
            object :
                TypeToken<C2Service1TkV1RequestTestController.Api10InputVo.InputJsonObject>() {}.type // 파싱할 데이터 객체 타입
        )

        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./files/temp").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 원본 파일명(with suffix)
        val multiPartFileNameString = StringUtils.cleanPath(inputVo.multipartFile.originalFilename!!)

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

        // multipartFile 을 targetPath 에 저장
        inputVo.multipartFile.transferTo(
            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            saveDirectoryPath.resolve(
                "${fileNameWithOutExtension}(${
                    LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                    )
                }).$fileExtension"
            ).normalize()
        )

        if (inputVo.multipartFileNullable != null) {
            // 원본 파일명(with suffix)
            val multiPartFileNullableNameString =
                StringUtils.cleanPath(inputVo.multipartFileNullable.originalFilename!!)

            // 파일 확장자 구분 위치
            val nullableFileExtensionSplitIdx = multiPartFileNullableNameString.lastIndexOf('.')

            // 확장자가 없는 파일명
            val nullableFileNameWithOutExtension: String
            // 확장자
            val nullableFileExtension: String

            if (nullableFileExtensionSplitIdx == -1) {
                nullableFileNameWithOutExtension = multiPartFileNullableNameString
                nullableFileExtension = ""
            } else {
                nullableFileNameWithOutExtension =
                    multiPartFileNullableNameString.substring(0, nullableFileExtensionSplitIdx)
                nullableFileExtension =
                    multiPartFileNullableNameString.substring(
                        nullableFileExtensionSplitIdx + 1,
                        multiPartFileNullableNameString.length
                    )
            }

            // multipartFile 을 targetPath 에 저장
            inputVo.multipartFileNullable.transferTo(
                // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                saveDirectoryPath.resolve(
                    "${nullableFileNameWithOutExtension}(${
                        LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                        )
                    }).$nullableFileExtension"
                ).normalize()
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api10OutputVo(
            inputJsonObject.requestFormString,
            inputJsonObject.requestFormStringNullable,
            inputJsonObject.requestFormInt,
            inputJsonObject.requestFormIntNullable,
            inputJsonObject.requestFormDouble,
            inputJsonObject.requestFormDoubleNullable,
            inputJsonObject.requestFormBoolean,
            inputJsonObject.requestFormBooleanNullable,
            inputJsonObject.requestFormStringList,
            inputJsonObject.requestFormStringListNullable
        )
    }


    ////
    fun api11(httpServletResponse: HttpServletResponse) {
        throw RuntimeException("Test Error")
    }

    ////
    fun api12(
        httpServletResponse: HttpServletResponse,
        errorType: C2Service1TkV1RequestTestController.Api12ErrorTypeEnum?
    ) {
        if (errorType == null) {
            httpServletResponse.status = HttpStatus.OK.value()
        } else {
            when (errorType) {
                C2Service1TkV1RequestTestController.Api12ErrorTypeEnum.A -> {
                    httpServletResponse.status = HttpStatus.NO_CONTENT.value()
                    httpServletResponse.setHeader("api-result-code", "1")
                }

                C2Service1TkV1RequestTestController.Api12ErrorTypeEnum.B -> {
                    httpServletResponse.status = HttpStatus.NO_CONTENT.value()
                    httpServletResponse.setHeader("api-result-code", "2")
                }

                C2Service1TkV1RequestTestController.Api12ErrorTypeEnum.C -> {
                    httpServletResponse.status = HttpStatus.NO_CONTENT.value()
                    httpServletResponse.setHeader("api-result-code", "3")
                }
            }
        }
    }


    ////
    fun api13(httpServletResponse: HttpServletResponse, delayTimeSec: Long) {
        val endTime = System.currentTimeMillis() + (delayTimeSec * 1000)

        while (System.currentTimeMillis() < endTime) {
            // 아무 것도 하지 않고 대기
            Thread.sleep(100)  // 100ms마다 스레드를 잠들게 하여 CPU 사용률을 줄임
        }

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api14(httpServletResponse: HttpServletResponse): String? {
        httpServletResponse.status = HttpStatus.OK.value()
        return "test Complete!"
    }


    ////
    fun api15(httpServletResponse: HttpServletResponse): ModelAndView? {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "template_c2_n15/html_response_example"

        httpServletResponse.status = HttpStatus.OK.value()
        return modelAndView
    }


    ////
    fun api16(httpServletResponse: HttpServletResponse): Resource? {
        httpServletResponse.status = HttpStatus.OK.value()
        return ByteArrayResource(
            byteArrayOf(
                'a'.code.toByte(),
                'b'.code.toByte(),
                'c'.code.toByte(),
                'd'.code.toByte(),
                'e'.code.toByte(),
                'f'.code.toByte()
            )
        )
    }


    ////
    fun api17(
        videoHeight: C2Service1TkV1RequestTestController.Api17VideoHeight,
        httpServletResponse: HttpServletResponse
    ): Resource? {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val serverFileAbsolutePathString = "$projectRootAbsolutePathString/src/main/resources/static/resource_c2_n17"

        // 멤버십 등의 정보로 해상도 제한을 걸 수도 있음
        val serverFileNameString =
            when (videoHeight) {
                C2Service1TkV1RequestTestController.Api17VideoHeight.H240 -> {
                    "test_240p.mp4"
                }

                C2Service1TkV1RequestTestController.Api17VideoHeight.H360 -> {
                    "test_360p.mp4"
                }

                C2Service1TkV1RequestTestController.Api17VideoHeight.H480 -> {
                    "test_480p.mp4"
                }

                C2Service1TkV1RequestTestController.Api17VideoHeight.H720 -> {
                    "test_720p.mp4"
                }
            }

        // 반환값에 전해줄 FIS
        val fileInputStream = FileInputStream("$serverFileAbsolutePathString/$serverFileNameString")

        httpServletResponse.status = HttpStatus.OK.value()
        return ByteArrayResource(FileCopyUtils.copyToByteArray(fileInputStream))
    }


    ////
    fun api18(httpServletResponse: HttpServletResponse): Resource? {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val serverFileAbsolutePathString = "$projectRootAbsolutePathString/src/main/resources/static/resource_c2_n18"
        val serverFileNameString = "test.mp3"

        // 반환값에 전해줄 FIS
        val fileInputStream = FileInputStream("$serverFileAbsolutePathString/$serverFileNameString")

        httpServletResponse.status = HttpStatus.OK.value()
        return ByteArrayResource(FileCopyUtils.copyToByteArray(fileInputStream))
    }


    ////
    fun api19(httpServletResponse: HttpServletResponse): DeferredResult<C2Service1TkV1RequestTestController.Api19OutputVo>? {
        // 연결 타임아웃 밀리초
        val deferredResultTimeoutMs = 1000L * 60
        val deferredResult = DeferredResult<C2Service1TkV1RequestTestController.Api19OutputVo>(deferredResultTimeoutMs)

        // 비동기 처리
        executorService.execute {
            // 지연시간 대기
            val delayMs = 5000L
            Thread.sleep(delayMs)

            // 결과 반환
            deferredResult.setResult(C2Service1TkV1RequestTestController.Api19OutputVo("${delayMs / 1000} 초 경과 후 반환했습니다."))
        }

        // 결과 대기 객체를 먼저 반환
        httpServletResponse.status = HttpStatus.OK.value()
        return deferredResult
    }


    ////
    // api20 에서 발급한 Emitter 객체
    private val api20SseEmitterWrapperMbr = SseEmitterWrapper(1000L * 10L)
    fun api20(httpServletResponse: HttpServletResponse, lastSseEventId: String?): SseEmitter? {
        api20SseEmitterWrapperMbr.emitterMapSemaphore.acquire()
        api20SseEmitterWrapperMbr.emitterEventMapSemaphore.acquire()

        val emitterPublishCount = api20SseEmitterWrapperMbr.emitterPublishSequence++

        // 수신 객체 아이디 (발행총개수_발행일_멤버고유번호)
        val sseEmitterId =
            "${emitterPublishCount}_${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Date())}"

        // 수신 객체
        val sseEmitter = api20SseEmitterWrapperMbr.getSseEmitter(sseEmitterId, lastSseEventId)

        api20SseEmitterWrapperMbr.emitterEventMapSemaphore.release()
        api20SseEmitterWrapperMbr.emitterMapSemaphore.release()

        httpServletResponse.status = HttpStatus.OK.value()
        return sseEmitter
    }


    ////
    private var api21TriggerTestCountMbr = 0
    fun api21(httpServletResponse: HttpServletResponse) {
        // emitter 이벤트 전송
        api20SseEmitterWrapperMbr.emitterMapSemaphore.acquire()
        api20SseEmitterWrapperMbr.emitterEventMapSemaphore.acquire()
        val nowTriggerTestCount = ++api21TriggerTestCountMbr

        for (emitter in api20SseEmitterWrapperMbr.emitterMap) { // 저장된 모든 emitter 에 발송 (필터링 하려면 emitter.key 에 저장된 정보로 필터링 가능)
            // 발송 시간
            val dateString = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Date())

            // 이벤트 고유값 생성 (이미터고유값/발송시간)
            val eventId = "${emitter.key}/${dateString}"

            // 이벤트 빌더 생성
            val sseEventBuilder = SseEmitterUtilObject.makeSseEventBuilder(
                "triggerTest", // 이벤트 그룹명
                eventId, // 이벤트 고유값
                "trigger $nowTriggerTestCount" // 전달 데이터
            )

            // 이벤트 누락 방지 처리를 위하여 이벤트 빌더 기록
            if (api20SseEmitterWrapperMbr.emitterEventMap.containsKey(emitter.key)) {
                api20SseEmitterWrapperMbr.emitterEventMap[emitter.key]!!.add(Pair(dateString, sseEventBuilder))
            } else {
                api20SseEmitterWrapperMbr.emitterEventMap[emitter.key] = arrayListOf(Pair(dateString, sseEventBuilder))
            }

            // 이벤트 발송
            SseEmitterUtilObject.sendSseEvent(
                emitter.value,
                sseEventBuilder
            )
        }

        api20SseEmitterWrapperMbr.emitterEventMapSemaphore.release()
        api20SseEmitterWrapperMbr.emitterMapSemaphore.release()

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api22(
        httpServletResponse: HttpServletResponse,
        stringList: List<String>,
        inputVo: C2Service1TkV1RequestTestController.Api22InputVo
    ): C2Service1TkV1RequestTestController.Api22OutputVo? {

        httpServletResponse.status = HttpStatus.OK.value()
        return C2Service1TkV1RequestTestController.Api22OutputVo(
            stringList,
            inputVo.requestBodyStringList
        )
    }
}