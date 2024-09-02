package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.*;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.service.LoggerService;
import com.grupoccr.placa.service.PlacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            PlacaRespDTO response = placaService.incluir(body);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            logger.error("Erro ao incluir placa: {}", body, e);
            throw new ApplicationException("Erro ao incluir placa", e);
        }
    }

    @Override
    public ResponseEntity<PlacaRespDTO> incluirLote(@Valid @RequestBody List<PlacaReqDTO> body) throws ApplicationException {
        try {
            PlacaRespDTO response = placaService.incluirLote(body);
            HttpStatus status = response.getRegistrosSalvos() == 0 ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            logger.error("Erro ao incluir lote de placas: {}", body, e);
            throw new ApplicationException("Erro ao incluir lote de placas", e);
        }
    }

    @Override
    public ResponseEntity<PlacaRespDTO> ativarDesativar(String placa, String cpfCnpj, StatusAtivacaoEnum status) throws ApplicationException {
        try {
            logger.info("Recebida solicitação para ativar/desativar a placa: {}", placa);
            boolean ativo = status == StatusAtivacaoEnum.ATIVAR;
            PlacaRespDTO response = placaService.ativarDesativar(placa, cpfCnpj ,ativo);
            logger.info("Solicitação para ativar/desativar a placa {} concluída com sucesso", placa);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            logger.error("Erro ao ativar/desativar a placa: {}", placa, e);
            throw new ApplicationException("Erro ao ativar/desativar a placa", e);
        }
    }

}
