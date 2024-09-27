package com.raillylinker.springboot_mvc_template.data_sources.memory_const_object

import org.springframework.core.io.ClassPathResource
import java.io.File
import java.nio.file.Paths

// [프로젝트 전역 상수 모음]
// 아래 변수들은 절대 런타임에 변경되어서는 안됩니다.
// 왜냐면, 서버 복제와 같은 Scale out 기법을 사용시 메모리에 저장되는 상태변수가 존재하면 에러가 날 것이기 때문입니다.
// 꼭 메모리에 저장을 해야한다면 Redis, Kafka 등을 사용해 결합성을 낮추는 방향으로 설계하세요.
object ProjectConst {
    // (DatabaseConfig)
    // !!!본인의 패키지명 작성!!!
    const val PACKAGE_NAME = "com.raillylinker.springboot_mvc_template"

    // !!!현 프로젝트에서 사용할 타임존 설정 (UTC, Asia/Seoul, ...)!!!
    // 로그, JPA 등에 사용됨
    const val SYSTEM_TIME_ZONE = "Asia/Seoul"

    // 프로젝트 루트 폴더 파일 객체
    val rootDirFile: File = if (File(Paths.get("").toAbsolutePath().toString()).exists()) {
        // jar 파일로 실행시켰을 때, jar 파일과 동일한 위치
        File(Paths.get("").toAbsolutePath().toString())
    } else {
        // 로컬 IDE 에서 실행시킬 때 src 폴더와 동일한 위치
        File(ClassPathResource("").uri).parentFile.parentFile.parentFile.parentFile
    }

    // (Actuator 정보 접근 허용 IP 리스트)
    val actuatorAllowIpList: List<String> =
        listOf(
            //"로컬 호스트"
            "127.0.0.1"
        )

    // (Logging Filter 의 로깅 대상에서 제외할 IP 리스트)
    val loggingDenyIpList: List<String> =
        listOf(
            //"로컬 호스트"
            "127.0.0.1"
        )
}