package com.raillylinker.module_dpd_kafka.configurations.kafka_producer_configs

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
class Kafka1MainProducerConfig {
    companion object {
        // !!!application.yml 의 kafka-cluster 안에 작성된 이름 할당하기!!!
        const val KAFKA_CONFIG_NAME: String = "kafka1-main"

        const val PRODUCER_BEAN_NAME: String =
            "${KAFKA_CONFIG_NAME}_ProducerFactory"
    }

    @Value("\${kafka-cluster.$KAFKA_CONFIG_NAME.uri}")
    private lateinit var uri: String

    @Value("\${kafka-cluster.$KAFKA_CONFIG_NAME.producer.username}")
    private lateinit var userName: String

    @Value("\${kafka-cluster.$KAFKA_CONFIG_NAME.producer.password}")
    private lateinit var password: String

    @Bean(PRODUCER_BEAN_NAME)
    fun kafkaProducer(): KafkaTemplate<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        // Kafka 브로커에 연결하기 위한 주소를 설정합니다. 여러 개의 브로커가 있을 경우, 콤마로 구분하여 나열합니다.
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = uri
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        // SASL/SCRAM 인증 설정 추가
        config["security.protocol"] = "SASL_PLAINTEXT"
        config["sasl.mechanism"] = "PLAIN"
        config["sasl.jaas.config"] =
            "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$userName\" password=\"$password\";"

        return KafkaTemplate(DefaultKafkaProducerFactory(config))
    }
}