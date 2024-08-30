package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Pessoa", description = "A fazer")
public class ClienteRespDTO {

    private String mensagemErro;
    private String mensagem;
    private int registrosSalvos;
    private int registrosInvalidos;
    private int totalRegistros;

    public ClienteRespDTO(String mensagemErro, String mensagem) {
        this.mensagemErro = mensagemErro;
        this.mensagem = mensagem;
    }
}
