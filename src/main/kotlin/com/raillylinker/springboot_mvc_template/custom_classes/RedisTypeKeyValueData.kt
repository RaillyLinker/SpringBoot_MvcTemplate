package com.raillylinker.springboot_mvc_template.custom_classes

// [RedisType 의 출력값 데이터 클래스]
data class RedisTypeKeyValueData<ValueVo>(
    val key: String, // 멤버가 입력한 키 : 실제 키는 ${groupName:key}
    val value: ValueVo,
    val expireTimeMs: Long // 남은 만료 시간 밀리초
)