package com.bookingservice.service.impl

import com.bookingservice.exception.BookingServiceException
import com.bookingservice.mapper.RecordMapper
import com.bookingservice.model.dto.RecordClientResponse
import com.bookingservice.model.dto.RecordCreateRequest
import com.bookingservice.repository.ClientRepository
import com.bookingservice.repository.RecordRepository
import com.bookingservice.service.ProviderClient
import com.bookingservice.service.RecordService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class RecordServiceImpl(
    private val recordRepository: RecordRepository,
    private val clientRepository: ClientRepository,
    private val recordMapper: RecordMapper,
    private val providerClient: ProviderClient
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

        val recordResponse = providerClient.bookSchedule(recordDto.companyName!!, recordDto.recordDateTime!!)
        if (recordResponse.isSuccess != true) {
            throw BookingServiceException(HttpStatus.CONFLICT, "record not save")
        }
        recordMapper.mapToEntity(phoneNumber, recordDto.companyName!!, recordResponse.dateTime!!)
            ?.let { record -> recordRepository.save(record) }
            ?: throw BookingServiceException(HttpStatus.NOT_FOUND, "record not save")
    }

}