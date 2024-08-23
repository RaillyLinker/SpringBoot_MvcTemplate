package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Template_TestData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.data.repository.query.Param;

@Repository
interface Database1_Template_TestsRepository : JpaRepository<Database1_Template_TestData, Long> {
    fun findAllByRowDeleteDateStrOrderByRowCreateDate(
        rowDeleteDateStr: String,
        pageable: Pageable
    ): Page<Database1_Template_TestData>

    fun countByRowDeleteDateStr(
        rowDeleteDateStr: String
    ): Long

    fun findByUidAndRowDeleteDateStr(
        uid: Long,
        rowDeleteDateStr: String
    ): Database1_Template_TestData?

    fun findAllByRowDeleteDateStrOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database1_Template_TestData>

    fun findAllByRowDeleteDateStrNotOrderByRowCreateDate(
        rowDeleteDateStr: String
    ): List<Database1_Template_TestData>

    fun findAllByContentOrderByRowCreateDate(
        content: String
    ): List<Database1_Template_TestData>

    @Query(
        """
        SELECT 
        template_test_data 
        FROM 
        Database1_Template_TestData AS template_test_data 
        WHERE 
        template_test_data.content = :content 
        order by 
        template_test_data.rowCreateDate desc
    """
    )
    fun findAllByContentOrderByRowCreateDateJpql(
        @Param("content") content: String
    ): List<Database1_Template_TestData>
}