package com.bookingservice.controller

import com.bookingservice.model.dto.RecordInfo
import com.bookingservice.service.RecordService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/record")
class RecordController(val recordService: RecordService) {

    @GetMapping
    fun getAllRecords(@RequestParam("phoneNumber") @NotEmpty phoneNumber: String) =
        ResponseEntity.ok(recordService.getAllRecords(phoneNumber))

    @PostMapping
    fun createRecord(
        @RequestParam @NotEmpty phoneNumber: String,
        @Valid @RequestBody recordDto: RecordInfo
    ): ResponseEntity<HttpStatus> {
        recordService.createRecord(phoneNumber, recordDto)
        return ResponseEntity.ok(HttpStatus.CREATED)
    }

}