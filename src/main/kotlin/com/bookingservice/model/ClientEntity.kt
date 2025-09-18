package com.bookingservice.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("client")
data class ClientEntity (
    @Id var id: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var middleName: String? = null,
    var age: Int? = null,
    var createdDate: LocalDateTime? = LocalDateTime.now(),
    var phoneNumber: String? = null,
    var description: String? = null,
)
