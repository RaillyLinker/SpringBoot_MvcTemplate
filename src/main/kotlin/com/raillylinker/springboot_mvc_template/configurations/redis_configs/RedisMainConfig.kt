package com.raillylinker.springboot_mvc_template.configurations.redis_configs

import com.raillylinker.springboot_mvc_template.data_sources.GlobalVariables
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

// [Redis 설정]
@Configuration
@EnableCaching
class RedisMainConfig {
    // <멤버 변수 공간>
    companion object {
        // !!!application.yml 의 datasource-redis 안에 작성된 이름 할당하기!!!
        const val REDIS_CONFIG_NAME = "redisMain"
    }

    // ---------------------------------------------------------------------------------------------
    @Value("\${datasource-redis.${REDIS_CONFIG_NAME}.host}")
    private lateinit var redisHost: String

    @Value("\${datasource-redis.${REDIS_CONFIG_NAME}.port}")
    private var redisPort: Int? = null

    @Bean(REDIS_CONFIG_NAME + "_ConnectionFactory")
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisHost, redisPort!!)
    }

    @Bean(REDIS_CONFIG_NAME)
    fun redisRedisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()

        // GlobalVariables.redisTemplatesMap에 바로 추가
        GlobalVariables.redisTemplatesMap[REDIS_CONFIG_NAME] = redisTemplate
        return redisTemplate
    }
}