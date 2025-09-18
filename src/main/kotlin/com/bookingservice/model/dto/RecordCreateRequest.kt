package com.bookingservice.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class RecordCreateRequest(
    @NotNull
    var recordDateTime: LocalDateTime? = null,
    @NotBlank
    var companyName: String? = null
)
