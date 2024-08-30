package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    Cliente toEntity(ClienteReqDTO clienteReqDTO);

    void updateDtoToEntity(ClienteReqDTO clienteReqDTO, @MappingTarget Cliente cliente);
}
