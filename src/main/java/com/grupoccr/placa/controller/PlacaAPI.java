package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.*;
import com.grupoccr.placa.exception.ApplicationException;
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
import java.util.List;

@Api(tags = {"Placa" }, description = "Serviço com o objetivo de realizar inclusão e alteração de Placa")
@RequestMapping("/api/placa")
@Validated
public interface PlacaAPI {

	@ApiOperation(value = "Incluir registro da placa")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseEntity<PlacaRespDTO> incluir(
		@Valid @RequestBody @ApiParam(value = "Dados da placa", required = true) PlacaReqDTO body) throws ApplicationException;

    @ApiOperation(value = "Incluir lote de placas", response = PlacaRespDTO.class)
    @PostMapping(value = "/lote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PlacaRespDTO> incluirLote(
            @Valid @RequestBody @ApiParam(value = "Dados das placas", required = true) List<PlacaReqDTO> body) throws ApplicationException;

//    @Operation(summary = "Atualizar registro da placa")
//    @PutMapping(path = "/{cpfCnpj}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<PlacaRespDTO> atualizar(
//            @Parameter(description = "CPF ou CNPJ da placa") @PathVariable String cpfCnpj,
//            @Valid @RequestBody PlacaUpdateReqDTO body) throws ApplicationException;

    @ApiOperation(value = "Ativar ou desativar placa")
    @PutMapping(path = "/{placa}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PlacaRespDTO> ativarDesativar(
            @Parameter(description = "Número da placa") @PathVariable String placa,
            @RequestParam @Parameter(description = "Status de ativação") StatusAtivacaoEnum status) throws ApplicationException;
}
