package com.raillylinker.springboot_mvc_template

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_RuntimeConfigDataForActuatorAllowIpRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_RuntimeConfigDataForLoggingDenyIpRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_RuntimeConfigDataRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_RuntimeConfigData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_RuntimeConfigDataForActuatorAllowIp
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_RuntimeConfigDataForLoggingDenyIp

/*
     [런타임에 변경 가능한 설정 정보를 모아둔 Object]
     이곳에 저장되어 있는 각 public 전역변수들은 런타임에 변경 가능한 설정 데이터를 의미합니다.
     아래 데이터를 이용하는 로직을 작성한다면, 런타임에 언제든 변경이 가능하다는 것을 인지한 상태로 개발하시고,
     런타임 설정을 변경 하려면 각 데이터 소스(파일, 데이터베이스, etc...)의 데이터를 수정한 후, 각 load 함수를 사용하여 설정 데이터를 갱신하면 됩니다.
     외부에서 갱신이 쉽도록, 갱신 함수는 C1 의 API 로 제공하면 좋습니다.

    !!!런타임 설정을 추가한다면, 아래에 추가, ApplicationMain 에서 load, C1 에 런타임 로딩 API 를 추가 해주면 됩니다.!!!
 */
object ApplicationRuntimeConfigs {
    // (런타임 설정 데이터)
    var runtimeConfigData: RuntimeConfigData =
        // !!!설정 파일이 없을 때의 초기 설정!!!
        RuntimeConfigData(
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
            "com.raillylinker.springboot_mvc_template.service1",

            // Actuator 정보 접근 허용 IP 리스트
            arrayListOf(
                RuntimeConfigData.ConfigIp(
                    "127.0.0.1",
                    "로컬 호스트"
                ),
                RuntimeConfigData.ConfigIp(
                    "127.0.0.2",
                    "샘플"
                )
            ),

            // Logging Filter 의 로깅 대상에서 제외할 IP 리스트
            arrayListOf(
                RuntimeConfigData.ConfigIp(
                    "127.0.0.2",
                    "샘플"
                ),
            )
        )

    // (설정 데이터 불러오기)
    fun loadRuntimeConfigData(
        database1Service1RuntimeConfigDataRepository: Database1_Service1_RuntimeConfigDataRepository,
        database1Service1RuntimeConfigDataForActuatorAllowIpRepository: Database1_Service1_RuntimeConfigDataForActuatorAllowIpRepository,
        database1Service1RuntimeConfigDataForLoggingDenyIpRepository: Database1_Service1_RuntimeConfigDataForLoggingDenyIpRepository
    ): RuntimeConfigData {
        val entityList = database1Service1RuntimeConfigDataRepository.findAllByOrderByUid()

        if (entityList.isEmpty()) {
            // 기존에 저장된 데이터가 없음 -> 데이터 새로 만들기
            val runtimeConfigDataEntity = database1Service1RuntimeConfigDataRepository.save(
                Database1_Service1_RuntimeConfigData(
                    runtimeConfigData.authJwtSecretKeyString,
                    runtimeConfigData.authJwtAccessTokenExpirationTimeSec,
                    runtimeConfigData.authJwtRefreshTokenExpirationTimeSec,
                    runtimeConfigData.authJwtClaimsAes256InitializationVector,
                    runtimeConfigData.authJwtClaimsAes256EncryptionKey,
                    runtimeConfigData.authJwtIssuer
                )
            )

            database1Service1RuntimeConfigDataForActuatorAllowIpRepository.deleteAll()
            for (actuatorAllowIp in runtimeConfigData.actuatorAllowIpList) {
                database1Service1RuntimeConfigDataForActuatorAllowIpRepository.save(
                    Database1_Service1_RuntimeConfigDataForActuatorAllowIp(
                        runtimeConfigDataEntity,
                        actuatorAllowIp.ipString,
                        actuatorAllowIp.ipDesc
                    )
                )
            }

            database1Service1RuntimeConfigDataForLoggingDenyIpRepository.deleteAll()
            for (loggingDenyIp in runtimeConfigData.loggingDenyIpList) {
                database1Service1RuntimeConfigDataForLoggingDenyIpRepository.save(
                    Database1_Service1_RuntimeConfigDataForLoggingDenyIp(
                        runtimeConfigDataEntity,
                        loggingDenyIp.ipString,
                        loggingDenyIp.ipDesc
                    )
                )
            }
        } else {
            // 기존에 데이터가 있음 -> 첫번째 행 데이터를 사용하기
            val chosenEntity = entityList[0]

            val actuatorAllowIpEntityList =
                database1Service1RuntimeConfigDataForActuatorAllowIpRepository.findAllByRuntimeConfigDataOrderByUid(
                    chosenEntity
                )
            val actuatorAllowIpList: ArrayList<RuntimeConfigData.ConfigIp> = arrayListOf()
            for (actuatorAllowIpEntity in actuatorAllowIpEntityList) {
                actuatorAllowIpList.add(
                    RuntimeConfigData.ConfigIp(
                        actuatorAllowIpEntity.ipString,
                        actuatorAllowIpEntity.ipDesc
                    )
                )
            }

            val loggingIpEntityList =
                database1Service1RuntimeConfigDataForLoggingDenyIpRepository.findAllByRuntimeConfigDataOrderByUid(
                    chosenEntity
                )
            val loggingIpList: ArrayList<RuntimeConfigData.ConfigIp> = arrayListOf()
            for (loggingIpEntity in loggingIpEntityList) {
                loggingIpList.add(
                    RuntimeConfigData.ConfigIp(
                        loggingIpEntity.ipString,
                        loggingIpEntity.ipDesc
                    )
                )
            }

            runtimeConfigData = RuntimeConfigData(
                chosenEntity.authJwtSecretKeyString,
                chosenEntity.authJwtAccessTokenExpirationTimeSec,
                chosenEntity.authJwtRefreshTokenExpirationTimeSec,
                chosenEntity.authJwtClaimsAes256InitializationVector,
                chosenEntity.authJwtClaimsAes256EncryptionKey,
                chosenEntity.authJwtIssuer,
                actuatorAllowIpList,
                loggingIpList
            )
        }

        return runtimeConfigData
    }

    // (설정 데이터 VO)
    // !!!설정 정보 종류를 원하는대로 수정!!!
    data class RuntimeConfigData(
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
        var authJwtIssuer: String,

        // Actuator 정보 접근 허용 IP 리스트
        val actuatorAllowIpList: List<ConfigIp>,

        // Logging Filter 의 로깅 대상에서 제외할 IP 리스트
        val loggingDenyIpList: List<ConfigIp>
    ) {
        data class ConfigIp(
            val ipString: String,
            val ipDesc: String
        )
    }
}