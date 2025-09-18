package com.bookingservice.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ClientCreateRequest(
    @field:NotBlank(message = "firstName cannot be empty")
    var firstName: String? = null,
    @field:NotBlank(message = "firstName cannot be empty")
    var lastName: String? = null,
    var middleName: String? = null,
    var age: Int? = null,
    @field:NotBlank(message = "phone number cannot be empty")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "not valid phone number")
    var phoneNumber: String? = null,
    var description: String? = null
)
