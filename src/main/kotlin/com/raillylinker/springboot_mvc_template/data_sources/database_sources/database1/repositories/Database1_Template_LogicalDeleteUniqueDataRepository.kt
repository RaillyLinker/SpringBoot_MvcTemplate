package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.entities.Database1_Template_LogicalDeleteUniqueData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database1_Template_LogicalDeleteUniqueDataRepository :
    JpaRepository<Database1_Template_LogicalDeleteUniqueData, Long> {
    fun findByUidAndRowDeleteDateStr(
        uid: Long,
        rowDeleteDateStr: String
    ): Database1_Template_LogicalDeleteUniqueData?

    fun findAllByRowDeleteDateStrOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database1_Template_LogicalDeleteUniqueData>

    fun findAllByRowDeleteDateStrNotOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database1_Template_LogicalDeleteUniqueData>

    fun findByUniqueValueAndRowDeleteDateStr(
        uniqueValue: Int,
        rowDeleteDateStr: String
    ): Database1_Template_LogicalDeleteUniqueData?
}