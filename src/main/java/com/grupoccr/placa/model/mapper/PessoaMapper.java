package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.PessoaReqDTO;
import com.grupoccr.placa.model.entity.Pessoa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PessoaMapper {

    Pessoa toEntity(PessoaReqDTO pessoaReqDTO);

    void updateDtoToEntity(PessoaReqDTO pessoaReqDTO, @MappingTarget Pessoa pessoa);
}
