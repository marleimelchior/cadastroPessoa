package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.PessoaReqDTO;
import com.grupoccr.placa.model.dto.PessoaRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.PessoasReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"Pessoa" }, description = "Serviço com o objetivo de realizar inclusão e alteração de Pessoa")
@RequestMapping("/api/pessoa")
@Validated
public interface PessoaAPI {

	@ApiOperation(value = "Incluir registro da pessoa")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Void> incluir(
		@Valid @RequestBody @ApiParam(value = "Dados da pessoa", required = true) PessoaReqDTO body,
		@RequestHeader @ApiParam(value = "API-KEY", required = true) String apiKey)
		throws ApplicationException;

    @Operation(summary = "Incluir registros de pessoas em lote")
    @PostMapping(path = "/lote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PessoaRespDTO> incluirLote(
            @Valid @RequestBody PessoasReqDTO body,
            @Parameter(description = "API-KEY") @RequestHeader String apiKey)
            throws ApplicationException;

    @Operation(summary = "Atualizar registro da pessoa")
    @PutMapping(path = "/{cpfCnpj}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PessoaRespDTO> atualizar(
            @Parameter(description = "CPF ou CNPJ da pessoa") @PathVariable String cpfCnpj,
            @Valid @RequestBody PessoaReqDTO body,
            @Parameter(description = "API-KEY") @RequestHeader String apiKey)
            throws ApplicationException;
}
