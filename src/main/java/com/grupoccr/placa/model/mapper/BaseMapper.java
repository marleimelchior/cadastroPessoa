package com.grupoccr.placa.model.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MappingTarget;


public interface BaseMapper<E, REQ, RESP> {
	RESP toDto(E entity);

	List<RESP> toDto(Iterable<E> entities);

	List<RESP> toDto(List<E> entities);
//	default PageableResponseDto<RESP> toDto(Page<E> page) {
//		PageableResponseDto<RESP> pageableResponseDto = new PageableResponseDto<RESP>();
//		Page<RESP> pageMap = page.map(this::toDto);
//		pageableResponseDto.setContent(pageMap.getContent());
//		pageableResponseDto.setNumberOfElements(pageMap.getNumberOfElements());
//		pageableResponseDto.setPageSize(pageMap.getSize());
//		pageableResponseDto.setTotalElements(pageMap.getTotalElements());
//		pageableResponseDto.setTotalPages(pageMap.getTotalPages());
//		pageableResponseDto.setPageNumber(pageMap.getNumber());
//		return pageableResponseDto;
//	}
	E toEntity(REQ dto);

	List<E> toEntity(List<REQ> dtos);

	@InheritInverseConfiguration(name = "toDto")
	void updateEntityFromDto(@MappingTarget E entity, REQ dto);
}
