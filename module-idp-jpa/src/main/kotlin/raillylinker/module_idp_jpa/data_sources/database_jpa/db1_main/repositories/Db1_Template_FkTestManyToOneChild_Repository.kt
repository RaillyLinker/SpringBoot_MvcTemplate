package raillylinker.module_idp_jpa.data_sources.database_jpa.db1_main.repositories

import raillylinker.module_idp_jpa.data_sources.database_jpa.db1_main.entities.Db1_Template_FkTestManyToOneChild
import raillylinker.module_idp_jpa.data_sources.database_jpa.db1_main.entities.Db1_Template_FkTestParent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Db1_Template_FkTestManyToOneChild_Repository :
    JpaRepository<Db1_Template_FkTestManyToOneChild, Long> {
    // 외래키 변수로 검색시, 테이블 컬럼명이 아닌 클래스 변수명을 기준으로 하며, 데이터 타입도 부모 테이블의 클래스 타입을 선언해야합니다.
    fun findAllByFkTestParentOrderByRowCreateDate(
        fkTestParent: Db1_Template_FkTestParent
    ): List<Db1_Template_FkTestManyToOneChild>
}