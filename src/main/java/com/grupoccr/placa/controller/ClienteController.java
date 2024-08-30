package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.dto.ClienteRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.ClientesReqDTO;
import com.grupoccr.placa.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClienteController implements ClienteAPI {

    @Autowired
    private ClienteService clienteService;

    @Override
    public ResponseEntity<Void> incluir(ClienteReqDTO body, String apiKey) throws ApplicationException {
        clienteService.incluir(body, apiKey);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ClienteRespDTO> incluirLote(ClientesReqDTO body, String apiKey) throws ApplicationException {
        ClienteRespDTO response = clienteService.incluirLote(body, apiKey);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<ClienteRespDTO> atualizar(String cpfCnpj, ClienteReqDTO body, String apiKey) throws ApplicationException {
        ClienteRespDTO response = clienteService.atualizar(cpfCnpj, body, apiKey);
        return ResponseEntity.status(201).body(response);
    }
}
