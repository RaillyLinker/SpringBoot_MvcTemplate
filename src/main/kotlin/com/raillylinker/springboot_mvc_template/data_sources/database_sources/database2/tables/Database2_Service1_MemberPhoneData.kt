package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database2.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_phone_data",
    catalog = "service1",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["phone_number"])
    ]
)
@Comment("회원 전화 정보 테이블")
class Database2_Service1_MemberPhoneData(
    @ManyToOne
    @JoinColumn(name = "member_uid", nullable = false)
    @Comment("멤버 고유번호(service1.member_data.uid)")
    var memberData: Database2_Service1_MemberData,

    @Column(name = "phone_number", nullable = false, columnDefinition = "VARCHAR(45)")
    @Comment("전화번호(국가번호 + 전화번호, 중복 비허용)")
    var phoneNumber: String
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

    @OneToMany(mappedBy = "frontMemberPhoneData", fetch = FetchType.LAZY)
    var memberDataList: MutableList<Database2_Service1_MemberData> = mutableListOf()


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>

}