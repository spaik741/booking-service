package com.bookingservice.exception

import org.springframework.http.HttpStatus

class BookingServiceException(val status: HttpStatus, override val message: String) :
    RuntimeException(message) {
}