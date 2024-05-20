package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
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

    @Column(name = "row_delete_date_str", nullable = false, columnDefinition = "VARCHAR(50)")
    @ColumnDefault("'-'")
    @Comment("행 삭제일(yyyy_MM_dd_T_HH_mm_ss_SSS_z, 삭제되지 않았다면 -)")
    var rowDeleteDateStr: String = "-"

    // ----
    // 아래와 같이 변수를 설정하면,
    // 실제 데이터베이스 테이블에는 영향이 없는 자식 클래스 리스트 조회용 변수가 만들어집니다.
    // 이를 사용하면,
    // 신기하게도 이 부모 테이블만 조회하면,
    // 프로그래밍 상으로 아래 변수 내의 각 아이템들을 조회하는 시점에 자동으로 자식 테이블 조회 쿼리가 실행되는데,
    // 문제는, 자식 테이블 조회가 findAll 로 한꺼번에 가져오는 것이 아니라, 각각을 따로 조회합니다.
    // 이렇게 되면 한번만 데이터베이스에 접속해도 되는 상황에도 비효율적으로 각각 엔티티마다 별도로 조회를 하게 됩니다.
    // 부모 테이블 조회 1번에, 자식 테이블 N번 조회마다 데이터베이스에 각각 요청을 보내는 이러한 문제를, N+1 문제라고 부릅니다.
    // 이러한 N+1 문제를 해결하려면 JpaRepository 에서 @Query 어노테이션으로 Fetch Join 처리를 해야하는데,
    // 완벽한 JPA 적용을 위해서라면 이렇게 해도 무관하지만,
    // 저의 경우 JPA 자체 제공 기능이 네이티브 쿼리에 비해서 빈약하다고 느끼기에, 이를 사용하지 않고,
    // 단순히 네이티브 쿼리 JOIN 을 사용하여 JOIN 을 하도록 할 것입니다.

//    @OneToMany(
//        // mappedBy 는 자식 테이블 클래스의 Join 정보를 나타내는 변수명을 적어주면 됩니다. (변수명이 다르면 에러가 납니다.)
//        // Fk 제약은 mappedBy 를 한 대상 테이블에 생성됩니다.
//        mappedBy = "fkTestParent",
//        // 이것에 해당하는 정보는 아래 변수를 get 했을 시점에 데이터베이스에서 가져오도록 설정
//        fetch = FetchType.LAZY
//    )
//    var fkTestManyToOneChildList: MutableList<Database1_Template_FkTestManyToOneChild> = mutableListOf()

    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>

}