package com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.entities.Db1_RaillyLinkerCompany_Service1MemberLockHistory
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.entities.Db1_RaillyLinkerCompany_Service1MemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Db1_RaillyLinkerCompany_Service1MemberLockHistory_Repository :
    JpaRepository<Db1_RaillyLinkerCompany_Service1MemberLockHistory, Long> {
    @Query(
        """
            SELECT 
            b 
            FROM 
            Db1_RaillyLinkerCompany_Service1MemberLockHistory b 
            WHERE 
            b.service1MemberData = :service1MemberData AND 
            b.earlyRelease IS NULL AND 
            b.lockBefore > :currentTime 
            ORDER BY b.lockBefore DESC
        """
    )
    fun findAllNowLocks(
        @Param("service1MemberData") service1MemberData: Db1_RaillyLinkerCompany_Service1MemberData,
        @Param("currentTime") currentTime: LocalDateTime
    ): List<Db1_RaillyLinkerCompany_Service1MemberLockHistory>
}