package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

// 주의 : 낙관적 Lock (@Version) 사용시 Transaction 기능과 충돌이 있음
@Entity
@Table(name = "test_data", catalog = "template")
@Comment("테스트 정보 테이블")
class Database1_Template_TestData(
    @Column(name = "content", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("테스트 본문")
    var content: String,

    @Column(name = "random_num", nullable = false, columnDefinition = "INT")
    @Comment("테스트 랜덤 번호")
    var randomNum: Int
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