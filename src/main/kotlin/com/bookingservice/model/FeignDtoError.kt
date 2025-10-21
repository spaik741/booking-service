package com.bookingservice.model

data class FeignDtoError(
    val title: String?,
    val status: Int?,
    val detail: String?
)