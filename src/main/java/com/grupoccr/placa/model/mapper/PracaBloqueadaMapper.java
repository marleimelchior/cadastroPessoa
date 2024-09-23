package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.PracaBloqueadaDTO;
import com.grupoccr.placa.model.entity.PlacaConcessionaria;
import com.grupoccr.placa.model.entity.PracaBloqueada;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PracaBloqueadaMapper {

    PracaBloqueadaMapper INSTANCE = Mappers.getMapper(PracaBloqueadaMapper.class);

    @Mapping(source = "id.codigoPraca", target = "codigoPraca")
    PracaBloqueadaDTO toDto(PracaBloqueada pracaBloqueada);

    @Mapping(target = "placaConcessionaria", ignore = true)
    @Mapping(target = "id", expression = "java(new PracaBloqueada.PracaBloqueadaId(placaConcessionaria.getId(), dto.getCodigoPraca()))")
    PracaBloqueada toEntity(PracaBloqueadaDTO dto, PlacaConcessionaria placaConcessionaria);
}
