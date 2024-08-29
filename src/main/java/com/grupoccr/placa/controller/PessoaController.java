package com.grupoccr.placa.controller;

import com.grupoccr.placa.model.dto.PessoaUpdateReqDTO;
import com.grupoccr.placa.model.dto.PessoaReqDTO;
import com.grupoccr.placa.model.dto.PessoaRespDTO;
import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.CustomException;
import com.grupoccr.placa.model.dto.PessoasReqDTO;
import com.grupoccr.placa.service.PessoaService;
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
@RequestMapping("/api/pessoa")
public class PessoaController implements PessoaAPI {

    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);

    @Autowired
    private PessoaService pessoaService;

    @Override
    public ResponseEntity<String> incluir(@Valid @RequestBody PessoaReqDTO body) throws ApplicationException {
        try {
            logger.info("Iniciando inclusão de pessoa: {}", body.getNome());
            pessoaService.incluir(body);
            logger.info("Pessoa incluída com sucesso: {}", body.getNome());
            return new ResponseEntity<>("Cadastro Realizado com sucesso", HttpStatus.CREATED);
        } catch (TransactionSystemException ex) {
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
                String errorMessage = constraintViolationException.getConstraintViolations().stream()
                        .map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                logger.error("Erro de validação ao incluir pessoa: {}", errorMessage);
                throw new ValidationException("Erro de validação: " + errorMessage, ex);
            }
            throw new CustomException("Erro de validação ao incluir pessoa, falta de campos obrigatórios");
        } catch (ApplicationException e) {
            logger.error("Erro ao incluir pessoa: CPF/CNPJ já cadastrado", e.getMessage());
            return new ResponseEntity<>("CPF/CNPJ ja cadastrado", HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Erro ao cadastrar pessoa campos necessario", e);
            throw new CustomException("Error ao cadastrar pessoa dados invalidos, por favor verifique os campos e tente novamente");
        }
    }

    @Override
    public ResponseEntity<PessoaRespDTO> incluirLote(@Valid @RequestBody List<PessoaReqDTO> body) throws ApplicationException {
        try {
            PessoaRespDTO response = pessoaService.incluirLote(body);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (TransactionSystemException ex) {
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
                String errorMessage = constraintViolationException.getConstraintViolations().stream()
                        .map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                logger.error("Erro de validação ao incluir lote de pessoas: {}", errorMessage);
                throw new ValidationException("Erro de validação: " + errorMessage, ex);
            }
            throw new CustomException("Erro de validação ao incluir lote de pessoas, falta de campos obrigatórios");
        } catch (Exception e) {
            logger.error("Erro interno ao incluir lote de pessoas", e);
            throw new CustomException("Erro interno ao incluir lote de pessoas");
        }
    }

    @Override
    public ResponseEntity<PessoaRespDTO> atualizar(@PathVariable String cpfCnpj, @Valid @RequestBody PessoaUpdateReqDTO body) throws ApplicationException {
        try {
            logger.info("Iniciando atualização de pessoa com CPF/CNPJ: {} para parceiro ID: {}", cpfCnpj);
            PessoaRespDTO response = pessoaService.atualizar(cpfCnpj, body);
            logger.info("Pessoa com CPF/CNPJ: {} atualizada com sucesso", cpfCnpj);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (TransactionSystemException ex) {
            if(ex.getRootCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
                String errorMessage = constraintViolationException.getConstraintViolations().stream()
                        .map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                logger.error("Erro de validação ao atualizar pessoa com CPF/CNPJ: {}", cpfCnpj, errorMessage);
                throw new ValidationException("Erro de validação: " + errorMessage, ex);
            }
            throw new CustomException("Erro de validação ao atualizar pessoa, falta de campos obrigatórios");
        } catch (Exception e) {
            logger.error("Erro interno ao atualizar pessoa com CPF/CNPJ: {}", cpfCnpj, e);
            throw new CustomException("Erro interno ao atualizar pessoa");
        }
    }
}