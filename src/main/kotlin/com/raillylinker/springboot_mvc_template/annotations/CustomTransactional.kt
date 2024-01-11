package com.raillylinker.springboot_mvc_template.annotations

// [JPA 의 Transactional 을 여러 TransactionManager 으로 사용 가능하도록 개조한 annotation]
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomTransactional(
    val transactionManagerBeanNameList: Array<String>
)