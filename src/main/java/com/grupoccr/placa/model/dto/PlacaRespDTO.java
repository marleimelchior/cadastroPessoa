package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Placa", description = "A fazer")
public class PlacaRespDTO {

    private String mensagem;
    private int registrosSalvos;
    private int registrosInvalidos;
    private int totalRegistros;
}
