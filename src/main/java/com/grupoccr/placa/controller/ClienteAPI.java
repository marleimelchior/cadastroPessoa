package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.dto.ClienteRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.ClientesReqDTO;
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

@Api(tags = {"Cliente" }, description = "Serviço com o objetivo de realizar inclusão e alteração de Cliente")
@RequestMapping("/api/cliente")
@Validated
public interface ClienteAPI {

	@ApiOperation(value = "Incluir registro de cliente")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<Void> incluir(
		@Valid @RequestBody @ApiParam(value = "Dados do cliente", required = true) ClienteReqDTO body,
		@RequestHeader @ApiParam(value = "API-KEY", required = true) String apiKey)
		throws ApplicationException;

    @Operation(summary = "Incluir registros de clientes em lote")
    @PostMapping(path = "/lote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteRespDTO> incluirLote(
            @Valid @RequestBody ClientesReqDTO body,
            @Parameter(description = "API-KEY") @RequestHeader String apiKey)
            throws ApplicationException;

    @Operation(summary = "Atualizar registro do cliente")
    @PutMapping(path = "/{cpfCnpj}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteRespDTO> atualizar(
            @Parameter(description = "CPF ou CNPJ do cliente") @PathVariable String cpfCnpj,
            @Valid @RequestBody ClienteReqDTO body,
            @Parameter(description = "API-KEY") @RequestHeader String apiKey)
            throws ApplicationException;
}
