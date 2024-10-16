package raillylinker.module_api_sample

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import raillylinker.module_api_sample.data_sources.memory_const_object.ProjectConst
import java.util.*

@EnableMongoAuditing // MongoDB 에서 @CreatedDate, @LastModifiedDate 사용 설정
@ComponentScan(
    // Bean 스캔할 모듈 패키지 리스트
    basePackages =
    [
        "raillylinker.module_api_sample",
        "raillylinker.module_idp_common",
        "raillylinker.module_idp_jpa",
        "raillylinker.module_idp_redis",
        "raillylinker.module_idp_mongodb",
        "raillylinker.module_dpd_sockjs",
        "raillylinker.module_dpd_socket_stomp",
        "raillylinker.module_dpd_kafka"
    ]
)
@SpringBootApplication
class ApplicationMain {
    @Bean
    fun init() = CommandLineRunner {
        // 서버 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone(ProjectConst.SYSTEM_TIME_ZONE))
        // println("Current TimeZone: ${TimeZone.getDefault().id}")
    }
}

fun main(args: Array<String>) {
    runApplication<ApplicationMain>(*args)
}