package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.PessoaReqDTO;
import com.grupoccr.placa.model.dto.PessoaRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.PessoasReqDTO;
import com.grupoccr.placa.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PessoaController implements PessoaAPI {

    @Autowired
    private PessoaService pessoaService;

    @Override
    public ResponseEntity<Void> incluir(PessoaReqDTO body, String apiKey) throws ApplicationException {
        pessoaService.incluir(body, apiKey);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PessoaRespDTO> incluirLote(PessoasReqDTO body, String apiKey) throws ApplicationException {
        PessoaRespDTO response = pessoaService.incluirLote(body, apiKey);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PessoaRespDTO> atualizar(String cpfCnpj, PessoaReqDTO body, String apiKey) throws ApplicationException {
        PessoaRespDTO response = pessoaService.atualizar(cpfCnpj, body, apiKey);
        return ResponseEntity.status(201).body(response);
    }
}
