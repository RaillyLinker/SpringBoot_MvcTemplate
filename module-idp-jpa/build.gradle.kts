plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"

    // 추가
    kotlin("plugin.allopen") version "2.0.21" // allOpen 에 지정한 어노테이션으로 만든 클래스에 open 키워드를 적용
    kotlin("plugin.noarg") version "2.0.21" // noArg 에 지정한 어노테이션으로 만든 클래스에 자동으로 no-arg 생성자를 생성

    // QueryDSL Kapt
    kotlin("kapt")
}

group = "com.raillylinker.module_idp_jpa"
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
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.3.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.18.0")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    implementation("com.mysql:mysql-connector-j:9.0.0") // MySQL

    // (QueryDSL)
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
}

// (Querydsl 설정부 추가 - start)
val generated = file("src/main/generated")
// querydsl QClass 파일 생성 위치를 지정
tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory.set(generated)
}
// kotlin source set 에 querydsl QClass 위치 추가
sourceSets {
    main {
        kotlin.srcDirs += generated
    }
}
// gradle clean 시에 QClass 디렉토리 삭제
tasks.named("clean") {
    doLast {
        generated.deleteRecursively()
    }
}
kapt {
    generateStubs = true
}
// (Querydsl 설정부 추가 - end)

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