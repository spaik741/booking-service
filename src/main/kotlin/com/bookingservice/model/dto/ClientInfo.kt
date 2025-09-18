package com.bookingservice.model.dto

import java.time.LocalDateTime

data class ClientInfo(
    var firstName: String? = null,
    var lastName: String? = null,
    var middleName: String? = null,
    var age: Int? = null,
    var createdDate: LocalDateTime? = null,
    var phoneNumber: String? = null,
    var description: String? = null,
)