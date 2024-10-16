package raillylinker.module_idp_jpa.data_sources.database_jpa.db1_main.repositories

import raillylinker.module_idp_jpa.data_sources.database_jpa.db1_main.entities.Db1_RaillyLinkerCompany_Service1JoinTheMembershipWithEmailVerificationData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Db1_RaillyLinkerCompany_Service1JoinTheMembershipWithEmailVerificationData_Repository :
    JpaRepository<Db1_RaillyLinkerCompany_Service1JoinTheMembershipWithEmailVerificationData, Long> {
}