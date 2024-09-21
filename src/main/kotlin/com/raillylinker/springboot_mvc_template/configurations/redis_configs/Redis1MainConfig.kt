package com.raillylinker.springboot_mvc_template.configurations.redis_configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


// [Redis 설정]
@Configuration
@EnableCaching
class Redis1MainConfig {
    // <멤버 변수 공간>
    companion object {
        // !!!application.yml 의 datasource-redis 안에 작성된 이름 할당하기!!!
        const val REDIS_CONFIG_NAME = "redis1-main"

        // Redis Template Bean 이름
        const val REDIS_TEMPLATE_NAME = "${REDIS_CONFIG_NAME}_template"
    }

    // ---------------------------------------------------------------------------------------------
    @Value("\${datasource-redis.${REDIS_CONFIG_NAME}.host}")
    private lateinit var redisHost: String

    @Value("\${datasource-redis.${REDIS_CONFIG_NAME}.port}")
    private var redisPort: Int? = null

    @Bean(REDIS_CONFIG_NAME + "_ConnectionFactory")
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val clientConfig = LettuceClientConfiguration.builder()
//            .useSsl().and()
            .commandTimeout(Duration.ofSeconds(2))
            .shutdownTimeout(Duration.ZERO)
            .build()

        return LettuceConnectionFactory(RedisStandaloneConfiguration(redisHost, redisPort!!), clientConfig)
    }

    @Bean(REDIS_TEMPLATE_NAME)
    fun redisRedisTemplate(): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
        return redisTemplate
    }
}