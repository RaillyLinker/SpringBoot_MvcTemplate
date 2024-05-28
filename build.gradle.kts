import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"

    // 추가
    kotlin("plugin.allopen") version "1.9.23" // allOpen 에 지정한 어노테이션으로 만든 클래스에 open 키워드를 적용
    kotlin("plugin.noarg") version "1.9.23" // noArg 에 지정한 어노테이션으로 만든 클래스에 자동으로 no-arg 생성자를 생성
}

group = "com.raillylinker"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // (기본)
    implementation("org.springframework.boot:spring-boot-starter:3.2.5")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.5")

    // (Spring Starter Web)
    // : 스프링 부트 웹 개발
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")

    // (ThymeLeaf)
    // : 웹 뷰 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.2.5")

    // (Swagger)
    // : API 자동 문서화
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // (GSON)
    // : Json - Object 라이브러리
    implementation("com.google.code.gson:gson:2.10.1")

    // (OkHttp3)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // (retrofit2 네트워크 요청)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")

    // (WebSocket)
    // : 웹소켓
    implementation("org.springframework.boot:spring-boot-starter-websocket:3.2.5")

    // (Spring email)
    // : 스프링 이메일 발송
    implementation("org.springframework.boot:spring-boot-starter-mail:3.2.5")

    // (Excel File Read Write)
    // : 액셀 파일 입출력 라이브러리
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("sax:sax:2.0.1")

    // (HTML 2 PDF)
    // : HTML -> PDF 변환 라이브러리
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.7.2")

    // (Kafka)
    implementation("org.springframework.kafka:spring-kafka:3.1.4")

    // (JPA)
    // : DB ORM
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.17.0")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    implementation("com.mysql:mysql-connector-j:8.3.0") // MySQL

    // (Redis)
    // : 메모리 키 값 데이터 구조 스토어
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.5")

    // (JWT)
    // : JWT 인증 토큰 라이브러리
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // (Spring Security)
    // : 스프링 부트 보안
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.5")
    testImplementation("org.springframework.security:spring-security-test:6.2.4")

    // (Spring Admin Client)
    // : Spring Actuator 포함
    implementation("de.codecentric:spring-boot-admin-starter-client:3.2.3")

    // (MongoDB)
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.2.5")

    // (ORM 관련 라이브러리)
    // WebSocket STOMP Controller 에서 입력값 매핑시 사용됨
    implementation("javax.persistence:persistence-api:1.0.2")

    // (폰트 파일 내부 이름 가져오기용)
    implementation("org.apache.pdfbox:pdfbox:3.0.2")

    // (JSOUP - HTML 태그 조작)
    implementation("org.jsoup:jsoup:1.17.2")

    // (AWS)
    implementation("io.awspring.cloud:spring-cloud-starter-aws:2.4.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// kotlin jpa : 아래의 어노테이션 클래스에 no-arg 생성자를 생성
noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

// kotlin jpa : 아래의 어노테이션 클래스를 open class 로 자동 설정
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
