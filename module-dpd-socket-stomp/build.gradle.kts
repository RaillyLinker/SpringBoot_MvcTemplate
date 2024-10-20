plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.raillylinker.module_dpd_socket_stomp"
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

    // (WebSocket)
    // : 웹소켓
    implementation("org.springframework.boot:spring-boot-starter-websocket:3.3.4")

    // (ORM 관련 라이브러리)
    // WebSocket STOMP Controller 에서 입력값 매핑시 사용됨
    implementation("javax.persistence:persistence-api:1.0.2")

    // (GSON)
    // : Json - Object 라이브러리
    implementation("com.google.code.gson:gson:2.11.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}