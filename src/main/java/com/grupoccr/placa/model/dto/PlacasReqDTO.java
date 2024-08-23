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
@ApiModel(value = "PlacaList", description = "A fazer")
public class PlacasReqDTO {

    @ApiModelProperty(required = true, value = "placas é obrigatório")
//	@NotNull(message = "O campo placas é obrigatório")
//    @NotEmpty(message = "O campo placas não pode estar vazio")
    private List<PlacaReqDTO> placas;

}
