package com.grupoccr.placa.model.mapper;

import org.mapstruct.Mapper;
import com.grupoccr.placa.model.dto.ClienteReqDTO.PessoaEmailDTO;
import com.grupoccr.placa.model.entity.ClienteEmail;

@Mapper(componentModel = "spring")
public interface ClienteEmailMapper {
    ClienteEmail toEntity(PessoaEmailDTO dto);
    PessoaEmailDTO toDto(ClienteEmail entity);
}
