package com.raillylinker.module_idp_jpa.configurations

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfig(
    val em: EntityManager
) {

    @Bean
    fun queryFactory(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}