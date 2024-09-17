package com.raillylinker.springboot_mvc_template.data_sources.memory_object

import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.RedisTemplate
import java.io.File
import java.nio.file.Paths

// [프로젝트 상태 전역 변수 모음]
object ProjectStates {
    // 프로젝트 루트 폴더 파일 객체
    val rootDirFile: File = if (File(Paths.get("").toAbsolutePath().toString()).exists()) {
        // jar 파일로 실행시켰을 때, jar 파일과 동일한 위치
        File(Paths.get("").toAbsolutePath().toString())
    } else {
        // 로컬 IDE 에서 실행시킬 때 src 폴더와 동일한 위치
        File(ClassPathResource("").uri).parentFile.parentFile.parentFile.parentFile
    }

    // RedisTemplate Bean 이름 - RedisTemplate 객체쌍 해쉬맵 (Redis Transaction Annotation 처리에 사용됩니다.)
    val redisTemplatesMap: HashMap<String, RedisTemplate<String, Any>> = hashMapOf()
}