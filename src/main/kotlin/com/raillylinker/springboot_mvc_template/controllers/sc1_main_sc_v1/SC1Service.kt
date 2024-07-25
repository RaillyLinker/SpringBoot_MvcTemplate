package com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1

import com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1.SC1Service.Api2ViewModel.MemberInfo
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.*
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.*
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.servlet.ModelAndView
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.Principal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.ArrayList

@Service
class SC1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val passwordEncoder: PasswordEncoder,

    // (Database1 Repository)
    private val database1RaillyLinkerProject1MemberDataRepository: Database1_RaillyLinkerProject1_MemberDataRepository,
    private val database1RaillyLinkerProject1MemberRoleDataRepository: Database1_RaillyLinkerProject1_MemberRoleDataRepository,
    private val database1RaillyLinkerProject1MemberEmailDataRepository: Database1_RaillyLinkerProject1_MemberEmailDataRepository,
    private val database1RaillyLinkerProject1MemberPhoneDataRepository: Database1_RaillyLinkerProject1_MemberPhoneDataRepository,
    private val database1RaillyLinkerProject1MemberOauth2LoginDataRepository: Database1_RaillyLinkerProject1_MemberOauth2LoginDataRepository,
    private val database1RaillyLinkerProject1JoinTheMembershipWithPhoneNumberVerificationDataRepository: Database1_RaillyLinkerProject1_JoinTheMembershipWithPhoneNumberVerificationDataRepository,
    private val database1RaillyLinkerProject1JoinTheMembershipWithEmailVerificationDataRepository: Database1_RaillyLinkerProject1_JoinTheMembershipWithEmailVerificationDataRepository,
    private val database1RaillyLinkerProject1JoinTheMembershipWithOauth2VerificationDataRepository: Database1_RaillyLinkerProject1_JoinTheMembershipWithOauth2VerificationDataRepository,
    private val database1RaillyLinkerProject1FindPasswordWithPhoneNumberVerificationDataRepository: Database1_RaillyLinkerProject1_FindPasswordWithPhoneNumberVerificationDataRepository,
    private val database1RaillyLinkerProject1FindPasswordWithEmailVerificationDataRepository: Database1_RaillyLinkerProject1_FindPasswordWithEmailVerificationDataRepository,
    private val database1RaillyLinkerProject1AddEmailVerificationDataRepository: Database1_RaillyLinkerProject1_AddEmailVerificationDataRepository,
    private val database1RaillyLinkerProject1AddPhoneNumberVerificationDataRepository: Database1_RaillyLinkerProject1_AddPhoneNumberVerificationDataRepository,
    private val database1RaillyLinkerProject1MemberProfileDataRepository: Database1_RaillyLinkerProject1_MemberProfileDataRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        principal: Principal?
    ): ModelAndView? {
        // 관리자 계정이 없다면 생성
        val adminNickname = "admin"
        val passwordString = "todoChange1234!"

        if (!database1RaillyLinkerProject1MemberDataRepository.existsByNickName(adminNickname)) {
            val password = passwordEncoder.encode(passwordString)!! // 비밀번호 암호화

            // 회원가입
            val database1MemberUser = database1RaillyLinkerProject1MemberDataRepository.save(
                Database1_RaillyLinkerProject1_MemberData(
                    adminNickname,
                    password,
                    null,
                    null,
                    null
                )
            )

            // 역할 저장
            val database1MemberUserRoleList = ArrayList<Database1_RaillyLinkerProject1_MemberRoleData>()
            // 관리자 권한 추가
            database1MemberUserRoleList.add(
                Database1_RaillyLinkerProject1_MemberRoleData(
                    database1MemberUser,
                    "ROLE_ADMIN"
                )
            )
            database1RaillyLinkerProject1MemberRoleDataRepository.saveAll(database1MemberUserRoleList)
        }

        // 화면 정보 생성 및 전달
        val mv = ModelAndView()
        mv.viewName = "template_sc1_n1/home_page"

        mv.addObject(
            "viewModel",
            Api1ViewModel(
                activeProfile,
                principal != null
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api1ViewModel(
        val env: String,
        val loggedIn: Boolean
    )

    ////
    fun api2(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        principal: Principal
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "template_sc1_n2/auth_info"

        val memberUid = principal.name.toLong()
        val memberEntity = database1RaillyLinkerProject1MemberDataRepository.findById(memberUid).get()

        val roleList: MutableList<String> = mutableListOf()
        for (role in memberEntity.memberRoleDataList) {
            roleList.add(role.role)
        }

        val emailList: MutableList<String> = mutableListOf()
        for (email in memberEntity.memberEmailDataList) {
            emailList.add(email.emailAddress)
        }

        val phoneList: MutableList<String> = mutableListOf()
        for (phone in memberEntity.memberPhoneDataList) {
            phoneList.add(phone.phoneNumber)
        }

        val oAuth2List: MutableList<MemberInfo.OAuth2Info> = mutableListOf()
        for (oAuth2 in memberEntity.memberOauth2LoginDataList) {
            oAuth2List.add(
                MemberInfo.OAuth2Info(
                    when (oAuth2.oauth2TypeCode.toInt()) {
                        1 -> {
                            "GOOGLE"
                        }

                        2 -> {
                            "NAVER"
                        }

                        3 -> {
                            "KAKAO"
                        }

                        4 -> {
                            "APPLE"
                        }

                        else -> {
                            ""
                        }
                    },
                    oAuth2.oauth2Id
                )
            )
        }

        mv.addObject(
            "viewModel",
            Api2ViewModel(
                MemberInfo(
                    memberUid,
                    memberEntity.nickName,
                    memberEntity.frontMemberProfileData?.imageFullUrl,
                    roleList,
                    emailList,
                    phoneList,
                    oAuth2List
                )
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api2ViewModel(
        val memberInfo: MemberInfo
    ) {
        data class MemberInfo(
            // 멤버 고유번호
            val memberUid: Long,
            // 멤버 닉네임
            val nickname: String,
            // 멤버 프로필 이미지 주소
            val frontProfileUrl: String?,
            // 멤버 권한 리스트
            val roleList: List<String>,
            // 등록 이메일 주소 리스트
            val emailList: List<String>,
            // 등록 전화번호 리스트
            val phoneNumberList: List<String>,
            // 등록 OAuth2 정보 리스트
            val oauth2InfoList: List<OAuth2Info>
        ) {
            data class OAuth2Info(
                // OAuth2 타입
                val oAuth2Type: String,
                // OAuth2 아이디
                val oAuth2Id: String
            )
        }
    }

    ////
    fun api3(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        principal: Principal?,
        fail: String?,
        logout: String?
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "template_sc1_n3/login"

        mv.addObject(
            "viewModel",
            Api3ViewModel(
                fail != null,
                logout != null
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api3ViewModel(
        // 로그인 정보 불일치
        val loginError: Boolean,
        // 로그아웃 진입
        val logoutEnter: Boolean
    )
}