package com.raillylinker.springboot_mvc_template.configurations

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

// [Kafka Producer 설정]
/*
    @Qualifier("kafkaProducerForTest") private val kafkaProducerForTest: KafkaTemplate<String, Any>
    위와 같이 DI 를 해서,
    kafkaProducerForTest.send("testTopic", "testMessage")
    이렇게 토픽 메세지를 발행하면 됩니다.
 */
@Configuration
class KafkaProducerConfig {
    // !!!등록할 Kafka 앤드포인트가 있다면 아래에 Bean 으로 등록하세요!!!
    // 예시 :
    @Bean("kafkaProducerForTest")
    fun kafkaProducerForTest(): KafkaTemplate<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        // Kafka 브로커에 연결하기 위한 주소를 설정합니다. 여러 개의 브로커가 있을 경우, 콤마로 구분하여 나열합니다.
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092,localhost:9093,localhost:9094"
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return KafkaTemplate(DefaultKafkaProducerFactory(config))
    }
}