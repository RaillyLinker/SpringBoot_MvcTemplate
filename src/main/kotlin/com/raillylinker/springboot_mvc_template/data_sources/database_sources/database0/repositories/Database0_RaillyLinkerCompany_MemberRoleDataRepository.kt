package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.tables.Database0_RaillyLinkerCompany_MemberData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.tables.Database0_RaillyLinkerCompany_MemberRoleData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database0_RaillyLinkerCompany_MemberRoleDataRepository :
    JpaRepository<Database0_RaillyLinkerCompany_MemberRoleData, Long> {
    fun findAllByMemberData(memberData: Database0_RaillyLinkerCompany_MemberData): List<Database0_RaillyLinkerCompany_MemberRoleData>
}