package com.raillylinker.springboot_mvc_template

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/*
     [런타임에 변경 가능한 설정 정보를 모아둔 Object]
     이곳에 저장되어 있는 각 public 전역변수들은 런타임에 변경 가능한 설정 데이터를 의미합니다.
     아래 데이터를 이용하는 로직을 작성한다면, 런타임에 언제든 변경이 가능하다는 것을 인지한 상태로 개발하세요.
 */
object ApplicationRuntimeConfigs {
    // (런타임 설정 데이터)
    var runtimeConfigData: RuntimeConfigData =
        // !!!설정 파일이 없을 때의 초기 설정!!!
        RuntimeConfigData(
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

    // (설정 데이터 저장)
    fun saveRuntimeConfigData(configJsonString: String): RuntimeConfigData? {
        val runtimeConfigJsonFile = File(ApplicationConstants.rootDirFile, "by_product_files/runtime_config.json")
        try {
            val newConfigObject: RuntimeConfigData = Gson().fromJson(
                configJsonString,
                object : TypeToken<RuntimeConfigData>() {}.type
            )
            runtimeConfigJsonFile.writeText(Gson().toJson(newConfigObject))
            runtimeConfigData = newConfigObject
            return runtimeConfigData
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // (설정 데이터 불러오기)
    fun loadRuntimeConfigData(): RuntimeConfigData? {
        val runtimeConfigJsonFile = File(ApplicationConstants.rootDirFile, "by_product_files/runtime_config.json")
        try {
            if (runtimeConfigJsonFile.exists()) {
                runtimeConfigData = Gson().fromJson(
                    runtimeConfigJsonFile.readText(),
                    object : TypeToken<RuntimeConfigData>() {}.type
                )
            } else {
                runtimeConfigJsonFile.writeText(Gson().toJson(runtimeConfigData))
            }
            return runtimeConfigData
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // (설정 데이터 VO)
    // !!!설정 정보 종류를 원하는대로 수정!!!
    data class RuntimeConfigData(
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