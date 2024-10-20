package com.raillylinker.module_dpd_actuator.components.impls

import com.raillylinker.module_dpd_actuator.components.ActuatorWhiteList
import org.springframework.stereotype.Component
import com.raillylinker.module_idp_redis.redis_beans.redis1_main.Redis1_RuntimeConfigIpList

@Component
class ActuatorWhiteListImpl(
    private val redis1RuntimeConfigIpList: Redis1_RuntimeConfigIpList
) : ActuatorWhiteList {
    override fun getActuatorWhiteList(): List<ActuatorWhiteList.ActuatorAllowIpVo> {
        val keyValue =
            redis1RuntimeConfigIpList.findKeyValue(Redis1_RuntimeConfigIpList.KeyEnum.ACTUATOR_ALLOW_IP_LIST.name)

        val actuatorAllowIpList: MutableList<ActuatorWhiteList.ActuatorAllowIpVo> = mutableListOf()
        if (keyValue != null) {
            for (vl in keyValue.value.ipInfoList) {
                actuatorAllowIpList.add(
                    ActuatorWhiteList.ActuatorAllowIpVo(
                        vl.ip,
                        vl.desc
                    )
                )
            }
        }

        return actuatorAllowIpList
    }

    override fun setActuatorWhiteList(actuatorAllowIpVoList: List<ActuatorWhiteList.ActuatorAllowIpVo>) {
        val ipDescVoList: MutableList<Redis1_RuntimeConfigIpList.ValueVo.IpDescVo> = mutableListOf()

        for (ipDescInfo in actuatorAllowIpVoList) {
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
    }
}