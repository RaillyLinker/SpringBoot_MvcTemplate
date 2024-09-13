package com.raillylinker.springboot_mvc_template.annotations

// [Redis 트랜젝션 어노테이션]
// 사용 예시 : @CustomRedisTransactional([Redis1_Test.TRANSACTION_NAME])
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomRedisTransactional(
    // RedisConfig 에 설정된 RedisTemplate 객체의 Bean 이름과 Redis Table 이름을 : 로 결합한 String 리스트
    // ex : ["RedisTemplate1:Table1", "RedisTemplate1:Table2", "RedisTemplate2:Table1", ...]
    val redisTemplateBeanNameAndTableNameList: Array<String>
)