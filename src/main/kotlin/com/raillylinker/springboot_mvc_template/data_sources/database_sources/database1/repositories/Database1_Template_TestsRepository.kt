package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Template_TestData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface Database1_Template_TestsRepository : JpaRepository<Database1_Template_TestData, Long> {
    fun findAllByRowDeleteDateOrderByRowCreateDate(
        rowDeleteDate : LocalDateTime?,
        pageable: Pageable
    ): Page<Database1_Template_TestData>

    fun countByRowDeleteDate(rowDeleteDate : LocalDateTime?): Long

}