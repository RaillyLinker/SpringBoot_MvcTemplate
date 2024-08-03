package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.tables.Database0_RaillyLinkerCompany_MemberLockHistory
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.tables.Database0_RaillyLinkerCompany_MemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database0_RaillyLinkerCompany_MemberLockHistoryRepository :
    JpaRepository<Database0_RaillyLinkerCompany_MemberLockHistory, Long> {
    @Query(
        """
            SELECT 
            b 
            FROM 
            Database0_RaillyLinkerCompany_MemberLockHistory b 
            WHERE 
            b.memberData = :memberData AND 
            b.earlyRelease IS NULL AND 
            b.lockBefore > :currentTime 
            ORDER BY b.lockBefore DESC
        """
    )
    fun findAllNowLocks(
        @Param("memberData") memberData: Database0_RaillyLinkerCompany_MemberData,
        @Param("currentTime") currentTime: LocalDateTime
    ): List<Database0_RaillyLinkerCompany_MemberLockHistory>
}