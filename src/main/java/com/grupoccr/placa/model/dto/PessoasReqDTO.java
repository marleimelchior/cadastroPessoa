package com.grupoccr.placa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Pessoa", description = "A fazer")
public class PessoasReqDTO {

    @ApiModelProperty(required = true, value = "placas é obrigatório")
	@NotNull(message = "O campo pessoas é obrigatório")
    @NotEmpty(message = "O campo pessoas não pode estar vazio")
    private List<PessoaReqDTO> pessoas;
}
