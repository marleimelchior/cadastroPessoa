package com.grupoccr.placa.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grupoccr.placa.model.entity.Parceiro;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Pessoa", description = "A fazer")
public class ClienteReqDTO {

    @ApiModelProperty(required = true, value = "cpfCnpj é obrigatório")
	@NotNull(message = "O cpfCnpj é obrigatório")
    private String cpfCnpj;

    @ApiModelProperty(required = true, value = "nome é obrigatório")
//	@NotNull(message = "O campo nome é obrigatório")
//    @NotEmpty(message = "O campo nome não pode estar vazio")
    private String nome;

    @JsonIgnore
    private Parceiro parceiro;

    private List<PessoaEmailDTO> emails;
    private List<PessoaTelefoneDTO> telefones;
    private List<PessoaEnderecoDTO> enderecos;

    @Data
    @NoArgsConstructor
    public static class PessoaEmailDTO {
        private String email;

    }

    @Data
    @NoArgsConstructor
    public static class PessoaTelefoneDTO {
        private String ddd;
        private String numero;
    }

    @Data
    @NoArgsConstructor
    public static class PessoaEnderecoDTO {
        private String bairro;
        private String cep;
        private String cidade;
        private String complemento;
        private String logradouro;
        private String numero;
        private String tipo;
        private String uf;
    }

}
