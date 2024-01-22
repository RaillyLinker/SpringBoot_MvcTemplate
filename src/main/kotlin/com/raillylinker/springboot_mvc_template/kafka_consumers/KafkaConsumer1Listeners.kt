package com.raillylinker.springboot_mvc_template.kafka_consumers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer1Listeners {
    // <멤버 변수 공간>
    companion object {
        // !!!kafka consumer container factory 이름 - KafkaConsumerConfig 의 Bean 함수명을 입력하세요!!!
        const val KAFKA_CONSUMER_CONTAINER_FACTORY = "kafkaConsumer0"
    }

    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @KafkaListener(topics = ["testTopic"], groupId = "group_0", containerFactory = KAFKA_CONSUMER_CONTAINER_FACTORY)
    fun listener(data: Any?) {
        classLogger.info(">>>>>>>>>>$data<<<<<<<<<<")
    }
}