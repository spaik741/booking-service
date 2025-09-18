package com.bookingservice.repository

import com.bookingservice.model.ClientEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ClientRepository : MongoRepository<ClientEntity, String> {
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByPhoneNumber(phoneNumber: String): ClientEntity?
}