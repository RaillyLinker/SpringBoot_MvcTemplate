package com.raillylinker.springboot_mvc_template

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_RuntimeConfigDataRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@SpringBootApplication
class ApplicationMain(
    private val database1Service1Service1RuntimeConfigDataRepository: Database1_Service1_RuntimeConfigDataRepository
) {
    @Bean
    fun init() = CommandLineRunner {
        // 서버 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone(ApplicationConstants.SYSTEM_TIME_ZONE))

        // 런타임 설정 가져오기
        ApplicationRuntimeConfigs.loadRuntimeConfigFile()
        ApplicationRuntimeConfigs.loadService1RuntimeConfigDb(
            database1Service1Service1RuntimeConfigDataRepository
        )
    }
}

fun main(args: Array<String>) {
    // 서버 실행
    runApplication<ApplicationMain>(*args)
}