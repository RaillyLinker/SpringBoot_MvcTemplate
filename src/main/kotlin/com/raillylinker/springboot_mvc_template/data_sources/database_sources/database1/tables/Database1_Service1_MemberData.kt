package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "member_data", catalog = "service1")
@Comment("회원 정보 테이블")
class Database1_Service1_MemberData(
    @Column(name = "nick_name", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("닉네임 (중복 비허용 = uid 에 대한 별칭의 역할)")
    var nickName: String,

    @Column(name = "account_password", nullable = true, columnDefinition = "VARCHAR(100)")
    @Comment("계정 로그인시 사용하는 비밀번호 (닉네임, 이메일, 전화번호 로그인에 모두 사용됨. OAuth2 만 등록했다면 null)")
    var accountPassword: String?,

    @Column(name = "front_profile_uid", nullable = true, columnDefinition = "BIGINT UNSIGNED")
    @Comment("대표 프로필 Uid (service1.member_profile_data.uid)")
    var frontProfileUid: Long?,

    @Column(name = "front_email_uid", nullable = true, columnDefinition = "BIGINT UNSIGNED")
    @Comment("대표 이메일 Uid (service1.member_email_data.uid)")
    var frontEmailUid: Long?,

    @Column(name = "front_phone_uid", nullable = true, columnDefinition = "BIGINT UNSIGNED")
    @Comment("대표 프로필 Uid (service1.member_phone_data.uid)")
    var frontPhoneUid: Long?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    var uid: Long? = null

    @Column(name = "row_create_date", nullable = false, columnDefinition = "DATETIME")
    @CreationTimestamp
    @Comment("행 생성일")
    var rowCreateDate: LocalDateTime? = null

    @Column(name = "row_update_date", nullable = false, columnDefinition = "DATETIME")
    @UpdateTimestamp
    @Comment("행 수정일")
    var rowUpdateDate: LocalDateTime? = null

    @Column(name = "row_delete_date", nullable = true, columnDefinition = "DATETIME")
    @Comment("행 삭제일")
    var rowDeleteDate: LocalDateTime? = null


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
}