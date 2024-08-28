package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaRespDTO;
import com.grupoccr.placa.model.dto.PlacasReqDTO;
import com.grupoccr.placa.model.entity.*;
import com.grupoccr.placa.model.mapper.PlacaMapper;
import com.grupoccr.placa.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlacaService {

    @Autowired
    private PlacaRepository placaRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PlacaMapper placaMapper;

    private static final Logger logger = LoggerFactory.getLogger(PlacaService.class);

    @Transactional
    public PlacaRespDTO incluir(PlacaReqDTO placaReqDTO) {
        try {
            Placa placa = placaMapper.toEntity(placaReqDTO);
            Optional<Pessoa> pessoaOptional = pessoaRepository.findByCpfCnpj(placaReqDTO.getCpfCnpj());
            if (pessoaOptional.isEmpty()) {
                throw new ApplicationException("Pessoa não encontrada para o CPF/CNPJ informado");
            }
            Pessoa pessoa = pessoaOptional.get();
            placa.setPessoa(pessoa);
            placa.setParceiro(pessoa.getParceiro());
            placaRepository.save(placa);
            PlacaRespDTO placaRespDTO = new PlacaRespDTO();
            placaRespDTO.setMensagem("Cadastrado com sucesso");
            logger.info("Placa cadastrada com sucesso: {}", placaReqDTO.getPlaca());
            return placaRespDTO;
        } catch (ApplicationException e) {
            logger.error("Erro ao cadastrar placa: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao cadastrar placa: {}", e.getMessage());
            throw new ApplicationException("Erro inesperado ao cadastrar placa", e);
        }
    }

    @Transactional
    public PlacaRespDTO incluirLote(PlacasReqDTO placasReqDTO) {

//        Optional<Parceiro> parceiroOptional = parceiroRepository.findById(id);

//        if (parceiroOptional.isEmpty()) {
//            throw new ApplicationException("API-KEY não cadastrada");
//        }

//        Parceiro parceiro = parceiroOptional.get();
        int registrosSalvos = 0;


        for (PlacaReqDTO placaReqDTO : placasReqDTO.getPlacas()) {
            Placa placa = placaMapper.toEntity(placaReqDTO);
            Optional<Pessoa> pessoaOptional = pessoaRepository.findByCpfCnpj(placaReqDTO.getCpfCnpj());
//            if (parceiroOptional.isEmpty()) {
//                throw new ApplicationException("API-KEY não cadastrada");
//            }
            placa.setPessoa(pessoaOptional.get());
//            placa.setParceiro(parceiro);
            placaRepository.save(placa);
            registrosSalvos++;
        }

        PlacaRespDTO placaRespDTO = new PlacaRespDTO();
        placaRespDTO.setRegistrosSalvos(registrosSalvos);
        placaRespDTO.setTotalRegistros(placasReqDTO.getPlacas().size());
        placaRespDTO.setRegistrosInvalidos(placasReqDTO.getPlacas().size() - registrosSalvos);

        return placaRespDTO;
    }

    @Transactional
    public PlacaRespDTO atualizar(String cpfCnpj, PlacaReqDTO placaReqDTO) {

//        Optional<Parceiro> parceiroOptional = parceiroRepository.findById(id);

//        if (parceiroOptional.isEmpty()) {
//            throw new ApplicationException("API-KEY não cadastrada");
//        }

//        Placa placa = placaRepository.findByCpfCnpj(cpfCnpj)
//                .orElseThrow(() -> new ApplicationException("Placa não encontrada"));

//        placaMapper.updateDtoToEntity(placaReqDTO, placa);
//        placaRepository.save(placa);

        PlacaRespDTO placaRespDTO = new PlacaRespDTO();
        placaRespDTO.setMensagem("Alterado com sucesso");

        return placaRespDTO;
    }
}
