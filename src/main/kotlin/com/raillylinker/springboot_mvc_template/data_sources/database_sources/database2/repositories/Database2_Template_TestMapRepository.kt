package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables.Database2_Template_TestMap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database2_Template_TestMapRepository : JpaRepository<Database2_Template_TestMap, Long> {
}