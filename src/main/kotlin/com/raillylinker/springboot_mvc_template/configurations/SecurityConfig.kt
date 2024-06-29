package com.raillylinker.springboot_mvc_template.configurations

import com.raillylinker.springboot_mvc_template.ApplicationRuntimeConfigs
import com.raillylinker.springboot_mvc_template.custom_objects.JwtTokenUtilObject
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_LogInTokenInfoRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_MemberDataRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_MemberRoleDataRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter

// (서비스 보안 시큐리티 설정)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    // (회원 정보 및 상태 확인용 데이터베이스 레포지토리 객체)
    private val database1Service1MemberDataRepository: Database1_Service1_MemberDataRepository,
    private val database1Service1MemberRoleDataRepository: Database1_Service1_MemberRoleDataRepository,
    private val database1Service1LogInTokenInfoRepository: Database1_Service1_LogInTokenInfoRepository
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (비밀번호 인코딩, 매칭시 사용할 객체)
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        return UrlBasedCorsConfigurationSource().apply {
            this.registerCorsConfiguration(
                "/**", // 아래 설정을 적용할 컨트롤러 패턴
                CorsConfiguration().apply {
                    allowedOriginPatterns = listOf("*") // 허가 클라이언트 주소
                    allowedMethods = listOf("*") // 허가할 클라이언트 리퀘스트 http method
                    allowedHeaders = listOf("*") // 허가할 클라이언트 발신 header
                    exposedHeaders = listOf("*") // 허가할 클라이언트 수신 header
                    maxAge = 3600L
                    allowCredentials = true
                }
            )
        }
    }

    // !!!경로별 적용할 Security 설정 Bean 작성하기!!!

    // [/service1/tk 로 시작되는 리퀘스트의 시큐리티 설정 = Token 인증 사용]
    @Bean
    @Order(1)
    fun securityFilterChainService1Tk(http: HttpSecurity): SecurityFilterChain {
        // !!!시큐리티 필터 추가시 수정!!!
        // 본 시큐리티 필터가 관리할 주소 체계
        val securityUrlList = listOf(
            "/service1/tk/**",
            "/service1-admin/tk/**" // 보통 Admin 관리 서비스는 동일 인증 체계에서 Role 로 구분하기에 예시에 추가했습니다.
        ) // 위 모든 경로에 적용

        val securityMatcher = http.securityMatcher(*securityUrlList.toTypedArray())

        // sameOrigin 에서 iframe 허용
//        securityMatcher.headers { headersConfigurer ->
//            headersConfigurer.frameOptions { frameOptionsConfig ->
//                frameOptionsConfig.sameOrigin()
//            }
//        }

        securityMatcher.cors {}

        // (사이즈간 위조 요청(Cross site Request forgery) 방지 설정)
        // csrf 설정시 POST, PUT, DELETE 요청으로부터 보호하며 csrf 토큰이 포함되어야 요청을 받아들이게 됨
        // Rest API 에선 Token 이 요청의 위조 방지 역할을 하기에 비활성화
        securityMatcher.csrf { csrfCustomizer ->
            csrfCustomizer.disable()
        }

        securityMatcher.httpBasic { httpBasicCustomizer ->
            httpBasicCustomizer.disable()
        }

        // Token 인증을 위한 세션 비활성화
        securityMatcher.sessionManagement { sessionManagementCustomizer ->
            sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        // (Token 인증 검증 필터 연결)
        // API 요청마다 헤더로 들어오는 인증 토큰 유효성을 검증
        securityMatcher.addFilterBefore(
            // !!!시큐리티 필터 추가시 수정!!!
            AuthTokenFilterService1Tk(
                securityUrlList,
                database1Service1MemberDataRepository,
                database1Service1MemberRoleDataRepository,
                database1Service1LogInTokenInfoRepository
            ),
            UsernamePasswordAuthenticationFilter::class.java
        )

        // 스프링 시큐리티 기본 로그인 화면 비활성화
        securityMatcher.formLogin { formLoginCustomizer ->
            formLoginCustomizer.disable()
        }

        // 스프링 시큐리티 기본 로그아웃 비활성화
        securityMatcher.logout { logoutCustomizer ->
            logoutCustomizer.disable()
        }

        // 예외처리
        securityMatcher.exceptionHandling { exceptionHandlingCustomizer ->
            // 비인증(Security Context 에 멤버 정보가 없음) 처리
            exceptionHandlingCustomizer.authenticationEntryPoint { _, response, _ -> // Http Status 401
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: UnAuthorized")
            }
            // 비인가(멤버 권한이 충족되지 않음) 처리
            exceptionHandlingCustomizer.accessDeniedHandler { _, response, _ -> // Http Status 403
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Forbidden")
            }
        }

        // (API 요청 제한)
        // 기본적으로 모두 Open
        securityMatcher.authorizeHttpRequests { authorizeHttpRequestsCustomizer ->
            authorizeHttpRequestsCustomizer.anyRequest().permitAll()
            /*
                본 서버 접근 보안은 블랙 리스트 방식을 사용합니다.
                일반적으로 모든 요청을 허용하며, 인증/인가가 필요한 부분에는,
                @PreAuthorize("isAuthenticated() and (hasRole('ROLE_DEVELOPER') or hasRole('ROLE_ADMIN'))")
                위와 같은 어노테이션을 접근 통제하고자 하는 API 위에 달아주면 인증 필터가 동작하게 됩니다.
             */
        }

        return http.build()
    }

    // 인증 토큰 검증 필터 - API 요청마다 검증 실행
    class AuthTokenFilterService1Tk(
        private val filterPatternList: List<String>,
        private val database1Service1MemberDataRepository: Database1_Service1_MemberDataRepository,
        private val database1Service1MemberRoleDataRepository: Database1_Service1_MemberRoleDataRepository,
        private val database1Service1LogInTokenInfoRepository: Database1_Service1_LogInTokenInfoRepository
    ) : OncePerRequestFilter() {
        // <멤버 변수 공간>
        companion object {
            // (Swagger 에 표시될 401 api-result-code 설명)
            const val DESCRIPTION_FOR_UNAUTHORIZED_API_RESULT_CODE =
                "(Response Code 반환 원인) - Nullable\n\n" +
                        "반환 안됨 : 인증 토큰을 입력하지 않았습니다.\n\n" +
                        "1 : Request Header 에 Authorization 키로 넣어준 토큰이 올바르지 않습니다. (재 로그인 필요)\n\n" +
                        "2 : Request Header 에 Authorization 키로 넣어준 토큰의 유효시간이 만료되었습니다. (Refresh Token 으로 재발급 필요)\n\n" +
                        "3 : Request Header 에 Authorization 키로 넣어준 토큰의 멤버가 탈퇴 된 상태입니다. (다른 계정으로 재 로그인 필요)\n\n" +
                        "4 : Request Header 에 Authorization 키로 넣어준 토큰이 로그아웃 처리된 상태입니다. (재 로그인 필요)"

            // (액세스 토큰 검증 함수)
            // !!!입력받은 토큰을 검증하여 결과 코드를 반환하도록 작성합니다.!!!
            /*
                결과 코드
                0 : 정상적인 토큰입니다.
                1 : 토큰이 올바르지 않습니다.
                2 : 토큰의 유효시간이 만료되었습니다.
             */
            fun verifyAccessToken(tokenType: String, accessToken: String): Int {
                if (accessToken == "") {
                    // 액세스 토큰이 비어있음 (올바르지 않은 Authorization Token)
                    return 1
                }

                when (tokenType.lowercase()) { // 타입 검증
                    "bearer" -> { // Bearer JWT 토큰 검증
                        // 토큰 문자열 해석 가능여부 확인
                        val accessTokenType: String? = try {
                            JwtTokenUtilObject.getTokenType(accessToken)
                        } catch (_: Exception) {
                            null
                        }

                        if (accessTokenType == null || // 해석 불가능한 JWT 토큰
                            accessTokenType.lowercase() != "jwt" || // 토큰 타입이 JWT 가 아님
                            JwtTokenUtilObject.getTokenUsage(
                                accessToken,
                                ApplicationRuntimeConfigs.runtimeConfigData.authJwtClaimsAes256InitializationVector,
                                ApplicationRuntimeConfigs.runtimeConfigData.authJwtClaimsAes256EncryptionKey
                            ).lowercase() != "access" || // 토큰 용도가 다름
                            // 남은 시간이 최대 만료시간을 초과 (서버 기준이 변경되었을 때, 남은 시간이 더 많은 토큰을 견제하기 위한 처리)
                            JwtTokenUtilObject.getRemainSeconds(accessToken) > ApplicationRuntimeConfigs.runtimeConfigData.authJwtAccessTokenExpirationTimeSec ||
                            JwtTokenUtilObject.getIssuer(accessToken) != ApplicationRuntimeConfigs.runtimeConfigData.authJwtIssuer || // 발행인 불일치
                            !JwtTokenUtilObject.validateSignature(
                                accessToken,
                                ApplicationRuntimeConfigs.runtimeConfigData.authJwtSecretKeyString
                            ) // 시크릿 검증이 무효 = 위변조 된 토큰
                        ) {
                            // 올바르지 않은 Authorization Token
                            return 1
                        }

                        // 토큰 만료 검증
                        val jwtRemainSeconds = JwtTokenUtilObject.getRemainSeconds(accessToken)

                        if (jwtRemainSeconds <= 0L) {
                            // 토큰이 만료됨
                            return 2
                        }

                        // 토큰 정상
                        return 0
                    }

                    else -> {
                        // 지원하지 않는 토큰 타입 (올바르지 않은 Authorization Token)
                        return 1
                    }
                }
            }
        }

        // ---------------------------------------------------------------------------------------------
        // <공개 메소드 공간>
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
        ) {
            // 패턴에 매치되는지 확인
            var patternMatch = false

            for (filterPattern in filterPatternList) {
                if (AntPathRequestMatcher(filterPattern).matches(request)) {
                    patternMatch = true
                    break
                }
            }

            if (!patternMatch) {
                // 이 필터를 실행해야 할 패턴이 아님.

                // 다음 필터 실행
                filterChain.doFilter(request, response)
                return
            }

            // (리퀘스트에서 가져온 AccessToken 검증)
            // 헤더의 Authorization 의 값 가져오기
            // 정상적인 토큰값은 "Bearer {Token String}" 형식으로 온다고 가정.
            val authorization = request.getHeader("Authorization") // ex : "Bearer aqwer1234"
            if (authorization == null) {
                // Authorization 에 토큰을 넣지 않은 경우 = 인증 / 인가를 받을 의도가 없음

                // 다음 필터 실행
                filterChain.doFilter(request, response)
                return
            }

            // 타입과 토큰을 분리
            val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
            if (authorizationSplit.size < 2) {
                // 올바르지 않은 Authorization Token
                response.setHeader("api-result-code", "1")

                // 다음 필터 실행
                filterChain.doFilter(request, response)
                return
            }

            // 타입으로 추정되는 문장이 존재할 때
            // 타입 분리
            val tokenType = authorizationSplit[0].trim() // 첫번째 단어는 토큰 타입
            val accessToken = authorizationSplit[1].trim() // 앞의 타입을 자르고 남은 토큰

            // 토큰 검증
            val tokenVerificationCode = verifyAccessToken(tokenType, accessToken)

            when (tokenVerificationCode) {
                1 -> {
                    // 올바르지 않은 Authorization Token
                    response.setHeader("api-result-code", "1")

                    // 다음 필터 실행
                    filterChain.doFilter(request, response)
                    return
                }

                2 -> {
                    // 토큰이 만료됨
                    response.setHeader("api-result-code", "2")

                    // 다음 필터 실행
                    filterChain.doFilter(request, response)
                    return
                }

                else -> {
                    // 토큰 검증 정상 -> 데이터베이스 현 상태 확인

                    // 유저 탈퇴 여부 확인
                    val memberUid = JwtTokenUtilObject.getMemberUid(
                        accessToken,
                        ApplicationRuntimeConfigs.runtimeConfigData.authJwtClaimsAes256InitializationVector,
                        ApplicationRuntimeConfigs.runtimeConfigData.authJwtClaimsAes256EncryptionKey
                    ).toLong()

                    val memberDataOpt = database1Service1MemberDataRepository.findById(memberUid)

                    if (memberDataOpt.isEmpty) {
                        // 멤버 탈퇴
                        response.setHeader("api-result-code", "3")

                        // 다음 필터 실행
                        filterChain.doFilter(request, response)
                        return
                    }

                    val memberData = memberDataOpt.get()

                    // 로그아웃 여부 파악
                    val tokenInfo =
                        database1Service1LogInTokenInfoRepository.findByTokenTypeAndAccessTokenAndRowDeleteDateStr(
                            tokenType, accessToken,
                            "/"
                        )

                    if (tokenInfo == null) {
                        // 로그아웃된 토큰
                        response.setHeader("api-result-code", "4")

                        // 다음 필터 실행
                        filterChain.doFilter(request, response)
                        return
                    }

                    // !!!패널티로 인한 접근 거부 와 같은 인증 / 인가 조건을 추가하려면 이곳에 추가하세요.!!!

                    // 멤버의 권한 리스트를 조회 후 반환
                    val memberRoleEntityList = database1Service1MemberRoleDataRepository.findAllByMemberData(memberData)

                    // 회원 권한 형식 변경
                    val authorities: ArrayList<GrantedAuthority> = ArrayList()
                    for (memberRole in memberRoleEntityList) {
                        authorities.add(
                            SimpleGrantedAuthority(memberRole.role)
                        )
                    }

                    // (검증된 멤버 정보와 권한 정보를 Security Context 에 입력)
                    // authentication 정보가 context 에 존재하는지 여부로 로그인 여부를 확인
                    SecurityContextHolder.getContext().authentication =
                        UsernamePasswordAuthenticationToken(
                            null, // 세션을 유지하지 않으니 굳이 입력할 필요가 없음
                            null, // 세션을 유지하지 않으니 굳이 입력할 필요가 없음
                            authorities // 멤버 권한 리스트만 입력해주어 권한 확인에 사용
                        ).apply {
                            this.details =
                                WebAuthenticationDetailsSource().buildDetails(request)
                        }

                    filterChain.doFilter(request, response)
                    return
                }
            }
        }
    }
}