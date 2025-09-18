package com.bookingservice.repository

import com.bookingservice.model.RecordEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface RecordRepository : MongoRepository<RecordEntity, Long> {
    fun findAllByPhoneNumber(phoneNumber: String): List<RecordEntity>
}