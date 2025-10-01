package com.bookingservice.controller

import com.bookingservice.exception.BookingServiceException
import com.bookingservice.model.dto.ClientCreateRequest
import com.bookingservice.model.dto.ClientInfo
import com.bookingservice.service.ClientService
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

@WebMvcTest(controllers = [ClientController::class])
class ClientControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var clientService: ClientService

    private val PATH = "/v1/client"

    @Test
    fun create() {
        val createRequest = buildClientCreateRequest()
        Mockito.doNothing().`when`(clientService).create(createRequest)
        val result = mockMvc.perform(
            post("$PATH/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andReturn()
        assertEquals(201, result.response.status)
    }

    @Test
    fun createNotValidBody() {
        val createRequest = ClientCreateRequest()
        val result = mockMvc.perform(
            post("$PATH/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andReturn()
        assertEquals(400, result.response.status)
    }

    @Test
    fun getInfo() {
        val clientInfo = buildClientInfo()
        Mockito.`when`(clientService.findClient("+79879997766")).thenReturn(clientInfo)
        val result = mockMvc.perform(
            get("$PATH/info")
                .param("phoneNumber", "+79879997766")
        )
            .andReturn()
        assertEquals(200, result.response.status)
        assertEquals(objectMapper.writeValueAsString(clientInfo), result.response.contentAsString)
    }

    @Test
    fun getInfoNotFound() {
        Mockito.`when`(clientService.findClient("+79879997766"))
            .thenThrow(BookingServiceException(HttpStatus.NOT_FOUND, "not client found by phoneNumber: +79879997766"))
        val result = mockMvc.perform(
            get("$PATH/info")
                .param("phoneNumber", "+79879997766")
        )
            .andReturn()
        val json = objectMapper.readTree(result.response.contentAsString)
        assertEquals(404, result.response.status)
        assertEquals("Not Found", json["title"].textValue())
        assertEquals("not client found by phoneNumber: +79879997766", json["detail"].textValue())
        assertEquals("/v1/client/info", json["instance"].textValue())
    }

    private fun buildClientInfo() = ClientInfo(
        firstName = "Дмитрий",
        lastName = "Дмитриев",
        phoneNumber = "+79879997766",
        age = 19,
        createdDate = LocalDateTime.of(2025, 8, 8, 10, 0)
    )

    private fun buildClientCreateRequest() =
        ClientCreateRequest(firstName = "Дмитрий", lastName = "Дмитриев", phoneNumber = "+79879997766")
}