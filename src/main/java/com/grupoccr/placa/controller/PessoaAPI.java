package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.PessoaReqDTO;
import com.grupoccr.placa.model.dto.PessoaRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.PessoasReqDTO;
import com.grupoccr.placa.model.entity.Pessoa;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Incluir registro da pessoa", response = Pessoa.class)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> incluir(
            @Valid @RequestBody @ApiParam(value = "Dados da pessoa", required = true) PessoaReqDTO body)
            throws ApplicationException;

    @ApiOperation(value = "Incluir lote de pessoas", response = PessoaRespDTO.class)
    @PostMapping(value = "/lote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PessoaRespDTO> incluirLote(
            @Valid @RequestBody @ApiParam(value = "Dados das pessoas", required = true) PessoasReqDTO body,
            @RequestParam @ApiParam(value = "ID do parceiro", required = true) Long parceiroId) throws ApplicationException;

    @ApiOperation(value = "Atualizar dados de uma pessoa", response = PessoaRespDTO.class)
    @PutMapping(value = "/{cpfCnpj}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PessoaRespDTO> atualizar(
            @PathVariable @ApiParam(value = "CPF/CNPJ da pessoa", required = true) String cpfCnpj,
            @Valid @RequestBody @ApiParam(value = "Dados da pessoa", required = true) PessoaReqDTO body,
            @RequestParam @ApiParam(value = "ID do parceiro", required = true) Long parceiroId) throws ApplicationException;
}