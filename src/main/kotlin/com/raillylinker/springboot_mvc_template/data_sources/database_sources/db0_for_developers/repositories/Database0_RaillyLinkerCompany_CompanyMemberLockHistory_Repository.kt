package com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.entities.Database0_RaillyLinkerCompany_CompanyMemberLockHistory
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db0_for_developers.entities.Database0_RaillyLinkerCompany_CompanyMemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database0_RaillyLinkerCompany_CompanyMemberLockHistory_Repository :
    JpaRepository<Database0_RaillyLinkerCompany_CompanyMemberLockHistory, Long> {
    @Query(
        """
            SELECT 
            b 
            FROM 
            Database0_RaillyLinkerCompany_CompanyMemberLockHistory b 
            WHERE 
            b.companyMemberData = :companyMemberData AND 
            b.earlyRelease IS NULL AND 
            b.lockBefore > :currentTime 
            ORDER BY b.lockBefore DESC
        """
    )
    fun findAllNowLocks(
        @Param("companyMemberData") companyMemberData: Database0_RaillyLinkerCompany_CompanyMemberData,
        @Param("currentTime") currentTime: LocalDateTime
    ): List<Database0_RaillyLinkerCompany_CompanyMemberLockHistory>
}