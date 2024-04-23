package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

// 주의 : 낙관적 Lock (@Version) 사용시 Transaction 기능과 충돌이 있음
@Entity
@Table(name = "fk_test_parent", catalog = "template")
@Comment("Foreign Key 테스트용 테이블 (부모 테이블)")
class Database1_Template_FkTestParent(
    @Column(name = "parent_name", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("부모 테이블 이름")
    var parentName: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    var uid: Long? = null

    @Column(name = "row_create_date", nullable = false, columnDefinition = "DATETIME(3)")
    @CreationTimestamp
    @Comment("행 생성일")
    var rowCreateDate: LocalDateTime? = null

    @Column(name = "row_update_date", nullable = false, columnDefinition = "DATETIME(3)")
    @UpdateTimestamp
    @Comment("행 수정일")
    var rowUpdateDate: LocalDateTime? = null

    @Column(name = "row_delete_date_str", nullable = false, columnDefinition = "VARCHAR(30)")
    @Comment("행 삭제일(yyyy-MM-dd HH:mm:ss.SSS, 삭제되지 않았다면 -)")
    var rowDeleteDateStr: String = "-"

    // ----
    // (아래는 테이블에 컬럼이 생성되지 않는 매핑 변수)
    @OneToMany(
        // mappedBy 는 자식 테이블 클래스의 Join 정보를 나타내는 변수명을 적어주면 됩니다. (변수명이 다르면 에러가 납니다.)
        // Fk 제약은 mappedBy 를 한 대상 테이블에 생성됩니다.
        mappedBy = "fkTestParent",
        // 이것에 해당하는 정보는 아래 변수를 get 했을 시점에 데이터베이스에서 가져오도록 설정
        fetch = FetchType.LAZY
    )
    var fkTestOneToManyChildList: MutableList<Database1_Template_FkTestOneToManyChild> = mutableListOf()

    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>

}