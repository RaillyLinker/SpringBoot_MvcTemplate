package com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis1_main

import com.raillylinker.springboot_mvc_template.configurations.redis_configs.Redis1MainConfig
import com.raillylinker.springboot_mvc_template.custom_classes.BasicRedisType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

// [Redis Type 컴포넌트 - Test]
@Component
class Redis1_Test(
    // !!!RedisConfig 종류 변경!!!
    @Qualifier(Redis1MainConfig.REDIS_CONFIG_NAME) private val redisTemplate: RedisTemplate<String, Any>
) : BasicRedisType<Redis1_Test.ValueVo>(redisTemplate, TYPE_NAME, ValueVo::class.java) {
    // <멤버 변수 공간>
    companion object {
        // !!!중복되지 않도록, Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!!
        const val TYPE_NAME = "Redis1_Test"

        // Redis Transaction 이름
        // !!!RedisConfig 종류 변경!!!
        const val TRANSACTION_NAME = "${Redis1MainConfig.REDIS_CONFIG_NAME}:$TYPE_NAME"
    }

    // !!!본 Redis Type 의 Value 클래스 설정!!!
    class ValueVo(
        // 기본 변수 타입 String 사용 예시
        var content: String,
        // Object 변수 타입 사용 예시
        var innerVo: InnerVo,
        // Object List 변수 타입 사용 예시
        var innerVoList: List<InnerVo>
    ) {
        // 예시용 Object 데이터 클래스
        data class InnerVo(
            var testString: String,
            var testBoolean: Boolean
        )
    }
}