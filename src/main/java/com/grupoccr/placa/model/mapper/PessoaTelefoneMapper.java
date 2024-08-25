package com.grupoccr.placa.model.mapper;

import org.mapstruct.Mapper;
import com.grupoccr.placa.model.dto.PessoaTelefoneDTO;
import com.grupoccr.placa.model.entity.PessoaTelefone;

@Mapper(componentModel = "spring")
public interface PessoaTelefoneMapper {
    PessoaTelefone toEntity(PessoaTelefoneDTO dto);
    PessoaTelefoneDTO toDto(PessoaTelefone entity);
}
