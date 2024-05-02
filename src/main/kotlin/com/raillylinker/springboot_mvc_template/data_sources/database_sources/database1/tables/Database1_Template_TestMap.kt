package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "test_map", catalog = "template")
@Comment("지도 좌표 테스트용 테이블")
class Database1_Template_TestMap(
    @Column(name = "latitude", nullable = false, columnDefinition = "DOUBLE")
    @Comment("지도 위도")
    var latitude: Double,

    @Column(name = "longitude", nullable = false, columnDefinition = "DOUBLE")
    @Comment("지도 경도")
    var longitude: Double
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    var uid: Long? = null

    @Column(name = "row_create_date", nullable = false, columnDefinition = "DATETIME(6)")
    @CreationTimestamp
    @Comment("행 생성일")
    var rowCreateDate: LocalDateTime? = null

    @Column(name = "row_update_date", nullable = false, columnDefinition = "DATETIME(6)")
    @UpdateTimestamp
    @Comment("행 수정일")
    var rowUpdateDate: LocalDateTime? = null

    @Column(name = "row_delete_date_str", nullable = false, columnDefinition = "VARCHAR(50)")
    @Comment("행 삭제일(yyyy_MM_dd_T_HH_mm_ss_SSSSSS_z, 삭제되지 않았다면 -)")
    var rowDeleteDateStr: String = "-"


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>


}