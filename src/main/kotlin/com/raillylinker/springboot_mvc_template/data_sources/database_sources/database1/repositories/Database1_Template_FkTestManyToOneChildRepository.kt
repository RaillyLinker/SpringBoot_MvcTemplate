package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Template_FkTestManyToOneChild
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database1_Template_FkTestManyToOneChildRepository :
    JpaRepository<Database1_Template_FkTestManyToOneChild, Long> {
    fun findAllByFkTestParentUidAndRowDeleteDateStrOrderByRowCreateDate(
        fkTestParentUid: Long,
        rowDeleteDateStr: String
    ): List<Database1_Template_FkTestManyToOneChild>
}