package com.bookingservice.exception

import com.bookingservice.model.FeignDtoError
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder
import io.micrometer.core.instrument.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class FeignErrorDecoder : ErrorDecoder {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun decode(methodName: String, response: Response): Exception {
        val errStr = IOUtils.toString(response.body().asInputStream())
        val error = objectMapper.readValue(errStr, FeignDtoError::class.java)
        return BookingServiceException(
            HttpStatus.resolve(response.status()) ?: HttpStatus.BAD_REQUEST,
            error.detail ?: "Not init detail message"
        )
    }
}