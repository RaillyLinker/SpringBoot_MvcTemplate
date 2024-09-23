package com.raillylinker.springboot_mvc_template.data_sources.database_mongo_db.mdb1_main.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_mongo_db.mdb1_main.documents.Mdb1_Test
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface Mdb1_Test_Repository : MongoRepository<Mdb1_Test, String> {
}