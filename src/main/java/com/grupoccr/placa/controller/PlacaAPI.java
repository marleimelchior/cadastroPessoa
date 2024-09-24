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
import org.springframework.stereotype.Repository;
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


    @ApiOperation(value = "Alterar Placa")
    @PutMapping(path = "/{placa}/cpfCnpj/{cpfCnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PlacaRespDTO> ativarDesativar(
            @Parameter(description = "Número da placa") @PathVariable String placa,
            @Parameter(description = "CPF/CNPJ associado") @PathVariable String cpfCnpj,
            @RequestBody PlacaUpdateReqDTO body) throws ApplicationException;


    @ApiOperation(value = "Listar todas as placas")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<PlacaReqDTO>> listarTodasPlacas() throws ApplicationException;
}
