package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.RegistroNaoEncontradoException;
import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaRespDTO;
import com.grupoccr.placa.model.dto.PlacaUpdateReqDTO;
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
            if(placaRepository.findByPlaca(placaReqDTO.getPlaca()).isPresent()) {
                throw new ApplicationException("Placa já cadastrada");
            }

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
    public PlacaRespDTO incluirLote(List<PlacaReqDTO> placasReqDTO) {
        PlacaRespDTO placaRespDTO = new PlacaRespDTO();
        int registrosSalvos = 0;
        int totalRegistros = placasReqDTO.size();

        try {
            logger.info("Iniciando inclusão em lote de placas");

            for (PlacaReqDTO placaReqDTO : placasReqDTO) {
                try {
                    if(placaRepository.findByPlaca(placaReqDTO.getPlaca()).isPresent()) {
                        logger.error("Placa já cadastrada: {}", placaReqDTO.getPlaca());
                        throw new ApplicationException("Placa já cadastrada");
                    }
                    incluir(placaReqDTO);
                    registrosSalvos++;
                } catch (ApplicationException | RegistroNaoEncontradoException e) {
                    logger.error("Erro ao incluir placa: {}", e.getMessage(), e);
                } catch (Exception e) {
                    logger.error("Erro inesperado ao incluir placa: {}", e.getMessage(), e);
                }
            }
            placaRespDTO.setRegistrosSalvos(registrosSalvos);
            placaRespDTO.setTotalRegistros(totalRegistros);
            placaRespDTO.setRegistrosInvalidos(totalRegistros - registrosSalvos);
            logger.info("Inclusão em lote concluída. Registros salvos: {}, Registros inválidos: {}",
                    registrosSalvos, placaRespDTO.getRegistrosInvalidos());
            return placaRespDTO;
        } catch (Exception e) {
            logger.error("Erro inesperado ao incluir lote de placas", e);
            throw new RuntimeException("Ocorreu um erro interno. Por favor, tente novamente mais tarde.", e);
        }
    }

//    @Transactional
//    public PlacaRespDTO atualizar(String placa, PlacaUpdateReqDTO placaUpdateReqDTO) throws ApplicationException {
//        try {
//            // Buscar a placa existente pelo número da placa
//            Placa placaExistente = placaRepository.findByPlaca(placa)
//                    .orElseThrow(() -> new ApplicationException("Placa não encontrada"));
//
//            placaMapper.updateDtoToEntity(placaUpdateReqDTO, placaExistente);
//
//            placaRepository.save(placaExistente);
//
//            PlacaRespDTO placaRespDTO = new PlacaRespDTO();
//            placaRespDTO.setMensagem("Alterado com sucesso");
//
//            return placaRespDTO;
//        } catch (Exception e) {
//            logger.error("Erro ao atualizar placa: {}", placa, e);
//            throw new ApplicationException("Erro ao atualizar placa", e);
//        }
//    }
    @Transactional
    public PlacaRespDTO ativarDesativar(String placa, boolean ativo) throws ApplicationException {
        try {
            logger.info("Iniciando ativação/desativação da placa: {}", placa);

            Placa placaExistente = placaRepository.findByPlaca(placa)
                    .orElseThrow(() -> new ApplicationException("Placa não encontrada"));

            placaExistente.ativarDesativar(ativo);

            placaRepository.save(placaExistente);

            PlacaRespDTO placaRespDTO = new PlacaRespDTO();
            placaRespDTO.setMensagem(ativo ? "Placa ativada com sucesso" : "Placa desativada com sucesso");

            logger.info("Status da placa atualizado com sucesso: {}", placa);
            return placaRespDTO;
        } catch (Exception e) {
            logger.error("Erro ao atualizar status da placa: {}", placa, e);
            throw new ApplicationException("Erro ao atualizar status da placa", e);
        }
    }
}
