package com.raillylinker.module_dpd_kafka.kafka_consumers_service

interface Kafka1MainConsumerService {
    // <공개 메소드 공간>
    // (testTopic1 에 대한 리스너)
    fun testTopic1Group0Listener(data: Any?)

    // (testTopic2 에 대한 리스너)
    fun testTopic2Group0Listener(data: Any?)

    // (testTopic2 에 대한 동일 그룹 테스트 리스너)
    // 동일 topic 에 동일 group 을 설정할 경우, 리스너는 한개만을 선택하고 다른 하나는 침묵합니다.
    fun testTopic2Group0Listener2(data: Any?)

    // (testTopic2 에 대한 리스너 - 그룹 변경)
    fun testTopic2Group1Listener(data: Any?)
}