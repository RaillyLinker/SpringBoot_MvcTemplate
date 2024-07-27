package com.raillylinker.springboot_mvc_template

import org.springframework.core.io.ClassPathResource
import java.io.File
import java.nio.file.Paths

// (const 시점에서 사용하는 설정 변수 모음)
// application.yml 과 동일한 역할을 하지만, const 시점에 조회하기 위해 모아둔 변수입니다.
object ApplicationConstants {
    // (Database1Config)
    // !!!본인의 패키지명 작성!!!
    const val PACKAGE_NAME = "com.raillylinker.springboot_mvc_template"

    // 현 프로젝트에서 사용할 타임존 설정 (UTC, Asia/Seoul, ...)
    const val SYSTEM_TIME_ZONE = "Asia/Seoul"

    // 프로젝트 루트 폴더 파일 객체
    val rootDirFile: File = if (File(Paths.get("").toAbsolutePath().toString()).exists()) {
        // jar 파일로 실행시켰을 때, jar 파일과 동일한 위치
        File(Paths.get("").toAbsolutePath().toString())
    } else {
        // 로컬 IDE 에서 실행시킬 때 src 폴더와 동일한 위치
        File(ClassPathResource("").uri).parentFile.parentFile.parentFile.parentFile
    }
}