plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "springboot_mvc_template"

// (모듈 모음)
include("module-api-sample")
include("module-idp-jpa")
include("module-idp-common")
include("module-idp-retrofit2")
include("module-idp-redis")
include("module-idp-mongodb")
include("module-dpd-sockjs")
include("module-dpd-socket-stomp")
include("module-dpd-kafka")
