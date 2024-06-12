package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "service1_runtime_config_data",
    catalog = "service1"
)
@Comment("Service1 의 런타임 변경 가능 설정 정보를 저장하는 테이블. 첫번째 행의 데이터만 유효합니다.")
class Database1_Service1_Service1RuntimeConfigData(
    @Column(name = "auth_jwt_secret_key_string", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("계정 설정 - JWT 비밀키")
    var authJwtSecretKeyString: String,

    @Column(name = "auth_jwt_access_token_expiration_time_sec", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("계정 설정 - JWT AccessToken 유효기간(초)")
    var authJwtAccessTokenExpirationTimeSec: Long,

    @Column(name = "auth_jwt_refresh_token_expiration_time_sec", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("계정 설정 - JWT RefreshToken 유효기간(초)")
    var authJwtRefreshTokenExpirationTimeSec: Long,

    @Column(name = "auth_jwt_claims_aes256_initialization_vector", nullable = false, columnDefinition = "VARCHAR(16)")
    @Comment("계정 설정 - JWT 본문 암호화 AES256 IV 16자")
    var authJwtClaimsAes256InitializationVector: String,

    @Column(name = "auth_jwt_claims_aes256_encryption_key", nullable = false, columnDefinition = "VARCHAR(32)")
    @Comment("계정 설정 - JWT 본문 암호화 AES256 암호키 32자")
    var authJwtClaimsAes256EncryptionKey: String,

    @Column(name = "auth_jwt_issuer", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("계정 설정 - JWT 발행자")
    var authJwtIssuer: String
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