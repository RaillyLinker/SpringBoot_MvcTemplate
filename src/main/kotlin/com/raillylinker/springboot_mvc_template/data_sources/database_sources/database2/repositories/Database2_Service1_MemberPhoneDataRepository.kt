package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables.Database2_Service1_MemberData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables.Database2_Service1_MemberPhoneData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database2_Service1_MemberPhoneDataRepository : JpaRepository<Database2_Service1_MemberPhoneData, Long> {
    fun findByPhoneNumber(phoneNumber: String): Database2_Service1_MemberPhoneData?

    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun findAllByMemberData(memberData: Database2_Service1_MemberData): List<Database2_Service1_MemberPhoneData>

    fun existsByMemberData(memberData: Database2_Service1_MemberData): Boolean
}