package com.raillylinker.springboot_mvc_template.configurations

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import java.util.function.Consumer


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

    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            openApi
                .paths
                .values
                .forEach(
                    Consumer { pathItem: PathItem ->
                        pathItem
                            .readOperations()
                            .forEach(
                                Consumer { operation: Operation ->
                                    operation
                                        .responses
                                        .addApiResponse(
                                            "204",
                                            ApiResponse()
                                                .description(
                                                    "본문이 없습니다.\n\n" +
                                                            "Response Header 의 api-result-code 로 값 하나(리스트로 반환되지만 아이템은 하나. 예를들면 [1].)가 반환됩니다.\n\n" +
                                                            "API 상세 설명 내용과 대조하세요."
                                                )
//                                                .content(
//                                                    Content().addMediaType(
//                                                        "text/plain",
//                                                        MediaType().schema(
//                                                            ModelConverters.getInstance()
//                                                                .resolveAsResolvedSchema(AnnotatedType(String::class.java)).schema
//                                                        )
//                                                    )
//                                                )
                                        )
                                        .addApiResponse(
                                            "400",
                                            ApiResponse()
                                                .description("클라이언트가 잘못된 값을 넘겨주었습니다.")
                                        )
                                        .addApiResponse(
                                            "401",
                                            ApiResponse()
                                                .description(
                                                    "인증되지 않은 접근입니다.\n\n" +
                                                            "Response Header 의 api-result-code 로 값 하나(리스트로 반환되지만 아이템은 하나. 예를들면 [1].)가 반환되거나 안될 수 있습니다.\n\n" +
                                                            "null : Request Header 에 Authorization 키로 JWT 를 넣어줘야 합니다.\n\n" +
                                                            "1 : Request Header 에 Authorization 키로 넣어준 JWT 형식이 올바르지 않습니다. (재 로그인 필요)\n\n" +
                                                            "2 : Request Header 에 Authorization 키로 넣어준 JWT 가 만료되었습니다. (Refresh Token 으로 재발급 필요)"
                                                )
                                        )
                                        .addApiResponse(
                                            "403",
                                            ApiResponse()
                                                .description("인가되지 않은 접근입니다.")
                                        )
                                        .addApiResponse(
                                            "500",
                                            ApiResponse()
                                                .description(
                                                    "서버 에러.\n\n" +
                                                            "서버 개발자에게 에러 상황, 에러 로그 등의 정보를 알려주세요."
                                                )
                                        )
                                })
                    })
        }
    }
}