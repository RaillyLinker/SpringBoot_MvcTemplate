package com.raillylinker.springboot_mvc_template.configurations

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

// [Kafka Consumer 설정]
// kafka_consumers 폴더 안의 Listeners 클래스 파일과 연계하여 사용하세요.
@EnableKafka
@Configuration
class KafkaConsumerConfig {
    // !!!등록할 Kafka 앤드포인트가 있다면 아래에 Bean 으로 등록하세요!!!
    // 예시 :
    @Bean("kafkaConsumerForTestGroup1")
    fun kafkaConsumerForTestGroup1(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        // Kafka 브로커에 연결하기 위한 주소를 설정합니다. 여러 개의 브로커가 있을 경우, 콤마로 구분하여 나열합니다.
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092,localhost:9093,localhost:9094"
        // Kafka 그룹 아이디 설정
        // KafkaListener 에서 groupId 를 설정할 수도 있지만, 저는 이곳에서 통합하여 설정하였습니다.
        // 이유는, 프로젝트 단위로 그룹을 구분하려 하기 때문입니다.
        // 카프카 그룹에 대해 간략히 설명하자면, 동일 그룹으로 Kafka 에 접근시 동일 토픽의 이벤트를 처리하는 것은 그룹 내의 단 하나의 리스너입니다.
        // 같은 그룹 내의 다른 리스너는 이벤트에 침묵하며, 같은 그룹 내의 한 노드가 침묵할 경우 다른 리스너가 동작하는 방식으로 안정성을 높입니다.
        config[ConsumerConfig.GROUP_ID_CONFIG] = "group_1"
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(config)

        return factory
    }

    // (다른 그룹을 사용하는 예시)
    @Bean("kafkaConsumerForTestGroup2")
    fun kafkaConsumerForTestGroup2(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        // Kafka 브로커에 연결하기 위한 주소를 설정합니다. 여러 개의 브로커가 있을 경우, 콤마로 구분하여 나열합니다.
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092,localhost:9093,localhost:9094"
        // Kafka 그룹 아이디 설정
        // KafkaListener 에서 groupId 를 설정할 수도 있지만, 저는 이곳에서 통합하여 설정하였습니다.
        // 이유는, 프로젝트 단위로 그룹을 구분하려 하기 때문입니다.
        // 카프카 그룹에 대해 간략히 설명하자면, 동일 그룹으로 Kafka 에 접근시 동일 토픽의 이벤트를 처리하는 것은 그룹 내의 단 하나의 리스너입니다.
        // 같은 그룹 내의 다른 리스너는 이벤트에 침묵하며, 같은 그룹 내의 한 노드가 침묵할 경우 다른 리스너가 동작하는 방식으로 안정성을 높입니다.
        config[ConsumerConfig.GROUP_ID_CONFIG] = "group_2"
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(config)

        return factory
    }
}