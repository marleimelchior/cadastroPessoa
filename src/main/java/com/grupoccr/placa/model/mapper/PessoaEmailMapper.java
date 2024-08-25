package com.grupoccr.placa.model.mapper;

import org.mapstruct.Mapper;
import com.grupoccr.placa.model.dto.PessoaEmailDTO;
import com.grupoccr.placa.model.entity.PessoaEmail;

@Mapper(componentModel = "spring")
public interface PessoaEmailMapper {
    PessoaEmail toEntity(PessoaEmailDTO dto);
    PessoaEmailDTO toDto(PessoaEmail entity);
}
