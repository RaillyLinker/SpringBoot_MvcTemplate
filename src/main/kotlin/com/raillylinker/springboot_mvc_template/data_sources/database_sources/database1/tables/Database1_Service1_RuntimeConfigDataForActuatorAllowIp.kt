package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "runtime_config_data_for_actuator_allow_ip",
    catalog = "service1",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["runtime_config_data_uid", "ip_string"])
    ]
)
@Comment("Service1 의 런타임 변경 가능 설정 정보 중 Actuator 정보 접근 허용 IP")
class Database1_Service1_RuntimeConfigDataForActuatorAllowIp(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runtime_config_data_uid", nullable = false)
    @Comment("런타임 설정 고유번호(service1.runtime_config_data.uid)")
    var runtimeConfigData: Database1_Service1_RuntimeConfigData,

    @Column(name = "ip_string", nullable = false, columnDefinition = "VARCHAR(20)")
    @Comment("접근 허용 IP (ex : 127.0.0.1)")
    var ipString: String,

    @Column(name = "ip_desc", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("IP 설명")
    var ipDesc: String
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