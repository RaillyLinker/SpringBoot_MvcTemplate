package com.raillylinker.module_api_sample.services

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.web.servlet.ModelAndView


/*
    (세션 멤버 정보 가져오기)
    val authentication = SecurityContextHolder.getContext().authentication
    // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
    val username: String = authentication.name
    // 현 세션 권한 리스트 (비로그인 : [ROLE_ANONYMOUS], 권한없음 : [])
    val roles: List<String> = authentication.authorities.map(GrantedAuthority::getAuthority)
    println("username : $username")
    println("roles : $roles")

    (세션 만료시간 설정)
    session.maxInactiveInterval = 60
    위와 같이 세션 객체에 만료시간(초) 를 설정하면 됩니다.
*/
interface SC1MainScV1Service {
    // <공개 메소드 공간>
    fun api1HomePage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession
    ): ModelAndView?

    data class Api1ViewModel(
        val env: String,
        val showApiDocumentBtn: Boolean
    )
}