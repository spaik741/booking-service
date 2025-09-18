package com.bookingservice.exception

import org.springframework.http.*
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI
import java.util.function.Consumer

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BookingServiceException::class)
    fun handleBookingServiceException(ex: BookingServiceException, request: WebRequest) =
        buildProblemDetail(ex.message, ex.status, request)


    @ExceptionHandler(Exception::class)
    fun handleOthersException(ex: Exception, request: WebRequest) =
        buildProblemDetail(ex.message, HttpStatus.BAD_REQUEST, request)

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        logger.error("Intercepted HttpMessageNotReadableException. Message: {}", ex)
        return ResponseEntity.badRequest()
            .body(buildProblemDetail(ex.message, HttpStatus.BAD_REQUEST, request))
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val pd = ex.body
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.globalErrors.forEach(Consumer { e: ObjectError ->
            errors[e.objectName] = e.defaultMessage
        })
        ex.bindingResult.fieldErrors.forEach(Consumer { e: FieldError ->
            errors[e.field] = e.defaultMessage
        })
        logger.error("Intercepted MethodArgumentNotValidException. Errors: $errors")
        pd.setProperty("invalid_params", errors)
        pd.setStatus(HttpStatus.BAD_REQUEST)
        pd.instance = URI.create((request as ServletWebRequest).request.requestURI)
        return handleExceptionInternal(ex, pd, headers, HttpStatus.BAD_REQUEST, request)
    }

    private fun buildProblemDetail(
        message: String?,
        status: HttpStatus,
        request: WebRequest?
    ) = ProblemDetail.forStatusAndDetail(status, message).apply {
        this.instance = URI.create((request as ServletWebRequest).request.requestURI)
    }
}