package com.raillylinker.springboot_mvc_template.controllers.c1

import com.raillylinker.springboot_mvc_template.ApplicationRuntimeConfigs
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_RuntimeConfigDataForActuatorAllowIpRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_RuntimeConfigDataForLoggingDenyIpRepository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories.Database1_Service1_RuntimeConfigDataRepository
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.servlet.ModelAndView

@Service
class C1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // (Database1 Repository)
    private val database1Service1Service1RuntimeConfigDataRepository: Database1_Service1_RuntimeConfigDataRepository,
    private val database1Service1RuntimeConfigDataForActuatorAllowIpRepository: Database1_Service1_RuntimeConfigDataForActuatorAllowIpRepository,
    private val database1Service1RuntimeConfigDataForLoggingDenyIpRepository: Database1_Service1_RuntimeConfigDataForLoggingDenyIpRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "template_c1_n1/home_page"

        mv.addObject(
            "viewModel",
            Api1ViewModel(
                activeProfile
            )
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return mv
    }

    data class Api1ViewModel(
        val env: String
    )


    ////
    fun api2(httpServletResponse: HttpServletResponse): ApplicationRuntimeConfigs.RuntimeConfigData {
        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return ApplicationRuntimeConfigs.loadRuntimeConfigData(
            database1Service1Service1RuntimeConfigDataRepository,
            database1Service1RuntimeConfigDataForActuatorAllowIpRepository,
            database1Service1RuntimeConfigDataForLoggingDenyIpRepository
        )
    }
}