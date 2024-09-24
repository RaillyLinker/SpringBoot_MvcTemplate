package com.raillylinker.springboot_mvc_template.kafka_consumers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

/*
    !!!
    Kafka 사용시 @Component 주석을 푸세요.
    (kafka 환경이 구성되지 않았을 때 경고 메시지가 계속 발생하기에 주석처리)
    테스트를 하고 싶다면, 도커를 설치하고,
    cmd 를 열어,
    프로젝트 폴더 내의 external_files/docker/kafka_docker 로 이동 후,
    명령어.txt 에 적힌 명령어를 입력하여 카프카를 실행시킬 수 있습니다.
    !!!
 */
@Component
class KafkaConsumerForTestListeners {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (testTopic1 에 대한 리스너)
    // containerFactory 에 kafka consumer container factory 이름 - KafkaConsumerConfig 의 Bean 함수명을 입력하세요
    @KafkaListener(topics = ["testTopic1"], containerFactory = "kafkaConsumerForTestGroup1")
    fun testTopic1Group0Listener(data: Any?) {
        classLogger.info(">> testTopic1 group_1 : $data")
    }

    // (testTopic2 에 대한 리스너)
    // containerFactory 에 kafka consumer container factory 이름 - KafkaConsumerConfig 의 Bean 함수명을 입력하세요
    @KafkaListener(topics = ["testTopic2"], containerFactory = "kafkaConsumerForTestGroup1")
    fun testTopic2Group0Listener(data: Any?) {
        classLogger.info(">> testTopic2 group_1 : $data")
    }

    // (testTopic2 에 대한 동일 그룹 테스트 리스너)
    // 동일 topic 에 동일 group 을 설정할 경우, 리스너는 한개만을 선택하고 다른 하나는 침묵합니다.
    // containerFactory 에 kafka consumer container factory 이름 - KafkaConsumerConfig 의 Bean 함수명을 입력하세요
    @KafkaListener(topics = ["testTopic2"], containerFactory = "kafkaConsumerForTestGroup1")
    fun testTopic2Group0Listener2(data: Any?) {
        classLogger.info(">> testTopic2 group_1 2 : $data")
    }

    // (testTopic2 에 대한 리스너 - 그룹 변경)
    // containerFactory 에 kafka consumer container factory 이름 - KafkaConsumerConfig 의 Bean 함수명을 입력하세요
    @KafkaListener(topics = ["testTopic2"], containerFactory = "kafkaConsumerForTestGroup2")
    fun testTopic2Group1Listener(data: Any?) {
        classLogger.info(">> testTopic2 group_2 : $data")
    }
}