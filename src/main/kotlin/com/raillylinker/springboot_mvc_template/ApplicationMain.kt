package com.raillylinker.springboot_mvc_template

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*


@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@SpringBootApplication
class ApplicationMain

fun main(args: Array<String>) {
    val classLogger: Logger = LoggerFactory.getLogger(ApplicationMain::class.java)

    // 환경 설정 로딩
    val systemTimeZone =
        runApplication<ApplicationMain>(*args).getBean(Environment::class.java)
            .getProperty("customConfig.systemTimeZone")

    // 서버 타임존 설정
    TimeZone.setDefault(TimeZone.getTimeZone(systemTimeZone))

    classLogger.info("시스템 타임존 설정 : $systemTimeZone")
    classLogger.info("적용된 시스템 타임존 : ${TimeZone.getDefault().id}")

    // 서버 실행
    runApplication<ApplicationMain>(*args)
}
