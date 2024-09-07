package com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis_main.tables

import com.raillylinker.springboot_mvc_template.configurations.redis_configs.RedisMainConfig

// (테스트 Redis Table)
// 키 : 임의 값
class Test(
    // 기본 변수 타입 String 사용 예시
    var content: String,
    // Object 변수 타입 사용 예시
    var innerVo: InnerVo,
    // Object List 변수 타입 사용 예시
    var innerVoList: List<InnerVo>
) {
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!!
        const val TABLE_NAME = "Test"

        // !!!사용하는 Redis 설정의 REDIS_CONFIG_NAME 할당하기!!!
        private const val TEMPLATE_NAME = RedisMainConfig.REDIS_CONFIG_NAME

        // Redis Transaction 이름
        const val TRANSACTION_NAME = "$TEMPLATE_NAME:$TABLE_NAME"
    }

    // 예시용 Object 데이터 클래스
    data class InnerVo(
        var testString: String,
        var testBoolean: Boolean
    )
}