plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"

    // 추가
    kotlin("plugin.allopen") version "2.0.21" // allOpen 에 지정한 어노테이션으로 만든 클래스에 open 키워드를 적용
    kotlin("plugin.noarg") version "2.0.21" // noArg 에 지정한 어노테이션으로 만든 클래스에 자동으로 no-arg 생성자를 생성
}

group = "raillylinker.module_idp_jpa"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // (기본)
    implementation("org.springframework.boot:spring-boot-starter:3.3.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.21")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.2")

    // (JPA)
    // : DB ORM
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.17.1")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    implementation("com.mysql:mysql-connector-j:8.4.0") // MySQL
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
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