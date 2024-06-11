package com.raillylinker.springboot_mvc_template.custom_objects

import com.google.gson.Gson
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// [런타임에 변경 가능한 설정 정보를 모아둔 Object]
object RuntimeConfigObject {
    // (런타임 설정 데이터 객체)
    var runtimeConfig: RuntimeConfig =
        // 설정 파일이 없을 때의 초기 설정
        RuntimeConfig(
            // Actuator 정보 접근 허용 IP 리스트
            arrayListOf(
                "127.0.0.1",
                "127.0.0.3"
            ),

            // Logging Filter 의 로깅 대상에서 제외할 IP 리스트
            arrayListOf(
                "127.0.0.2"
            )
        )


    // -----------------------------------------------------------------------------------------------------------------
    // (저장 디렉토리 경로)
    private val saveDirectoryPath: Path = Paths.get("./by_product_files").toAbsolutePath().normalize()

    // (런타임 설정 파일 경로)
    private val runtimeConfigFilePath: Path = saveDirectoryPath.resolve("runtime_config.json")


    // -----------------------------------------------------------------------------------------------------------------
    // (런타임 설정 데이터를 불러오기)
    fun loadRuntimeConfig() {
        // 파일 저장 기본 디렉토리 없으면 생성
        Files.createDirectories(saveDirectoryPath)

        // (configFilePath 설정 정보 가져오기)
        if (Files.exists(runtimeConfigFilePath)) {
            // 설정 파일이 존재 -> 변수에 할당
            runtimeConfig = Gson().fromJson(Files.readString(runtimeConfigFilePath), RuntimeConfig::class.java)
        } else {
            // 설정 파일이 존재하지 않음
            // 설정 파일에 현재 변수의 데이터 저장
            Files.write(runtimeConfigFilePath, Gson().toJson(runtimeConfig).toByteArray())
        }
    }


    // -----------------------------------------------------------------------------------------------------------------
    // (런타임 설정 데이터 VO)
    data class RuntimeConfig(
        // Actuator 정보 접근 허용 IP 리스트
        val actuatorAllowIpList: List<String>,

        // Logging Filter 의 로깅 대상에서 제외할 IP 리스트
        val loggingDenyIpList: List<String>
    )
}