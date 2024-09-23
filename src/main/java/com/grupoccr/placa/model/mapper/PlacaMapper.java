package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaUpdateReqDTO;
import com.grupoccr.placa.model.entity.Placa;
import com.grupoccr.placa.model.entity.StatusAtivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlacaMapper {

    @Mapping(source = "ativo", target = "ativo", qualifiedByName = "statusAtivoToBoolean")
    PlacaReqDTO toDto(Placa placa);

    @Mapping(source = "ativo", target = "ativo", qualifiedByName = "booleanToStatusAtivo")
    Placa toEntity(PlacaReqDTO placaReqDTO);

    @Mapping(source = "ativo", target = "ativo", qualifiedByName = "booleanToStatusAtivo")
    void updateDtoToEntity(PlacaUpdateReqDTO dto, @MappingTarget Placa placa);

    // Converte de Boolean para StatusAtivo
    @Named("booleanToStatusAtivo")
    default StatusAtivo booleanToStatusAtivo(Boolean ativo) {
        return ativo != null && ativo ? StatusAtivo.S : StatusAtivo.N;
    }

    // Converte de StatusAtivo para Boolean
    @Named("statusAtivoToBoolean")
    default Boolean statusAtivoToBoolean(StatusAtivo statusAtivo) {
        return statusAtivo == StatusAtivo.S;
    }
}
