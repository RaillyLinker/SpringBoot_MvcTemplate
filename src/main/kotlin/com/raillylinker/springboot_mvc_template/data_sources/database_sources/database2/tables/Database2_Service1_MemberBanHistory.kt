package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_ban_history",
    catalog = "service1"
)
@Comment("계정 정지 히스토리 테이블")
class Database2_Service1_MemberBanHistory(
    @ManyToOne
    @JoinColumn(name = "member_uid", nullable = false)
    @Comment("멤버 고유번호(service1.member_data.uid)")
    var memberData: Database2_Service1_MemberData,

    @Column(name = "banned_before", nullable = false, columnDefinition = "DATETIME(3)")
    @Comment("계정 정지 만료 시간 (이 시간이 지나기 전까지 계정 정지 상태)")
    var bannedBefore: LocalDateTime,

    @Column(name = "banned_reason", nullable = false, columnDefinition = "VARCHAR(1000)")
    @Comment("계정 정지 이유")
    var bannedReason: String,

    @Column(name = "early_release", nullable = true, columnDefinition = "DATETIME(3)")
    @Comment("수동 밴 해제 시간 (이 값이 null 이 아니고 banned_before 보다 작다면 계정 밴 시간이 지나기 전에도 밴 헤제로 봅니다.)")
    var earlyRelease: LocalDateTime?
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