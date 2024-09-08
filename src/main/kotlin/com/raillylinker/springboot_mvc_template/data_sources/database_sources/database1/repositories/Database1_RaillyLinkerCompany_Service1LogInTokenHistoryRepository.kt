package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.entities.Database1_RaillyLinkerCompany_Service1LogInTokenHistory
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.entities.Database1_RaillyLinkerCompany_Service1MemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_RaillyLinkerCompany_Service1LogInTokenHistoryRepository :
    JpaRepository<Database1_RaillyLinkerCompany_Service1LogInTokenHistory, Long> {
    fun findByTokenTypeAndAccessTokenAndLogoutDate(
        tokenType: String,
        accessToken: String,
        logoutDate: LocalDateTime?
    ): Database1_RaillyLinkerCompany_Service1LogInTokenHistory?

    fun findAllByService1MemberDataAndLogoutDate(
        service1MemberData: Database1_RaillyLinkerCompany_Service1MemberData,
        logoutDate: LocalDateTime?
    ): List<Database1_RaillyLinkerCompany_Service1LogInTokenHistory>

    fun findAllByService1MemberDataAndAccessTokenExpireWhenAfter(
        service1MemberData: Database1_RaillyLinkerCompany_Service1MemberData,
        accessTokenExpireWhenAfter: LocalDateTime
    ): List<Database1_RaillyLinkerCompany_Service1LogInTokenHistory>
}