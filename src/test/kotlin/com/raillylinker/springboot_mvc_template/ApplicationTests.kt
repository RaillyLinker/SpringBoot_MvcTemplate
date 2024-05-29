package com.raillylinker.springboot_mvc_template

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.web.WebAppConfiguration

// (테스트 케이스 모음)
@SpringBootTest
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation::class) // @Order(1) 사용으로, 인자값의 숫자가 작은 함수부터 선행됨
@ActiveProfiles("dev", "prod") // 빌드 서버에서 환경 구축을 하지 않기 위해 local 테스트는 하지 않음.
class ApplicationTests {
    @Test
    @Order(1)
    fun test1() {
        // test 코드 작성

    }
}