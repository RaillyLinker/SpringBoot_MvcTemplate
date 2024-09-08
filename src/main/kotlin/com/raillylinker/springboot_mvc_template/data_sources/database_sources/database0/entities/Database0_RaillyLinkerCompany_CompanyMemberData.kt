package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database0.entities

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "company_member_data",
    catalog = "railly_linker_company",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["account_id"])
    ]
)
@Comment("회원 정보 테이블")
class Database0_RaillyLinkerCompany_CompanyMemberData(
    @Column(name = "account_id", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("계정 아이디")
    var accountId: String,

    @Column(name = "account_password", nullable = true, columnDefinition = "VARCHAR(100)")
    @Comment("계정 로그인시 사용하는 비밀번호 (계정 아이디, 이메일, 전화번호 로그인에 모두 사용됨. OAuth2 만 등록했다면 null)")
    var accountPassword: String?
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

    @OneToMany(mappedBy = "companyMemberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var companyMemberRoleDataList: MutableList<Database0_RaillyLinkerCompany_CompanyMemberRoleData> = mutableListOf()

    @OneToMany(mappedBy = "companyMemberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var companyMemberLockHistoryList: MutableList<Database0_RaillyLinkerCompany_CompanyMemberLockHistory> =
        mutableListOf()


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>

}