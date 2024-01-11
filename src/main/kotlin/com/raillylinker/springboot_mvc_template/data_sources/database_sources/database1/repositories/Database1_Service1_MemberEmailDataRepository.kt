package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Service1_MemberEmailData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_Service1_MemberEmailDataRepository : JpaRepository<Database1_Service1_MemberEmailData, Long> {
    fun findByEmailAddressAndRowActivate(emailAddress: String, rowActivate: Boolean): Database1_Service1_MemberEmailData?
    fun existsByEmailAddressAndRowActivate(emailAddress: String, rowActivate: Boolean): Boolean
    fun findAllByMemberUidAndRowActivate(memberUid: Long, rowActivate: Boolean): List<Database1_Service1_MemberEmailData>
    fun findByUidAndMemberUidAndRowActivate(uid: Long, memberUid: Long, rowActivate: Boolean): Database1_Service1_MemberEmailData?
    fun existsByMemberUidAndRowActivate(memberUid: Long, rowActivate: Boolean): Boolean
}