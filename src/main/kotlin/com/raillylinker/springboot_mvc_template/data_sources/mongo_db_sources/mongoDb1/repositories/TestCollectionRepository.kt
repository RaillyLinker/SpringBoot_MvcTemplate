package com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mongoDb1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.mongo_db_sources.mongoDb1.collections.TestCollection
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TestCollectionRepository : MongoRepository<TestCollection, String> {
}