package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables.Database2_Template_LogicalDeleteUniqueData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database2_Template_LogicalDeleteUniqueDataRepository :
    JpaRepository<Database2_Template_LogicalDeleteUniqueData, Long> {
    fun findByUidAndRowDeleteDateStr(
        uid: Long,
        rowDeleteDateStr: String
    ): Database2_Template_LogicalDeleteUniqueData?

    fun findAllByRowDeleteDateStrOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database2_Template_LogicalDeleteUniqueData>

    fun findAllByRowDeleteDateStrNotOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database2_Template_LogicalDeleteUniqueData>

    fun findByUniqueValueAndRowDeleteDateStr(
        uniqueValue: Int,
        rowDeleteDateStr: String
    ): Database2_Template_LogicalDeleteUniqueData?
}