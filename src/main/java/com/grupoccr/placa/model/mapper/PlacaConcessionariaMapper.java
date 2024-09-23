package com.grupoccr.placa.model.mapper;

import com.grupoccr.placa.model.dto.PlacaConcessionariaDTO;
import com.grupoccr.placa.model.dto.PracaBloqueadaDTO;
import com.grupoccr.placa.model.entity.PlacaConcessionaria;
import com.grupoccr.placa.model.entity.PracaBloqueada;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PlacaConcessionariaMapper {
    PlacaConcessionariaMapper INSTANCE = Mappers.getMapper(PlacaConcessionariaMapper.class);

    @Mapping(target = "pracasBloqueadas", ignore = true)
    PlacaConcessionaria toEntity(PlacaConcessionariaDTO dto);

    @Mapping(source = "codigoConcessionaria", target = "codigoConcessionaria")
    @Mapping(source = "pracasBloqueadas", target = "pracasBloqueadas")
    PlacaConcessionariaDTO toDto(PlacaConcessionaria placaConcessionaria);

    List<PlacaConcessionaria> toEntityList(List<PlacaConcessionariaDTO> dtoList);

    default PracaBloqueada toEntity(PracaBloqueadaDTO dto, PlacaConcessionaria placaConcessionaria) {
        if (dto == null) {
            return null;
        }
        PracaBloqueada pracaBloqueada = new PracaBloqueada();
        pracaBloqueada.setId(new PracaBloqueada.PracaBloqueadaId(placaConcessionaria.getId(), dto.getCodigoPraca()));
        pracaBloqueada.setPlacaConcessionaria(placaConcessionaria);
        return pracaBloqueada;
    }

    default List<PracaBloqueada> toEntityList(List<PracaBloqueadaDTO> dtoList, PlacaConcessionaria placaConcessionaria) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream()
                .map(dto -> toEntity(dto, placaConcessionaria))
                .collect(Collectors.toList());
    }
}
