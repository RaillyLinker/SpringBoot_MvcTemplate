package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_email_data",
    catalog = "service1",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email_address", "row_delete_date_str"])
    ]
)
@Comment("회원 이메일 정보 테이블")
class Database1_Service1_MemberEmailData(
    @Column(name = "member_uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("멤버 고유값 (member.members.uid)")
    var memberUid: Long,

    @Column(name = "email_address", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("이메일 주소 (중복 비허용)")
    var emailAddress: String
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