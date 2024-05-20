package com.raillylinker.springboot_mvc_template.filters

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

// 민감한 정보를 지닌 actuator 접근 제한 필터
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ActuatorEndpointFilter(
    @Value("\${customConfig.actuatorAllowIpList:}#{T(java.util.Collections).emptyList()}")
    private var actuatorAllowIpList: List<String>
) : Filter {
    override fun doFilter(
        request: ServletRequest, response: ServletResponse, chain: FilterChain
    ) {
        val httpServletRequest = (request as HttpServletRequest)
        val httpServletResponse = (response as HttpServletResponse)

        // 요청자 Ip (ex : 127.0.0.1)
        val clientAddressIp = httpServletRequest.remoteAddr

        // 리퀘스트 URI (ex : /sample/test) 가 /actuator 로 시작되는지를 확인 후 블록
        if (httpServletRequest.requestURI.startsWith("/actuator") && clientAddressIp !in actuatorAllowIpList) {
            // status 404 반환 및 무동작
            httpServletResponse.status = HttpStatus.NOT_FOUND.value()
            return
        }

        chain.doFilter(request, response)
    }
}