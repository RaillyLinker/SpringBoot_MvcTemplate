package com.raillylinker.springboot_mvc_template.data_sources.file_and_memory_object

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raillylinker.springboot_mvc_template.data_sources.memory_const_object.ProjectConst
import java.io.File

object RuntimeConfig {
    // (JSON 파일 이름)
    // {프로젝트 경로}/by_product_files/{JSON_FILE_NAME} 폴더 안에 저장됩니다.
    // !!!저장 파일명을 입력하세요. (object 명을 snake 패턴으로)!!!
    private const val JSON_FILE_NAME = "runtime_config.json"

    // (런타임 설정 데이터)
    var linkedData: LinkedDataVo =
        // !!!설정 파일이 없을 때의 초기 설정값!!!
        LinkedDataVo(
            // Actuator 정보 접근 허용 IP 리스트
            arrayListOf(
                LinkedDataVo.ConfigIp(
                    "127.0.0.1",
                    "로컬 호스트"
                ),
                LinkedDataVo.ConfigIp(
                    "127.0.0.2",
                    "샘플"
                )
            ),

            // Logging Filter 의 로깅 대상에서 제외할 IP 리스트
            arrayListOf(
                LinkedDataVo.ConfigIp(
                    "127.0.0.2",
                    "샘플"
                ),
            )
        )

    // (설정 데이터 저장)
    // 입력받은 linkedDataVo 파라미터를 파일에 저장 및 linkedData 변수에 할당합니다.
    // 에러 발생 가능성이 있으므로 외부에서 try catch 필수
    fun saveToFile(linkedDataVo: LinkedDataVo) {
        File(ProjectConst.rootDirFile, "by_product_files/${JSON_FILE_NAME}").writeText(Gson().toJson(linkedDataVo))
        linkedData = linkedDataVo
    }

    // (JSON 파일에서 데이터 불러오기)
    // FILE_PATH 의 파일을 읽어들입니다.
    // 파일이 없다면 새로 만들어 linkedData 에 할당된 변수를 Json String 형식으로 저장하고,
    // 파일이 존재한다면 해당 파일의 내용을 해석하여 linkedData 에 할당할 것입니다.
    // 에러 발생 가능성이 있으므로 외부에서 try catch 필수
    fun loadFromFile(): LinkedDataVo {
        val linkedDataJsonFile = File(ProjectConst.rootDirFile, "by_product_files/${JSON_FILE_NAME}")
        if (linkedDataJsonFile.exists()) {
            linkedData = Gson().fromJson(
                linkedDataJsonFile.readText(),
                object : TypeToken<LinkedDataVo>() {}.type
            )
        } else {
            linkedDataJsonFile.writeText(Gson().toJson(linkedData))
        }
        return linkedData
    }

    // (데이터 VO)
    // !!!데이터 형태를 원하는대로 수정!!!
    data class LinkedDataVo(
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