package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_RuntimeConfigData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_RuntimeConfigDataForActuatorAllowIp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_Service1_RuntimeConfigDataForActuatorAllowIpRepository :
    JpaRepository<Database1_Service1_RuntimeConfigDataForActuatorAllowIp, Long> {
    fun findAllByRuntimeConfigDataOrderByUid(
        runtimeConfigData: Database1_Service1_RuntimeConfigData
    ): List<Database1_Service1_RuntimeConfigDataForActuatorAllowIp>
}