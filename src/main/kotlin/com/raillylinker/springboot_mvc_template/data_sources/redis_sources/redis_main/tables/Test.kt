package com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis_main.tables

import com.raillylinker.springboot_mvc_template.configurations.redis_configs.RedisMainConfig

// (테스트 Redis Table)
// 키 : 임의 값
data class Test(
    var content: String,
    var innerVo: InnerVo,
    var innerVoList: List<InnerVo>
) {
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!!
        const val TABLE_NAME = "Test"

        // Redis Template 이름 설정
        private const val TEMPLATE_NAME = RedisMainConfig.REDIS_CONFIG_NAME

        // Redis Transaction 이름
        const val TRANSACTION_NAME = "$TEMPLATE_NAME:$TABLE_NAME"
    }

    data class InnerVo(
        var testString: String,
        var testBoolean: Boolean
    )
}