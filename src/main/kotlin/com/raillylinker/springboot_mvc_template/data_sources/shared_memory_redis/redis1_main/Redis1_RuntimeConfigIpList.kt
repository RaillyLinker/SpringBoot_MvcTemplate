package com.raillylinker.springboot_mvc_template.data_sources.shared_memory_redis.redis1_main

import com.raillylinker.springboot_mvc_template.configurations.redis_configs.Redis1MainConfig
import com.raillylinker.springboot_mvc_template.custom_classes.BasicRedisMap
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

// [RedisMap 컴포넌트]
// 프로젝트 내부에서 사용할 IP 관련 설정 저장 타입입니다.
// key :
// actuatorAllowIpList : Actuator 정보 접근 허용 IP 리스트
// loggingDenyIpList : Logging Filter 의 로깅 대상에서 제외할 IP 리스트
@Component
class Redis1_RuntimeConfigIpList(
    // !!!RedisConfig 종류 변경!!!
    @Qualifier(Redis1MainConfig.REDIS_TEMPLATE_NAME) val redisTemplate: RedisTemplate<String, String>
) : BasicRedisMap<Redis1_RuntimeConfigIpList.ValueVo>(redisTemplate, MAP_NAME, ValueVo::class.java) {
    // <멤버 변수 공간>
    companion object {
        // !!!중복되지 않도록, 본 클래스명을 MAP_NAME 으로 설정하기!!!
        const val MAP_NAME = "Redis1_RuntimeConfigIpList"
    }

    // !!!본 RedisMAP 의 Value 클래스 설정!!!
    class ValueVo(
        var ipInfoList: List<IpDescVo>
    ) {
        data class IpDescVo(
            // 설정 ip
            val ip: String,
            // ip 설명
            val desc: String
        )
    }
}