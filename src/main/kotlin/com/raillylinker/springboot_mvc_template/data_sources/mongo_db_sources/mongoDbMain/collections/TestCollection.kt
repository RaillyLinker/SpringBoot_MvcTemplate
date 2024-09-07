package com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mongoDbMain.collections

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "testCollection")
data class TestCollection(
    var content: String,
    var randomNum: Int,
    var rowActivate: Boolean,
    var rowCreateDate: LocalDateTime,
    var rowUpdateDate: LocalDateTime
) {
    @Id
    var uid: String? = null
}