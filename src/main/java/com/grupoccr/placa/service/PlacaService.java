package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.RegistroNaoEncontradoException;
import com.grupoccr.placa.model.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlacaService {

    @Autowired
    private PlacaRepository placaRepository;

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
            verificarPlacaCadastrada(placaReqDTO.getPlaca());
            Placa placa = placaMapper.toEntity(placaReqDTO);
            Cliente cliente = obterClientePorCpfCnpj(placaReqDTO.getCpfCnpj());
            placa.setCliente(cliente);
            placa.setParceiro(cliente.getParceiro());
            Placa placaSalva = placaRepository.save(placa);
            salvarConcessionarias(placaReqDTO, placaSalva);
            return criarRespostaSucesso();
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
        int totalRegistros = placasReqDTO.size();
        int registrosSalvos = 0;

        try {
            logger.info("Iniciando inclusão em lote de placas");

            for (PlacaReqDTO placaReqDTO : placasReqDTO) {
                if (processarPlacaEmLote(placaReqDTO)) {
                    registrosSalvos++;
                }
            }

            placaRespDTO.setRegistrosSalvos(registrosSalvos);
            placaRespDTO.setMensagem("Inclusão em lote concluída");
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
    public PlacaRespDTO alterarPlaca(String placa, String cpfCnpj, PlacaUpdateReqDTO placaUpdateReqDTO) throws ApplicationException {
        try {
            logger.info("Iniciando atualização da placa: {} para CPF/CNPJ: {}", placa, cpfCnpj);
            Placa placaExistente = obterPlacaExistente(placa, cpfCnpj);
            atualizarStatusPlaca(placaExistente, placaUpdateReqDTO.isAtivo());
            atualizarConcessionarias(placaExistente, placaUpdateReqDTO.getConcessionaria());
            placaRepository.save(placaExistente);
            return criarRespostaPlacaAtualizada();
        } catch (Exception e) {
            logger.error("Erro ao atualizar placa: {}", placa, e);
            throw new ApplicationException("Erro ao atualizar placa", e);
        }
    }

    @Transactional(readOnly = true)
    public List<PlacaReqDTO> listarTodasPlacas() {
        try {
            List<Placa> placas = placaRepository.findAll();
            logger.info("Listando todas as placas cadastradas: {}", placas.size());
            return placas.stream().map(placa -> {
                PlacaReqDTO placaReqDTO = placaMapper.toDto(placa);
                List<PlacaConcessionariaDTO> concessionariaDTOs = placa.getConcessionarias().stream().map(concessionaria -> {
                    PlacaConcessionariaDTO concessionariaDTO = PlacaConcessionariaMapper.INSTANCE.toDto(concessionaria);
                    List<PracaBloqueadaDTO> pracasBloqueadasDTO = concessionaria.getPracasBloqueadas().stream()
                            .map(praca -> PracaBloqueadaMapper.INSTANCE.toDto(praca))
                            .collect(Collectors.toList());

                    concessionariaDTO.setPracasBloqueadas(pracasBloqueadasDTO);
                    return concessionariaDTO;
                }).collect(Collectors.toList());

                placaReqDTO.setConcessionaria(concessionariaDTOs);
                return placaReqDTO;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao listar todas as placas", e);
            throw new ApplicationException("Erro ao listar todas as placas", e);
        }
    }

    private void verificarPlacaCadastrada(String placa) throws ApplicationException {
        logger.info("Verificando se a placa já está cadastrada: {}", placa);
        if (placaRepository.existsByPlaca(placa)) {
            logger.error("Placa já cadastrada no banco de dados: {}", placa);
            throw new ApplicationException("Placa já cadastrada");
        }
    }

    private Cliente obterClientePorCpfCnpj(String cpfCnpj) throws ApplicationException {
        Optional<Cliente> pessoaOptional = clienteRepository.findByCpfCnpj(cpfCnpj);
        if (pessoaOptional.isEmpty()) {
            throw new ApplicationException("Pessoa não encontrada para o CPF/CNPJ informado");
        }
        return pessoaOptional.get();
    }

    private void salvarConcessionarias(PlacaReqDTO placaReqDTO, Placa placaSalva) throws ApplicationException {
        List<PlacaConcessionaria> concessionarias = PlacaConcessionariaMapper.INSTANCE.toEntityList(placaReqDTO.getConcessionaria());
        for (PlacaConcessionaria placaConcessionaria : concessionarias) {
            logger.info("Salvando placa concessionária: {}", placaConcessionaria.getCodigoConcessionaria());
            placaConcessionaria.setPlaca(placaSalva);
            List<PracaBloqueadaDTO> pracasBloqueadasDTO = obterPracasBloqueadasDTO(placaReqDTO, placaConcessionaria);
            if (pracasBloqueadasDTO != null && !pracasBloqueadasDTO.isEmpty()) {
                List<PracaBloqueada> pracasBloqueadas = PlacaConcessionariaMapper.INSTANCE.toEntityList(pracasBloqueadasDTO, placaConcessionaria);
                logger.info("Salvando praças bloqueadas: {}", pracasBloqueadas.stream().map(PracaBloqueada::getId).collect(Collectors.toList()));
                placaConcessionaria.setPracasBloqueadas(pracasBloqueadas);
            }
        }
        placaConcessionariaRepository.saveAll(concessionarias);
    }

    private List<PracaBloqueadaDTO> obterPracasBloqueadasDTO(PlacaReqDTO placaReqDTO, PlacaConcessionaria placaConcessionaria) throws ApplicationException {
        return placaReqDTO.getConcessionaria().stream()
                .filter(concessionariaDTO -> concessionariaDTO.getCodigoConcessionaria() == placaConcessionaria.getCodigoConcessionaria())
                .findFirst()
                .orElseThrow(() -> new ApplicationException("Concessionaria não encontrada"))
                .getPracasBloqueadas();
    }

    private PlacaRespDTO criarRespostaSucesso() {
        PlacaRespDTO placaRespDTO = new PlacaRespDTO();
        placaRespDTO.setMensagem("Cadastrado com sucesso");
        logger.info("Placa cadastrada com sucesso");
        return placaRespDTO;
    }

    private boolean processarPlacaEmLote(PlacaReqDTO placaReqDTO) {
        try {
            logger.info("Verificando se a placa já está cadastrada: {}", placaReqDTO.getPlaca());
            if (placaRepository.existsByPlaca(placaReqDTO.getPlaca())) {
                logger.error("Placa já cadastrada no banco de dados: {}", placaReqDTO.getPlaca());
                throw new ApplicationException("Placa já cadastrada");
            }
            incluir(placaReqDTO);
            return true;
        } catch (ApplicationException | RegistroNaoEncontradoException e) {
            logger.error("Erro ao incluir placa: {}", e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Erro inesperado ao incluir placa: {}", e.getMessage(), e);
            return false;
        }
    }

    private Placa obterPlacaExistente(String placa, String cpfCnpj) throws ApplicationException {
        return placaRepository.findByPlacaAndCliente_CpfCnpj(placa, cpfCnpj)
                .orElseThrow(() -> new ApplicationException("Placa não encontrada"));
    }

    private void atualizarStatusPlaca(Placa placaExistente, boolean ativo) {
        placaExistente.ativarDesativar(ativo);
    }

    private void atualizarConcessionarias(Placa placaExistente, List<PlacaConcessionariaDTO> concessionariaDTOs) {
        List<PlacaConcessionaria> concessionariasExistentes = placaExistente.getConcessionarias();
        concessionariasExistentes.clear();
        logger.info("Removendo concessionárias existentes da placa: {}", placaExistente.getPlaca());
        for (PlacaConcessionariaDTO concessionariaDTO : concessionariaDTOs) {
            PlacaConcessionaria placaConcessionaria = criarPlacaConcessionaria(placaExistente, concessionariaDTO);
            concessionariasExistentes.add(placaConcessionaria);
        }
    }

    private PlacaConcessionaria criarPlacaConcessionaria(Placa placaExistente, PlacaConcessionariaDTO concessionariaDTO) {
        PlacaConcessionaria placaConcessionaria = new PlacaConcessionaria();
        placaConcessionaria.setCodigoConcessionaria(concessionariaDTO.getCodigoConcessionaria());
        placaConcessionaria.setDsConcessionaria(concessionariaDTO.getDsConcessionaria());
        placaConcessionaria.setPlaca(placaExistente);
        logger.info("Salvando placa concessionária: {}", placaConcessionaria.getCodigoConcessionaria());
        List<PracaBloqueada> pracasBloqueadas = criarPracasBloqueadas(placaConcessionaria, concessionariaDTO.getPracasBloqueadas());
        placaConcessionaria.setPracasBloqueadas(pracasBloqueadas);
        return placaConcessionaria;
    }

    private List<PracaBloqueada> criarPracasBloqueadas(PlacaConcessionaria placaConcessionaria, List<PracaBloqueadaDTO> pracasBloqueadasDTO) {
        List<PracaBloqueada> pracasBloqueadas = new ArrayList<>();
        for (PracaBloqueadaDTO pracaBloqueadaDTO : pracasBloqueadasDTO) {
            PracaBloqueada.PracaBloqueadaId pracaBloqueadaId = new PracaBloqueada.PracaBloqueadaId(
                    placaConcessionaria.getId(),
                    pracaBloqueadaDTO.getCodigoPraca()
            );
            PracaBloqueada pracaBloqueada = new PracaBloqueada();
            pracaBloqueada.setId(pracaBloqueadaId);
            pracaBloqueada.setPlacaConcessionaria(placaConcessionaria);
            pracasBloqueadas.add(pracaBloqueada);
        }
        return pracasBloqueadas;
    }

    private PlacaRespDTO criarRespostaPlacaAtualizada() {
        PlacaRespDTO placaRespDTO = new PlacaRespDTO();
        placaRespDTO.setMensagem("Placa atualizada com sucesso");
        logger.info("Placa atualizada com sucesso");
        return placaRespDTO;
    }
}