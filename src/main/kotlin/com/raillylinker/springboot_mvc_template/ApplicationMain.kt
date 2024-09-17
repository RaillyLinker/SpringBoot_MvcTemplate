package com.raillylinker.springboot_mvc_template

import com.raillylinker.springboot_mvc_template.data_sources.memory_object.ProjectConfigs
import com.raillylinker.springboot_mvc_template.data_sources.RuntimeConfig
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

// [Main]
// Framework 의 시작점입니다.
@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@SpringBootApplication
class ApplicationMain {
    @Bean
    fun init() = CommandLineRunner {
        // 서버 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone(ProjectConfigs.SYSTEM_TIME_ZONE))

        // 런타임 설정 가져오기
        RuntimeConfig.loadRuntimeConfigData()
    }
}

fun main(args: Array<String>) {
    // 서버 실행
    runApplication<ApplicationMain>(*args)
}