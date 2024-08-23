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

import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaEmailRepository pessoaEmailRepository;

    @Autowired
    private PessoaTelefoneRepository pessoaTelefoneRepository;

    @Autowired
    private PessoaEnderecoRepository pessoaEnderecoRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Transactional
    public void incluir(PessoaReqDTO pessoaReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = Optional.ofNullable(parceiroRepository.findByApiKey(apiKey).orElseThrow(() ->
                new RegistroNaoEncontradoException("API-KEY " + apiKey + "não cadastrada")));


        pessoaReqDTO.setParceiro(parceiroOptional.get());
        Pessoa pessoa = pessoaMapper.toEntity(pessoaReqDTO);
        pessoa = inserirParceiro(pessoaMapper.toEntity(pessoaReqDTO), parceiroOptional.get());

        pessoaRepository.save(pessoa);

    }

    @Transactional
    public PessoaRespDTO incluirLote(PessoasReqDTO pessoasReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = parceiroRepository.findByApiKey(apiKey);

        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }

        Parceiro parceiro = parceiroOptional.get();
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
    public PessoaRespDTO atualizar(String cpfCnpj, PessoaReqDTO pessoaReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = parceiroRepository.findByApiKey(apiKey);

        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }

        Parceiro parceiro = parceiroOptional.get();

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
        if (pessoa.getEmails() != null) {
            for (PessoaEmail email : pessoa.getEmails()) {
                email.setParceiro(parceiro);
                email.setPessoa(pessoa);
            }
        }

        if (pessoa.getTelefones() != null) {
            for (PessoaTelefone telefone : pessoa.getTelefones()) {
                telefone.setParceiro(parceiro);
                telefone.setPessoa(pessoa);
            }
        }

        if (pessoa.getEnderecos() != null) {
            for (PessoaEndereco endereco : pessoa.getEnderecos()) {
                endereco.setParceiro(parceiro);
                endereco.setPessoa(pessoa);
            }
        }

        return pessoa;
    }
}
