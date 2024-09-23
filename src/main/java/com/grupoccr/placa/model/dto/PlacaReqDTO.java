package com.grupoccr.placa.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Placa", description = "A fazer")
public class PlacaReqDTO {

    @ApiModelProperty(required = true, value = "cpfCnpj é obrigatório")
    @NotNull(message = "O cpfCnpj nome é obrigatório")
    private String cpfCnpj;

    @ApiModelProperty(required = true, value = "placa é obrigatório")
    @NotNull(message = "O campo placa é obrigatório")
    @NotEmpty(message = "O campo plca não pode estar vazio")
    private String placa;

    @ApiModelProperty(required = true, value = "ativo é obrigatório")
    @NotNull(message = "O campo ativo é obrigatório")
//    @NotEmpty(message = "O campo ativo não pode estar vazio")
    private Boolean ativo;

    private List<PlacaConcessionariaDTO> concessionaria;

}
