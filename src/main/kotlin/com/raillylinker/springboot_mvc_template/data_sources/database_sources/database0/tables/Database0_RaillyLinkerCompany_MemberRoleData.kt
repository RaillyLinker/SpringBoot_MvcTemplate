package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_role_data",
    catalog = "railly_linker_company",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["member_uid", "role"])
    ]
)
@Comment("회원 권한 정보 테이블")
class Database0_RaillyLinkerCompany_MemberRoleData(
    @ManyToOne
    @JoinColumn(name = "member_uid", nullable = false)
    @Comment("멤버 고유번호(RaillyLinkerProject1.member_data.uid)")
    var memberData: Database0_RaillyLinkerCompany_MemberData,

    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment(
        "권한 코드 (ROLE_{권한} 형식으로 저장합니다.) " +
                "(ex : (관리자 : ROLE_ADMIN, 개발 관계자 : ROLE_DEVELOPER, 서버 개발자 : ROLE_SERVER_DEVELOPER))"
    )
    var role: String
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


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>

}