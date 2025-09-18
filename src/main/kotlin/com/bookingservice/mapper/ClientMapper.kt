package com.bookingservice.mapper

import com.bookingservice.model.ClientEntity
import com.bookingservice.model.dto.ClientCreateRequest
import com.bookingservice.model.dto.ClientInfo
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
abstract class ClientMapper {
    abstract fun mapToEntity(clientCreateRequest: ClientCreateRequest): ClientEntity?
    abstract fun mapToClientInfo(clientEntity: ClientEntity): ClientInfo?
}