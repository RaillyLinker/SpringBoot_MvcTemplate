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
import java.util.ArrayList

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
        // (관리자 계정이 없다면 생성)
        // 만약 관리자 계정이 탈취되었다면,
        // 서버를 정지시키거나, 데이터베이스에서 재빨리 관리자 권한을 회수처리 하거나,
        // 계정 삭제 처리 후 다른 계정에 관리자 계정을 부여하여 다른 비밀번호로 복구 시키면 됩니다.
        val adminNickname = "admin"
        val adminPasswordString = "todoChange1234!"

        // 관리자 계정이 탈취되었을 때 이 계정에 권한을 부여하고 관리자 계정을 복구시키세요.
        val tempAdminNickname = "adminTemp"
        val tempAdminPasswordString = "todoChange1357!"

        if (!database1RaillyLinkerCompanyMemberDataRepository.existsByNickName(adminNickname)) {
            val password = passwordEncoder.encode(adminPasswordString)!! // 비밀번호 암호화

            // 회원가입
            val database1MemberUser = database1RaillyLinkerCompanyMemberDataRepository.save(
                Database1_RaillyLinkerCompany_MemberData(
                    adminNickname,
                    password
                )
            )

            // 역할 저장
            val database1MemberUserRoleList = ArrayList<Database1_RaillyLinkerCompany_MemberRoleData>()
            // 관리자 권한 추가
            database1MemberUserRoleList.add(
                Database1_RaillyLinkerCompany_MemberRoleData(
                    database1MemberUser,
                    "ROLE_ADMIN"
                )
            )
            database1RaillyLinkerCompanyMemberRoleDataRepository.saveAll(database1MemberUserRoleList)
        }

        // 임시 관리자 계정 생성
        if (!database1RaillyLinkerCompanyMemberDataRepository.existsByNickName(tempAdminNickname)) {
            val password = passwordEncoder.encode(tempAdminPasswordString)!! // 비밀번호 암호화

            // 회원가입
            database1RaillyLinkerCompanyMemberDataRepository.save(
                Database1_RaillyLinkerCompany_MemberData(
                    tempAdminNickname,
                    password
                )
            )
        }

        // (화면 정보 생성 및 전달)
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
                    memberEntity.nickName,
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
            // 멤버 닉네임
            val nickname: String,
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
                fail != null
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api3ViewModel(
        // 로그인 정보 불일치
        val loginError: Boolean
    )
}