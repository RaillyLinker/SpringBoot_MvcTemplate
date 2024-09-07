package com.raillylinker.springboot_mvc_template.data_sources

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/*
     [런타임에 변경 가능한 설정 정보를 모아둔 Object]
     이곳에 저장되어 있는 runtimeConfigData 변수를 전역변수로 사용하면 됩니다.
     이 데이터는 by_product_files/runtime_config.json 이 파일에 Json 형식으로 저장되고,
     by_product_files/runtime_config.json 파일에서 runtimeConfigData 변수로 데이터가 불러와지므로,
     외부에서 런타임으로 이 설정을 바꾸시려면, by_product_files/runtime_config.json 파일의 내용을 변경 후,
     loadRuntimeConfigData 함수를 호출하시면 됩니다.

     여기서, 굳이 메모리상의 데이터를 그냥 코드로 조작하는 것이 아니라, runtime_config.json 파일의 내용으로 조작하는 이유는,
     파일의 비휘발성 특성을 이용하기 위한 것입니다.

     재배포가 이루어 졌을 때도, ApplicationMain 에서 API 가 오픈되기 전에 loadRuntimeConfigData 를 호출하여 설정 파일에서
     runtimeConfigData 변수로, 저장된 내용을 불러올 것입니다.
 */
object RuntimeConfig {
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
    // 입력받은 configJsonString 파라미터로 RuntimeConfigData 객체를 만들고,
    // 이를 설정 파일에 저장 및 runtimeConfigData 변수에 할당합니다.
    // 만약 configJsonString 의 JSON 형식이 다르거나 하여 에러가 일어났다면,
    // 설정 파일과 설정 변수는 아무 변화가 없이 넘어갈 것입니다.
    fun saveRuntimeConfigData(configJsonString: String): RuntimeConfigData? {
        val runtimeConfigJsonFile = File(GlobalVariables.rootDirFile, "by_product_files/runtime_config.json")
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
    // by_product_files/runtime_config.json 파일을 읽어들여,
    // 파일이 없다면 새로 만들어 위에 선언된 runtimeConfigData 변수를 Json 형식으로 파일에 저장하고,
    // 파일이 존재한다면 해당 파일의 내용을 해석하여 runtimeConfigData 에 할당할 것입니다.
    // 만약 불러오려는 설정 파일 내의 JSON 형식이 잘못되는 등의 일로인하여 에러가 나버린다면,
    // 설정 파일과 설정 변수는 아무 변화가 없이 넘어갈 것입니다.
    fun loadRuntimeConfigData(): RuntimeConfigData? {
        val runtimeConfigJsonFile = File(GlobalVariables.rootDirFile, "by_product_files/runtime_config.json")
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