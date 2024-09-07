package com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis_main

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raillylinker.springboot_mvc_template.configurations.redis_configs.RedisMainConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

// [Redis Type 컴포넌트 - Test]
@Component
class TestRedisType(
    // !!!RedisConfig 종류 변경!!!
    @Qualifier(RedisMainConfig.REDIS_CONFIG_NAME) private val redisTemplate: RedisTemplate<String, Any>
) {
    // <멤버 변수 공간>
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!!
        const val TABLE_NAME = "TestRedisType"

        // Redis Transaction 이름
        // !!!RedisConfig 종류 변경!!!
        const val TRANSACTION_NAME = "${RedisMainConfig.REDIS_CONFIG_NAME}:$TABLE_NAME"
    }


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (Redis Table 에 Key-Value 저장)
    fun saveKeyValue(
        key: String,
        value: ValueVo,
        expireTimeMs: Long
    ) {
        if (key.trim() == "") {
            throw RuntimeException("key 는 비어있을 수 없습니다.")
        }
        if (key.contains(":")) {
            throw RuntimeException("key 는 : 를 포함 할 수 없습니다.")
        }

        // Redis Storage 에 실제로 저장 되는 키 (테이블 이름과 키를 합친 String)
        val innerKey = "${TABLE_NAME}:${key}" // 실제 저장되는 키 = 그룹명:키

        // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
        redisTemplate.opsForValue()[innerKey] = Gson().toJson(value)

        // Redis Key 에 대한 만료시간 설정
        redisTemplate.expire(innerKey, expireTimeMs, TimeUnit.MILLISECONDS)
    }

    // (Redis Table 의 모든 Key-Value 리스트 반환)
    fun findAllKeyValues(): List<KeyValueData> {
        val resultList = ArrayList<KeyValueData>()

        val keySet: Set<String> = redisTemplate.keys("${TABLE_NAME}:*")

        for (innerKey in keySet) {
            // innerKey = Redis Storage 에 실제로 저장 되는 키 (테이블 이름과 키를 합친 String)

            // 외부적으로 사용되는 Key (innerKey 에서 table 이름을 제거한 String)
            val key = innerKey.substring("${TABLE_NAME}:".length) // 키

            // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
            val innerValue = redisTemplate.opsForValue()[innerKey] ?: continue // 값

            // 외부적으로 사용되는 Value (Json String 을 테이블 객체로 변환)
            val valueObject = Gson().fromJson<ValueVo>(
                innerValue as String, // 해석하려는 json 형식의 String
                object : TypeToken<ValueVo>() {}.type // 파싱할 데이터 객체 타입
            )

            resultList.add(
                KeyValueData(
                    key,
                    valueObject,
                    redisTemplate.getExpire(innerKey, TimeUnit.MILLISECONDS) // 남은 만료시간
                )
            )
        }

        return resultList
    }

    // (Redis Table 의 key-Value 를 반환)
    fun findKeyValue(
        key: String
    ): KeyValueData? {
        if (key.trim() == "") {
            throw RuntimeException("key 는 비어있을 수 없습니다.")
        }
        if (key.contains(":")) {
            throw RuntimeException("key 는 : 를 포함 할 수 없습니다.")
        }

        // Redis Storage 에 실제로 저장 되는 키 (테이블 이름과 키를 합친 String)
        val innerKey = "${TABLE_NAME}:$key"

        // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
        val innerValue = redisTemplate.opsForValue()[innerKey] // 값

        return if (innerValue == null) {
            null
        } else {
            // 외부적으로 사용되는 Value (Json String 을 테이블 객체로 변환)
            val valueObject = Gson().fromJson<ValueVo>(
                innerValue as String, // 해석하려는 json 형식의 String
                object : TypeToken<ValueVo>() {}.type // 파싱할 데이터 객체 타입
            )
            KeyValueData(
                key,
                valueObject,
                redisTemplate.getExpire(innerKey, TimeUnit.MILLISECONDS) // 남은 만료시간
            )
        }
    }

    // (Redis Table 의 모든 Key-Value 리스트 삭제)
    fun deleteAllKeyValues() {
        val keySet: Set<String> = redisTemplate.keys("${TABLE_NAME}:*")

        for (innerKey in keySet) {
            // innerKey = Redis Storage 에 실제로 저장 되는 키 (테이블 이름과 키를 합친 String)

            redisTemplate.delete(innerKey)
        }
    }

    // (Redis Table 의 Key-Value 를 삭제)
    fun deleteKeyValue(
        key: String
    ) {
        if (key.trim() == "") {
            throw RuntimeException("key 는 비어있을 수 없습니다.")
        }
        if (key.contains(":")) {
            throw RuntimeException("key 는 : 를 포함 할 수 없습니다.")
        }

        // Redis Storage 에 실제로 저장 되는 키 (테이블 이름과 키를 합친 String)
        val innerKey = "${TABLE_NAME}:$key"

        redisTemplate.delete(innerKey)
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class KeyValueData(
        val key: String, // 멤버가 입력한 키 : 실제 키는 ${groupName:key}
        val value: ValueVo,
        val expireTimeMs: Long // 남은 만료 시간 밀리초
    )

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