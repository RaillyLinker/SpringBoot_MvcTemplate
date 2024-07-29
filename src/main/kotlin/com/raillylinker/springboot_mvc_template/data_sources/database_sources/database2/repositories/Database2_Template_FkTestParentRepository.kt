package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables.Database2_Template_FkTestParent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database2_Template_FkTestParentRepository : JpaRepository<Database2_Template_FkTestParent, Long> {
    fun findAllByOrderByRowCreateDate(): List<Database2_Template_FkTestParent>
}