package com.raillylinker.springboot_mvc_template.configurations

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
class KafkaProducerConfig {
    @Bean
    @Qualifier("kafkaProducer1")
    fun kafkaProducer1(): KafkaTemplate<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092" // Kafka 접속 주소
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return KafkaTemplate(DefaultKafkaProducerFactory(config))
    }

    // !!!추가 등록할 Kafka 앤드포인트가 있다면 아래에 Bean 으로 등록하세요!!!
    // 예시 :
//    @Bean
//    @Qualifier("kafkaProducer2")
//    fun kafkaProducer2(): KafkaTemplate<String, Any> {
//        val config: MutableMap<String, Any> = HashMap()
//        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9093" // Kafka 접속 주소
//        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
//        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
//
//        return KafkaTemplate(DefaultKafkaProducerFactory(config))
//    }
}