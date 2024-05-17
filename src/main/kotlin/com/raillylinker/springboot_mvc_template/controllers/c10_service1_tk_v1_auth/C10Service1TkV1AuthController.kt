package com.raillylinker.springboot_mvc_template.controllers.c10_service1_tk_v1_auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

// [용어정리]
// login : 로그인
// logout : 로그아웃
// join the membership : 회원가입
// withdrawal : 회원탈퇴
// member : 회원
// non member : 비회원
// user : 서비스 이용자 (비회원 포함)
@Tag(name = "/service1/tk/v1/auth APIs", description = "C10 : 인증/인가 API 컨트롤러")
@Controller
@RequestMapping("/service1/tk/v1/auth")
class C10Service1TkV1AuthController(
    private val service: C10Service1TkV1AuthService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 비 로그인 접속 테스트",
        description = "비 로그인 접속 테스트용 API\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/for-no-logged-in"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Map<String, Any>? {
        return service.api1(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N2 : 로그인 진입 테스트 <>",
        description = "로그인 되어 있어야 진입 가능\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/for-logged-in"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Map<String, Any>? {
        return service.api2(httpServletResponse, authorization!!)
    }


    ////
    @Operation(
        summary = "N3 : ADMIN 권한 진입 테스트 <'ADMIN'>",
        description = "ADMIN 권한이 있어야 진입 가능\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/for-admin"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @ResponseBody
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Map<String, Any>? {
        return service.api3(httpServletResponse, authorization!!)
    }


    ////
    @Operation(
        summary = "N4 : Developer 권한 진입 테스트 <'ADMIN' or 'Developer'>",
        description = "Developer 권한이 있어야 진입 가능\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/for-developer"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    @ResponseBody
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Map<String, Any>? {
        return service.api4(httpServletResponse, authorization!!)
    }


    ////
    @Operation(
        summary = "N5 : 계정 비밀번호 로그인",
        description = "계정 아이디 + 비밀번호를 사용하는 로그인 요청\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 가입 되지 않은 회원\n\n" +
                "2 : 패스워드 불일치\n\n"
    )
    @PostMapping(
        path = ["/login-with-password"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api5InputVo
    ): Api5OutputVo? {
        return service.api5(httpServletResponse, inputVo)
    }

    data class Api5InputVo(
        @Schema(
            description = "로그인 타입 (0 : 닉네임, 1 : 이메일, 2 : 전화번호)",
            required = true,
            example = "1"
        )
        @JsonProperty("loginTypeCode")
        val loginTypeCode: Int,

        @Schema(
            description = "아이디 (0 : 홍길동, 1 : test@gmail.com, 2 : 82)000-0000-0000)",
            required = true,
            example = "test@gmail.com"
        )
        @JsonProperty("id")
        val id: String,

        @Schema(
            description = "사용할 비밀번호",
            required = true,
            example = "kkdli!!"
        )
        @JsonProperty("password")
        val password: String
    )

    data class Api5OutputVo(
        @Schema(description = "멤버 고유값", required = true, example = "1")
        @JsonProperty("memberUid")
        val memberUid: Long,

        @Schema(description = "닉네임", required = true, example = "홍길동")
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "권한 리스트 (관리자 : ROLE_ADMIN, 개발자 : ROLE_DEVELOPER)",
            required = true,
            example = "[\"ROLE_ADMIN\", \"ROLE_DEVELOPER\"]"
        )
        @JsonProperty("roleList")
        val roleList: List<String>,

        @Schema(description = "인증 토큰 타입", required = true, example = "Bearer")
        @JsonProperty("tokenType")
        val tokenType: String,

        @Schema(description = "엑세스 토큰", required = true, example = "kljlkjkfsdlwejoe")
        @JsonProperty("accessToken")
        val accessToken: String,

        @Schema(description = "리프레시 토큰", required = true, example = "cxfdsfpweiijewkrlerw")
        @JsonProperty("refreshToken")
        val refreshToken: String,

        @Schema(
            description = "엑세스 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("accessTokenExpireWhen")
        val accessTokenExpireWhen: String,

        @Schema(
            description = "리프레시 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("refreshTokenExpireWhen")
        val refreshTokenExpireWhen: String,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>,

        @Schema(description = "내가 등록한 Profile 정보 리스트", required = true)
        @JsonProperty("myProfileList")
        val myProfileList: List<ProfileInfo>,

        @Schema(description = "내가 등록한 이메일 정보 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<EmailInfo>,

        @Schema(description = "내가 등록한 전화번호 정보 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<PhoneNumberInfo>,

        @Schema(
            description = "계정 로그인 비밀번호 설정 Null 여부 (OAuth2 만으로 회원가입한 경우는 비밀번호가 없으므로 true)",
            required = true,
            example = "true"
        )
        @JsonProperty("authPasswordIsNull")
        val authPasswordIsNull: Boolean
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao, 4 : Apple)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2TypeCode")
            val oauth2TypeCode: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )

        @Schema(description = "Profile 정보")
        data class ProfileInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "프로필 이미지 Full URL", required = true, example = "https://profile-image.com/1.jpg")
            @JsonProperty("imageFullUrl")
            val imageFullUrl: String,
            @Schema(description = "대표 프로필 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "이메일 정보")
        data class EmailInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "이메일 주소", required = true, example = "test@gmail.com")
            @JsonProperty("emailAddress")
            val emailAddress: String,
            @Schema(description = "대표 이메일 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "전화번호 정보")
        data class PhoneNumberInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "전화번호", required = true, example = "82)010-6222-6461")
            @JsonProperty("phoneNumber")
            val phoneNumber: String,
            @Schema(description = "대표 전화번호 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )
    }

    ////
    @Operation(
        summary = "N6 : OAuth2 Code 로 OAuth2 AccessToken 발급",
        description = "OAuth2 Code 를 사용하여 얻은 OAuth2 AccessToken 발급\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 유효하지 않은 OAuth2 인증 정보\n\n"
    )
    @GetMapping(
        path = ["/oauth2-access-token"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(
            name = "oauth2TypeCode",
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            example = "1"
        )
        @RequestParam("oauth2TypeCode")
        oauth2TypeCode: Int,
        @Parameter(name = "oauth2Code", description = "OAuth2 인증으로 받은 OAuth2 Code", example = "asdfeqwer1234")
        @RequestParam("oauth2Code")
        oauth2Code: String
    ): Api6OutputVo? {
        return service.api6(httpServletResponse, oauth2TypeCode, oauth2Code)
    }

    data class Api6OutputVo(
        @Schema(
            description = "Code 로 발급받은 SNS AccessToken Type",
            required = true,
            example = "Bearer"
        )
        @JsonProperty("oAuth2AccessTokenType")
        val oAuth2AccessTokenType: String,

        @Schema(
            description = "Code 로 발급받은 SNS AccessToken",
            required = true,
            example = "abcd1234!@#$"
        )
        @JsonProperty("oAuth2AccessToken")
        val oAuth2AccessToken: String
    )

    ////
    @Operation(
        summary = "N7 : OAuth2 로그인 (Access Token)",
        description = "OAuth2 Access Token 으로 로그인 요청\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 유효하지 않은 OAuth2 Access Token\n\n" +
                "2 : 가입 되지 않은 회원\n\n"
    )
    @PostMapping(
        path = ["/login-with-oauth2-access-token"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api7InputVo
    ): Api7OutputVo? {
        return service.api7(httpServletResponse, inputVo)
    }

    data class Api7InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 TokenType + AccessToken",
            required = true,
            example = "Bearer asdfeqwer1234"
        )
        @JsonProperty("oauth2AccessToken")
        val oauth2AccessToken: String
    )

    data class Api7OutputVo(
        @Schema(description = "멤버 고유값", required = true, example = "1")
        @JsonProperty("memberUid")
        val memberUid: Long,

        @Schema(description = "닉네임", required = true, example = "홍길동")
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "권한 리스트 (관리자 : ROLE_ADMIN, 개발자 : ROLE_DEVELOPER)",
            required = true,
            example = "[\"ROLE_ADMIN\", \"ROLE_DEVELOPER\"]"
        )
        @JsonProperty("roleList")
        val roleList: List<String>,

        @Schema(description = "인증 토큰 타입", required = true, example = "Bearer")
        @JsonProperty("tokenType")
        val tokenType: String,

        @Schema(description = "엑세스 토큰", required = true, example = "kljlkjkfsdlwejoe")
        @JsonProperty("accessToken")
        val accessToken: String,

        @Schema(description = "리프레시 토큰", required = true, example = "cxfdsfpweiijewkrlerw")
        @JsonProperty("refreshToken")
        val refreshToken: String,

        @Schema(
            description = "엑세스 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("accessTokenExpireWhen")
        val accessTokenExpireWhen: String,

        @Schema(
            description = "리프레시 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("refreshTokenExpireWhen")
        val refreshTokenExpireWhen: String,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>,

        @Schema(description = "내가 등록한 Profile 정보 리스트", required = true)
        @JsonProperty("myProfileList")
        val myProfileList: List<ProfileInfo>,

        @Schema(description = "내가 등록한 이메일 정보 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<EmailInfo>,

        @Schema(description = "내가 등록한 전화번호 정보 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<PhoneNumberInfo>,

        @Schema(
            description = "계정 로그인 비밀번호 설정 Null 여부 (OAuth2 만으로 회원가입한 경우는 비밀번호가 없으므로 true)",
            required = true,
            example = "true"
        )
        @JsonProperty("authPasswordIsNull")
        val authPasswordIsNull: Boolean
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao, 4 : Apple)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2TypeCode")
            val oauth2TypeCode: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )

        @Schema(description = "Profile 정보")
        data class ProfileInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "프로필 이미지 Full URL", required = true, example = "https://profile-image.com/1.jpg")
            @JsonProperty("imageFullUrl")
            val imageFullUrl: String,
            @Schema(description = "대표 프로필 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "이메일 정보")
        data class EmailInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "이메일 주소", required = true, example = "test@gmail.com")
            @JsonProperty("emailAddress")
            val emailAddress: String,
            @Schema(description = "대표 이메일 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "전화번호 정보")
        data class PhoneNumberInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "전화번호", required = true, example = "82)010-6222-6461")
            @JsonProperty("phoneNumber")
            val phoneNumber: String,
            @Schema(description = "대표 전화번호 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )
    }

    ////
    @Operation(
        summary = "N7.1 : OAuth2 로그인 (ID Token)",
        description = "OAuth2 ID Token 으로 로그인 요청\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 유효하지 않은 OAuth2 ID Token\n\n" +
                "2 : 가입 되지 않은 회원\n\n"
    )
    @PostMapping(
        path = ["/login-with-oauth2-id-token"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api7Dot1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api7Dot1InputVo
    ): Api7Dot1OutputVo? {
        return service.api7Dot1(httpServletResponse, inputVo)
    }

    data class Api7Dot1InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (4 : Apple)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 ID Token",
            required = true,
            example = "asdfeqwer1234"
        )
        @JsonProperty("oauth2IdToken")
        val oauth2IdToken: String
    )

    data class Api7Dot1OutputVo(
        @Schema(description = "멤버 고유값", required = true, example = "1")
        @JsonProperty("memberUid")
        val memberUid: Long,

        @Schema(description = "닉네임", required = true, example = "홍길동")
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "권한 리스트 (관리자 : ROLE_ADMIN, 개발자 : ROLE_DEVELOPER)",
            required = true,
            example = "[\"ROLE_ADMIN\", \"ROLE_DEVELOPER\"]"
        )
        @JsonProperty("roleList")
        val roleList: List<String>,

        @Schema(description = "인증 토큰 타입", required = true, example = "Bearer")
        @JsonProperty("tokenType")
        val tokenType: String,

        @Schema(description = "엑세스 토큰", required = true, example = "kljlkjkfsdlwejoe")
        @JsonProperty("accessToken")
        val accessToken: String,

        @Schema(description = "리프레시 토큰", required = true, example = "cxfdsfpweiijewkrlerw")
        @JsonProperty("refreshToken")
        val refreshToken: String,

        @Schema(
            description = "엑세스 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("accessTokenExpireWhen")
        val accessTokenExpireWhen: String,

        @Schema(
            description = "리프레시 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("refreshTokenExpireWhen")
        val refreshTokenExpireWhen: String,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>,

        @Schema(description = "내가 등록한 Profile 정보 리스트", required = true)
        @JsonProperty("myProfileList")
        val myProfileList: List<ProfileInfo>,

        @Schema(description = "내가 등록한 이메일 정보 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<EmailInfo>,

        @Schema(description = "내가 등록한 전화번호 정보 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<PhoneNumberInfo>,

        @Schema(
            description = "계정 로그인 비밀번호 설정 Null 여부 (OAuth2 만으로 회원가입한 경우는 비밀번호가 없으므로 true)",
            required = true,
            example = "true"
        )
        @JsonProperty("authPasswordIsNull")
        val authPasswordIsNull: Boolean
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao, 4 : Apple)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2TypeCode")
            val oauth2TypeCode: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )

        @Schema(description = "Profile 정보")
        data class ProfileInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "프로필 이미지 Full URL", required = true, example = "https://profile-image.com/1.jpg")
            @JsonProperty("imageFullUrl")
            val imageFullUrl: String,
            @Schema(description = "대표 프로필 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "이메일 정보")
        data class EmailInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "이메일 주소", required = true, example = "test@gmail.com")
            @JsonProperty("emailAddress")
            val emailAddress: String,
            @Schema(description = "대표 이메일 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "전화번호 정보")
        data class PhoneNumberInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "전화번호", required = true, example = "82)010-6222-6461")
            @JsonProperty("phoneNumber")
            val phoneNumber: String,
            @Schema(description = "대표 전화번호 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )
    }


    ////
    @Operation(
        summary = "N8 : 로그아웃 처리 <>",
        description = "로그아웃 처리\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @DeleteMapping(
        path = ["/logout"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api8(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") authorization: String?
    ) {
        service.api8(authorization!!, httpServletResponse)
    }


    ////
    @Operation(
        summary = "N9 : 토큰 재발급 <>",
        description = "엑세스 토큰 및 리프레시 토큰 재발행\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 유효하지 않은 리프레시 토큰\n\n" +
                "2 : 리프레시 토큰 만료\n\n" +
                "3 : 리프레시 토큰이 액세스 토큰과 매칭되지 않음\n\n"
    )
    @PostMapping(
        path = ["/reissue"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api9(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api9InputVo
    ): Api9OutputVo? {
        return service.api9(authorization!!, inputVo, httpServletResponse)
    }

    data class Api9InputVo(
        @Schema(description = "리프레시 토큰 (토큰 타입을 앞에 붙이기)", required = true, example = "Bearer 1sdfsadfsdafsdafsdafd")
        @JsonProperty("refreshToken")
        val refreshToken: String
    )

    data class Api9OutputVo(
        @Schema(description = "멤버 고유값", required = true, example = "1")
        @JsonProperty("memberUid")
        val memberUid: Long,

        @Schema(description = "닉네임", required = true, example = "홍길동")
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "권한 리스트 (관리자 : ROLE_ADMIN, 개발자 : ROLE_DEVELOPER)",
            required = true,
            example = "[\"ROLE_ADMIN\", \"ROLE_DEVELOPER\"]"
        )
        @JsonProperty("roleList")
        val roleList: List<String>,

        @Schema(description = "인증 토큰 타입", required = true, example = "Bearer")
        @JsonProperty("tokenType")
        val tokenType: String,

        @Schema(description = "엑세스 토큰", required = true, example = "kljlkjkfsdlwejoe")
        @JsonProperty("accessToken")
        val accessToken: String,

        @Schema(description = "리프레시 토큰", required = true, example = "cxfdsfpweiijewkrlerw")
        @JsonProperty("refreshToken")
        val refreshToken: String,

        @Schema(
            description = "엑세스 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("accessTokenExpireWhen")
        val accessTokenExpireWhen: String,

        @Schema(
            description = "리프레시 토큰 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("refreshTokenExpireWhen")
        val refreshTokenExpireWhen: String,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>,

        @Schema(description = "내가 등록한 Profile 정보 리스트", required = true)
        @JsonProperty("myProfileList")
        val myProfileList: List<ProfileInfo>,

        @Schema(description = "내가 등록한 이메일 정보 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<EmailInfo>,

        @Schema(description = "내가 등록한 전화번호 정보 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<PhoneNumberInfo>,

        @Schema(
            description = "계정 로그인 비밀번호 설정 Null 여부 (OAuth2 만으로 회원가입한 경우는 비밀번호가 없으므로 true)",
            required = true,
            example = "true"
        )
        @JsonProperty("authPasswordIsNull")
        val authPasswordIsNull: Boolean
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao, 4 : Apple)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2TypeCode")
            val oauth2TypeCode: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )

        @Schema(description = "Profile 정보")
        data class ProfileInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "프로필 이미지 Full URL", required = true, example = "https://profile-image.com/1.jpg")
            @JsonProperty("imageFullUrl")
            val imageFullUrl: String,
            @Schema(description = "대표 프로필 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "이메일 정보")
        data class EmailInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "이메일 주소", required = true, example = "test@gmail.com")
            @JsonProperty("emailAddress")
            val emailAddress: String,
            @Schema(description = "대표 이메일 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )

        @Schema(description = "전화번호 정보")
        data class PhoneNumberInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "전화번호", required = true, example = "82)010-6222-6461")
            @JsonProperty("phoneNumber")
            val phoneNumber: String,
            @Schema(description = "대표 전화번호 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )
    }


    ////
    @Operation(
        summary = "N10 : 멤버의 현재 발행된 모든 토큰 비활성화 (= 모든 기기에서 로그아웃) <>",
        description = "멤버의 현재 발행된 모든 토큰을 비활성화 (= 모든 기기에서 로그아웃) 하는 API\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @DeleteMapping(
        path = ["/all-authorization-token"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api10(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ) {
        service.api10(authorization!!, httpServletResponse)
    }


    ////
    @Operation(
        summary = "N11 : 닉네임 중복 검사",
        description = "닉네임 중복 여부 반환\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/nickname-duplicate-check"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api11(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "nickName", description = "중복 검사 닉네임", example = "홍길동")
        @RequestParam("nickName")
        nickName: String
    ): Api11OutputVo? {
        return service.api11(
            httpServletResponse,
            nickName
        )
    }

    data class Api11OutputVo(
        @Schema(description = "중복여부", required = true, example = "false")
        @JsonProperty("duplicated")
        val duplicated: Boolean
    )


    ////
    @Operation(
        summary = "N12 : 닉네임 수정하기 <>",
        description = "닉네임 수정하기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 중복된 닉네임 - 중복검사를 했음에도 그 사이에 동일 닉네임이 등록되었을 수 있음\n\n"
    )
    @PatchMapping(
        path = ["/my/profile/nickname"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api12(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "nickName", description = "닉네임", example = "홍길동")
        @RequestParam(value = "nickName")
        nickName: String
    ) {
        service.api12(
            httpServletResponse,
            authorization!!,
            nickName
        )
    }

    ////
    @Operation(
        summary = "N13 : 이메일 회원가입 본인 인증 이메일 발송",
        description = "이메일 회원가입시 본인 이메일 확인 메일 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 기존 회원 존재\n\n"
    )
    @PostMapping(
        path = ["/join-the-membership-email-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api13(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api13InputVo
    ): Api13OutputVo? {
        return service.api13(httpServletResponse, inputVo)
    }

    data class Api13InputVo(
        @Schema(description = "수신 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String
    )

    data class Api13OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,
        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N14 : 이메일 회원가입 본인 확인 이메일에서 받은 코드 검증하기",
        description = "이메일 회원가입시 본인 이메일에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n"
    )
    @GetMapping(
        path = ["/join-the-membership-email-verification-check"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api14(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "email", description = "확인 이메일", example = "test@gmail.com")
        @RequestParam("email")
        email: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ) {
        service.api14(httpServletResponse, verificationUid, email, verificationCode)
    }


    ////
    @Operation(
        summary = "N15 : 이메일 회원가입",
        description = "이메일 회원가입 처리\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 가입된 회원이 있습니다.\n\n" +
                "5 : 이미 사용중인 닉네임\n\n"
    )
    @PostMapping(
        path = ["/join-the-membership-with-email"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api15(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        @RequestBody
        inputVo: Api15InputVo
    ) {
        service.api15(httpServletResponse, inputVo)
    }

    data class Api15InputVo(
        @Schema(
            description = "아이디 - 이메일",
            required = true,
            example = "test@gmail.com"
        )
        @JsonProperty("email")
        val email: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "사용할 비밀번호",
            required = true,
            example = "kkdli!!"
        )
        @JsonProperty("password")
        val password: String,

        @Schema(
            description = "닉네임",
            required = true,
            example = "홍길동"
        )
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "이메일 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String,

        @Schema(description = "프로필 이미지 파일", required = false)
        @JsonProperty("profileImageFile")
        val profileImageFile: MultipartFile?
    )

    ////
    @Operation(
        summary = "N16 : 전화번호 회원가입 본인 인증 문자 발송",
        description = "전화번호 회원가입시 본인 전화번호 확인 문자 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 기존 회원 존재\n\n"
    )
    @PostMapping(
        path = ["/join-the-membership-phone-number-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api16(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api16InputVo
    ): Api16OutputVo? {
        return service.api16(httpServletResponse, inputVo)
    }

    data class Api16InputVo(
        @Schema(description = "인증 문자 수신 전화번호(국가번호 + 전화번호)", required = true, example = "82)010-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String
    )

    data class Api16OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N17 : 전화번호 회원가입 본인 확인 문자에서 받은 코드 검증하기",
        description = "전화번호 회원가입시 본인 전화번호에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n"
    )
    @GetMapping(
        path = ["/join-the-membership-phone-number-verification-check"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api17(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "phoneNumber", description = "인증 문자 수신 전화번호(국가번호 + 전화번호)", example = "82)010-0000-0000")
        @RequestParam("phoneNumber")
        phoneNumber: String,
        @Parameter(name = "verificationCode", description = "확인 문자에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ) {
        service.api17(httpServletResponse, verificationUid, phoneNumber, verificationCode)
    }


    ////
    @Operation(
        summary = "N18 : 전화번호 회원가입",
        description = "전화번호 회원가입 처리\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 가입된 회원이 있습니다.\n\n" +
                "5 : 이미 사용중인 닉네임\n\n"
    )
    @PostMapping(
        path = ["/join-the-membership-with-phone-number"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api18(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        @RequestBody
        inputVo: Api18InputVo
    ) {
        service.api18(httpServletResponse, inputVo)
    }

    data class Api18InputVo(
        @Schema(
            description = "아이디 - 전화번호(국가번호 + 전화번호)",
            required = true,
            example = "82)010-0000-0000"
        )
        @JsonProperty("phoneNumber")
        val phoneNumber: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "사용할 비밀번호",
            required = true,
            example = "kkdli!!"
        )
        @JsonProperty("password")
        val password: String,

        @Schema(
            description = "닉네임",
            required = true,
            example = "홍길동"
        )
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "문자 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String,

        @Schema(description = "프로필 이미지 파일", required = false)
        @JsonProperty("profileImageFile")
        val profileImageFile: MultipartFile?
    )


    ////
    @Operation(
        summary = "N19 : OAuth2 AccessToken 으로 회원가입 검증",
        description = "OAuth2 AccessToken 으로 회원가입 검증\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 잘못된 OAuth2 AccessToken\n\n" +
                "2 : 기존 회원 존재\n\n"
    )
    @PostMapping(
        path = ["/join-the-membership-oauth2-access-token-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api19(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api19InputVo
    ): Api19OutputVo? {
        return service.api19(httpServletResponse, inputVo)
    }

    data class Api19InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 OAuth2 TokenType + AccessToken",
            required = true,
            example = "Bearer asdfeqwer1234"
        )
        @JsonProperty("oauth2AccessToken")
        val oauth2AccessToken: String
    )

    data class Api19OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "OAuth2 가입시 검증에 사용할 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("oauth2VerificationCode")
        val oauth2VerificationCode: String,

        @Schema(
            description = "가입에 사용할 OAuth2 고유 아이디",
            required = true,
            example = "abcd1234"
        )
        @JsonProperty("oauth2Id")
        val oauth2Id: String,

        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String
    )


    ////
    @Operation(
        summary = "N19.1 : OAuth2 IdToken 으로 회원가입 검증",
        description = "OAuth2 IdToken 으로 회원가입 검증\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 잘못된 OAuth2 IdToken\n\n" +
                "2 : 기존 회원 존재\n\n"
    )
    @PostMapping(
        path = ["/join-the-membership-oauth2-id-token-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api19Dot1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api19Dot1InputVo
    ): Api19Dot1OutputVo? {
        return service.api19Dot1(httpServletResponse, inputVo)
    }

    data class Api19Dot1InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (4 : Apple)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 OAuth2 IdToken",
            required = true,
            example = "asdfeqwer1234"
        )
        @JsonProperty("oauth2IdToken")
        val oauth2IdToken: String
    )

    data class Api19Dot1OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "OAuth2 가입시 검증에 사용할 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("oauth2VerificationCode")
        val oauth2VerificationCode: String,

        @Schema(
            description = "가입에 사용할 OAuth2 고유 아이디",
            required = true,
            example = "abcd1234"
        )
        @JsonProperty("oauth2Id")
        val oauth2Id: String,

        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String
    )


    ////
    @Operation(
        summary = "N20 : OAuth2 회원가입",
        description = "OAuth2 회원가입 처리\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : OAuth2 검증 요청을 보낸 적 없음\n\n" +
                "2 : OAuth2 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 가입된 회원이 있습니다.\n\n" +
                "5 : 이미 사용중인 닉네임\n\n"
    )
    @PostMapping(
        path = ["/join-the-membership-with-oauth2"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api20(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        @RequestBody
        inputVo: Api20InputVo
    ) {
        service.api20(httpServletResponse, inputVo)
    }

    data class Api20InputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "가입에 사용할 OAuth2 고유 아이디",
            required = true,
            example = "abcd1234"
        )
        @JsonProperty("oauth2Id")
        val oauth2Id: String,

        @Schema(
            description = "OAuth2 종류 코드 (1 : Google, 2 : Naver, 3 : Kakao, 4 : Apple)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "닉네임",
            required = true,
            example = "홍길동"
        )
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "oauth2Id 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String,

        @Schema(description = "프로필 이미지 파일", required = false)
        @JsonProperty("profileImageFile")
        val profileImageFile: MultipartFile?
    )


    ////
    @Operation(
        summary = "N21 : 계정 비밀번호 변경 <>",
        description = "계정 비밀번호 변경\n\n" +
                "변경 완료된 후, 기존 모든 인증/인가 토큰이 비활성화(로그아웃) 됩니다.\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 기존 비밀번호가 일치하지 않음\n\n" +
                "2 : 비번을 null 로 만들려고 할 때 account 외의 OAuth2 인증이 없기에 비번 제거 불가\n\n"
    )
    @PutMapping(
        path = ["/change-account-password"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api21(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api21InputVo
    ) {
        service.api21(httpServletResponse, authorization!!, inputVo)
    }

    data class Api21InputVo(
        @Schema(description = "기존 계정 로그인용 비밀번호(기존 비밀번호가 없다면 null)", required = false, example = "kkdli!!")
        @JsonProperty("oldPassword")
        val oldPassword: String?,

        @Schema(description = "새 계정 로그인용 비밀번호(비밀번호를 없애려면 null)", required = false, example = "fddsd##")
        @JsonProperty("newPassword")
        val newPassword: String?
    )


    ////
    @Operation(
        summary = "N22 : 이메일 비밀번호 찾기 본인 인증 이메일 발송",
        description = "이메일 비밀번호 찾기 본인 이메일 확인 메일 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 가입되지 않은 회원\n\n"
    )
    @PostMapping(
        path = ["/find-password-email-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api22(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api22InputVo
    ): Api22OutputVo? {
        return service.api22(httpServletResponse, inputVo)
    }

    data class Api22InputVo(
        @Schema(description = "수신 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String
    )

    data class Api22OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,
        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N23 : 이메일 비밀번호 찾기 본인 확인 이메일에서 받은 코드 검증하기",
        description = "이메일 비밀번호 찾기 시 본인 이메일에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n"
    )
    @GetMapping(
        path = ["/find-password-email-verification-check"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api23(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "email", description = "확인 이메일", example = "test@gmail.com")
        @RequestParam("email")
        email: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ) {
        service.api23(httpServletResponse, verificationUid, email, verificationCode)
    }


    ////
    @Operation(
        summary = "N24 : 이메일 비밀번호 찾기 완료",
        description = "계정 비밀번호를 랜덤 값으로 변경 후 인증한 이메일로 발송\n\n" +
                "변경 완료된 후, 기존 모든 인증/인가 토큰이 비활성화(로그아웃) 됩니다.\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 탈퇴한 회원입니다.\n\n"
    )
    @PostMapping(
        path = ["/find-password-with-email"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api24(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api24InputVo
    ) {
        service.api24(httpServletResponse, inputVo)
    }

    data class Api24InputVo(
        @Schema(description = "비밀번호를 찾을 계정 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "이메일 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N25 : 전화번호 비밀번호 찾기 본인 인증 문자 발송",
        description = "전화번호 비밀번호 찾기 본인 전화번호 확인 문자 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 가입되지 않은 회원\n\n"
    )
    @PostMapping(
        path = ["/find-password-phone-number-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api25(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api25InputVo
    ): Api25OutputVo? {
        return service.api25(httpServletResponse, inputVo)
    }

    data class Api25InputVo(
        @Schema(description = "수신 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String
    )

    data class Api25OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N26 : 전화번호 비밀번호 찾기 본인 확인 문자에서 받은 코드 검증하기",
        description = "전화번호 비밀번호 찾기 시 본인 전와번호에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n"
    )
    @GetMapping(
        path = ["/find-password-phone-number-verification-check"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api26(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "phoneNumber", description = "수신 전화번호", example = "82)000-0000-0000")
        @RequestParam("phoneNumber")
        phoneNumber: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ) {
        service.api26(httpServletResponse, verificationUid, phoneNumber, verificationCode)
    }


    ////
    @Operation(
        summary = "N27 : 전화번호 비밀번호 찾기 완료",
        description = "계정 비밀번호를 랜덤 값으로 변경 후 인증한 전화번호로 발송\n\n" +
                "변경 완료된 후, 기존 모든 인증/인가 토큰이 비활성화 됩니다.\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 탈퇴된 회원\n\n"
    )
    @PostMapping(
        path = ["/find-password-with-phone-number"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api27(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api27InputVo
    ) {
        service.api27(httpServletResponse, inputVo)
    }

    data class Api27InputVo(
        @Schema(description = "비밀번호를 찾을 계정 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N29 : 내 이메일 리스트 가져오기 <>",
        description = "내 이메일 리스트 가져오기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/my-email-addresses"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api29(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api29OutputVo? {
        return service.api29(httpServletResponse, authorization!!)
    }

    data class Api29OutputVo(
        @Schema(description = "내가 등록한 이메일 리스트", required = true)
        @JsonProperty("emailInfoList")
        val emailInfoList: List<EmailInfo>
    ) {
        @Schema(description = "이메일 정보")
        data class EmailInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "이메일 주소", required = true, example = "test@gmail.com")
            @JsonProperty("emailAddress")
            val emailAddress: String,
            @Schema(description = "대표 이메일 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )
    }


    ////
    @Operation(
        summary = "N30 : 내 전화번호 리스트 가져오기 <>",
        description = "내 전화번호 리스트 가져오기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/my-phone-numbers"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api30(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api30OutputVo? {
        return service.api30(httpServletResponse, authorization!!)
    }

    data class Api30OutputVo(
        @Schema(description = "내가 등록한 전화번호 리스트", required = true)
        @JsonProperty("phoneInfoList")
        val phoneInfoList: List<PhoneInfo>
    ) {
        @Schema(description = "전화번호 정보")
        data class PhoneInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "전화번호", required = true, example = "82)010-6222-6461")
            @JsonProperty("phoneNumber")
            val phoneNumber: String,
            @Schema(description = "대표 전화번호 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )
    }


    ////
    @Operation(
        summary = "N31 : 내 OAuth2 로그인 리스트 가져오기 <>",
        description = "내 OAuth2 로그인 리스트 가져오기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/my-oauth2-list"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api31(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api31OutputVo? {
        return service.api31(httpServletResponse, authorization!!)
    }

    data class Api31OutputVo(
        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao, 4 : Apple)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2Type")
            val oauth2Type: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )
    }


    ////
    @Operation(
        summary = "N32 : 이메일 추가하기 본인 인증 이메일 발송 <>",
        description = "이메일 추가하기 본인 이메일 확인 메일 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이미 사용중인 이메일\n\n"
    )
    @PostMapping(
        path = ["/add-email-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api32(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api32InputVo
    ): Api32OutputVo? {
        return service.api32(httpServletResponse, inputVo, authorization!!)
    }

    data class Api32InputVo(
        @Schema(description = "수신 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String
    )

    data class Api32OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N33 : 이메일 추가하기 본인 확인 이메일에서 받은 코드 검증하기 <>",
        description = "이메일 추가하기 본인 이메일에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n"
    )
    @GetMapping(
        path = ["/add-email-verification-check"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api33(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "email", description = "확인 이메일", example = "test@gmail.com")
        @RequestParam("email")
        email: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ) {
        service.api33(httpServletResponse, verificationUid, email, verificationCode, authorization!!)
    }


    ////
    @Operation(
        summary = "N34 : 이메일 추가하기 <>",
        description = "내 계정에 이메일 추가\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 사용중인 이메일\n\n"
    )
    @PostMapping(
        path = ["/my-new-email"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api34(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api34InputVo
    ): Api34OutputVo? {
        return service.api34(httpServletResponse, inputVo, authorization!!)
    }

    data class Api34InputVo(
        @Schema(description = "추가할 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "이메일 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String,

        @Schema(
            description = "대표 이메일 설정 여부",
            required = true,
            example = "true"
        )
        @JsonProperty("frontEmail")
        val frontEmail: Boolean
    )

    data class Api34OutputVo(
        @Schema(description = "이메일의 고유값", required = true, example = "1")
        @JsonProperty("emailUid")
        val emailUid: Long
    )


    ////
    @Operation(
        summary = "N35 : 내 이메일 제거하기 <>",
        description = "내 계정에서 이메일 제거\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : emailUid 의 정보가 없습니다.\n\n" +
                "2 : 제거할 수 없습니다. (이외에 로그인 할 방법이 없음)\n\n"
    )
    @DeleteMapping(
        path = ["/my-email/{emailUid}"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api35(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "emailUid", description = "이메일의 고유값", example = "1")
        @PathVariable("emailUid")
        emailUid: Long
    ) {
        service.api35(httpServletResponse, emailUid, authorization!!)
    }


    ////
    @Operation(
        summary = "N36 : 전화번호 추가하기 본인 인증 문자 발송 <>",
        description = "전화번호 추가하기 본인 전화번호 확인 문자 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이미 사용중인 전화번호\n\n"
    )
    @PostMapping(
        path = ["/add-phone-number-verification"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api36(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api36InputVo
    ): Api36OutputVo? {
        return service.api36(httpServletResponse, inputVo, authorization!!)
    }

    data class Api36InputVo(
        @Schema(description = "수신 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String
    )

    data class Api36OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
            required = true,
            example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N37 : 전화번호 추가하기 본인 확인 문자에서 받은 코드 검증하기 <>",
        description = "전화번호 추가하기 본인 전화번호에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n"
    )
    @GetMapping(
        path = ["/add-phone-number-verification-check"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api37(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "phoneNumber", description = "수신 전화번호", example = "82)000-0000-0000")
        @RequestParam("phoneNumber")
        phoneNumber: String,
        @Parameter(name = "verificationCode", description = "확인 문자에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ) {
        service.api37(httpServletResponse, verificationUid, phoneNumber, verificationCode, authorization!!)
    }

    ////
    @Operation(
        summary = "N38 : 전화번호 추가하기 <>",
        description = "내 계정에 전화번호 추가\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 사용중인 전화번호\n\n"
    )
    @PostMapping(
        path = ["/my-new-phone-number"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api38(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api38InputVo
    ): Api38OutputVo? {
        return service.api38(httpServletResponse, inputVo, authorization!!)
    }

    data class Api38InputVo(
        @Schema(description = "추가할 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "문자 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String,

        @Schema(
            description = "대표 전화번호 설정 여부",
            required = true,
            example = "true"
        )
        @JsonProperty("frontPhoneNumber")
        val frontPhoneNumber: Boolean
    )

    data class Api38OutputVo(
        @Schema(description = "전화번호의 고유값", required = true, example = "1")
        @JsonProperty("phoneUid")
        val phoneUid: Long
    )


    ////
    @Operation(
        summary = "N39 : 내 전화번호 제거하기 <>",
        description = "내 계정에서 전화번호 제거\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : phoneUid 의 정보가 없습니다.\n\n" +
                "2 : 제거할 수 없습니다. (이외에 로그인 할 방법이 없음)\n\n"
    )
    @DeleteMapping(
        path = ["/my-phone-number/{phoneUid}"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api39(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "phoneUid", description = "전화번호의 고유값", example = "1")
        @PathVariable("phoneUid")
        phoneUid: Long
    ) {
        service.api39(httpServletResponse, phoneUid, authorization!!)
    }


    ////
    @Operation(
        summary = "N40 : OAuth2 추가하기 (Access Token) <>",
        description = "내 계정에 OAuth2 Access Token 으로 인증 추가\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : oAuth2 Access Token 정보 검증 불일치\n\n" +
                "2 : 이미 사용중인 인증\n\n"
    )
    @PostMapping(
        path = ["/my-new-oauth2-token"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api40(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api40InputVo
    ) {
        service.api40(httpServletResponse, inputVo, authorization!!)
    }

    data class Api40InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 oauth2 TokenType + AccessToken",
            required = true,
            example = "Bearer asdfeqwer1234"
        )
        @JsonProperty("oauth2AccessToken")
        val oauth2AccessToken: String
    )


    ////
    @Operation(
        summary = "N40.1 : OAuth2 추가하기 (Id Token) <>",
        description = "내 계정에 OAuth2 Id Token 으로 인증 추가\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : oAuth2 Id Token 정보 검증 불일치\n\n" +
                "2 : 이미 사용중인 인증\n\n"
    )
    @PostMapping(
        path = ["/my-new-oauth2-id-token"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api40Dot1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api40Dot1InputVo
    ) {
        service.api40Dot1(httpServletResponse, inputVo, authorization!!)
    }

    data class Api40Dot1InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (4 : Apple)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 oauth2 IdToken",
            required = true,
            example = "asdfeqwer1234"
        )
        @JsonProperty("oauth2IdToken")
        val oauth2IdToken: String
    )


    ////
    @Operation(
        summary = "N41 : 내 OAuth2 제거하기 <>",
        description = "내 계정에서 OAuth2 제거\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : emailUid 의 정보가 없습니다.\n\n" +
                "2 : 제거할 수 없습니다. (이외에 로그인 할 방법이 없음)\n\n"
    )
    @DeleteMapping(
        path = ["/my-oauth2/{oAuth2Uid}"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api41(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "oAuth2Uid", description = "OAuth2 고유값", example = "1")
        @PathVariable("oAuth2Uid")
        oAuth2Uid: Long
    ) {
        service.api41(httpServletResponse, oAuth2Uid, authorization!!)
    }


    ////
    @Operation(
        summary = "N42 : 회원탈퇴 <>",
        description = "회원탈퇴 요청\n\n" +
                "탈퇴 완료 후 모든 토큰이 비활성화 됩니다.\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @DeleteMapping(
        path = ["/withdrawal"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api42(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ) {
        service.api42(httpServletResponse, authorization!!)
    }


    ////
    @Operation(
        summary = "N43 : 내 Profile 이미지 정보 리스트 가져오기 <>",
        description = "내 Profile 이미지 정보 리스트 가져오기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/my-profile-list"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api43(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api43OutputVo? {
        return service.api43(httpServletResponse, authorization!!)
    }

    data class Api43OutputVo(
        @Schema(description = "내가 등록한 Profile 이미지 정보 리스트", required = true)
        @JsonProperty("myProfileList")
        val myProfileList: List<ProfileInfo>
    ) {
        @Schema(description = "Profile 정보")
        data class ProfileInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "프로필 이미지 Full URL", required = true, example = "https://profile-image.com/1.jpg")
            @JsonProperty("imageFullUrl")
            val imageFullUrl: String,
            @Schema(description = "대표 프로필 여부", required = true, example = "true")
            @JsonProperty("isFront")
            val isFront: Boolean
        )
    }


    ////
    @Operation(
        summary = "N44 : 내 대표 Profile 이미지 정보 가져오기 <>",
        description = "내 대표 Profile 이미지 정보 가져오기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/my-front-profile"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api44(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api44OutputVo? {
        return service.api44(httpServletResponse, authorization!!)
    }

    data class Api44OutputVo(
        @Schema(description = "내 대표 Profile 이미지 정보", required = true)
        @JsonProperty("myFrontProfileInfo")
        val myFrontProfileInfo: ProfileInfo?
    ) {
        @Schema(description = "Profile 정보")
        data class ProfileInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "프로필 이미지 Full URL", required = true, example = "https://profile-image.com/1.jpg")
            @JsonProperty("imageFullUrl")
            val imageFullUrl: String
        )
    }


    ////
    @Operation(
        summary = "N45 : 내 대표 프로필 설정하기 <>",
        description = "내가 등록한 프로필들 중 대표 프로필 설정하기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 선택한 profileUid 가 없습니다.\n\n"
    )
    @PatchMapping(
        path = ["/my-front-profile"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api45(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "profileUid", description = "대표 프로필로 설정할 프로필의 고유값(null 이라면 대표 프로필 해제)", example = "1")
        @RequestParam(value = "profileUid")
        profileUid: Long?
    ) {
        service.api45(
            httpServletResponse,
            authorization!!,
            profileUid
        )
    }


    ////
    @Operation(
        summary = "N46 : 내 프로필 삭제 <>",
        description = "내가 등록한 프로필들 중 하나를 삭제합니다.\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : profileUid 의 정보가 없습니다.\n\n"
    )
    @DeleteMapping(
        path = ["/my-profile/{profileUid}"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api46(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "profileUid", description = "프로필의 고유값", example = "1")
        @PathVariable("profileUid")
        profileUid: Long
    ) {
        service.api46(authorization!!, httpServletResponse, profileUid)
    }


    ////
    @Operation(
        summary = "N47 : 내 프로필 이미지 추가",
        description = "내 프로필 이미지 추가\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @PostMapping(
        path = ["/my-profile"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api47(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @ModelAttribute
        @RequestBody
        inputVo: Api47InputVo
    ): Api47OutputVo? {
        return service.api47(httpServletResponse, authorization!!, inputVo)
    }

    data class Api47InputVo(
        @Schema(description = "프로필 이미지 파일", required = true)
        @JsonProperty("profileImageFile")
        val profileImageFile: MultipartFile,
        @Schema(description = "대표 이미지로 설정할 것인지 여부", required = true)
        @JsonProperty("frontProfile")
        val frontProfile: Boolean
    )

    data class Api47OutputVo(
        @Schema(description = "프로필의 고유값", required = true, example = "1")
        @JsonProperty("profileUid")
        val profileUid: Long,
        @Schema(description = "업로드한 프로필 이미지 파일 Full URL", required = true, example = "1")
        @JsonProperty("profileImageFullUrl")
        val profileImageFullUrl: String
    )


    ////
    @Operation(
        summary = "N48 : files/member/profile 폴더에서 파일 다운받기",
        description = "프로필 이미지를 files/member/profile 위치에 저장했을 때 파일을 가져오기 위한 API 로,\n\n" +
                "AWS 나 다른 Storage 서비스를 사용해도 좋습니다.\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 파일이 존재하지 않습니다.\n\n"
    )
    @GetMapping(
        path = ["/member-profile/{fileName}"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @ResponseBody
    fun api48(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "fileName", description = "files/member/profile 폴더 안의 파일명", example = "test.jpg")
        @PathVariable("fileName")
        fileName: String
    ): ResponseEntity<Resource>? {
        return service.api48(httpServletResponse, fileName)
    }


    ////
    @Operation(
        summary = "N49 : 내 대표 이메일 정보 가져오기 <>",
        description = "내 대표 이메일 정보 가져오기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/my-front-email"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api49(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api49OutputVo? {
        return service.api49(httpServletResponse, authorization!!)
    }

    data class Api49OutputVo(
        @Schema(description = "내 대표 이메일 정보", required = true)
        @JsonProperty("myFrontEmailInfo")
        val myFrontEmailInfo: EmailInfo?
    ) {
        @Schema(description = "이메일 정보")
        data class EmailInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "이메일 주소", required = true, example = "test@gmail.com")
            @JsonProperty("emailAddress")
            val emailAddress: String
        )
    }


    ////
    @Operation(
        summary = "N50 : 내 대표 이메일 설정하기 <>",
        description = "내가 등록한 이메일들 중 대표 이메일 설정하기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 선택한 emailUid 가 없습니다.\n\n"
    )
    @PatchMapping(
        path = ["/my-front-email"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api50(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "emailUid", description = "대표 이메일로 설정할 이메일의 고유값(null 이라면 대표 이메일 해제)", example = "1")
        @RequestParam(value = "emailUid")
        emailUid: Long?
    ) {
        service.api50(
            httpServletResponse,
            authorization!!,
            emailUid
        )
    }


    ////
    @Operation(
        summary = "N51 : 내 대표 전화번호 정보 가져오기 <>",
        description = "내 대표 전화번호 정보 가져오기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n"
    )
    @GetMapping(
        path = ["/my-front-phone-number"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api51(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api51OutputVo? {
        return service.api51(httpServletResponse, authorization!!)
    }

    data class Api51OutputVo(
        @Schema(description = "내 대표 전화번호 정보", required = true)
        @JsonProperty("myFrontPhoneNumberInfo")
        val myFrontPhoneNumberInfo: PhoneNumberInfo?
    ) {
        @Schema(description = "전화번호 정보")
        data class PhoneNumberInfo(
            @Schema(description = "행 고유값", required = true, example = "1")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "전화번호", required = true, example = "82)010-6222-6461")
            @JsonProperty("phoneNumber")
            val phoneNumber: String
        )
    }


    ////
    @Operation(
        summary = "N52 : 내 대표 전화번호 설정하기 <>",
        description = "내가 등록한 전화번호들 중 대표 전화번호 설정하기\n\n" +
                "(응답 코드 204 일 때 반환되는 api-result-code)\n\n" +
                "1 : 선택한 phoneNumberUid 가 없습니다.\n\n"
    )
    @PatchMapping(
        path = ["/my-front-phone-number"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    fun api52(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "phoneNumberUid", description = "대표 전화번호로 설정할 전화번호의 고유값(null 이라면 대표 전화번호 해제)", example = "1")
        @RequestParam(value = "phoneNumberUid")
        phoneNumberUid: Long?
    ) {
        service.api52(
            httpServletResponse,
            authorization!!,
            phoneNumberUid
        )
    }
}