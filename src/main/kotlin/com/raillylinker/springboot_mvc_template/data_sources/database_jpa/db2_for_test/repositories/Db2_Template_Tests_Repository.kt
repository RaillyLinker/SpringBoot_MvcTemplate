package com.raillylinker.springboot_mvc_template.data_sources.database_jpa.db2_for_test.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_jpa.db2_for_test.entities.Db2_Template_TestData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Db2_Template_Tests_Repository : JpaRepository<Db2_Template_TestData, Long> {
    fun findByUidAndRowDeleteDateStr(
        uid: Long,
        rowDeleteDateStr: String
    ): Db2_Template_TestData?

    fun findAllByRowDeleteDateStrOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Db2_Template_TestData>

    fun findAllByRowDeleteDateStrNotOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Db2_Template_TestData>
}