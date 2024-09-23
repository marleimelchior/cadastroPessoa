package com.grupoccr.placa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlacaConcessionariaDTO {

    private int codigoConcessionaria;
    private String dsConcessionaria;
    private List<PracaBloqueadaDTO> pracasBloqueadas;
}
