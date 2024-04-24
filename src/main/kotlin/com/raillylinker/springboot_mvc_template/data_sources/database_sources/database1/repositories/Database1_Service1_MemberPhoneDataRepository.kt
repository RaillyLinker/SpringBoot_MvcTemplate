package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_MemberData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_MemberPhoneData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_Service1_MemberPhoneDataRepository : JpaRepository<Database1_Service1_MemberPhoneData, Long> {
    fun findByPhoneNumberAndRowDeleteDateStr(
        phoneNumber: String,
        rowDeleteDateStr: String
    ): Database1_Service1_MemberPhoneData?

    fun existsByPhoneNumberAndRowDeleteDateStr(
        phoneNumber: String,
        rowDeleteDateStr: String
    ): Boolean

    fun findAllByMemberDataAndRowDeleteDateStr(
        memberData: Database1_Service1_MemberData,
        rowDeleteDateStr: String
    ): List<Database1_Service1_MemberPhoneData>

    fun findByUidAndMemberDataAndRowDeleteDateStr(
        uid: Long,
        memberData: Database1_Service1_MemberData,
        rowDeleteDateStr: String
    ): Database1_Service1_MemberPhoneData?

    fun existsByMemberDataAndRowDeleteDateStr(
        memberData: Database1_Service1_MemberData,
        rowDeleteDateStr: String
    ): Boolean
}