package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.entities.Database0_Template_TestData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database0_Template_TestsRepository : JpaRepository<Database0_Template_TestData, Long> {
    fun findByUidAndRowDeleteDateStr(
        uid: Long,
        rowDeleteDateStr: String
    ): Database0_Template_TestData?

    fun findAllByRowDeleteDateStrOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database0_Template_TestData>

    fun findAllByRowDeleteDateStrNotOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database0_Template_TestData>
}