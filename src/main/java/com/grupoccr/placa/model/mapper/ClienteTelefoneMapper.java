package com.grupoccr.placa.model.mapper;

import org.mapstruct.Mapper;
import com.grupoccr.placa.model.dto.ClienteReqDTO.PessoaTelefoneDTO;
import com.grupoccr.placa.model.entity.ClienteTelefone;

@Mapper(componentModel = "spring")
public interface ClienteTelefoneMapper {
    ClienteTelefone toEntity(PessoaTelefoneDTO dto);
    PessoaTelefoneDTO toDto(ClienteTelefone entity);
}
