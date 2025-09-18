package com.bookingservice.controller

import com.bookingservice.exception.BookingServiceException
import com.bookingservice.model.dto.RecordClientResponse
import com.bookingservice.model.dto.RecordInfo
import com.bookingservice.service.RecordService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.LocalDateTime
import kotlin.test.assertEquals

@WebMvcTest(controllers = [RecordController::class])
class RecordControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var recordService: RecordService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun getAllRecords() {
        val recordDto = buildRecordClientResponse()
        Mockito.`when`(recordService.getAllRecords("+79879997766")).thenReturn(recordDto)
        val result = mockMvc.perform(
            get("/v1/record")
                .param("phoneNumber", "+79879997766")
        )
            .andReturn()
        assertEquals(200, result.response.status)
        assertEquals(objectMapper.writeValueAsString(recordDto), result.response.contentAsString)
    }

    @Test
    fun getAllRecordsError() {
        Mockito.`when`(recordService.getAllRecords("+79879997766")).thenThrow(
            BookingServiceException(
                HttpStatus.NOT_FOUND,
                "client not found"
            )
        )
        val result = mockMvc.perform(
            get("/v1/record")
                .param("phoneNumber", "+79879997766")
        )
            .andReturn()
        val json = objectMapper.readTree(result.response.contentAsString)
        assertEquals(404, result.response.status)
        assertEquals("Not Found", json["title"].textValue())
        assertEquals("client not found", json["detail"].textValue())
        assertEquals("/v1/record", json["instance"].textValue())
    }


    fun createRecord() {
        mockMvc.perform(
            post("/v1/record/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildRecordClientResponse()))
                .param("+79879997766")
        )
    }


    private fun buildRecordClientResponse(): RecordClientResponse {
        val recordDto = RecordInfo(LocalDateTime.of(2025, 8, 8, 10, 0), "Ноготочки у Сафиры")
        return RecordClientResponse("+79879997766", "Сергеев Сергей Сергеевич", listOf(recordDto))
    }
}