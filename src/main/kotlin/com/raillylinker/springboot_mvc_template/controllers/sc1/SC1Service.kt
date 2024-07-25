package com.raillylinker.springboot_mvc_template.controllers.sc1

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.*
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.servlet.ModelAndView
import java.security.Principal

@Service
class SC1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // (Database1 Repository)
    private val database1Service1MemberDataRepository: Database1_Service1_MemberDataRepository,
    private val database1Service1MemberRoleDataRepository: Database1_Service1_MemberRoleDataRepository,
    private val database1Service1MemberEmailDataRepository: Database1_Service1_MemberEmailDataRepository,
    private val database1Service1MemberPhoneDataRepository: Database1_Service1_MemberPhoneDataRepository,
    private val database1Service1MemberOauth2LoginDataRepository: Database1_Service1_MemberOauth2LoginDataRepository,
    private val database1Service1JoinTheMembershipWithPhoneNumberVerificationDataRepository: Database1_Service1_JoinTheMembershipWithPhoneNumberVerificationDataRepository,
    private val database1Service1JoinTheMembershipWithEmailVerificationDataRepository: Database1_Service1_JoinTheMembershipWithEmailVerificationDataRepository,
    private val database1Service1JoinTheMembershipWithOauth2VerificationDataRepository: Database1_Service1_JoinTheMembershipWithOauth2VerificationDataRepository,
    private val database1Service1FindPasswordWithPhoneNumberVerificationDataRepository: Database1_Service1_FindPasswordWithPhoneNumberVerificationDataRepository,
    private val database1Service1FindPasswordWithEmailVerificationDataRepository: Database1_Service1_FindPasswordWithEmailVerificationDataRepository,
    private val database1Service1AddEmailVerificationDataRepository: Database1_Service1_AddEmailVerificationDataRepository,
    private val database1Service1AddPhoneNumberVerificationDataRepository: Database1_Service1_AddPhoneNumberVerificationDataRepository,
    private val database1Service1MemberProfileDataRepository: Database1_Service1_MemberProfileDataRepository,
    private val database1Service1LogInTokenHistoryRepository: Database1_Service1_LogInTokenHistoryRepository
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

        mv.addObject(
            "viewModel",
            Api2ViewModel(true)
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api2ViewModel(
        val placeHolder: Boolean
    )

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