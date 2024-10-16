plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "raillylinker.module_api_sample"
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
    // (모듈)
    implementation(project(":module-idp-common"))
    implementation(project(":module-idp-jpa"))
    implementation(project(":module-idp-retrofit2"))
    implementation(project(":module-idp-redis"))
    implementation(project(":module-idp-mongodb"))
    implementation(project(":module-dpd-sockjs"))
    implementation(project(":module-dpd-socket-stomp"))
    implementation(project(":module-dpd-kafka"))
    implementation(project(":module-dpd-scheduler"))

    // (기본)
    implementation("org.springframework.boot:spring-boot-starter:3.3.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.21")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.2")

    // (Spring Starter Web)
    // : 스프링 부트 웹 개발
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")

    // (Swagger)
    // : API 자동 문서화
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // (ThymeLeaf)
    // : 웹 뷰 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.3.4")

    // (GSON)
    // : Json - Object 라이브러리
    implementation("com.google.code.gson:gson:2.11.0")

    // (OkHttp3)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // (Kafka)
    implementation("org.springframework.kafka:spring-kafka:3.2.4")

    // (폰트 파일 내부 이름 가져오기용)
    implementation("org.apache.pdfbox:pdfbox:3.0.3")

    // (JSOUP - HTML 태그 조작)
    implementation("org.jsoup:jsoup:1.18.1")

    // (Spring Actuator)
    // : 서버 모니터링 정보
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.4")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:1.13.6")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}