package com.raillylinker.springboot_mvc_template

import com.raillylinker.springboot_mvc_template.data_sources.memory_const_object.ProjectConst
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

// [Main]
// Framework 의 시작점입니다.
@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@EnableMongoAuditing // MongoDB 에서 @CreatedDate, @LastModifiedDate 사용 설정
@SpringBootApplication
class ApplicationMain {
    @Bean
    fun init() = CommandLineRunner {
        // 서버 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone(ProjectConst.SYSTEM_TIME_ZONE))
    }
}

fun main(args: Array<String>) {
    // 서버 실행
    runApplication<ApplicationMain>(*args)
}