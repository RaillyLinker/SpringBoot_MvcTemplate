package com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.md1_main.repositories

import com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.md1_main.documents.Md1_Test
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface Md1_TestRepository : MongoRepository<Md1_Test, String> {
}