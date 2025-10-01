package com.bookingservice.controller

import com.bookingservice.model.dto.ClientCreateRequest
import com.bookingservice.service.ClientService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/client")
class ClientController(
    val clientService: ClientService
) {

    @PostMapping("/create")
    fun create(@RequestBody @Valid client: ClientCreateRequest): ResponseEntity<HttpStatus> {
        clientService.create(client)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/info")
    fun getInfo(@RequestParam("phoneNumber") @NotEmpty phoneNumber: String) =
        ResponseEntity.ok(clientService.findClient(phoneNumber))
}