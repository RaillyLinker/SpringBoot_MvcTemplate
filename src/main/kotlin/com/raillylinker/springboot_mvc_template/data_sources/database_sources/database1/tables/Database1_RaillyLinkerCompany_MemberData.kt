package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_data",
    catalog = "railly_linker_company",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["account_id"])
    ]
)
@Comment("회원 정보 테이블")
class Database1_RaillyLinkerCompany_MemberData(
    @Column(name = "account_id", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("계정 아이디")
    var accountId: String,

    @Column(name = "account_password", nullable = true, columnDefinition = "VARCHAR(100)")
    @Comment("계정 로그인시 사용하는 비밀번호 (계정 아이디, 이메일, 전화번호 로그인에 모두 사용됨. OAuth2 만 등록했다면 null)")
    var accountPassword: String?,

    @Column(name = "banned_before", nullable = false, columnDefinition = "DATETIME(3)")
    @Comment("계정 밴 시간 (이 시간이 지나기 전까지 계정 정지)")
    var bannedBefore: LocalDateTime
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

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var memberRoleDataList: MutableList<Database1_RaillyLinkerCompany_MemberRoleData> = mutableListOf()


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>

}