package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ClienteUpdate", description = "Dados de pessoa para atualização")
public class PessoaUpdateReqDTO {

    private Long parceiroId;

    private List<PessoaEmailDTO> emails;

    private List<PessoaTelefoneDTO> telefones;

    private List<PessoaEnderecoDTO> enderecos;
}
