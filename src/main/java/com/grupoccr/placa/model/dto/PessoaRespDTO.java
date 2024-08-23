package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Pessoa", description = "A fazer")
public class PessoaRespDTO {

    private String mensagem;
    private int registrosSalvos;
    private int registrosInvalidos;
    private int totalRegistros;
}
