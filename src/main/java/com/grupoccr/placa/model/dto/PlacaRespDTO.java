package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Placa", description = "A fazer")
@AllArgsConstructor
public class PlacaRespDTO {

    private String mensagem;
    private int registrosSalvos;
    private int registrosInvalidos;
    private int totalRegistros;

    public PlacaRespDTO(String mensagem) {
        this.mensagem = mensagem;
    }
}
