package com.bookingservice.service.impl

import com.bookingservice.exception.BookingServiceException
import com.bookingservice.mapper.ClientMapper
import com.bookingservice.model.dto.ClientCreateRequest
import com.bookingservice.model.dto.ClientInfo
import com.bookingservice.repository.ClientRepository
import com.bookingservice.service.ClientService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper
) : ClientService {

    override fun create(createRequest: ClientCreateRequest) {
        if (clientRepository.existsByPhoneNumber(createRequest.phoneNumber!!)) {
            throw BookingServiceException(HttpStatus.CONFLICT, "phone number already exists")
        }
        clientMapper.mapToEntity(createRequest)?.let {client -> clientRepository.save(client) }
    }

    override fun findClient(phoneNumber: String): ClientInfo =
        clientRepository.findByPhoneNumber(phoneNumber)?.let { entity -> clientMapper.mapToClientInfo(entity) }
            ?: throw BookingServiceException(HttpStatus.NOT_FOUND, "not client found by phoneNumber: $phoneNumber")

}