package com.bookingservice.service

import com.bookingservice.model.BookRecordResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@FeignClient(name = "booking-provider-service", path = "/v1/provider")
interface ProviderClient {

    @PostMapping("/schedule/book")
    fun bookSchedule(
        @RequestParam(value = "companyName") companyName: String,
        @RequestParam(value = "dateTime") dateTime: LocalDateTime
    ): BookRecordResponse
}