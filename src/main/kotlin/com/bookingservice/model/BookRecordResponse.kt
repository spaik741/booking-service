package com.bookingservice.model

import java.time.LocalDateTime

data class BookRecordResponse(
    var isSuccess: Boolean? = null,
    var dateTime: LocalDateTime? = null
)