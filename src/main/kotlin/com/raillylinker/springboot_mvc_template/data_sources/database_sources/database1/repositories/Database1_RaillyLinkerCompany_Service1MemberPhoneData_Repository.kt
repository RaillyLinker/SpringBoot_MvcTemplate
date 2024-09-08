package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.entities.Database1_RaillyLinkerCompany_Service1MemberData
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.entities.Database1_RaillyLinkerCompany_Service1MemberPhoneData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_RaillyLinkerCompany_Service1MemberPhoneData_Repository :
    JpaRepository<Database1_RaillyLinkerCompany_Service1MemberPhoneData, Long> {
    fun findByPhoneNumber(
        phoneNumber: String
    ): Database1_RaillyLinkerCompany_Service1MemberPhoneData?

    fun existsByPhoneNumber(
        phoneNumber: String
    ): Boolean

    fun findAllByService1MemberData(
        service1MemberData: Database1_RaillyLinkerCompany_Service1MemberData
    ): List<Database1_RaillyLinkerCompany_Service1MemberPhoneData>

    fun existsByService1MemberData(
        service1MemberData: Database1_RaillyLinkerCompany_Service1MemberData
    ): Boolean
}