package com.raillylinker.springboot_mvc_template.configurations

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class SwaggerConfig(
    // (버전 정보)
    @Value("\${customConfig.swagger.documentVersion}")
    private var documentVersion: String,

    // (문서 제목)
    @Value("\${customConfig.swagger.documentTitle}")
    private var documentTitle: String,

    // (문서 설명)
    @Value("\${customConfig.swagger.documentDescription}")
    private var documentDescription: String
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    "JWT",
                    SecurityScheme().apply {
                        this.type = SecurityScheme.Type.HTTP
                        this.scheme = "bearer"
                        this.bearerFormat = "JWT"
                        this.`in` = SecurityScheme.In.HEADER
                        this.name = HttpHeaders.AUTHORIZATION
                    })
            )
            .addSecurityItem(
                SecurityRequirement().apply {
                    this.addList("JWT")
                }
            )
            .info(Info().apply {
                this.title(documentTitle)
                this.version(documentVersion)
                this.description(documentDescription)
            })
    }
}