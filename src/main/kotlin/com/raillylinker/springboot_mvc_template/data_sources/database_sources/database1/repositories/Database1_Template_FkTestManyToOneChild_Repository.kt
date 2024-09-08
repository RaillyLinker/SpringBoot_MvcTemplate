package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.entities.Database1_Template_FkTestManyToOneChild
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.entities.Database1_Template_FkTestParent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database1_Template_FkTestManyToOneChild_Repository :
    JpaRepository<Database1_Template_FkTestManyToOneChild, Long> {
    // 외래키 변수로 검색시, 테이블 컬럼명이 아닌 클래스 변수명을 기준으로 하며, 데이터 타입도 부모 테이블의 클래스 타입을 선언해야합니다.
    fun findAllByFkTestParentOrderByRowCreateDate(
        fkTestParent: Database1_Template_FkTestParent
    ): List<Database1_Template_FkTestManyToOneChild>
}