package com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.entities.Db0_RaillyLinkerCompany_CompanyMemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Db0_RaillyLinkerCompany_CompanyMemberData_Repository :
    JpaRepository<Db0_RaillyLinkerCompany_CompanyMemberData, Long> {
    fun existsByAccountId(
        accountId: String
    ): Boolean

    fun findByAccountId(
        accountId: String
    ): Db0_RaillyLinkerCompany_CompanyMemberData?
}