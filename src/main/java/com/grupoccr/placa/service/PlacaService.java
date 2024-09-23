package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.RegistroNaoEncontradoException;
import com.grupoccr.placa.model.dto.PlacaConcessionariaDTO;
import com.grupoccr.placa.model.dto.PlacaReqDTO;
import com.grupoccr.placa.model.dto.PlacaRespDTO;
import com.grupoccr.placa.model.dto.PracaBloqueadaDTO;
import com.grupoccr.placa.model.entity.*;
import com.grupoccr.placa.model.mapper.PlacaConcessionariaMapper;
import com.grupoccr.placa.model.mapper.PlacaMapper;
import com.grupoccr.placa.model.mapper.PracaBloqueadaMapper;
import com.grupoccr.placa.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlacaService {

    @Autowired
    private PlacaRepository placaRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PlacaConcessionariaRepository placaConcessionariaRepository;

    @Autowired
    private PlacaMapper placaMapper;

    private static final Logger logger = LoggerFactory.getLogger(PlacaService.class);

    @Transactional
    public PlacaRespDTO incluir(PlacaReqDTO placaReqDTO) {
        try {
            logger.info("Verificando se a placa já está cadastrada: {}", placaReqDTO.getPlaca());
            if (placaRepository.existsByPlaca(placaReqDTO.getPlaca())) {
                logger.error("Placa já cadastrada no banco de dados: {}", placaReqDTO.getPlaca());
                throw new ApplicationException("Placa já cadastrada");
            }
            Placa placa = placaMapper.toEntity(placaReqDTO);

            Optional<Cliente> pessoaOptional = clienteRepository.findByCpfCnpj(placaReqDTO.getCpfCnpj());
            if (pessoaOptional.isEmpty()) {
                throw new ApplicationException("Pessoa não encontrada para o CPF/CNPJ informado");
            }
            Cliente cliente = pessoaOptional.get();
            placa.setCliente(cliente);
            placa.setParceiro(cliente.getParceiro());
            Placa placaSalva = placaRepository.save(placa);

            List<PlacaConcessionaria> concessionarias = PlacaConcessionariaMapper.INSTANCE.toEntityList(placaReqDTO.getConcessionaria());
            for (PlacaConcessionaria placaConcessionaria : concessionarias) {
                logger.info("Salvando placa concessionária: {}", placaConcessionaria.getCodigoConcessionaria());
                placaConcessionaria.setPlaca(placaSalva);

                List<PracaBloqueadaDTO> pracasBloqueadasDTO = placaReqDTO.getConcessionaria().stream()
                        .filter(concessionariaDTO -> concessionariaDTO.getCodigoConcessionaria() == placaConcessionaria.getCodigoConcessionaria())
                        .findFirst()
                        .orElseThrow(() -> new ApplicationException("Concessionaria não encontrada"))
                        .getPracasBloqueadas();

                if (pracasBloqueadasDTO != null && !pracasBloqueadasDTO.isEmpty()) {
                    List<PracaBloqueada> pracasBloqueadas = PlacaConcessionariaMapper.INSTANCE.toEntityList(pracasBloqueadasDTO, placaConcessionaria);
                    logger.info("Salvando praças bloqueadas: {}", pracasBloqueadas.stream().map(PracaBloqueada::getId).collect(Collectors.toList()));
                    placaConcessionaria.setPracasBloqueadas(pracasBloqueadas);
                }
            }
            placaConcessionariaRepository.saveAll(concessionarias);
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
                    logger.info("Verificando se a placa já está cadastrado no meio do lote : {}", placaReqDTO.getPlaca());
                    if(placaRepository.existsByPlaca(placaReqDTO.getPlaca())) {
                        logger.error("Placa já cadastrada no banco de dados no meio do lote: {}", placaReqDTO.getPlaca());
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

    @Transactional
    public PlacaRespDTO ativarDesativar(String placa, String cpfCnpj, boolean ativo) throws ApplicationException {
        try {
            logger.info("Iniciando ativação/desativação da placa: {} para CPF/CNPJ: {}", placa, cpfCnpj);

            Placa placaExistente = placaRepository.findByPlacaAndCpfCnpj(placa, cpfCnpj)
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

    @Transactional(readOnly = true)
    public List<PlacaReqDTO> listarTodasPlacas() {
        List<Placa> placas = placaRepository.findAll();
        logger.info("Listando todas as placas cadastradas: {}", placas.size());
        return placas.stream().map(placa -> {
            PlacaReqDTO placaReqDTO = placaMapper.toDto(placa);
            logger.info("Listando placa: {}", placaReqDTO.getPlaca());
            List<PlacaConcessionariaDTO> concessionariaDTOs = placa.getConcessionarias().stream().map(concessionaria -> {
                PlacaConcessionariaDTO concessionariaDTO = PlacaConcessionariaMapper.INSTANCE.toDto(concessionaria);
                logger.info("Listando concessionária: {}", concessionariaDTO.getCodigoConcessionaria());
                List<PracaBloqueadaDTO> pracasBloqueadasDTO = concessionaria.getPracasBloqueadas().stream()
                        .map(praca -> PracaBloqueadaMapper.INSTANCE.toDto(praca))
                        .collect(Collectors.toList());

                concessionariaDTO.setPracasBloqueadas(pracasBloqueadasDTO);
                return concessionariaDTO;
            }).collect(Collectors.toList());

            placaReqDTO.setConcessionaria(concessionariaDTOs);
            logger.info("Placa listada com sucesso: {}", placaReqDTO.getPlaca());
            return placaReqDTO;
        }).collect(Collectors.toList());
    }
}
