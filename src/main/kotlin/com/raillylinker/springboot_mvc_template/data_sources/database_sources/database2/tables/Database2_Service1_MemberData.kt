package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_data",
    catalog = "service1",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["account_id"])
    ]
)
@Comment("회원 정보 테이블")
class Database2_Service1_MemberData(
    @Column(name = "account_id", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("계정 아이디")
    var accountId: String,

    @Column(name = "account_password", nullable = true, columnDefinition = "VARCHAR(100)")
    @Comment("계정 로그인시 사용하는 비밀번호 (계정 아이디, 이메일, 전화번호 로그인에 모두 사용됨. OAuth2 만 등록했다면 null)")
    var accountPassword: String?,

    @ManyToOne
    @JoinColumn(name = "front_profile_uid", nullable = true)
    @Comment("대표 프로필 Uid (service1.member_profile_data.uid)")
    var frontMemberProfileData: Database2_Service1_MemberProfileData?,

    @ManyToOne
    @JoinColumn(name = "front_email_uid", nullable = true)
    @Comment("대표 이메일 Uid (service1.member_email_data.uid)")
    var frontMemberEmailData: Database2_Service1_MemberEmailData?,

    @ManyToOne
    @JoinColumn(name = "front_phone_uid", nullable = true)
    @Comment("대표 전화번호 Uid (service1.member_phone_data.uid)")
    var frontMemberPhoneData: Database2_Service1_MemberPhoneData?
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
    var memberRoleDataList: MutableList<Database2_Service1_MemberRoleData> = mutableListOf()

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var memberProfileDataList: MutableList<Database2_Service1_MemberProfileData> = mutableListOf()

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var memberPhoneDataList: MutableList<Database2_Service1_MemberPhoneData> = mutableListOf()

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var memberOauth2LoginDataList: MutableList<Database2_Service1_MemberOauth2LoginData> = mutableListOf()

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var memberEmailDataList: MutableList<Database2_Service1_MemberEmailData> = mutableListOf()

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var logInTokenHistoryList: MutableList<Database2_Service1_LogInTokenHistory> = mutableListOf()

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var addPhoneNumberVerificationDataList: MutableList<Database2_Service1_AddPhoneNumberVerificationData> =
        mutableListOf()

    @OneToMany(mappedBy = "memberData", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var addEmailVerificationDataList: MutableList<Database2_Service1_AddEmailVerificationData> = mutableListOf()


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>

}