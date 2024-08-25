package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Pessoa", description = "A fazer")
public class PessoaReqDTO {

    @ApiModelProperty(required = true, value = "cpfCnpj é obrigatório")
	@NotNull(message = "O cpfCnpj é obrigatório")
    @Pattern(regexp = "\\d{11}|\\d{14}", message = "O cpfCnpj deve ter 11 ou 14 caracteres")
    private String cpfCnpj;

    @ApiModelProperty(required = true, value = "nome é obrigatório")
    private String nome;

    @ApiModelProperty(required = true, value = "ID do paceiro é obrigatório")
    private Long parceiroId;

    @Valid
    private List<PessoaEmailDTO> emails;

    private List<PessoaTelefoneDTO> telefones;

    private List<PessoaEnderecoDTO> enderecos;


}
