package com.raillylinker.springboot_mvc_template

import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component

// 일정 시간마다 실행되는 함수들의 클래스 (프로젝트 Application 클래스 선언 위에 @EnableScheduling 추가 필요)
// scheduler 를 사용할 Class 에 @Component, Method 에 @Scheduled 추가
// @Scheduled 규칙 : Method 반환값 void, 매개변수 0개의 인터페이스 형태 사용
@Component
@EnableAsync
class ApplicationScheduler {
    // [사용 예시]
    // (fixedDelay)
    // 해당 메서드가 끝나는 시간 기준으로 milliseconds 후의 간격으로 실행
//    @Scheduled(fixedDelay = 1000)
//    // @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}") // 문자열 milliseconds 사용 시
//    fun scheduleFixedDelayTask() {
//        println("scheduleFixedDelayTask")
//    }

    // (initialDelay + fixedDelay)
    // initialDelay 값 이후 처음 실행 되고, fixedDelay 값에 따라 계속 실행 = fixedDelay 에 최초 실행 시간이 달린 것
//    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
//    fun scheduleFixedRateWithInitialDelayTask() {
//
//    }

    // (fixedRate)
    // 해당 메서드가 시작하는 시간 기준, milliseconds 간격으로 실행
    // 병렬로 Scheduler 를 사용할 경우, Class에 @EnableAsync, Method에 @Async 추가
//    @Async
//    @Scheduled(fixedRate = 1000)
//    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")  // 문자열 milliseconds 사용 시
//    fun scheduleFixedRateTask() {
//
//    }

    // (Cron)
    // 작업 예약으로 실행
//    @Scheduled(cron = "0 15 10 15 * ?", zone = "Asia/Seoul") // 매월 15일 오전 10시 15분에 실행
//    // @Scheduled(cron = "0 15 10 15 11 ?", zone = "Asia/Seoul") // 11월 15일 오전 10시 15분에 실행
//    // @Scheduled(cron = "${cron.expression}", zone = "Asia/Seoul")
//    // @Scheduled(cron = "0 15 10 15 * ?", zone = "Asia/Seoul") // timezone 설정
//    fun scheduleTaskUsingCronExpression() {
//
//    }
}