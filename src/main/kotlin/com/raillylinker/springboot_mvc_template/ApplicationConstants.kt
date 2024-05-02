package com.raillylinker.springboot_mvc_template

// (const 시점에서 사용하는 설정 변수 모음)
// application.yml 과 동일한 역할을 하지만, const 시점에 조회하기 위해 모아둔 변수입니다.
object ApplicationConstants {
    // (Database1Config)
    // !!!본인의 패키지명 작성!!!
    const val PACKAGE_NAME = "com.raillylinker.springboot_mvc_template"

    // 현 프로젝트에서 사용할 타임존 설정 (UTC, Asia/Seoul)
    // !!!이것을 변경했다면, datasource.{데이터베이스 종류}.jdbcUrl 의 serverTimezone 설정을 이와 동일하게 맞춰주세요.!!!
    const val SYSTEM_TIME_ZONE = "Asia/Seoul"
}