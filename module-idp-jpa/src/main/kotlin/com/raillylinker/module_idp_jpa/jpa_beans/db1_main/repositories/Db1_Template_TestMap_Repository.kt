package com.raillylinker.module_idp_jpa.jpa_beans.db1_main.repositories

import com.raillylinker.module_idp_jpa.jpa_beans.db1_main.entities.Db1_Template_TestMap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Db1_Template_TestMap_Repository : JpaRepository<Db1_Template_TestMap, Long> {
}