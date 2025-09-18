package com.bookingservice.service.impl

import com.bookingservice.exception.BookingServiceException
import com.bookingservice.mapper.RecordMapper
import com.bookingservice.model.dto.RecordClientResponse
import com.bookingservice.model.dto.RecordCreateRequest
import com.bookingservice.repository.ClientRepository
import com.bookingservice.repository.RecordRepository
import com.bookingservice.service.RecordService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class RecordServiceImpl(
    val recordRepository: RecordRepository,
    val clientRepository: ClientRepository,
    val recordMapper: RecordMapper
) : RecordService {

    override fun getAllRecords(phoneNumber: String): RecordClientResponse? {
        val client = clientRepository.findByPhoneNumber(phoneNumber) ?: throw BookingServiceException(
            HttpStatus.NOT_FOUND,
            "client not found"
        )
        val records = recordRepository.findAllByPhoneNumber(phoneNumber)
        return recordMapper.mapToResponse(client, records)
    }

    override fun createRecord(phoneNumber: String, recordDto: RecordCreateRequest) {
        if (!clientRepository.existsByPhoneNumber(phoneNumber)) {
            throw BookingServiceException(HttpStatus.NOT_FOUND, "client not found")
        }
        recordMapper.mapToEntity(phoneNumber, recordDto)?.let { record -> recordRepository.save(record) }
            ?: throw BookingServiceException(HttpStatus.NO_CONTENT, "record not save")
    }

}