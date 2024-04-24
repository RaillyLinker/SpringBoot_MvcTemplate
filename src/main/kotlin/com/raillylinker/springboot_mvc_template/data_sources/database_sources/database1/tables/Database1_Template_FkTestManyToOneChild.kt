package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

// Fk 관계 중 OneToOne 은 논리적 삭제를 적용하는 본 프로젝트에서 사용이 불가능합니다.
//     고로, One to One 역시 Many to One 을 사용하며,
//     로직상으로 활성화된 행이 한개 뿐이라고 처리하면 됩니다. (합성 Unique 로 FK 변수를 유니크 처리하면 더 좋습니다.)

// 주의 : 낙관적 Lock (@Version) 사용시 Transaction 기능과 충돌이 있음
@Entity
@Table(name = "fk_test_many_to_one_child", catalog = "template")
@Comment("Foreign Key 테스트용 테이블 (one to many 테스트용 자식 테이블)")
class Database1_Template_FkTestManyToOneChild(
    @Column(name = "child_name", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("자식 테이블 이름")
    var childName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_test_parent_uid", nullable = false)
    @Comment("FK 부모 테이블 고유번호 (template.fk_test_parent.uid)")
    var fkTestParent: Database1_Template_FkTestParent
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


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>


}