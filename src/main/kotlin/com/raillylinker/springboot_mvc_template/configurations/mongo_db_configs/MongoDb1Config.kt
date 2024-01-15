package com.raillylinker.springboot_mvc_template.configurations.mongo_db_configs

import com.raillylinker.springboot_mvc_template.ApplicationConstants
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

// [MongoDB 설정]
@Configuration
@EnableMongoRepositories(
    basePackages = [MongoDb1Config.REPOSITORY_PATH],
    mongoTemplateRef = MongoDb1Config.DATASOURCE_NAME
)
class MongoDb1Config(
    @Value("\${datasource-mongodb.mongoDb1.uri}")
    private var mongoDb1Uri: String
) {
    companion object {
        // !!!application.yml 에 정의된 datasource 내의 DB명 작성!!!
        const val DATASOURCE_NAME: String = "mongoDb1"

        // 위 설정을 조합한 변수
        // Database Repository 객체가 저장된 위치 (아래와 같이 위치 해야 함)
        const val REPOSITORY_PATH: String =
            "${ApplicationConstants.PACKAGE_NAME}.data_sources.mongo_db_sources.${DATASOURCE_NAME}.repositories"

        // Database 트랜젝션을 사용할 때 사용하는 이름 변수
        const val TRANSACTION_NAME: String =
            "${DATASOURCE_NAME}_PlatformTransactionManager"
    }

    private val mongoClientFactory = SimpleMongoClientDatabaseFactory(mongoDb1Uri)

    @Bean(name = [DATASOURCE_NAME])
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClientFactory)
    }

    @Bean(TRANSACTION_NAME)
    fun customTransactionManager(): MongoTransactionManager {
        return MongoTransactionManager(mongoClientFactory)
    }
}