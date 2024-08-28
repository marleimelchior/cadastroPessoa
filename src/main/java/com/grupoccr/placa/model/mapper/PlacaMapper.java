package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaUpdateReqDTO;
import com.grupoccr.placa.model.entity.Placa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlacaMapper {

    Placa toEntity(PlacaReqDTO placaReqDTO);

    void updateDtoToEntity(PlacaUpdateReqDTO dto, @MappingTarget Placa placa);
}
