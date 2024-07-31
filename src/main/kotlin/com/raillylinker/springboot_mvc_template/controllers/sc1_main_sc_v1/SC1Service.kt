package com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1

import com.raillylinker.springboot_mvc_template.ApplicationConstants
import com.raillylinker.springboot_mvc_template.ApplicationRuntimeConfigs
import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.database_configs.Database1Config
import com.raillylinker.springboot_mvc_template.controllers.sc1_main_sc_v1.SC1Service.Api2ViewModel.MemberInfo
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_RaillyLinkerCompany_MemberDataRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_RaillyLinkerCompany_MemberRoleDataRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_RaillyLinkerCompany_MemberData
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.ModelAndView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/*
    (세션 멤버 정보 가져오기)
    val authentication = SecurityContextHolder.getContext().authentication
    // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
    val username: String = authentication.name
    // 현 세션 권한 리스트 (비로그인 : [ROLE_ANONYMOUS], 권한없음 : [])
    val roles: List<String> = authentication.authorities.map(GrantedAuthority::getAuthority)
    println("username : $username")
    println("roles : $roles")
*/
@Service
class SC1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val passwordEncoder: PasswordEncoder,

    // (Database Repository)
    private val database1RaillyLinkerCompanyMemberDataRepository: Database1_RaillyLinkerCompany_MemberDataRepository,
    private val database1RaillyLinkerCompanyMemberRoleDataRepository: Database1_RaillyLinkerCompany_MemberRoleDataRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        httpServletResponse: HttpServletResponse,
        session: HttpSession
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name
        // 현 세션 권한 리스트 (비로그인 : [ROLE_ANONYMOUS], 권한없음 : [])
        val roles: List<String> = authentication.authorities.map(GrantedAuthority::getAuthority)

        val mv = ModelAndView()
        mv.viewName = "template_sc1_n1/home_page"

        mv.addObject(
            "viewModel",
            Api1ViewModel(
                username != "anonymousUser",
                activeProfile,
                roles
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api1ViewModel(
        val loggedIn: Boolean,
        val env: String,
        val roles: List<String>
    )

    ////
    fun api2(
        httpServletResponse: HttpServletResponse,
        session: HttpSession
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name
        // 현 세션 권한 리스트 (비로그인 : [ROLE_ANONYMOUS], 권한없음 : [])
        val roles: List<String> = authentication.authorities.map(GrantedAuthority::getAuthority)

        val mv = ModelAndView()
        mv.viewName = "template_sc1_n2/member_info"

        val memberUid = username.toLong()
        val memberEntity = database1RaillyLinkerCompanyMemberDataRepository.findById(memberUid).get()

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
        fail: String?,
        expired: String?
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name

        val mv = ModelAndView()
        mv.viewName = "template_sc1_n3/login"

        mv.addObject(
            "viewModel",
            Api3ViewModel(
                username != "anonymousUser",
                fail != null,
                expired != null
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api3ViewModel(
        val loggedIn: Boolean,
        // 로그인 정보 불일치
        val loginError: Boolean,
        // 세션 만료
        val expired: Boolean
    )

    ////
    fun api4(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        complete: String?,
        idExists: String?
    ): ModelAndView? {
        val authentication = SecurityContextHolder.getContext().authentication
        // 현 세션 멤버 이름 (비로그인 : "anonymousUser")
        val username: String = authentication.name

        val mv = ModelAndView()
        mv.viewName = "template_sc1_n4/join"

        mv.addObject(
            "viewModel",
            Api4ViewModel(
                username != "anonymousUser",
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
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api5(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
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

    ////
    fun api6(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        currentPath: String?
    ): ModelAndView? {
        val rootLogDirFile = File(ApplicationConstants.rootDirFile, "by_product_files/logs")

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
        mv.viewName = "template_sc1_n6/project_logs"

        mv.addObject(
            "viewModel",
            Api6ViewModel(
                currentDirFile.absolutePath,
                currentDirFiles
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
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
    fun api7(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        filePath: String
    ): ModelAndView? {
        val rootLogDirFile = File(ApplicationConstants.rootDirFile, "by_product_files/logs")
        val logFile = File(rootLogDirFile, filePath)

        val fileName: String
        val logLines: MutableList<Api7ViewModel.LogLine> = mutableListOf()
        if (logFile.exists()) {
            fileName = logFile.name

            // 로그 파일을 읽어 String 으로 저장
            val fileContent = logFile.readText()

            // 로그 라인을 추출하기 위한 정규 표현식 패턴
            val logPattern = Pattern.compile(
                """\[(\d{4}_\d{2}_\d{2}_T_\d{2}_\d{2}_\d{2}_\d{3}_\w+)] \[(DEBUG|ERROR|WARN |INFO |TRACE)](.*?)(?=\n\[\d{4}_\d{2}_\d{2}_T_\d{2}_\d{2}_\d{2}_\d{3}_\w+]|$)""",
                Pattern.DOTALL
            )

            // 정규 표현식 매처
            val matcher = logPattern.matcher(fileContent)

            // 로그 라인을 추출하고 데이터 클래스에 저장
            while (matcher.find()) {
                val logDatetime = matcher.group(1).trim()
                val logLevel = matcher.group(2).uppercase().trim()
                val logString = matcher.group(3).trim()

                // 로그 라인 객체를 리스트에 추가
                logLines.add(Api7ViewModel.LogLine(logDatetime, logLevel, logString))
            }
        } else {
            fileName = "-"
        }

        val mv = ModelAndView()
        mv.viewName = "template_sc1_n7/project_log_file"

        mv.addObject(
            "viewModel",
            Api7ViewModel(
                fileName,
                logLines
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api7ViewModel(
        val fileName: String,
        val logLines: List<LogLine>
    ) {
        data class LogLine(
            val logDatetime: String,
            val logLevel: String,
            val logString: String
        )
    }

    ////
    fun api8(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        type: SC1Controller.Api8ErrorTypeEnum
    ): ModelAndView? {
        val errorMsg = when (type) {
            SC1Controller.Api8ErrorTypeEnum.ACCESS_DENIED -> {
                "권한이 없습니다."
            }
        }

        val mv = ModelAndView()
        mv.viewName = "template_sc1_n8/error_page"

        mv.addObject(
            "viewModel",
            Api8ViewModel(
                errorMsg
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api8ViewModel(
        val errorMsg: String
    )

    ////
    fun api9(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        fail: String?,
        complete: String?
    ): ModelAndView? {
        val runtimeConfigFile = File(ApplicationConstants.rootDirFile, "by_product_files/runtime_config.json")

        val configJsonString: String = if (runtimeConfigFile.exists()) {
            // 파일을 읽어 String 으로 저장
            runtimeConfigFile.readText()
        } else {
            ""
        }

        val mv = ModelAndView()
        mv.viewName = "template_sc1_n9/runtime_config_editor"

        mv.addObject(
            "viewModel",
            Api9ViewModel(
                configJsonString,
                complete != null,
                fail != null
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api9ViewModel(
        val configJsonString: String,
        val complete: Boolean,
        val fail: Boolean
    )

    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api10(
        httpServletResponse: HttpServletResponse,
        session: HttpSession,
        configJsonString: String
    ): ModelAndView? {
        val mv = ModelAndView()

        val resultObject = ApplicationRuntimeConfigs.saveRuntimeConfigData(configJsonString)

        println("resultObject : $resultObject")

        if (resultObject == null) {
            mv.viewName = "redirect:/main/sc/v1/runtime-config-editor?fail"
        } else {
            mv.viewName = "redirect:/main/sc/v1/runtime-config-editor?complete"
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }
}