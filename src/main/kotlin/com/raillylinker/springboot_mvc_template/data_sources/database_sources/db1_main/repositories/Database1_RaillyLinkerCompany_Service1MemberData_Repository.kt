package com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.entities.Database1_RaillyLinkerCompany_Service1MemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_RaillyLinkerCompany_Service1MemberData_Repository :
    JpaRepository<Database1_RaillyLinkerCompany_Service1MemberData, Long> {
    fun existsByAccountId(
        accountId: String
    ): Boolean

    fun findByAccountId(
        accountId: String
    ): Database1_RaillyLinkerCompany_Service1MemberData?
}