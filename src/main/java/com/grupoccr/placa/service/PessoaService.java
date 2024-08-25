package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.RegistroNaoEncontradoException;
import com.grupoccr.placa.model.dto.PessoaReqDTO;
import com.grupoccr.placa.model.dto.PessoaRespDTO;
import com.grupoccr.placa.model.dto.PessoasReqDTO;
import com.grupoccr.placa.model.entity.*;
import com.grupoccr.placa.model.mapper.PessoaMapper;
import com.grupoccr.placa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class PessoaService {

    private static final Logger logger = LoggerFactory.getLogger(PessoaService.class);

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Transactional
    public void incluir(PessoaReqDTO pessoaReqDTO) {
        try {
            if (pessoaRepository.existsByCpfCnpj(pessoaReqDTO.getCpfCnpj())) {
                throw new ApplicationException("CPF/CNPJ já cadastrado");
            }
            Parceiro parceiro = parceiroRepository.findById(pessoaReqDTO.getParceiroId()).orElseThrow(() ->
                    new RegistroNaoEncontradoException("Parceiro não encontrado"));
            Pessoa pessoa = pessoaMapper.toEntity(pessoaReqDTO);
            pessoa = inserirParceiro(pessoa, parceiro);
            pessoa.setParceiro(parceiro);
            pessoaRepository.save(pessoa);
            logger.info("Pessoa salva no banco: {} com parceiro {}", pessoa, parceiro.getNome());
        } catch (ApplicationException | RegistroNaoEncontradoException e) {
            logger.error("Erro ao incluir pessoa: {}", e.getMessage(), e);
            throw e; // Re-lançar a exceção para que possa ser tratada em outros lugares se necessário
        } catch (Exception e) {
            logger.error("Erro inesperado ao incluir pessoa: {}", e.getMessage(), e);
            throw new RuntimeException("Ocorreu um erro interno. Por favor, tente novamente mais tarde.", e);
        }
    }


    @Transactional
    public PessoaRespDTO incluirLote(PessoasReqDTO pessoasReqDTO, Long parceiroId) {

        Parceiro parceiro = parceiroRepository.findById(parceiroId)
                .orElseThrow(() -> new ApplicationException("Parceiro não encontrado"));

        int registrosSalvos = 0;

        for (PessoaReqDTO pessoaReqDTO : pessoasReqDTO.getPessoas()) {
            Pessoa pessoa = pessoaMapper.toEntity(pessoaReqDTO);
            pessoa = inserirParceiro(pessoa, parceiro);
            pessoaRepository.save(pessoa);
            registrosSalvos++;
        }

        PessoaRespDTO pessoaRespDTO = new PessoaRespDTO();
        pessoaRespDTO.setRegistrosSalvos(registrosSalvos);
        pessoaRespDTO.setTotalRegistros(pessoasReqDTO.getPessoas().size());
        pessoaRespDTO.setRegistrosInvalidos(pessoasReqDTO.getPessoas().size() - registrosSalvos);

        return pessoaRespDTO;
    }

    @Transactional
    public PessoaRespDTO atualizar(String cpfCnpj, PessoaReqDTO pessoaReqDTO, Long parceiroId) {

        Parceiro parceiro = parceiroRepository.findById(parceiroId)
                .orElseThrow(() -> new ApplicationException("Parceiro não encontrado"));

        Pessoa pessoa = pessoaRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new ApplicationException("Pessoa não encontrada"));

        pessoaMapper.updateDtoToEntity(pessoaReqDTO, pessoa);
        pessoa = inserirParceiro(pessoa, parceiro);
        pessoaRepository.save(pessoa);

        PessoaRespDTO pessoaRespDTO = new PessoaRespDTO();
        pessoaRespDTO.setMensagem("Alterado com sucesso");

        return pessoaRespDTO;
    }

    private Pessoa inserirParceiro(Pessoa pessoa, Parceiro parceiro) {
        associarParceiroAEmails(pessoa, parceiro);
        associarParceiroATelefones(pessoa, parceiro);
        associarParceiroAEnderecos(pessoa, parceiro);
        logger.info("Parceiro inserido com sucesso: {}", pessoa.getNome());
        return pessoa;
    }

    private void associarParceiroAEmails(Pessoa pessoa, Parceiro parceiro) {
        if (pessoa.getEmails() != null) {
            pessoa.getEmails().forEach(email -> {
                logger.info("Inserindo email: {}", email.getEmail());
                email.setParceiro(parceiro);
                email.setPessoa(pessoa);
            });
        }
    }

    private void associarParceiroATelefones(Pessoa pessoa, Parceiro parceiro) {
        if (pessoa.getTelefones() != null) {
            pessoa.getTelefones().forEach(telefone -> {
                logger.info("Inserindo telefone: {}", telefone.getNumero());
                telefone.setParceiro(parceiro);
                telefone.setPessoa(pessoa);
            });
        }
    }

    private void associarParceiroAEnderecos(Pessoa pessoa, Parceiro parceiro) {
        if (pessoa.getEnderecos() != null) {
            pessoa.getEnderecos().forEach(endereco -> {
                logger.info("Inserindo endereço: {}", endereco.getLogradouro());
                endereco.setParceiro(parceiro);
                endereco.setPessoa(pessoa);
            });
        }
    }

    public boolean isPessoaAssociadaAoParceiro(String cpfCnpj, Long parceiroId) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findByCpfCnpj(cpfCnpj);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            return pessoa.getParceiro() != null && pessoa.getParceiro().getId().equals(parceiroId);
        }
        return false;
    }
}
