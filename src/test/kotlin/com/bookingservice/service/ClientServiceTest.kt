package com.bookingservice.service

import com.bookingservice.exception.BookingServiceException
import com.bookingservice.mapper.ClientMapperImpl
import com.bookingservice.model.ClientEntity
import com.bookingservice.model.dto.ClientCreateRequest
import com.bookingservice.model.dto.ClientInfo
import com.bookingservice.repository.ClientRepository
import com.bookingservice.service.impl.ClientServiceImpl
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class ClientServiceTest {
    @InjectMocks
    private lateinit var clientService: ClientServiceImpl

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Spy
    private lateinit var mapper: ClientMapperImpl

    companion object {
        @JvmStatic
        @BeforeAll
        fun before() {
            mockStatic(LocalDateTime::class.java)
            val fixedDate = LocalDateTime.of(2025, 1, 1, 0, 0)
            Mockito.`when`(LocalDateTime.now()).thenReturn(fixedDate)
        }
    }


    @Test
    fun createTest() {
        val request = buildClientCreateRequest()
        Mockito.`when`(clientRepository.existsByPhoneNumber("+79879997766")).thenReturn(false)
        clientService.create(request)
        val clientCaptor = ArgumentCaptor.forClass(ClientEntity::class.java)
        verify(clientRepository, times(1)).save(clientCaptor.capture())
        assertEquals(buildClientEntity(), clientCaptor.value)
    }

    @Test
    fun createTestError() {
        val request = buildClientCreateRequest()
        Mockito.`when`(clientRepository.existsByPhoneNumber("+79879997766")).thenReturn(true)
        val err = assertThrows<BookingServiceException> { clientService.create(request) }
        assertEquals("phone number already exists", err.message)
        assertEquals(HttpStatus.CONFLICT, err.status)
    }

    @Test
    fun findClientTest() {
        val client: ClientEntity = buildClientEntity()
        Mockito.`when`(clientRepository.findByPhoneNumber("+79879997766")).thenReturn(client)
        val clientInfo = clientService.findClient("+79879997766")
        assertEquals(buildClientInfo(), clientInfo)
    }

    @Test
    fun findClientTestError() {
        Mockito.`when`(clientRepository.findByPhoneNumber("+79879997766")).thenReturn(null)
        val err = assertThrows<BookingServiceException> { clientService.findClient("+79879997766") }
        assertEquals("not client found by phoneNumber: +79879997766", err.message)
        assertEquals(HttpStatus.NOT_FOUND, err.status)
    }

    private fun buildClientInfo() = ClientInfo(
        firstName = "Дмитрий",
        lastName = "Дмитриев",
        phoneNumber = "+79879997766",
        age = 18,
        middleName = "Дмитриеевич"
    )

    private fun buildClientEntity() =
        ClientEntity(
            firstName = "Дмитрий",
            lastName = "Дмитриев",
            phoneNumber = "+79879997766",
            age = 18,
            middleName = "Дмитриеевич"
        )

    private fun buildClientCreateRequest() =
        ClientCreateRequest(
            firstName = "Дмитрий",
            lastName = "Дмитриев",
            phoneNumber = "+79879997766",
            age = 18,
            middleName = "Дмитриеевич"
        )
}