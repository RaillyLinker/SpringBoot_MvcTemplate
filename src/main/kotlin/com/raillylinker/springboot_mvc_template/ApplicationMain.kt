package com.raillylinker.springboot_mvc_template

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@SpringBootApplication
class ApplicationMain

fun main(args: Array<String>) {
    // 서버 타임존 설정
    TimeZone.setDefault(TimeZone.getTimeZone(ApplicationConstants.SYSTEM_TIME_ZONE))

    // 서버 실행
    runApplication<ApplicationMain>(*args)
}
