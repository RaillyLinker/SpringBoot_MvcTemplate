package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_RaillyLinkerCompany_MemberBanHistory
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_RaillyLinkerCompany_MemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_RaillyLinkerCompany_MemberBanHistoryRepository :
    JpaRepository<Database1_RaillyLinkerCompany_MemberBanHistory, Long> {
    @Query(
        """
            SELECT 
            b 
            FROM 
            Database1_RaillyLinkerCompany_MemberBanHistory b 
            WHERE 
            b.memberData = :memberData AND 
            b.earlyRelease IS NULL AND 
            b.bannedBefore > :currentTime
        """
    )
    fun findAllNowBans(
        @Param("memberData") memberData: Database1_RaillyLinkerCompany_MemberData,
        @Param("currentTime") currentTime: LocalDateTime
    ): List<Database1_RaillyLinkerCompany_MemberBanHistory>
}