package com.bookingservice.service

import com.bookingservice.exception.BookingServiceException
import com.bookingservice.mapper.RecordMapperImpl
import com.bookingservice.model.BookRecordResponse
import com.bookingservice.model.ClientEntity
import com.bookingservice.model.RecordEntity
import com.bookingservice.model.dto.RecordClientResponse
import com.bookingservice.model.dto.RecordCreateRequest
import com.bookingservice.model.dto.RecordInfo
import com.bookingservice.repository.ClientRepository
import com.bookingservice.repository.RecordRepository
import com.bookingservice.service.impl.RecordServiceImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class RecordServiceTest {
    @InjectMocks
    private lateinit var recordService: RecordServiceImpl

    @Mock
    private lateinit var recordRepository: RecordRepository

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var providerClient: ProviderClient

    @Spy
    private lateinit var recordMapper: RecordMapperImpl

    @Test
    fun getAllRecords() {
        val client = buildClientEntity()
        val record = buildRecordEntity()
        `when`(clientRepository.findByPhoneNumber("+79879997766")).thenReturn(client)
        `when`(recordRepository.findAllByPhoneNumber("+79879997766")).thenReturn(listOf(record))
        val recordResponse = recordService.getAllRecords("+79879997766")
        assertEquals(buildRecordClientResponse(), recordResponse)
    }

    @Test
    fun getAllRecordsErrorClient() {
        `when`(clientRepository.findByPhoneNumber("+79879997766")).thenReturn(null)
        val err = assertThrows<BookingServiceException> { recordService.getAllRecords("+79879997766") }
        assertEquals("client not found", err.message)
        assertEquals(HttpStatus.NOT_FOUND, err.status)
    }

    @Test
    fun createRecord() {
        val record = buildRecordEntity()
        `when`(clientRepository.existsByPhoneNumber("+79879997766")).thenReturn(true)
        `when`(recordRepository.save(record)).thenReturn(record)
        `when`(providerClient.bookSchedule("Реснички у Иришки", LocalDateTime.of(2025, 8, 8, 10, 0))).thenReturn(
            buildBookRecordResponse()
        )
        recordService.createRecord("+79879997766", buildRecordCreateRequest())
        val recordCaptor = ArgumentCaptor.forClass(RecordEntity::class.java)
        verify(recordRepository).save(recordCaptor.capture())
        assertEquals(record, recordCaptor.value)
    }

    @Test
    fun createRecordErrorClient() {
        `when`(clientRepository.existsByPhoneNumber("+79879997766")).thenReturn(false)
        val err =
            assertThrows<BookingServiceException> {
                recordService.createRecord(
                    "+79879997766",
                    buildRecordCreateRequest()
                )
            }
        assertEquals("client not found", err.message)
        assertEquals(HttpStatus.NOT_FOUND, err.status)
        verify(recordRepository, times(0)).save(any())
    }

    private fun buildRecordClientResponse() = RecordClientResponse(
        phoneNumber = "+79879997766", fullName = "Дмитриев Дмитрий Дмитриеевич", records = listOf(
            buildRecordInfo()
        )
    )

    private fun buildRecordInfo() =
        RecordInfo(recordDateTime = LocalDateTime.of(2025, 8, 8, 10, 0), companyName = "Реснички у Иришки")

    private fun buildRecordCreateRequest() =
        RecordCreateRequest(recordDateTime = LocalDateTime.of(2025, 8, 8, 10, 0), companyName = "Реснички у Иришки")

    private fun buildRecordEntity() = RecordEntity(
        companyName = "Реснички у Иришки",
        phoneNumber = "+79879997766",
        recordDateTime = LocalDateTime.of(2025, 8, 8, 10, 0)
    )

    private fun buildClientEntity() =
        ClientEntity(
            firstName = "Дмитрий",
            lastName = "Дмитриев",
            phoneNumber = "+79879997766",
            age = 18,
            middleName = "Дмитриеевич",
            createdDate = LocalDateTime.of(2025, 8, 8, 10, 0)
        )

    private fun buildBookRecordResponse() = BookRecordResponse(true, LocalDateTime.of(2025, 8, 8, 10, 0))

}