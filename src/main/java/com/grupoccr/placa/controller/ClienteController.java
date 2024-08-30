package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.dto.ClienteListReqDTO;
import com.grupoccr.placa.model.dto.ClienteRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.CustomException;
import com.grupoccr.placa.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController implements ClienteAPI {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService clienteService;

    @Override
    public ResponseEntity<String> incluir(@Valid @RequestBody ClienteReqDTO body) throws ApplicationException {
        try {
            logger.info("Iniciando inclusão de cliente: {}", body.getNome());
            clienteService.incluir(body);
            logger.info("Cliente incluída com sucesso: {}", body.getNome());
            return new ResponseEntity<>("Cadastro Realizado com sucesso", HttpStatus.CREATED);
        } catch (TransactionSystemException ex) {
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
                String errorMessage = constraintViolationException.getConstraintViolations().stream()
                        .map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                logger.error("Erro de validação ao incluir cliente: {}", errorMessage);
                throw new ValidationException("Erro de validação: " + errorMessage, ex);
            }
            throw new CustomException("Erro de validação ao incluir cliente, falta de campos obrigatórios");
        } catch (ApplicationException e) {
            logger.error("Erro ao incluir cliente: CPF/CNPJ já cadastrado", e.getMessage());
            return new ResponseEntity<>("CPF/CNPJ ja cadastrado", HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Erro ao cadastrar cliente campos necessario", e);
            throw new CustomException("Error ao cadastrar cliente dados invalidos, por favor verifique os campos e tente novamente");
        }
    }

    @Override
    public ResponseEntity<ClienteRespDTO> incluirLote(@Valid @RequestBody List<ClienteReqDTO> body) throws ApplicationException {
        try {
            ClienteRespDTO response = clienteService.incluirLote(body);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (TransactionSystemException ex) {
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
                String errorMessage = constraintViolationException.getConstraintViolations().stream()
                        .map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                logger.error("Erro de validação ao incluir lote de clientes: {}", errorMessage);
                throw new ValidationException("Erro de validação: " + errorMessage, ex);
            }
            throw new CustomException("Erro de validação ao incluir lote de clientes, falta de campos obrigatórios");
        } catch (Exception e) {
            logger.error("Erro interno ao incluir lote de clientes", e);
            throw new CustomException("Erro interno ao incluir lote de clientes");
        }
    }

    @Override
    public ResponseEntity<ClienteRespDTO> atualizar(@PathVariable String cpfCnpj, @Valid @RequestBody ClienteListReqDTO body) throws ApplicationException {
        try {
            logger.info("Iniciando atualização de cliente com CPF/CNPJ: {} para parceiro ID: {}", cpfCnpj);
            ClienteRespDTO response = clienteService.atualizar(cpfCnpj, body);
            logger.info("Cliente com CPF/CNPJ: {} atualizada com sucesso", cpfCnpj);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (TransactionSystemException ex) {
            if(ex.getRootCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
                String errorMessage = constraintViolationException.getConstraintViolations().stream()
                        .map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                logger.error("Erro de validação ao atualizar cliente com CPF/CNPJ: {}", cpfCnpj, errorMessage);
                throw new ValidationException("Erro de validação: " + errorMessage, ex);
            }
            throw new CustomException("Erro de validação ao atualizar cliente, falta de campos obrigatórios");
        } catch (Exception e) {
            logger.error("Erro interno ao atualizar cliente com CPF/CNPJ: {}", cpfCnpj, e);
            throw new CustomException("Erro interno ao atualizar cliente");
        }
    }
}