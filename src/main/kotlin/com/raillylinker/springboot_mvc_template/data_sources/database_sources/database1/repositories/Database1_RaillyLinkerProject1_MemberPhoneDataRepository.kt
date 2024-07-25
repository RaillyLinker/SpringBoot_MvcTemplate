package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_RaillyLinkerProject1_MemberData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_RaillyLinkerProject1_MemberPhoneData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_RaillyLinkerProject1_MemberPhoneDataRepository : JpaRepository<Database1_RaillyLinkerProject1_MemberPhoneData, Long> {
    fun findByPhoneNumber(phoneNumber: String): Database1_RaillyLinkerProject1_MemberPhoneData?

    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun findAllByMemberData(memberData: Database1_RaillyLinkerProject1_MemberData): List<Database1_RaillyLinkerProject1_MemberPhoneData>

    fun existsByMemberData(memberData: Database1_RaillyLinkerProject1_MemberData): Boolean
}