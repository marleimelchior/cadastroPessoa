package com.grupoccr.placa.controller;

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
            logger.error("Erro interno ao incluir pessoa", e);
            throw new CustomException("Erro interno ao incluir pessoa");
        }
    }

    @Override
    public ResponseEntity<PessoaRespDTO> incluirLote(
            @Valid @RequestBody PessoasReqDTO body,
            @RequestParam Long parceiroId) throws ApplicationException {
        try {
            logger.info("Iniciando inclusão em lote de pessoas para parceiro ID: {}", parceiroId);
            PessoaRespDTO response = pessoaService.incluirLote(body, parceiroId);
            logger.info("Inclusão em lote concluída com {} registros salvos", response.getRegistrosSalvos());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            logger.error("Erro ao incluir lote de pessoas: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro interno ao incluir lote de pessoas", e);
            throw new CustomException("Erro interno ao incluir lote de pessoas");
        }
    }

    @Override
    public ResponseEntity<PessoaRespDTO> atualizar(
            @PathVariable String cpfCnpj,
            @Valid @RequestBody PessoaReqDTO body,
            @RequestParam Long parceiroId) throws ApplicationException {
        try {
            logger.info("Iniciando atualização de pessoa com CPF/CNPJ: {} para parceiro ID: {}", cpfCnpj, parceiroId);
            PessoaRespDTO response = pessoaService.atualizar(cpfCnpj, body, parceiroId);
            logger.info("Pessoa com CPF/CNPJ: {} atualizada com sucesso", cpfCnpj);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CustomException e) {
            logger.error("Erro ao atualizar pessoa com CPF/CNPJ: {}", cpfCnpj, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro interno ao atualizar pessoa com CPF/CNPJ: {}", cpfCnpj, e);
            throw new CustomException("Erro interno ao atualizar pessoa");
        }
    }
    @GetMapping("/verificar-associacao")
    public ResponseEntity<String> verificarAssociacao(@RequestParam String cpfCnpj, @RequestParam Long parceiroId) {
        boolean isAssociada = pessoaService.isPessoaAssociadaAoParceiro(cpfCnpj, parceiroId);
        if (isAssociada) {
            return ResponseEntity.ok("Pessoa está associada ao parceiro com ID " + parceiroId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não está associada ao parceiro com ID " + parceiroId);
        }
    }
}