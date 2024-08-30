package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.ClienteListReqDTO;
import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.dto.ClienteRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.entity.Cliente;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Cliente" }, description = "Serviço com o objetivo de realizar inclusão e alteração de Cliente")
@RequestMapping("/api/cliente")
@Validated
public interface ClienteAPI {

    @ApiOperation(value = "Incluir registro do cliente", response = Cliente.class)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> incluir(
            @Valid @RequestBody @ApiParam(value = "Dados do cliente", required = true) ClienteReqDTO body)
            throws ApplicationException;

    @ApiOperation(value = "Incluir lote de clientes", response = ClienteRespDTO.class)
    @PostMapping(value = "/lote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteRespDTO> incluirLote(
            @Valid @RequestBody @ApiParam(value = "Dados dos clientes", required = true) List<ClienteReqDTO> body) throws ApplicationException;

    @ApiOperation(value = "Atualizar dados de um cliente", response = ClienteRespDTO.class)
    @PutMapping(value = "/{cpfCnpj}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteRespDTO> atualizar(
            @PathVariable @ApiParam(value = "CPF/CNPJ do cliente", required = true) String cpfCnpj,
            @Valid @RequestBody @ApiParam(value = "Dados do cliente", required = true) ClienteListReqDTO body) throws ApplicationException;
}