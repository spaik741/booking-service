package com.bookingservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("record")
data class RecordEntity(
    @Id var id: String? = null,
    var title: String? = null,
    var companyName: String? = null,
    var phoneNumber: String? = null,
    var recordDateTime: LocalDateTime? = null
)
