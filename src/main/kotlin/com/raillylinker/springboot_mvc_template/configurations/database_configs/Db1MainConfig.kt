package com.raillylinker.springboot_mvc_template.configurations.database_configs

import com.raillylinker.springboot_mvc_template.data_sources.memory_const_object.ProjectConst
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

// [DB 설정]
@Configuration
@EnableJpaRepositories(
    // database repository path
    basePackages = ["${ProjectConst.PACKAGE_NAME}.data_sources.database_jpa.${Db1MainConfig.DATABASE_DIRECTORY_NAME}.repositories"],
    entityManagerFactoryRef = "${Db1MainConfig.DATABASE_DIRECTORY_NAME}_LocalContainerEntityManagerFactoryBean", // 아래 bean 이름과 동일
    transactionManagerRef = Db1MainConfig.TRANSACTION_NAME // 아래 bean 이름과 동일
)
class Db1MainConfig(
    private val environment: Environment
) {
    companion object {
        // !!!application.yml 의 datasource 안에 작성된 이름 할당하기!!!
        const val DATABASE_CONFIG_NAME: String = "db1-main"

        // !!!data_sources/database_jpa 안의 서브 폴더(entities, repositories 를 가진 폴더)의 이름 할당하기!!!
        const val DATABASE_DIRECTORY_NAME: String = "db1_main"

        // Database 트랜젝션을 사용할 때 사용하는 이름 변수
        // 트랜젝션을 적용할 함수 위에, @CustomTransactional 어노테이션과 결합하여,
        // @CustomTransactional([DbConfig.TRANSACTION_NAME])
        // 위와 같이 적용하세요.
        const val TRANSACTION_NAME: String =
            "${DATABASE_DIRECTORY_NAME}_PlatformTransactionManager"
    }

    @Bean("${DATABASE_DIRECTORY_NAME}_LocalContainerEntityManagerFactoryBean")
    fun customEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = customDataSource()
        em.setPackagesToScan("${ProjectConst.PACKAGE_NAME}.data_sources.database_jpa.${DATABASE_DIRECTORY_NAME}.entities")
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

    @Bean("${DATABASE_DIRECTORY_NAME}_DataSource")
    @ConfigurationProperties(prefix = "datasource.${DATABASE_CONFIG_NAME}")
    fun customDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}