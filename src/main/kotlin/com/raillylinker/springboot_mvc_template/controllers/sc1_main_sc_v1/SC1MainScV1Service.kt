package com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1

import com.raillylinker.springboot_mvc_template.data_sources.GlobalVariables
import com.raillylinker.springboot_mvc_template.data_sources.RuntimeConfig
import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.database_configs.Db0ForDevelopersConfig
import com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1.SC1MainScV1Service.Api2ViewModel.MemberInfo
import com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1.SC1MainScV1Service.Api3ViewModel.LockInfo
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.repositories.Db0_RaillyLinkerCompany_CompanyMemberData_Repository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.repositories.Db0_RaillyLinkerCompany_CompanyMemberLockHistory_Repository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.entities.Db0_RaillyLinkerCompany_CompanyMemberData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.entities.Db0_RaillyLinkerCompany_CompanyMemberLockHistory
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Service
import org.springframework.web.servlet.ModelAndView
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors


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
@Service
class SC1MainScV1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val passwordEncoder: PasswordEncoder,

    private val sessionRegistry: SessionRegistry,

    // (Database Repository)
    private val db0RaillyLinkerCompanyCompanyMemberDataRepository: Db0_RaillyLinkerCompany_CompanyMemberData_Repository,
    private val db0RaillyLinkerCompanyCompanyMemberLockHistoryRepository: Db0_RaillyLinkerCompany_CompanyMemberLockHistory_Repository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1HomePage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name
        // 현 세션 권한 리스트 (비로그인 : [ROLE_ANONYMOUS], 권한없음 : [])
        val roles: List<String> = authentication.authorities.map(GrantedAuthority::getAuthority)

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n1_home_page/home_page"

        mv.addObject(
            "viewModel",
            Api1ViewModel(
                username != "anonymousUser",
                activeProfile,
                roles
            )
        )

        return mv
    }

    data class Api1ViewModel(
        val loggedIn: Boolean,
        val env: String,
        val roles: List<String>
    )

    ////
    fun api2MemberInfoPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name
        // 현 세션 권한 리스트 (비로그인 : [ROLE_ANONYMOUS], 권한없음 : [])
        val roles: List<String> = authentication.authorities.map(GrantedAuthority::getAuthority)

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n2_member_info_page/member_info"

        val memberUid = username.toLong()
        val memberEntity = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(memberUid).get()

        val roleList: MutableList<String> = mutableListOf()
        for (role in roles) {
            roleList.add(role)
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
    fun api3LoginPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        fail: String?,
        expired: String?,
        duplicated: String?,
        changePassword: String?,
        lock: Long?
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n3_login_page/login"

        val lockInfo: LockInfo?
        if (lock == null) {
            lockInfo = null
        } else {
            val lockEntity: Db0_RaillyLinkerCompany_CompanyMemberLockHistory?
            val memberEntity = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(lock).get()
            val lockList = db0RaillyLinkerCompanyCompanyMemberLockHistoryRepository.findAllNowLocks(
                memberEntity, LocalDateTime.now()
            )
            lockEntity = if (lockList.isEmpty()) {
                null
            } else {
                lockList[0]
            }

            lockInfo = LockInfo(
                if (lockEntity == null) {
                    null
                } else {
                    LockInfo.LockDesc(
                        memberEntity.accountId,
                        lockEntity.rowCreateDate!!.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")),
                        lockEntity.lockBefore.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")),
                        lockEntity.lockReason
                    )
                }
            )
        }

        mv.addObject(
            "viewModel",
            Api3ViewModel(
                username != "anonymousUser",
                fail != null,
                expired != null,
                duplicated != null,
                changePassword != null,
                lockInfo
            )
        )

        return mv
    }

    data class Api3ViewModel(
        val loggedIn: Boolean,
        // 로그인 정보 불일치
        val loginError: Boolean,
        // 세션 만료
        val expired: Boolean,
        // 동시 접속 금지
        val duplicated: Boolean,
        // 비밀번호 변경
        val changePassword: Boolean,
        // 계정 정지 정보
        val lockInfo: LockInfo?
    ) {
        // 계정 정지 정보
        data class LockInfo(
            val lockDesc: LockDesc?
        ) {
            data class LockDesc(
                // 정지된 회원 아이디
                val accountId: String,
                // 언제부터 계정 정지(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)
                val lockCreate: String,
                // 이때까지 계정 정지(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)
                val lockBefore: String,
                //계정 정지 사유
                val lockReason: String
            )
        }
    }

    ////
    fun api4JoinPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        complete: String?,
        idExists: String?
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n4_join_page/join"

        mv.addObject(
            "viewModel",
            Api4ViewModel(
                username != "anonymousUser",
                complete != null,
                idExists != null
            )
        )

        return mv
    }

    data class Api4ViewModel(
        val loggedIn: Boolean,
        val complete: Boolean,
        val idExists: Boolean
    )

    ////
    @CustomTransactional([Db0ForDevelopersConfig.TRANSACTION_NAME])
    fun api5JoinProcess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        accountId: String,
        password: String
    ): ModelAndView? {
        val mv = ModelAndView()

        if (db0RaillyLinkerCompanyCompanyMemberDataRepository.existsByAccountId(accountId.trim())) {
            mv.viewName = "redirect:/main/sc/v1/join?idExists"
            return mv
        }

        val passwordEnc = passwordEncoder.encode(password)!! // 비밀번호 암호화

        // 회원가입
        db0RaillyLinkerCompanyCompanyMemberDataRepository.save(
            Db0_RaillyLinkerCompany_CompanyMemberData(
                accountId,
                passwordEnc
            )
        )

        mv.viewName = "redirect:/main/sc/v1/join?complete"
        return mv
    }

    ////
    fun api6ProjectLogListPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        currentPath: String?
    ): ModelAndView? {
        val rootLogDirFile = File(GlobalVariables.rootDirFile, "by_product_files/logs")

        // 현재 화면의 부모 폴더
        // 현재 화면의 폴더
        val currentDirFile: File = if (currentPath == null || currentPath.trim().isEmpty()) {
            // 파라미터가 없다면 루트 폴더
            rootLogDirFile
        } else {
            // 파라미터가 있다면 해당 위치
            File(rootLogDirFile, currentPath)
        }

        // 현재 로그 폴더에서 .log 확장자 파일 및 디렉토리 파일들을 추려오고 정렬
        val currentDirFiles = currentDirFile.listFiles()?.filter {
            it.isDirectory || it.extension == "log"
        }?.sortedWith(compareBy<File> { it.isDirectory }
            .thenByDescending { if (it.isDirectory) it.name else it.lastModified() })?.map {
            val filePath: String? = if (it.isDirectory) {
                null
            } else {
                it.absolutePath.replaceFirst(rootLogDirFile.absolutePath, "").drop(1)
            }

            val fileSize = if (it.isDirectory) {
                "-"
            } else {
                val fileSizeInBytes = it.length()
                val fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0)
                "%.2f MB".format(fileSizeInMB)
            }

            Api6ViewModel.FileViewModel(
                it.name,
                it.path,
                filePath,
                SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date(it.lastModified())),
                fileSize
            )
        }

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n6_project_log_list_page/project_logs"

        mv.addObject(
            "viewModel",
            Api6ViewModel(
                currentDirFile.absolutePath,
                currentDirFiles
            )
        )

        return mv
    }

    data class Api6ViewModel(
        val currentDirFilePath: String,
        val currentDirFiles: List<FileViewModel>?
    ) {
        data class FileViewModel(
            val name: String,
            val path: String,
            val filePath: String?,
            val lastModified: String,
            val fileSize: String
        )
    }


    ////
    fun api7DownloadProjectLogFile(httpServletResponse: HttpServletResponse, fileName: String): ResponseEntity<Resource>? {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명 (프로젝트 루트 경로에 있는 by_product_files/test 폴더를 기준으로 함)
        val serverFilePathObject =
            Paths.get("$projectRootAbsolutePathString/by_product_files/logs/$fileName")

        when {
            Files.isDirectory(serverFilePathObject) -> {
                // 파일이 디렉토리일때
                httpServletResponse.status = HttpStatus.NO_CONTENT.value()
                httpServletResponse.setHeader("api-result-code", "1")
                return null
            }

            Files.notExists(serverFilePathObject) -> {
                // 파일이 없을 때
                httpServletResponse.status = HttpStatus.NO_CONTENT.value()
                httpServletResponse.setHeader("api-result-code", "1")
                return null
            }
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return ResponseEntity<Resource>(
            InputStreamResource(Files.newInputStream(serverFilePathObject)),
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(fileName, StandardCharsets.UTF_8)
                    .build()
                this.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(serverFilePathObject))
            },
            HttpStatus.OK
        )
    }

    ////
    fun api8ErrorPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        type: SC1MainScV1Controller.Api8ErrorPageErrorTypeEnum
    ): ModelAndView? {
        val errorMsg = when (type) {
            SC1MainScV1Controller.Api8ErrorPageErrorTypeEnum.ACCESS_DENIED -> {
                "권한이 없습니다."
            }
        }

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n8_error_page/error_page"

        mv.addObject(
            "viewModel",
            Api8ViewModel(
                errorMsg
            )
        )

        return mv
    }

    data class Api8ViewModel(
        val errorMsg: String
    )

    ////
    fun api9RuntimeConfigEditorPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        fail: String?,
        complete: String?
    ): ModelAndView? {
        val runtimeConfigFile = File(GlobalVariables.rootDirFile, "by_product_files/runtime_config.json")

        val configJsonString: String = if (runtimeConfigFile.exists()) {
            // 파일을 읽어 String 으로 저장
            runtimeConfigFile.readText()
        } else {
            ""
        }

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n9_runtime_config_editor_page/runtime_config_editor"

        mv.addObject(
            "viewModel",
            Api9ViewModel(
                configJsonString,
                complete != null,
                fail != null
            )
        )

        return mv
    }

    data class Api9ViewModel(
        val configJsonString: String,
        val complete: Boolean,
        val fail: Boolean
    )

    ////
    @CustomTransactional([Db0ForDevelopersConfig.TRANSACTION_NAME])
    fun api10UpdateRuntimeConfig(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        configJsonString: String
    ): ModelAndView? {
        val mv = ModelAndView()

        val resultObject = RuntimeConfig.saveRuntimeConfigData(configJsonString)

        println("resultObject : $resultObject")

        if (resultObject == null) {
            mv.viewName = "redirect:/main/sc/v1/runtime-config-editor?fail"
        } else {
            mv.viewName = "redirect:/main/sc/v1/runtime-config-editor?complete"
        }

        return mv
    }

    ////
    fun api11ChangeIdPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        complete: String?,
        idExists: String?
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        val userUid: Long = authentication.name.toLong()

        val memberEntity = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(userUid).get()

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n11_change_id_page/change_id"

        mv.addObject(
            "viewModel",
            Api11ViewModel(
                memberEntity.accountId,
                complete != null,
                idExists != null
            )
        )

        return mv
    }

    data class Api11ViewModel(
        val accountId: String,
        val complete: Boolean,
        val idExists: Boolean
    )

    ////
    @CustomTransactional([Db0ForDevelopersConfig.TRANSACTION_NAME])
    fun api12ChangeIdProcess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        accountId: String
    ): ModelAndView? {
        val mv = ModelAndView()

        if (db0RaillyLinkerCompanyCompanyMemberDataRepository.existsByAccountId(accountId.trim())) {
            mv.viewName = "redirect:/main/sc/v1/change-id?idExists"
            return mv
        }

        val authentication = SecurityContextHolder.getContext().authentication
        val userUid: Long = authentication.name.toLong()
        val memberEntity = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(userUid).get()
        memberEntity.accountId = accountId
        db0RaillyLinkerCompanyCompanyMemberDataRepository.save(memberEntity)

        mv.viewName = "redirect:/main/sc/v1/change-id?complete"
        return mv
    }

    ////
    fun api13ChangePasswordPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        passwordNotMatch: String?
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "for_sc1_n13_change_password_page/change_password"

        mv.addObject(
            "viewModel",
            Api13ViewModel(
                passwordNotMatch != null
            )
        )

        return mv
    }

    data class Api13ViewModel(
        val passwordNotMatch: Boolean
    )

    ////
    @CustomTransactional([Db0ForDevelopersConfig.TRANSACTION_NAME])
    fun api14ChangePasswordProcess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        oldPassword: String,
        newPassword: String
    ): ModelAndView? {
        val mv = ModelAndView()

        val authentication = SecurityContextHolder.getContext().authentication
        val userUid: Long = authentication.name.toLong()
        val memberEntity = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(userUid).get()

        if (memberEntity.accountPassword == null || // 페스워드는 아직 만들지 않음
            !passwordEncoder.matches(oldPassword, memberEntity.accountPassword!!) // 패스워드 불일치
        ) {
            // 두 상황 모두 비밀번호 찾기를 하면 해결이 됨
            mv.viewName = "redirect:/main/sc/v1/change-password?passwordNotMatch"
            return mv
        }

        memberEntity.accountPassword = passwordEncoder.encode(newPassword) // 비밀번호는 암호화
        db0RaillyLinkerCompanyCompanyMemberDataRepository.save(memberEntity)

        // SecurityContextLogoutHandler를 사용하여 로그아웃을 처리합니다.
        SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication)

        mv.viewName = "redirect:/main/sc/v1/login?changePassword"
        return mv
    }

    ////
    fun api15WithdrawalMembershipPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        val userUid: Long = authentication.name.toLong()

        val memberEntity = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(userUid).get()

        val mv = ModelAndView()
        mv.viewName = "for_sc1_n15_withdrawal_membership_page/withdrawal"

        mv.addObject(
            "viewModel",
            Api15ViewModel(
                memberEntity.accountId
            )
        )

        return mv
    }

    data class Api15ViewModel(
        val accountId: String
    )

    ////
    @CustomTransactional([Db0ForDevelopersConfig.TRANSACTION_NAME])
    fun api16WithdrawalMembershipProcess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession
    ): ModelAndView? {
        val mv = ModelAndView()

        val authentication = SecurityContextHolder.getContext().authentication
        val userUid: Long = authentication.name.toLong()
        val memberEntity = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(userUid).get()
        val accountId = memberEntity.accountId
        db0RaillyLinkerCompanyCompanyMemberDataRepository.deleteById(userUid)

        // SecurityContextLogoutHandler를 사용하여 로그아웃을 처리합니다.
        SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication)

        mv.viewName = "redirect:/main/sc/v1/good-bye?accountId=$accountId"
        return mv
    }

    ////
    fun api17MembershipGoodbyePage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        accountId: String
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "for_sc1_n17_membership_good_bye_page/good_bye"

        mv.addObject(
            "viewModel",
            Api17ViewModel(
                accountId
            )
        )

        return mv
    }

    data class Api17ViewModel(
        val accountId: String
    )

    ////
    fun api18ChangePasswordPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        complete: String?,
        memberNotFound: String?
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "for_sc1_n18_change_password_page/member_password_change"

        mv.addObject(
            "viewModel",
            Api18ViewModel(
                complete != null,
                memberNotFound != null
            )
        )

        return mv
    }

    data class Api18ViewModel(
        val complete: Boolean,
        val memberNotFound: Boolean
    )

    ////
    @CustomTransactional([Db0ForDevelopersConfig.TRANSACTION_NAME])
    fun api19ChangePasswordProcess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        memberUid: Long,
        newPassword: String
    ): ModelAndView? {
        val mv = ModelAndView()

        val memberEntityOpt = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(memberUid)

        if (memberEntityOpt.isEmpty) {
            mv.viewName = "redirect:/main/sc/v1/member-password-change?memberNotFound"
            return mv
        }

        val memberEntity = memberEntityOpt.get()
        memberEntity.accountPassword = passwordEncoder.encode(newPassword) // 비밀번호는 암호화
        db0RaillyLinkerCompanyCompanyMemberDataRepository.save(memberEntity)

        // 강제 로그아웃 로직 추가
        for (principal in sessionRegistry.allPrincipals.stream().map { o: Any? -> o as UserDetails? }
            .collect(Collectors.toList())) {
            // 인증된 객체들중 정지시키려는 객체의 유니크값과 루프돌던 인증객체의 유니크값이 같을경우
            if (principal?.username?.toLong() == memberUid) {
                val sessionList = sessionRegistry.getAllSessions(principal, false) // 해당 인증객체로 생성된 모든 세션을 가져옴
                for (memberSession in sessionList) {
                    memberSession.expireNow() // 해당 인증객체의 현재 만료되지 않은 세션을 모두 만료시킴 -> HttpSessionEventPublisher가 세션 만료를 감지하고 로그아웃 처리시킴
                }
            }
        }

        mv.viewName = "redirect:/main/sc/v1/member-password-change?complete"
        return mv
    }

    ////
    fun api20ExpireMemberSessionPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        complete: String?,
        memberNotFound: String?
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "for_sc1_n20_expire_member_session_page/member_session_expire"

        mv.addObject(
            "viewModel",
            Api20ViewModel(
                complete != null,
                memberNotFound != null
            )
        )

        return mv
    }

    data class Api20ViewModel(
        val complete: Boolean,
        val memberNotFound: Boolean
    )

    ////
    fun api21ExpireMemberSessionProcess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        memberUid: Long
    ): ModelAndView? {
        val mv = ModelAndView()

        val memberEntityOpt = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(memberUid)

        if (memberEntityOpt.isEmpty) {
            mv.viewName = "redirect:/main/sc/v1/member-session-expire?memberNotFound"
            return mv
        }

        // 강제 로그아웃 로직 추가
        for (principal in sessionRegistry.allPrincipals.stream().map { o: Any? -> o as UserDetails? }
            .collect(Collectors.toList())) {
            // 인증된 객체들중 정지시키려는 객체의 유니크값과 루프돌던 인증객체의 유니크값이 같을경우
            if (principal?.username?.toLong() == memberUid) {
                val sessionList = sessionRegistry.getAllSessions(principal, false) // 해당 인증객체로 생성된 모든 세션을 가져옴
                for (memberSession in sessionList) {
                    memberSession.expireNow() // 해당 인증객체의 현재 만료되지 않은 세션을 모두 만료시킴 -> HttpSessionEventPublisher가 세션 만료를 감지하고 로그아웃 처리시킴
                }
            }
        }

        mv.viewName = "redirect:/main/sc/v1/member-session-expire?complete"
        return mv
    }

    ////
    fun api22ForceWithdrawalMembershipPage(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        complete: String?,
        memberNotFound: String?
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "for_sc1_n22_force_withdrawal_membership_page/member_withdrawal"

        mv.addObject(
            "viewModel",
            Api22ViewModel(
                complete != null,
                memberNotFound != null
            )
        )

        return mv
    }

    data class Api22ViewModel(
        val complete: Boolean,
        val memberNotFound: Boolean
    )

    ////
    fun api23ForceWithdrawalMembershipProcess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        memberUid: Long
    ): ModelAndView? {
        val mv = ModelAndView()

        val memberEntityOpt = db0RaillyLinkerCompanyCompanyMemberDataRepository.findById(memberUid)

        if (memberEntityOpt.isEmpty) {
            mv.viewName = "redirect:/main/sc/v1/member-withdrawal?memberNotFound"
            return mv
        }

        // 강제 회원탈퇴
        db0RaillyLinkerCompanyCompanyMemberDataRepository.deleteById(memberUid)

        // 강제 로그아웃 로직 추가
        for (principal in sessionRegistry.allPrincipals.stream().map { o: Any? -> o as UserDetails? }
            .collect(Collectors.toList())) {
            // 인증된 객체들중 정지시키려는 객체의 유니크값과 루프돌던 인증객체의 유니크값이 같을경우
            if (principal?.username?.toLong() == memberUid) {
                val sessionList = sessionRegistry.getAllSessions(principal, false) // 해당 인증객체로 생성된 모든 세션을 가져옴
                for (memberSession in sessionList) {
                    memberSession.expireNow() // 해당 인증객체의 현재 만료되지 않은 세션을 모두 만료시킴 -> HttpSessionEventPublisher가 세션 만료를 감지하고 로그아웃 처리시킴
                }
            }
        }

        mv.viewName = "redirect:/main/sc/v1/member-withdrawal?complete"
        return mv
    }
}