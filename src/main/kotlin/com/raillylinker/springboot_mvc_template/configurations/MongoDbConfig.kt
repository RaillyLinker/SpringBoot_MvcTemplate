package com.raillylinker.springboot_mvc_template.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

// [MongoDB 설정]
@Configuration
class MongoDbConfig {
    // !!!MongoDB 추가시 아래와 같이 추가 후,!!!
    // @Qualifier("mongoDb0") private val mongoDb0: MongoTemplate
    // 이렇게 DI 하여 사용하세요.
//    @Value("\${datasource-mongodb.mongoDb0.uri}")
//    private lateinit var mongoDb0Uri: String
//
//    @Bean(name = ["mongoDb1"])
//    fun mongoDb0(): MongoTemplate {
//        return MongoTemplate(SimpleMongoClientDatabaseFactory(mongoDb0Uri))
//    }

    @Value("\${datasource-mongodb.mongoDb1.uri}")
    private lateinit var mongoDb1Uri: String

    @Bean(name = ["mongoDb1"])
    fun mongoDb1(): MongoTemplate {
        return MongoTemplate(SimpleMongoClientDatabaseFactory(mongoDb1Uri))
    }
}