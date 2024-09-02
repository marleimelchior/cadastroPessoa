package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.dto.ClienteListReqDTO;
import com.grupoccr.placa.model.dto.ClienteRespDTO;
import com.grupoccr.placa.model.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper extends BaseMapper<Cliente, ClienteReqDTO, ClienteRespDTO> {

    Cliente toEntity(ClienteListReqDTO clienteListReqDTO);

    void updateDtoToEntity(ClienteListReqDTO pessoaReqDTO, @MappingTarget Cliente cliente);

//    Cliente toEntity(ClienteReqDTO clienteReqDTO);
//
//    void updateDtoToEntity(ClienteListReqDTO pessoaReqDTO, @MappingTarget Cliente cliente);
}
