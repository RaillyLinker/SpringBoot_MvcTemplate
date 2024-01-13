package com.raillylinker.springboot_mvc_template.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

// [Kafka Consumer 설정]
// kafka_consumers 폴더 안의 Listeners 클래스 파일과 연계하여 사용하세요.
@EnableKafka
@Configuration
class KafkaConsumerConfig {
    // !!!등록할 Kafka 앤드포인트가 있다면 아래에 Bean 으로 등록하세요!!!
    // 예시 :
//    @Bean
//    fun kafkaConsumer0(): ConcurrentKafkaListenerContainerFactory<String, Any> {
//        val config: MutableMap<String, Any> = HashMap()
//        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092" // Kafka 접속 주소
//        config[ConsumerConfig.GROUP_ID_CONFIG] = "group_0"
//        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//
//        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
//        factory.consumerFactory = DefaultKafkaConsumerFactory(config)
//
//        return factory
//    }
}