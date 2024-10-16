package raillylinker.module_dpd_kafka.configurations.kafka_consumer_configs

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory


// [Kafka Consumer 설정]
// kafka_consumers 폴더 안의 Listeners 클래스 파일과 연계하여 사용하세요.
@EnableKafka
@Configuration
class Kafka1MainConsumerConfig {
    companion object {
        // !!!application.yml 의 kafka-cluster 안에 작성된 이름 할당하기!!!
        const val KAFKA_CONFIG_NAME: String = "kafka1-main"

        const val CONSUMER_BEAN_NAME: String =
            "${KAFKA_CONFIG_NAME}_ConsumerFactory"
    }

    @Value("\${kafka-cluster.${KAFKA_CONFIG_NAME}.uri}")
    private lateinit var uri: String

    @Value("\${kafka-cluster.${KAFKA_CONFIG_NAME}.consumer.username}")
    private lateinit var userName: String

    @Value("\${kafka-cluster.${KAFKA_CONFIG_NAME}.consumer.password}")
    private lateinit var password: String

    @Bean(CONSUMER_BEAN_NAME)
    fun kafkaConsumer(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        // Kafka 브로커에 연결하기 위한 주소를 설정합니다. 여러 개의 브로커가 있을 경우, 콤마로 구분하여 나열합니다.
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = uri
        // Kafka 그룹 아이디 설정
        // KafkaListener 에서 groupId 를 설정할 수도 있지만, 저는 이곳에서 통합하여 설정하였습니다.
        // 이유는, 프로젝트 단위로 그룹을 구분하려 하기 때문입니다.
        // 카프카 그룹에 대해 간략히 설명하자면, 동일 그룹으로 Kafka 에 접근시 동일 토픽의 이벤트를 처리하는 것은 그룹 내의 단 하나의 리스너입니다.
        // 같은 그룹 내의 다른 리스너는 이벤트에 침묵하며, 같은 그룹 내의 한 노드가 침묵할 경우 다른 리스너가 동작하는 방식으로 안정성을 높입니다.
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        // SASL/SCRAM 인증 설정 추가
        config["security.protocol"] = "SASL_PLAINTEXT"
        config["sasl.mechanism"] = "PLAIN"
        config["sasl.jaas.config"] =
            "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$userName\" password=\"$password\";"

        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(config)

        return factory
    }
}