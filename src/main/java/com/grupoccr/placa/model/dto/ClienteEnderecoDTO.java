package com.grupoccr.placa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEnderecoDTO {

    private String bairro;


    private String cep;


    private String cidade;


    private String complemento;


    private String logradouro;


    private String numero;


    private String tipo;


    private String uf;

}
