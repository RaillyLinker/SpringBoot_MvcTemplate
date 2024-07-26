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
import org.springframework.web.servlet.ModelAndView
import java.security.Principal

@Service
class SC1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val passwordEncoder: PasswordEncoder,

    // (Database1 Repository)
    private val database1RaillyLinkerCompanyMemberDataRepository: Database1_RaillyLinkerCompany_MemberDataRepository,
    private val database1RaillyLinkerCompanyMemberRoleDataRepository: Database1_RaillyLinkerCompany_MemberRoleDataRepository
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
        mv.viewName = "template_sc1_n2/member_info"

        val memberUid = principal.name.toLong()
        val memberEntity = database1RaillyLinkerCompanyMemberDataRepository.findById(memberUid).get()

        val roleList: MutableList<String> = mutableListOf()
        for (role in memberEntity.memberRoleDataList) {
            roleList.add(role.role)
        }

        mv.addObject(
            "viewModel",
            Api2ViewModel(
                MemberInfo(
                    memberUid,
                    memberEntity.accountId,
                    roleList
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
            // 멤버 아이디
            val accountId: String,
            // 멤버 권한 리스트
            val roleList: List<String>
        )
    }

    ////
    fun api3(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        principal: Principal?,
        fail: String?
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "template_sc1_n3/login"

        mv.addObject(
            "viewModel",
            Api3ViewModel(
                principal != null,
                fail != null
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api3ViewModel(
        val loggedIn: Boolean,
        // 로그인 정보 불일치
        val loginError: Boolean
    )

    ////
    fun api4(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        principal: Principal?,
        complete: String?,
        idExists: String?
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "template_sc1_n4/join"

        mv.addObject(
            "viewModel",
            Api4ViewModel(
                principal != null,
                complete != null,
                idExists != null
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api4ViewModel(
        val loggedIn: Boolean,
        val complete: Boolean,
        val idExists: Boolean
    )

    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        principal: Principal?,
        accountId: String,
        password: String
    ): ModelAndView? {
        val mv = ModelAndView()

        if (database1RaillyLinkerCompanyMemberDataRepository.existsByAccountId(accountId.trim())) {
            mv.viewName = "redirect:/main/sc/v1/join?idExists"

            httpServletResponse.setHeader("api-result-code", "")
            httpServletResponse.status = HttpStatus.OK.value()
            return mv
        }

        val passwordEnc = passwordEncoder.encode(password)!! // 비밀번호 암호화

        // 회원가입
        database1RaillyLinkerCompanyMemberDataRepository.save(
            Database1_RaillyLinkerCompany_MemberData(
                accountId,
                passwordEnc
            )
        )

        mv.viewName = "redirect:/main/sc/v1/join?complete"

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }
}