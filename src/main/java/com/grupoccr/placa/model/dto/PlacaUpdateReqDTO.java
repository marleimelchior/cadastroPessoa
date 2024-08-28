package com.grupoccr.placa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlacaUpdateReqDTO {

    private String placa;
    private Boolean ativo;
}
