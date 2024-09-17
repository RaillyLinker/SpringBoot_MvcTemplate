package com.raillylinker.springboot_mvc_template.data_sources.database_jpa.db0_for_developers.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_jpa.db0_for_developers.entities.Db0_Template_TestData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Db0_Template_Tests_Repository : JpaRepository<Db0_Template_TestData, Long> {
    fun findByUidAndRowDeleteDateStr(
        uid: Long,
        rowDeleteDateStr: String
    ): Db0_Template_TestData?

    fun findAllByRowDeleteDateStrOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Db0_Template_TestData>

    fun findAllByRowDeleteDateStrNotOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Db0_Template_TestData>
}