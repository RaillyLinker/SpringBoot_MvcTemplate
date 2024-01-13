package com.raillylinker.springboot_mvc_template.configurations

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConsumerConfig {
    @Bean
    fun kafkaConsumer1(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092" // Kafka 접속 주소
        config[ConsumerConfig.GROUP_ID_CONFIG] = "group_1"
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(config)

        return factory
    }

    // !!!추가 등록할 Kafka 앤드포인트가 있다면 아래에 Bean 으로 등록하세요!!!
    // 예시 :
//    @Bean
//    fun kafkaConsumer2(): ConcurrentKafkaListenerContainerFactory<String, Any> {
//        val config: MutableMap<String, Any> = HashMap()
//        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092" // Kafka 접속 주소
//        config[ConsumerConfig.GROUP_ID_CONFIG] = "group_1"
//        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//
//        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
//        factory.consumerFactory = DefaultKafkaConsumerFactory(config)
//
//        return factory
//    }
}