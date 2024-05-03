package com.raillylinker.springboot_mvc_template.configurations

import com.raillylinker.springboot_mvc_template.ApplicationConstants
import com.raillylinker.springboot_mvc_template.custom_objects.JwtTokenUtilObject
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
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter

// (서비스 보안 시큐리티 설정)
/*
    JWT 인증 / 인가 :
    JWT 인증 / 인가 시스템은 검증 시점에 데이터베이스 접근을 하지 않으며, Stateless 하게 검증 정보를 메모리에 저장하지 않습니다.
    즉, 토큰 검증은 발행 된 시점에 토큰 안에 넣어준 바로 그 정보만을 기준으로 하여 판단됩니다.
    예를들어 토큰 발행 시점에 Admin 권한이 있었는데, 서버측에서 이 권한을 취소하여도 토큰만 정상적이라면 여전히 Admin 권한을 가집니다.
    이에 대해 서버측에서 할 수 있는 것은, 액세스 토큰이 만료된 이후에 재발급 시점에 이를 판단하여 처리하는 방법,
    혹은 SSE 등으로 클라이언트에 신호를 보내어 해당 위치에서 처리를 하도록 하는 방법 밖에는 없습니다.
    되도록 액세스 토큰 만료시간을 짧게 잡고(15분 ~ 1시간), 클라이언트 측에서 판단하여 처리할 수 있는 부분은 클라이언트에서 처리하도록 합니다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {
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
        val securityUrl = "/service1/tk/**" // /service1/tk/** 의 모든 경로에 적용

        http.securityMatcher(securityUrl)
            .cors {}

        // (사이즈간 위조 요청(Cross site Request forgery) 방지 설정)
        // csrf 설정시 POST, PUT, DELETE 요청으로부터 보호하며 csrf 토큰이 포함되어야 요청을 받아들이게 됨
        // Rest API 에선 Token 이 요청의 위조 방지 역할을 하기에 비활성화
        http.securityMatcher(securityUrl)
            .csrf { csrfCustomizer ->
                csrfCustomizer.disable()
            }

        http.securityMatcher(securityUrl)
            .httpBasic { httpBasicCustomizer ->
                httpBasicCustomizer.disable()
            }

        // Token 인증을 위한 세션 비활성화
        http.securityMatcher(securityUrl)
            .sessionManagement { sessionManagementCustomizer ->
                sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        // (Token 인증 검증 필터 연결)
        // API 요청마다 헤더로 들어오는 인증 토큰 유효성을 검증
        http.securityMatcher(securityUrl)
            .addFilterBefore(
                // !!!시큐리티 필터 추가시 수정!!!
                AuthTokenFilterService1Tk(securityUrl),
                UsernamePasswordAuthenticationFilter::class.java
            )

        // 스프링 시큐리티 기본 로그인 화면 비활성화
        http.securityMatcher(securityUrl)
            .formLogin { formLoginCustomizer ->
                formLoginCustomizer.disable()
            }

        // 스프링 시큐리티 기본 로그아웃 비활성화
        http.securityMatcher(securityUrl)
            .logout { logoutCustomizer ->
                logoutCustomizer.disable()
            }

        // 예외처리
        http.securityMatcher(securityUrl)
            .exceptionHandling { exceptionHandlingCustomizer ->
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
        http.securityMatcher(securityUrl)
            .authorizeHttpRequests { authorizeHttpRequestsCustomizer ->
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
    class AuthTokenFilterService1Tk(private val filterPattern: String) : OncePerRequestFilter() {
        // <멤버 변수 공간>
        companion object {
            // (JWT signature 비밀키)
            // !!!비밀키 변경!!!
            const val JWT_SECRET_KEY_STRING = "123456789abcdefghijklmnopqrstuvw"

            // (액세스 토큰 유효시간(초))
            // !!!유효시간 변경 (최소 15분 ~ {리프레시 토큰 유효시간})!!!
            const val ACCESS_TOKEN_EXPIRATION_TIME_SEC = 60L * 30L // 30분

            // (리프레시 토큰 유효시간(초))
            // !!!유효시간 변경(최소 {액세스 토큰 유효시간} ~ {Long.MAX_VALUE = 292471208.7 년})!!!
            const val REFRESH_TOKEN_EXPIRATION_TIME_SEC = 60L * 60L * 24L * 7L // 7일

            // (JWT Claims 암호화 AES 키)
            // !!!암호키 변경!!!
            const val JWT_CLAIMS_AES256_INITIALIZATION_VECTOR: String = "odkejduc726dj48d" // 16자
            const val JWT_CLAIMS_AES256_ENCRYPTION_KEY: String = "8fu3jd0ciiu3384hfucy36dye9sjv7b3" // 32자

            // (JWT 발행자)
            const val ISSUER = "${ApplicationConstants.PACKAGE_NAME}.service1"
        }

        // ---------------------------------------------------------------------------------------------
        // <공개 메소드 공간>
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
        ) {
            // 패턴에 매치되는지 확인
            val pattern = AntPathRequestMatcher(filterPattern)
            if (pattern.matches(request)) {
                // (리퀘스트에서 가져온 AccessToken 검증)
                // 헤더의 Authorization 의 값 가져오기
                // 정상적인 토큰값은 "Bearer {Token String}" 형식으로 온다고 가정.
                val authorization = request.getHeader("Authorization") // ex : "Bearer aqwer1234"

                if (authorization != null) {
                    // 타입과 토큰을 분리
                    val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]

                    if (authorizationSplit.size >= 2) { // 타입으로 추정되는 문장이 존재할 때
                        // 타입 분리
                        val accessTokenType = authorizationSplit[0].trim() // 첫번째 단어는 토큰 타입

                        when (accessTokenType.lowercase()) { // 타입 검증
                            "bearer" -> { // Bearer JWT 토큰 검증
                                val jwtAccessToken = authorizationSplit[1].trim() // 앞의 타입을 자르고 남은 액세스 토큰

                                // 토큰 문자열 해석 가능여부 확인
                                val tokenType: String? = try {
                                    JwtTokenUtilObject.getTokenType(jwtAccessToken)
                                } catch (_: Exception) {
                                    null
                                }

                                if (tokenType != null && // 해석 가능한 JWT 토큰이라는 뜻
                                    jwtAccessToken != "" && // 액세스 토큰이 비어있지 않음
                                    tokenType.lowercase() == "jwt" && // 토큰 타입 JWT
                                    JwtTokenUtilObject.getTokenUsage(
                                        jwtAccessToken,
                                        JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                                        JWT_CLAIMS_AES256_ENCRYPTION_KEY
                                    ).lowercase() == "access" && // 토큰 용도 확인
                                    JwtTokenUtilObject.getIssuer(jwtAccessToken) == ISSUER && // 발행인 동일
                                    JwtTokenUtilObject.validateSignature(
                                        jwtAccessToken,
                                        JWT_SECRET_KEY_STRING
                                    ) // 시크릿 검증이 유효 = 위변조 되지 않은 토큰
                                ) {
                                    // 토큰 만료 검증
                                    val jwtRemainSeconds = JwtTokenUtilObject.getRemainSeconds(jwtAccessToken)

                                    // 특정 request 에는 만료 필터링을 적용시키지 않음 (토큰 유효성 검증은 통과된 상황)
                                    if (jwtRemainSeconds > 0L) { // 만료 검증 통과
                                        val memberRoleList = JwtTokenUtilObject.getMemberRoleList(
                                            jwtAccessToken,
                                            JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                                            JWT_CLAIMS_AES256_ENCRYPTION_KEY
                                        )

                                        // 회원 권한 형식 변경
                                        val authorities: ArrayList<GrantedAuthority> = ArrayList()
                                        for (role in memberRoleList) {
                                            authorities.add(
                                                SimpleGrantedAuthority(role)
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
                                    } else { // 액세스 토큰 만료
                                        response.setHeader("api-result-code", "2")
                                        /*
                                             바로 filterChain.doFilter(request, response) 를 통해 API 에 진입합니다.
                                             SecurityContextHolder.getContext().authentication 를 입력하지 않았으므로,
                                             @PreAuthorize 설정이 된 API 진입시에는 401 에러와 함께 result code 반환,
                                             @PreAuthorize 설정이 안된 API 진입시에는 해당 API 동작 완료 후 result code 가 해당 API 의 것으로 덮어써집니다.
                                         */
                                    }
                                } else {
                                    // 올바르지 않은 Authorization Token
                                    response.setHeader("api-result-code", "1")
                                    /*
                                         바로 filterChain.doFilter(request, response) 를 통해 API 에 진입합니다.
                                         SecurityContextHolder.getContext().authentication 를 입력하지 않았으므로,
                                         @PreAuthorize 설정이 된 API 진입시에는 401 에러와 함께 result code 반환,
                                         @PreAuthorize 설정이 안된 API 진입시에는 해당 API 동작 완료 후 result code 가 해당 API 의 것으로 덮어써집니다.
                                     */
                                }
                            }

                            else -> {
                                // 올바르지 않은 Authorization Token
                                response.setHeader("api-result-code", "1")
                                /*
                                     바로 filterChain.doFilter(request, response) 를 통해 API 에 진입합니다.
                                     SecurityContextHolder.getContext().authentication 를 입력하지 않았으므로,
                                     @PreAuthorize 설정이 된 API 진입시에는 401 에러와 함께 result code 반환,
                                     @PreAuthorize 설정이 안된 API 진입시에는 해당 API 동작 완료 후 result code 가 해당 API 의 것으로 덮어써집니다.
                                 */
                            }
                        }
                    } else {
                        // 올바르지 않은 Authorization Token
                        response.setHeader("api-result-code", "1")
                        /*
                             바로 filterChain.doFilter(request, response) 를 통해 API 에 진입합니다.
                             SecurityContextHolder.getContext().authentication 를 입력하지 않았으므로,
                             @PreAuthorize 설정이 된 API 진입시에는 401 에러와 함께 result code 반환,
                             @PreAuthorize 설정이 안된 API 진입시에는 해당 API 동작 완료 후 result code 가 해당 API 의 것으로 덮어써집니다.
                         */
                    }
                }
            }

            // 필터 체인 실행
            /*
                 정상 로그인이 완료되면 Security Context 에 정보가 있고, 아니라면 없습니다.
                 로그인 되지 않은 상태로 인증/인가 어노테이션을 붙인 api 에 진입하면, 401 에러가,
                 인가받지 않은 상태로 진입하면 403 에러가 발생합니다.
             */
            filterChain.doFilter(request, response)
        }
    }
}