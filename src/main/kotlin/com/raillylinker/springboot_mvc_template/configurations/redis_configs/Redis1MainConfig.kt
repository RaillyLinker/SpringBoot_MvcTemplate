package com.raillylinker.springboot_mvc_template.configurations.redis_configs

import io.lettuce.core.SocketOptions
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
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
    @Value("\${datasource-redis.${REDIS_CONFIG_NAME}.node-list:}#{T(java.util.Collections).emptyList()}")
    private lateinit var nodeList: List<String>

    @Bean(REDIS_CONFIG_NAME + "_ConnectionFactory")
    fun redisConnectionFactory(): LettuceConnectionFactory {
        // todo 주석 추가

        //----------------- (1) Socket Option
        val socketOptions: SocketOptions = SocketOptions.builder()
            .connectTimeout(Duration.ofMillis(3000L))
            .keepAlive(true)
            .build()


        //----------------- (2) Cluster topology refresh 옵션
        val clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
            .builder()
            .dynamicRefreshSources(true)
            .enableAllAdaptiveRefreshTriggers()
            .enablePeriodicRefresh(Duration.ofSeconds(30))
            .build()


        //----------------- (3) Cluster client 옵션
        val clusterClientOptions = ClusterClientOptions
            .builder()
            .pingBeforeActivateConnection(true)
            .autoReconnect(true)
            .socketOptions(socketOptions)
            .topologyRefreshOptions(clusterTopologyRefreshOptions)
            .maxRedirects(3).build()


        //----------------- (4) Lettuce Client 옵션
        val clientConfig = LettuceClientConfiguration
            .builder()
            .commandTimeout(Duration.ofMillis(3000L))
            .clientOptions(clusterClientOptions)
            .build()

        val clusterConfig: RedisClusterConfiguration = RedisClusterConfiguration(nodeList)
        clusterConfig.maxRedirects = 3
//        clusterConfig.setPassword("password")

        val factory = LettuceConnectionFactory(clusterConfig, clientConfig)

        //----------------- (5) LettuceConnectionFactory 옵션
        factory.validateConnection = false

        return factory
    }

    @Bean(REDIS_TEMPLATE_NAME)
    fun redisRedisTemplate(): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        return redisTemplate
    }
}