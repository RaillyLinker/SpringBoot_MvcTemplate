package com.raillylinker.springboot_mvc_template.data_sources.database_mongo_db.mdb1_main.documents

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "test")
data class Mdb1_Test(
    var content: String,
    var randomNum: Int,
    var rowActivate: Boolean,
    var rowCreateDate: LocalDateTime,
    var rowUpdateDate: LocalDateTime
) {
    @Id
    var uid: String? = null
}