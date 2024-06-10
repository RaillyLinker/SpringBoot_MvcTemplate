package com.raillylinker.springboot_mvc_template.custom_objects

import com.google.gson.Gson
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// [런타임에 변경 가능한 설정 정보를 모아둔 Object]
object RuntimeConfigObject {
    // (런타임 설정 데이터 객체)
    var runtimeConfigVo: RuntimeConfigVo =
        // 설정 파일이 없을 때의 초기 설정
        RuntimeConfigVo(
            arrayListOf(
                "127.0.0.1",
                "127.0.0.3"
            ),
            arrayListOf(
                "127.0.0.2"
            )
        )

    // Gson 객체
    private val gson = Gson()

    // 저장 디렉토리 경로
    private val saveDirectoryPath: Path = Paths.get("./by_product_files").toAbsolutePath().normalize()

    // 저장 파일 경로
    private val configFilePath: Path = saveDirectoryPath.resolve("runtime_config.json")

    // (설정 파일에서 runtimeConfigVo 변수로 데이터를 불러오기)
    fun loadFromConfigFile() {
        // 파일 존재 여부 확인
        if (Files.exists(configFilePath)) {
            runtimeConfigVo = gson.fromJson(Files.readString(configFilePath), RuntimeConfigVo::class.java)
        } else {
            // 설정 파일이 존재하지 않는다면 파일 생성
            saveToConfigFile()
        }
    }

    // (runtimeConfigVo 변수의 데이터를 설정 파일로 저장)
    private fun saveToConfigFile() {
        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 기존 파일이 있다면 삭제
        if (Files.exists(configFilePath)) {
            Files.delete(configFilePath)
        }

        // 설정 파일에 데이터 저장
        Files.write(configFilePath, gson.toJson(runtimeConfigVo).toByteArray())
    }

    // (런타임 설정 데이터 VO)
    data class RuntimeConfigVo(
        // Actuator 정보 접근 허용 IP 리스트
        val actuatorAllowIpList: List<String>,

        // Logging Filter 의 로깅 대상에서 제외할 IP 리스트
        val loggingDenyIpList: List<String>
    )
}