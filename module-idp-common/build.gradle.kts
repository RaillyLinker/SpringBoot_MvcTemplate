plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.raillylinker.module_idp_common"
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

    // (Spring Starter Web)
    // : 스프링 부트 웹 개발
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")

    // (AOP)
    implementation("org.springframework.boot:spring-boot-starter-aop:3.3.4")

    // (ThymeLeaf)
    // : 웹 뷰 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.3.4")

    // (OkHttp3)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // (Excel File Read Write)
    // : 액셀 파일 입출력 라이브러리
    implementation("org.apache.poi:poi:5.3.0")
    implementation("org.apache.poi:poi-ooxml:5.3.0")
    implementation("sax:sax:2.0.1")

    // (HTML 2 PDF)
    // : HTML -> PDF 변환 라이브러리
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.9.5")

    // (Spring email)
    // : 스프링 이메일 발송
    implementation("org.springframework.boot:spring-boot-starter-mail:3.3.4")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}