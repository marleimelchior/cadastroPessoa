package com.grupoccr.placa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEmailDTO {

    @Email(message = "O email é inválido")
    private String email;
}
