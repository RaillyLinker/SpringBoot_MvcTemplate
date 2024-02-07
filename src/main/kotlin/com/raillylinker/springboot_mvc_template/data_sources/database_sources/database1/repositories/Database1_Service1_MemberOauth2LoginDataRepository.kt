package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_MemberOauth2LoginData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_Service1_MemberOauth2LoginDataRepository :
    JpaRepository<Database1_Service1_MemberOauth2LoginData, Long> {
    fun findByOauth2TypeCodeAndOauth2IdAndRowDeleteDate(
        oauth2TypeCode: Byte,
        snsId: String,
        rowDeleteDate : LocalDateTime?
    ): Database1_Service1_MemberOauth2LoginData?

    fun existsByOauth2TypeCodeAndOauth2IdAndRowDeleteDate(
        oauth2TypeCode: Byte,
        snsId: String,
        rowDeleteDate : LocalDateTime?
    ): Boolean

    fun findAllByMemberUidAndRowDeleteDate(
        memberUid: Long,
        rowDeleteDate : LocalDateTime?
    ): List<Database1_Service1_MemberOauth2LoginData>

    fun findByUidAndMemberUidAndRowDeleteDate(
        uid : Long,
        memberUid: Long,
        rowDeleteDate : LocalDateTime?
    ): Database1_Service1_MemberOauth2LoginData?

    fun existsByMemberUidAndRowDeleteDate(memberUid: Long, rowDeleteDate : LocalDateTime?): Boolean
}