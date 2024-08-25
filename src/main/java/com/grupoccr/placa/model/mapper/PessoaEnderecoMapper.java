package com.grupoccr.placa.model.mapper;

import org.mapstruct.Mapper;
import com.grupoccr.placa.model.dto.PessoaEnderecoDTO;
import com.grupoccr.placa.model.entity.PessoaEndereco;

@Mapper(componentModel = "spring")
public interface PessoaEnderecoMapper {
    PessoaEndereco toEntity(PessoaEnderecoDTO dto);
    PessoaEnderecoDTO toDto(PessoaEndereco entity);
}
