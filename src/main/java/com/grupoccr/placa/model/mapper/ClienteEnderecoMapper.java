package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.ClienteEnderecoDTO;
import com.grupoccr.placa.model.entity.ClienteEndereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteEnderecoMapper {
    ClienteEndereco toEntity(ClienteEnderecoDTO dto);
    ClienteEnderecoDTO toDto(ClienteEndereco entity);
}
