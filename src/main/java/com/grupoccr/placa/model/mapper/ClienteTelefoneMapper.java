package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.ClienteTelefoneDTO;
import org.mapstruct.Mapper;
import com.grupoccr.placa.model.entity.ClienteTelefone;

@Mapper(componentModel = "spring")
public interface ClienteTelefoneMapper {
    ClienteTelefone toEntity(ClienteTelefoneDTO dto);
    ClienteTelefoneDTO toDto(ClienteTelefone entity);
}
