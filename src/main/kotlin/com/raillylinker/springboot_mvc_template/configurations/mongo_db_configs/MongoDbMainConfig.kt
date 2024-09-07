package com.raillylinker.springboot_mvc_template.configurations.mongo_db_configs

import com.raillylinker.springboot_mvc_template.data_sources.GlobalVariables
import jakarta.annotation.PostConstruct
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
    basePackages = [MongoDbMainConfig.REPOSITORY_PATH],
    mongoTemplateRef = MongoDbMainConfig.MONGO_DB_CONFIG_NAME
)
class MongoDbMainConfig {
    companion object {
        // !!!application.yml 의 datasource-mongodb 안에 작성된 이름 할당하기!!!
        // data_sources/mongo_db_sources 안의 서브 폴더(collections, repositories 를 가진 폴더)의 이름이 이와 동일해야 합니다.
        const val MONGO_DB_CONFIG_NAME: String = "mongo_db_main"

        // 위 설정을 조합한 변수
        // Database Repository 객체가 저장된 위치 (아래와 같이 위치 해야 함)
        const val REPOSITORY_PATH: String =
            "${GlobalVariables.PACKAGE_NAME}.data_sources.mongo_db_sources.${MONGO_DB_CONFIG_NAME}.repositories"

        // Database 트랜젝션을 사용할 때 사용하는 이름 변수
        const val TRANSACTION_NAME: String =
            "${MONGO_DB_CONFIG_NAME}_PlatformTransactionManager"
    }

    // ---------------------------------------------------------------------------------------------
    @Value("\${datasource-mongodb.${MONGO_DB_CONFIG_NAME}.uri}")
    private lateinit var mongoDbUri: String

    private lateinit var mongoClientFactory: SimpleMongoClientDatabaseFactory

    @PostConstruct
    fun init() {
        mongoClientFactory = SimpleMongoClientDatabaseFactory(mongoDbUri)
    }

    @Bean(name = [MONGO_DB_CONFIG_NAME])
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClientFactory)
    }

    @Bean(TRANSACTION_NAME)
    fun customTransactionManager(): MongoTransactionManager {
        return MongoTransactionManager(mongoClientFactory)
    }
}