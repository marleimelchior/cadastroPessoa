package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.PlacasReqDTO;
import com.grupoccr.placa.model.entity.Logger;
import com.grupoccr.placa.service.LoggerService;
import com.grupoccr.placa.service.PlacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PlacaController implements PlacaAPI {

    @Autowired
    private PlacaService placaService;

    @Autowired
    private LoggerService loggerService;

    @Override
    public ResponseEntity<PlacaRespDTO> incluir(PlacaReqDTO body, String apiKey, HttpServletRequest request) throws ApplicationException {
        System.out.println(request);
        PlacaRespDTO response = placaService.incluir(body, apiKey);
        loggerService.saveLogger(request, response);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PlacaRespDTO> incluirLote(PlacasReqDTO body, String apiKey) throws ApplicationException {
        PlacaRespDTO response = placaService.incluirLote(body, apiKey);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PlacaRespDTO> atualizar(String cpfCnpj, PlacaReqDTO body, String apiKey) throws ApplicationException {
        PlacaRespDTO response = placaService.atualizar(cpfCnpj, body, apiKey);
        return ResponseEntity.status(201).body(response);
    }
}
