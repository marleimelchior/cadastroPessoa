package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.ClienteEmailDTO;
import com.grupoccr.placa.model.entity.ClienteEmail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteEmailMapper {
    ClienteEmail toEntity(ClienteEmailDTO dto);
    ClienteEmailDTO toDto(ClienteEmail entity);
}
