package com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis1.tables

import com.raillylinker.springboot_mvc_template.configurations.RedisConfig

// (테스트 Redis Table)
// 키 : 임의 값
data class Redis1_Test(
    var content: String,
    var innerVo: InnerVo,
    var innerVoList: List<InnerVo>
) {
    companion object {
        // !!!Redis Template 이름 설정!!!
        private const val TEMPLATE_NAME = RedisConfig.TN_REDIS1

        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!!
        const val TABLE_NAME = "Redis1_Test"

        // Redis Transaction 이름
        const val TRANSACTION_NAME = "$TEMPLATE_NAME:$TABLE_NAME"
    }

    data class InnerVo(
        var testString: String,
        var testBoolean: Boolean
    )
}