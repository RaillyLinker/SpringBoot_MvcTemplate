package com.raillylinker.springboot_mvc_template.configurations.database_configs

import com.raillylinker.springboot_mvc_template.data_sources.GlobalVariables
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

// !!! 로 감싼 메세지가 있는 주석 부분을 상황에 맞게 수정해주세요.

// [DB 설정]
/*
    트랜젝션 처리를 할 때는
    @CustomTransactional([Database0Config.TRANSACTION_NAME])
    fun api1(...
    와 같이 DB 를 사용하는 함수 위에 어노테이션을 붙여주세요.
*/
@Configuration
@EnableJpaRepositories(
    // database repository path
    basePackages = [Database0Config.REPOSITORY_PATH],
    entityManagerFactoryRef = Database0Config.LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN_NAME, // 아래 bean 이름과 동일
    transactionManagerRef = Database0Config.TRANSACTION_NAME // 아래 bean 이름과 동일
)
class Database0Config(
    private val environment: Environment
) {
    companion object {
        // !!!application.yml 에 정의된 datasource 내의 DB명 작성!!!
        const val DATASOURCE_NAME: String = "database0"

        // 위 설정을 조합한 변수
        // Database Repository 객체가 저장된 위치 (아래와 같이 위치 해야 함)
        const val REPOSITORY_PATH: String =
            "${GlobalVariables.PACKAGE_NAME}.data_sources.database_sources.${DATASOURCE_NAME}.repositories"

        // Database Table 객체가 저장된 위치 작성 (아래와 같이 위치 해야 함)
        private const val TABLE_PATH: String =
            "${GlobalVariables.PACKAGE_NAME}.data_sources.database_sources.${DATASOURCE_NAME}.tables"

        const val LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN_NAME: String =
            "${DATASOURCE_NAME}_LocalContainerEntityManagerFactoryBean"

        private const val DATASOURCE_BEAN_NAME: String =
            "${DATASOURCE_NAME}_DataSource"

        // Database 트랜젝션을 사용할 때 사용하는 이름 변수
        const val TRANSACTION_NAME: String =
            "${DATASOURCE_NAME}_PlatformTransactionManager"
    }

    @Bean(LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN_NAME)
    fun customEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = customDataSource()
        em.setPackagesToScan(TABLE_PATH)
        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties = HashMap<String, Any?>()
        properties["hibernate.hbm2ddl.auto"] = environment.getProperty("spring.jpa.hibernate.ddl-auto")
        properties["hibernate.dialect"] = environment.getProperty("spring.jpa.database-platform")
        em.setJpaPropertyMap(properties)
        return em
    }

    @Bean(TRANSACTION_NAME)
    fun customTransactionManager(): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = customEntityManagerFactory().`object`
        return transactionManager
    }

    @Bean(DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = "datasource.${DATASOURCE_NAME}")
    fun customDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}