package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaRespDTO;
import com.grupoccr.placa.model.dto.PlacasReqDTO;
import com.grupoccr.placa.model.entity.*;
import com.grupoccr.placa.model.mapper.PlacaMapper;
import com.grupoccr.placa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlacaService {

    @Autowired
    private PlacaRepository placaRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PlacaMapper placaMapper;

    @Transactional
    public PlacaRespDTO incluir(PlacaReqDTO placaReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = parceiroRepository.findByApiKey(apiKey);

        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }

        Placa placa = placaMapper.toEntity(placaReqDTO);
        Optional<Cliente> pessoaOptional = clienteRepository.findByCpfCnpj(placa.getCpfCnpj());
        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }
        placa.setCliente(pessoaOptional.get());
        placa.setParceiro(parceiroOptional.get());
        placaRepository.save(placa);

        PlacaRespDTO placaRespDTO = new PlacaRespDTO();
        placaRespDTO.setMensagem("Cadastrado com sucesso");

        return placaRespDTO;
    }

    @Transactional
    public PlacaRespDTO incluirLote(PlacasReqDTO placasReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = parceiroRepository.findByApiKey(apiKey);

        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }

        Parceiro parceiro = parceiroOptional.get();
        int registrosSalvos = 0;


        for (PlacaReqDTO placaReqDTO : placasReqDTO.getPlacas()) {
            Placa placa = placaMapper.toEntity(placaReqDTO);
            Optional<Cliente> pessoaOptional = clienteRepository.findByCpfCnpj(placa.getCpfCnpj());
            if (parceiroOptional.isEmpty()) {
                throw new ApplicationException("API-KEY não cadastrada");
            }
            placa.setCliente(pessoaOptional.get());
            placa.setParceiro(parceiro);
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
    public PlacaRespDTO atualizar(String cpfCnpj, PlacaReqDTO placaReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = parceiroRepository.findByApiKey(apiKey);

        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }

        Placa placa = placaRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new ApplicationException("Placa não encontrada"));

        placaMapper.updateDtoToEntity(placaReqDTO, placa);
        placaRepository.save(placa);

        PlacaRespDTO placaRespDTO = new PlacaRespDTO();
        placaRespDTO.setMensagem("Alterado com sucesso");

        return placaRespDTO;
    }
}
