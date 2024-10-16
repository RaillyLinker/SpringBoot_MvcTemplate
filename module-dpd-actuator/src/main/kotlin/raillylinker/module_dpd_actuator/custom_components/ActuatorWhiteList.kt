package raillylinker.module_dpd_actuator.custom_components

import org.springframework.stereotype.Component
import raillylinker.module_idp_redis.data_sources.shared_memory_redis.redis1_main.Redis1_RuntimeConfigIpList

@Component
class ActuatorWhiteList(
    private val redis1RuntimeConfigIpList: Redis1_RuntimeConfigIpList
) {
    fun getActuatorWhiteList(): List<ActuatorAllowIpVo> {
        val keyValue =
            redis1RuntimeConfigIpList.findKeyValue(Redis1_RuntimeConfigIpList.KeyEnum.ACTUATOR_ALLOW_IP_LIST.name)

        val actuatorAllowIpList: MutableList<ActuatorAllowIpVo> = mutableListOf()
        if (keyValue != null) {
            for (vl in keyValue.value.ipInfoList) {
                actuatorAllowIpList.add(
                    ActuatorAllowIpVo(
                        vl.ip,
                        vl.desc
                    )
                )
            }
        }

        return actuatorAllowIpList
    }

    fun setActuatorWhiteList(actuatorAllowIpVoList: List<ActuatorAllowIpVo>) {
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

    data class ActuatorAllowIpVo(
        // 설정 ip
        val ip: String,
        // ip 설명
        val desc: String
    )
}