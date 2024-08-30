package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Pessoa", description = "A fazer")
public class ClienteRespDTO {

    private String mensagem;
    private int registrosSalvos;
    private int registrosInvalidos;
    private int totalRegistros;
}
