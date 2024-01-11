package com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis1.repositories

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raillylinker.springboot_mvc_template.configurations.RedisConfig
import com.raillylinker.springboot_mvc_template.data_sources.redis_sources.redis1.tables.Redis1_Test
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Redis1_TestRepository(
    // !!!RedisTemplate 설정!!!
    @Qualifier(RedisConfig.TN_REDIS1) private val redisTemplate: RedisTemplate<String, Any>
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (Redis Table 에 Key-Value 저장)
    fun saveKeyValue(
        key: String,
        // !!!value Type 를 모두 변경!!!
        value: Redis1_Test,
        expireTimeMs: Long
    ) {
        if (key.trim() == "") {
            throw RuntimeException("key 는 비어있을 수 없습니다.")
        }
        if (key.contains(":")) {
            throw RuntimeException("key 는 : 를 포함 할 수 없습니다.")
        }

        // Redis Storage 에 실제로 저장 되는 키 (테이블 이름과 키를 합친 String)
        val innerKey = "${Redis1_Test.TABLE_NAME}:${key}" // 실제 저장되는 키 = 그룹명:키

        // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
        val innerValue = Gson().toJson(value)
        redisTemplate.opsForValue()[innerKey] = innerValue

        // Redis Key 에 대한 만료시간 설정
        redisTemplate.expire(innerKey, expireTimeMs, TimeUnit.MILLISECONDS)
    }

    // (Redis Table 의 모든 Key-Value 리스트 반환)
    fun findAllKeyValues(): List<KeyValueData> {
        val resultList = ArrayList<KeyValueData>()

        val keySet: Set<String> = redisTemplate.keys("${Redis1_Test.TABLE_NAME}:*")

        for (innerKey in keySet) {
            // innerKey = Redis Storage 에 실제로 저장 되는 키 (테이블 이름과 키를 합친 String)

            // 외부적으로 사용되는 Key (innerKey 에서 table 이름을 제거한 String)
            val key = innerKey.substring("${Redis1_Test.TABLE_NAME}:".length) // 키

            // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
            val innerValue = redisTemplate.opsForValue()[innerKey] ?: continue // 값

            // 외부적으로 사용되는 Value (Json String 을 테이블 객체로 변환)
            val valueObject = Gson().fromJson<Redis1_Test>(
                innerValue as String, // 해석하려는 json 형식의 String
                object : TypeToken<Redis1_Test>() {}.type // 파싱할 데이터 객체 타입
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
        val innerKey = "${Redis1_Test.TABLE_NAME}:$key"

        // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
        val innerValue = redisTemplate.opsForValue()[innerKey] // 값

        return if (innerValue == null) {
            null
        } else {
            // 외부적으로 사용되는 Value (Json String 을 테이블 객체로 변환)
            val valueObject = Gson().fromJson<Redis1_Test>(
                innerValue as String, // 해석하려는 json 형식의 String
                object : TypeToken<Redis1_Test>() {}.type // 파싱할 데이터 객체 타입
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
        val keySet: Set<String> = redisTemplate.keys("${Redis1_Test.TABLE_NAME}:*")

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
        val innerKey = "${Redis1_Test.TABLE_NAME}:$key"

        redisTemplate.delete(innerKey)
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class KeyValueData(
        val key: String, // 멤버가 입력한 키 : 실제 키는 ${groupName:key}
        val value: Redis1_Test,
        val expireTimeMs: Long // 남은 만료 시간 밀리초
    )

}