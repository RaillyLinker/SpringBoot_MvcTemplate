package com.raillylinker.module_dpd_kafka.kafka_consumers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import com.raillylinker.module_dpd_kafka.configurations.kafka_consumer_configs.Kafka1MainConsumerConfig
import com.raillylinker.module_dpd_kafka.kafka_consumers_service.Kafka1MainConsumerService

@Component
class Kafka1MainConsumer(
    private val kafka1MainConsumerService: Kafka1MainConsumerService
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (testTopic1 에 대한 리스너)
    @KafkaListener(
        topics = ["testTopic1"],
        groupId = "group_1",
        containerFactory = Kafka1MainConsumerConfig.CONSUMER_BEAN_NAME
    )
    fun testTopic1Group0Listener(data: Any?) {
        kafka1MainConsumerService.testTopic1Group0Listener(data)
    }

    // (testTopic2 에 대한 리스너)
    @KafkaListener(
        topics = ["testTopic2"],
        groupId = "group_1",
        containerFactory = Kafka1MainConsumerConfig.CONSUMER_BEAN_NAME
    )
    fun testTopic2Group0Listener(data: Any?) {
        kafka1MainConsumerService.testTopic2Group0Listener(data)
    }

    // (testTopic2 에 대한 동일 그룹 테스트 리스너)
    // 동일 topic 에 동일 group 을 설정할 경우, 리스너는 한개만을 선택하고 다른 하나는 침묵합니다.
    @KafkaListener(
        topics = ["testTopic2"],
        groupId = "group_1",
        containerFactory = Kafka1MainConsumerConfig.CONSUMER_BEAN_NAME
    )
    fun testTopic2Group0Listener2(data: Any?) {
        kafka1MainConsumerService.testTopic2Group0Listener2(data)
    }

    // (testTopic2 에 대한 리스너 - 그룹 변경)
    @KafkaListener(
        topics = ["testTopic2"],
        groupId = "group_2",
        containerFactory = Kafka1MainConsumerConfig.CONSUMER_BEAN_NAME
    )
    fun testTopic2Group1Listener(data: Any?) {
        kafka1MainConsumerService.testTopic2Group1Listener(data)
    }
}