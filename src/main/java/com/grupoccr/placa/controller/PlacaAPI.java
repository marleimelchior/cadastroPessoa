package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.PlacasReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = {"Placa" }, description = "Serviço com o objetivo de realizar inclusão e alteração de Placa")
@RequestMapping("/api/placa")
@Validated
public interface PlacaAPI {

	@ApiOperation(value = "Incluir registro da placa")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<PlacaRespDTO> incluir(
		@Valid @RequestBody @ApiParam(value = "Dados da placa", required = true) PlacaReqDTO body,
		@RequestHeader @ApiParam(value = "API-KEY", required = true) String apiKey, HttpServletRequest request)
		throws ApplicationException;

    @Operation(summary = "Incluir registros de placas em lote")
    @PostMapping(path = "/em-lote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PlacaRespDTO> incluirLote(
            @Valid @RequestBody  @ApiParam(value = "Dados da placa", required = true) PlacasReqDTO body,
            @RequestHeader @ApiParam(value = "API-KEY", required = true) String apiKey)
            throws ApplicationException;

    @Operation(summary = "Atualizar registro da placa")
    @PutMapping(path = "/{cpfCnpj}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PlacaRespDTO> atualizar(
            @Parameter(description = "CPF ou CNPJ da placa") @PathVariable String cpfCnpj,
            @Valid @RequestBody PlacaReqDTO body,
            @Parameter(description = "API-KEY") @RequestHeader String apiKey)
            throws ApplicationException;
}
