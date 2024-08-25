package com.grupoccr.placa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaEnderecoDTO {

    private String bairro;


    private String cep;


    private String cidade;


    private String complemento;


    private String logradouro;


    private String numero;


    private String tipo;


    private String uf;

}
