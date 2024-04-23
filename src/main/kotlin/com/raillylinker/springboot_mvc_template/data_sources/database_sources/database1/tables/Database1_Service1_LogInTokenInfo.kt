package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "login_token_info",
    catalog = "service1",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["token_type", "access_token", "row_delete_date_str"])
    ]
)
@Comment("토큰 발행 정보 테이블 : 로그인, 로그아웃 기록 역할도 겸함")
class Database1_Service1_LogInTokenInfo(
    @Column(name = "member_uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("멤버 고유값 (member.members.uid)")
    var memberUid: Long,

    @Column(name = "token_type", nullable = false, columnDefinition = "VARCHAR(50)")
    @Comment("토큰 타입 (ex : Bearer)")
    var tokenType: String,

    @Column(name = "login_date", nullable = false, columnDefinition = "DATETIME(3)")
    @Comment("로그인 시간")
    var loginDate: LocalDateTime,

    @Column(name = "access_token", nullable = false, columnDefinition = "VARCHAR(500)")
    @Comment("발행된 액세스 토큰")
    var accessToken: String,

    @Column(name = "access_token_expire_when", nullable = false, columnDefinition = "DATETIME(3)")
    @Comment("액세스 토큰 만료 일시")
    var accessTokenExpireWhen: LocalDateTime,

    @Column(name = "refresh_token", nullable = false, columnDefinition = "VARCHAR(500)")
    @Comment("발행된 리플레시 토큰")
    var refreshToken: String,

    @Column(name = "refresh_token_expire_when", nullable = false, columnDefinition = "DATETIME(3)")
    @Comment("리플레시 토큰 만료 일시")
    var refreshTokenExpireWhen: LocalDateTime
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