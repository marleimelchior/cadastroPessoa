package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.*;
import com.grupoccr.placa.exception.ApplicationException;
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

    @Override
    public ResponseEntity<PlacaRespDTO> incluir(PlacaReqDTO body) throws ApplicationException {
        try {
            PlacaRespDTO response = placaService.incluir(body);
            return ResponseEntity.status(201).body(response);
        } catch (ApplicationException e) {
            String errorMessage = "Erro ao incluir placa";
            if (e.getMessage().contains("Placa já cadastrada")) {

                errorMessage = "Placa já cadastrada, verifica os campos por favor";
            }
            logger.error(errorMessage, body, e);
            return ResponseEntity.status(400).body(new PlacaRespDTO(errorMessage));
        } catch (Exception e) {
            logger.error("Erro ao incluir placa", body, e);
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
            logger.error("Erro ao incluir lote de placas, dados não persistido no DB: {}", body, e);
            throw new ApplicationException("Erro ao incluir lote de placas, verifique os dados por favor", e);
        }
    }

    @Override
    public ResponseEntity<PlacaRespDTO> alterarPlaca(String placa, String cpfCnpj, PlacaUpdateReqDTO body) throws ApplicationException {
        try {

            boolean ativo = body.isAtivo();
            PlacaRespDTO response = placaService.alterarPlaca(placa, cpfCnpj, body);
            logger.info("Solicitação para atualizar placa concluída com sucesso");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            logger.error("Erro ao ativar/desativar a placa: {}", placa, e);
            throw new ApplicationException("Erro ao ativar/desativar a placa", e);
        }
    }

    @Override
    public ResponseEntity<List<PlacaReqDTO>> listarTodasPlacas() throws ApplicationException {
        try {
            List<PlacaReqDTO> response = placaService.listarTodasPlacas();
            logger.info("Solicitação para listar todas as placas concluída com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao listar todas as placas", e);
            throw new ApplicationException("Erro ao listar todas as placas", e);
        }
    }

}
