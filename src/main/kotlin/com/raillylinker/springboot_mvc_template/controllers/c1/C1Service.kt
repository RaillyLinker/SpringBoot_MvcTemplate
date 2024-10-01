package com.raillylinker.springboot_mvc_template.controllers.c1

import com.raillylinker.springboot_mvc_template.data_sources.shared_memory_redis.redis1_main.Redis1_RuntimeConfigIpList
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

    private val redis1RuntimeConfigIpList: Redis1_RuntimeConfigIpList
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1GetRoot(
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "forward:/main/sc/v1/home"

        return mv
    }


    ////
    fun api2SelectAllProjectRuntimeConfigsRedisKeyValue(httpServletResponse: HttpServletResponse): C1Controller.Api2SelectAllProjectRuntimeConfigsRedisKeyValueOutputVo? {
        // 전체 조회 테스트
        val keyValueList = redis1RuntimeConfigIpList.findAllKeyValues()

        val testEntityListVoList =
            ArrayList<C1Controller.Api2SelectAllProjectRuntimeConfigsRedisKeyValueOutputVo.KeyValueVo>()
        for (keyValue in keyValueList) {
            val ipDescVoList =
                ArrayList<C1Controller.Api2SelectAllProjectRuntimeConfigsRedisKeyValueOutputVo.KeyValueVo.IpDescVo>()
            for (ipInfo in keyValue.value.ipInfoList) {
                ipDescVoList.add(
                    C1Controller.Api2SelectAllProjectRuntimeConfigsRedisKeyValueOutputVo.KeyValueVo.IpDescVo(
                        ipInfo.ip,
                        ipInfo.desc
                    )
                )
            }

            testEntityListVoList.add(
                C1Controller.Api2SelectAllProjectRuntimeConfigsRedisKeyValueOutputVo.KeyValueVo(
                    keyValue.key,
                    ipDescVoList,
                    keyValue.expireTimeMs
                )
            )
        }

        httpServletResponse.status = HttpStatus.OK.value()
        return C1Controller.Api2SelectAllProjectRuntimeConfigsRedisKeyValueOutputVo(
            testEntityListVoList
        )
    }


    ////
    fun api3InsertProjectRuntimeConfigActuatorAllowIpList(
        httpServletResponse: HttpServletResponse,
        inputVo: C1Controller.Api3InsertProjectRuntimeConfigActuatorAllowIpListInputVo
    ) {
        val ipDescVoList: MutableList<Redis1_RuntimeConfigIpList.ValueVo.IpDescVo> = mutableListOf()

        for (ipDescInfo in inputVo.ipInfoList) {
            ipDescVoList.add(
                Redis1_RuntimeConfigIpList.ValueVo.IpDescVo(
                    ipDescInfo.ip,
                    ipDescInfo.desc
                )
            )
        }

        redis1RuntimeConfigIpList.saveKeyValue(
            Redis1_RuntimeConfigIpList.KeyEnum.ACTUATOR_ALLOW_IP_LIST.name,
            Redis1_RuntimeConfigIpList.ValueVo(
                ipDescVoList
            ),
            null
        )

        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api4InsertProjectRuntimeConfigLoggingDenyIpList(
        httpServletResponse: HttpServletResponse,
        inputVo: C1Controller.Api4InsertProjectRuntimeConfigLoggingDenyIpListInputVo
    ) {
        val ipDescVoList: MutableList<Redis1_RuntimeConfigIpList.ValueVo.IpDescVo> = mutableListOf()

        for (ipDescInfo in inputVo.ipInfoList) {
            ipDescVoList.add(
                Redis1_RuntimeConfigIpList.ValueVo.IpDescVo(
                    ipDescInfo.ip,
                    ipDescInfo.desc
                )
            )
        }

        redis1RuntimeConfigIpList.saveKeyValue(
            Redis1_RuntimeConfigIpList.KeyEnum.LOGGING_DENY_IP_LIST.name,
            Redis1_RuntimeConfigIpList.ValueVo(
                ipDescVoList
            ),
            null
        )

        httpServletResponse.status = HttpStatus.OK.value()
    }
}