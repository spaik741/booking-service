package com.bookingservice.service

import com.bookingservice.model.dto.ClientCreateRequest
import com.bookingservice.model.dto.ClientInfo

interface ClientService {
    fun create(createRequest: ClientCreateRequest)
    fun findClient(phoneNumber: String): ClientInfo
}