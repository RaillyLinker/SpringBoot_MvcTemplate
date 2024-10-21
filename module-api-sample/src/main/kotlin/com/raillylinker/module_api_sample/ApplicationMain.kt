package com.raillylinker.module_api_sample

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import com.raillylinker.module_api_sample.const_objects.ProjectConst
import java.util.*

@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@EnableMongoAuditing // MongoDB 에서 @CreatedDate, @LastModifiedDate 사용 설정
@ComponentScan(
    // Bean 스캔할 모듈 패키지 리스트
    basePackages =
    [
        "com.raillylinker.module_api_sample",

        "com.raillylinker.module_idp_common",
        "com.raillylinker.module_idp_jpa",
        "com.raillylinker.module_idp_redis",
        "com.raillylinker.module_idp_mongodb",
        "com.raillylinker.module_idp_aws",

        "com.raillylinker.module_dpd_common",
        "com.raillylinker.module_dpd_sockjs",
        "com.raillylinker.module_dpd_socket_stomp",
        "com.raillylinker.module_dpd_kafka",
        "com.raillylinker.module_dpd_scheduler",
        "com.raillylinker.module_dpd_actuator"
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
    runApplication<com.raillylinker.module_api_sample.ApplicationMain>(*args)
}