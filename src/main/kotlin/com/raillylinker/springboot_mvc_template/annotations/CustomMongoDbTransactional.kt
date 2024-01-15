package com.raillylinker.springboot_mvc_template.annotations

// [JPA 의 Transactional 을 여러 TransactionManager 으로 사용 가능하도록 개조한 annotation]
// MongoDB 에서 트랜젝션을 사용하려면 MongoDB Replica Set 설정을 하여 실행시키는 환경에서만 가능합니다.
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomMongoDbTransactional(
    val transactionManagerBeanNameList: Array<String>
)