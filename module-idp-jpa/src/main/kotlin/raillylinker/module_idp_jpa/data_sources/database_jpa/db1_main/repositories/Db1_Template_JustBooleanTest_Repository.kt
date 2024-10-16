package raillylinker.module_idp_jpa.data_sources.database_jpa.db1_main.repositories

import raillylinker.module_idp_jpa.data_sources.database_jpa.db1_main.entities.Db1_Template_JustBooleanTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Db1_Template_JustBooleanTest_Repository : JpaRepository<Db1_Template_JustBooleanTest, Long> {
}