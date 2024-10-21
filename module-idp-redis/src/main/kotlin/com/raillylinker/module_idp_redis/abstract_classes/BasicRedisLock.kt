package com.raillylinker.module_idp_redis.abstract_classes

import org.springframework.data.redis.core.RedisTemplate
import java.util.*
import java.util.concurrent.TimeUnit

// [RedisLock 의 Abstract 클래스]
abstract class BasicRedisLock(
    private val redisTemplateObj: RedisTemplate<String, String>,
    private val mapName: String
) {
    // <공개 메소드 공간>
    // (락 획득 메소드)
    fun tryLock(expireTimeMs: Long): String? {
        val uuid = UUID.randomUUID().toString()
        // mapName 이 저장되어 있다면 false 반환, 저장되어 있지 않다면 true 반환 후 해당 키로 저장
        val success = redisTemplateObj.opsForValue().setIfAbsent(mapName, uuid, expireTimeMs, TimeUnit.MILLISECONDS)

        return if (success == true) {
            // 락을 성공적으로 획득한 경우
            uuid
        } else {
            // 락을 획득하지 못한 경우
            null
        }
    }

    // (락 해제 메소드)
    fun unlock(uuid: String) {
        val currentValue = redisTemplateObj.opsForValue().get(mapName)
        if (uuid == currentValue) {
            // 현재 프로세스가 락을 소유하는 경우에만 해제
            redisTemplateObj.delete(mapName)
        }
    }

    // (락 강제 삭제)
    fun deleteLock() {
        redisTemplateObj.delete(mapName)
    }
}