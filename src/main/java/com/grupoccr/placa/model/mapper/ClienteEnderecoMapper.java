package com.grupoccr.placa.model.mapper;

import org.mapstruct.Mapper;
import com.grupoccr.placa.model.dto.ClienteReqDTO.PessoaEnderecoDTO;
import com.grupoccr.placa.model.entity.ClienteEndereco;

@Mapper(componentModel = "spring")
public interface ClienteEnderecoMapper {
    ClienteEndereco toEntity(PessoaEnderecoDTO dto);
    PessoaEnderecoDTO toDto(ClienteEndereco entity);
}
