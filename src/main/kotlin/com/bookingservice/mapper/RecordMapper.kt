package com.bookingservice.mapper

import com.bookingservice.model.ClientEntity
import com.bookingservice.model.RecordEntity
import com.bookingservice.model.dto.RecordClientResponse
import com.bookingservice.model.dto.RecordInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring")
abstract class RecordMapper {

    abstract fun mapToInfo(recordEntity: RecordEntity): RecordInfo?

    @Mapping(target = "fullName", source = "clientEntity", qualifiedByName = ["buildFullName"])
    abstract fun mapToResponse(clientEntity: ClientEntity, records: List<RecordEntity>): RecordClientResponse?

    @Named("buildFullName")
    fun buildFullName(clientEntity: ClientEntity) = StringBuilder().apply {
        append(clientEntity.lastName)
        append(" ")
        append(clientEntity.firstName)
        clientEntity.middleName?.let {
            append(" ")
            append(it)
        }
    }

    @Mapping(target = "phoneNumber", source = "phoneNumber")
    abstract fun mapToEntity(phoneNumber: String, recordDto: RecordInfo): RecordEntity?
}