package com.bookingservice.service

import com.bookingservice.model.dto.RecordClientResponse
import com.bookingservice.model.dto.RecordInfo

interface RecordService {
    fun getAllRecords(phoneNumber: String): RecordClientResponse?
    fun createRecord(phoneNumber: String, recordDto: RecordInfo)
}