package com.bookingservice.service

import com.bookingservice.model.dto.RecordClientResponse
import com.bookingservice.model.dto.RecordCreateRequest

interface RecordService {
    fun getAllRecords(phoneNumber: String): RecordClientResponse?
    fun createRecord(phoneNumber: String, recordDto: RecordCreateRequest)
}