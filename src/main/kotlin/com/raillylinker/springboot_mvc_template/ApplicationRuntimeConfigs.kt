package com.raillylinker.springboot_mvc_template

import com.google.gson.Gson
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_Service1RuntimeConfigDataRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_Service1RuntimeConfigData
import java.nio.file.Files
import java.nio.file.Paths

// [런타임에 변경 가능한 설정 정보를 모아둔 Object]
object ApplicationRuntimeConfigs {
    // (런타임 설정 데이터)
    // 범용적인 런타임 설정 데이터는 이곳에 있습니다.
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


    // (설정 데이터 불러오기)
    fun loadRuntimeConfig() {
        // (설정 파일 저장 디렉토리 경로)
        val saveDirectoryPath = Paths.get("./by_product_files/configs").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 없으면 생성
        Files.createDirectories(saveDirectoryPath)

        // 런타임 설정 파일 경로
        val runtimeConfigFilePath = saveDirectoryPath.resolve("runtime_config.json")

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

    // (설정 데이터 VO)
    data class RuntimeConfig(
        // Actuator 정보 접근 허용 IP 리스트
        val actuatorAllowIpList: List<String>,

        // Logging Filter 의 로깅 대상에서 제외할 IP 리스트
        val loggingDenyIpList: List<String>
    )


    // -----------------------------------------------------------------------------------------------------------------
    // (service1 런타임 설정 데이터)
    // service1 의 런타임 설정 데이터는 이곳에 있습니다.
    var service1RuntimeConfig: Service1RuntimeConfig =
        // 설정 파일이 없을 때의 초기 설정
        Service1RuntimeConfig(
            // 계정 설정 - JWT 비밀키
            "123456789abcdefghijklmnopqrstuvw",
            // 계정 설정 - JWT AccessToken 유효기간(초)
            60L * 30L,// 30분
            // 계정 설정 - JWT RefreshToken 유효기간(초)
            60L * 60L * 24L * 7L,// 7일
            // 계정 설정 - JWT 본문 암호화 AES256 IV 16자
            "odkejduc726dj48d",
            // 계정 설정 - JWT 본문 암호화 AES256 암호키 32자
            "8fu3jd0ciiu3384hfucy36dye9sjv7b3",
            // 계정 설정 - JWT 발행자
            "com.raillylinker.springboot_mvc_template.service1"
        )


    // (설정 데이터 불러오기)
    fun loadService1RuntimeConfig(
        database1Service1Service1RuntimeConfigDataRepository: Database1_Service1_Service1RuntimeConfigDataRepository
    ) {
        val entityList = database1Service1Service1RuntimeConfigDataRepository.findAll()

        if (entityList.isEmpty()) {
            // 기존에 저장된 데이터가 없음 -> 데이터 새로 만들기
            database1Service1Service1RuntimeConfigDataRepository.save(
                Database1_Service1_Service1RuntimeConfigData(
                    service1RuntimeConfig.authJwtSecretKeyString,
                    service1RuntimeConfig.authJwtAccessTokenExpirationTimeSec,
                    service1RuntimeConfig.authJwtRefreshTokenExpirationTimeSec,
                    service1RuntimeConfig.authJwtClaimsAes256InitializationVector,
                    service1RuntimeConfig.authJwtClaimsAes256EncryptionKey,
                    service1RuntimeConfig.authJwtIssuer
                )
            )
        } else {
            // 기존에 데이터가 있음 -> 첫번째 행 데이터를 사용하기
            val chosenEntity = entityList[0]

            service1RuntimeConfig = Service1RuntimeConfig(
                chosenEntity.authJwtSecretKeyString,
                chosenEntity.authJwtAccessTokenExpirationTimeSec,
                chosenEntity.authJwtRefreshTokenExpirationTimeSec,
                chosenEntity.authJwtClaimsAes256InitializationVector,
                chosenEntity.authJwtClaimsAes256EncryptionKey,
                chosenEntity.authJwtIssuer
            )
        }
    }

    // (설정 데이터 VO)
    data class Service1RuntimeConfig(
        // 계정 설정 - JWT 비밀키
        var authJwtSecretKeyString: String,
        // 계정 설정 - JWT AccessToken 유효기간(초)
        var authJwtAccessTokenExpirationTimeSec: Long,
        // 계정 설정 - JWT RefreshToken 유효기간(초)
        var authJwtRefreshTokenExpirationTimeSec: Long,
        // 계정 설정 - JWT 본문 암호화 AES256 IV 16자
        var authJwtClaimsAes256InitializationVector: String,
        // 계정 설정 - JWT 본문 암호화 AES256 암호키 32자
        var authJwtClaimsAes256EncryptionKey: String,
        // 계정 설정 - JWT 발행자
        var authJwtIssuer: String
    )
}