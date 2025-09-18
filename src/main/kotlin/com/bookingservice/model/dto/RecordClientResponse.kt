package com.bookingservice.model.dto

import java.time.LocalDateTime

data class RecordClientResponse(
    var phoneNumber: String? = null,
    var fullName: String? = null,
    var records: List<RecordInfo>? = emptyList()
)

data class RecordInfo(
    var recordDateTime: LocalDateTime? = null,
    var companyName: String? = null
)
