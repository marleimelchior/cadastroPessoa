package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.*;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.service.LoggerService;
import com.grupoccr.placa.service.PlacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PlacaController implements PlacaAPI {

    private static final Logger logger = LoggerFactory.getLogger(PlacaController.class);

    @Autowired
    private PlacaService placaService;

    @Autowired
    private LoggerService loggerService;

    @Override
    public ResponseEntity<PlacaRespDTO> incluir(PlacaReqDTO body) throws ApplicationException {
        PlacaRespDTO response = placaService.incluir(body);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PlacaRespDTO> incluirLote(@Valid @RequestBody List<PlacaReqDTO> body) throws ApplicationException {
        PlacaRespDTO response = placaService.incluirLote(body);
        return ResponseEntity.status(201).body(response);
    }

//    @Override
//    public ResponseEntity<PlacaRespDTO> atualizar(String cpfCnpj, PlacaUpdateReqDTO body) throws ApplicationException {
//        PlacaRespDTO response = placaService.atualizar(cpfCnpj, body);
//        return ResponseEntity.status(201).body(response);
//    }

    @Override
    public ResponseEntity<PlacaRespDTO> ativarDesativar(String placa, StatusAtivacaoEnum status) throws ApplicationException {
        logger.info("Recebida solicitação para ativar/desativar a placa: {}", placa);
        boolean ativo = status == StatusAtivacaoEnum.ATIVAR;
        PlacaRespDTO response = placaService.ativarDesativar(placa, ativo);
        logger.info("Solicitação para ativar/desativar a placa {} concluída com sucesso", placa);
        return ResponseEntity.status(200).body(response);
    }


}
