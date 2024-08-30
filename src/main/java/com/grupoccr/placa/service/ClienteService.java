package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.RegistroNaoEncontradoException;
import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.dto.ClienteRespDTO;
import com.grupoccr.placa.model.dto.ClienteListReqDTO;
import com.grupoccr.placa.model.entity.*;
import com.grupoccr.placa.model.mapper.ClienteMapper;
import com.grupoccr.placa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Transactional
    public void incluir(ClienteReqDTO clienteReqDTO) {
        try {
            if (clienteRepository.existsByCpfCnpj(clienteReqDTO.getCpfCnpj())) {
                throw new ApplicationException("CPF/CNPJ já cadastrado");
            }
            Parceiro parceiro = parceiroRepository.findById(clienteReqDTO.getParceiroId()).orElseThrow(() ->
                    new RegistroNaoEncontradoException("Parceiro não encontrado"));
            Cliente cliente = clienteMapper.toEntity(clienteReqDTO);
            cliente = inserirParceiro(cliente, parceiro);
            cliente.setParceiro(parceiro);
            clienteRepository.save(cliente);
            logger.info("Cliente salva no banco: {} com parceiro {}", cliente, parceiro.getNome());
        } catch (ApplicationException | RegistroNaoEncontradoException e) {
            logger.error("Erro ao incluir Cliente: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao incluir Cliente falta de dados: {}", e.getMessage(), e);
            throw new RuntimeException("Ocorreu um erro interno. Por favor, tente novamente mais tarde.", e);
        }
    }

    @Transactional
    public ClienteRespDTO incluirLote(List<ClienteReqDTO> clientesReqDTO) {
        ClienteRespDTO clienteRespDTO = new ClienteRespDTO();
        int registrosSalvos = 0;
        int totalRegistros = clientesReqDTO.size();

        try {
            logger.info("Iniciando inclusão em lote de clientes");

            for (ClienteReqDTO clienteReqDTO : clientesReqDTO) {
                try {
                    incluir(clienteReqDTO);
                    registrosSalvos++;
                } catch (ApplicationException | RegistroNaoEncontradoException e) {
                    logger.error("Erro ao incluir Cliente: {}", e.getMessage(), e);
                } catch (Exception e) {
                    logger.error("Erro inesperado ao incluir Cliente: {}", e.getMessage(), e);
                }
            }
            clienteRespDTO.setRegistrosSalvos(registrosSalvos);
            clienteRespDTO.setTotalRegistros(totalRegistros);
            clienteRespDTO.setRegistrosInvalidos(totalRegistros - registrosSalvos);
            logger.info("Inclusão em lote concluída. Registros salvos: {}, Registros inválidos: {}",
                    registrosSalvos, clienteRespDTO.getRegistrosInvalidos());
            return clienteRespDTO;
        } catch (Exception e) {
            logger.error("Erro inesperado ao incluir lote de Clientes", e);
            throw new RuntimeException("Ocorreu um erro interno. Por favor, tente novamente mais tarde.", e);
        }
    }

    @Transactional
    public ClienteRespDTO atualizar(String cpfCnpj, ClienteListReqDTO clienteListReqDTO) throws ApplicationException {
        try {
            // Buscar o parceiro
            Parceiro parceiro = parceiroRepository.findById(clienteListReqDTO.getParceiroId())
                    .orElseThrow(() -> new ApplicationException("Parceiro não encontrado"));

            // Buscar a Cliente existente pelo CPF/CNPJ
            Cliente clienteExistente = clienteRepository.findByCpfCnpj(cpfCnpj)
                    .orElseThrow(() -> new ApplicationException("Cliente não encontrada"));

            inativarRegistrosRelacionados(clienteExistente);

            clienteMapper.updateDtoToEntity(clienteListReqDTO, clienteExistente);
            clienteExistente.setParceiro(parceiro);

            clienteRepository.save(clienteExistente);
            atualizarRegistrosRelacionados(clienteExistente, clienteListReqDTO);

            ClienteRespDTO clienteRespDTO = new ClienteRespDTO();
            clienteRespDTO.setMensagem("Alterado com sucesso");

            return clienteRespDTO;
        } catch (Exception e) {
            logger.error("Erro ao atualizar Cliente com CPF/CNPJ: {}", cpfCnpj, e);
            throw new ApplicationException("Erro ao atualizar Cliente", e);
        }
    }

    private void inativarRegistrosRelacionados(Cliente cliente) {
        cliente.getEmails().forEach(email -> {
            email.setStAtivo("N");
            logger.info("Inativando email: {}", email.getEmail());
        });
        cliente.getTelefones().forEach(telefone -> {
            telefone.setStAtivo("N");
            logger.info("Inativando telefone: {}", telefone.getNumero());
        });
        cliente.getEnderecos().forEach(endereco -> {
            endereco.setStAtivo("N");
            logger.info("Inativando endereço: {}", endereco.getLogradouro());
        });
        clienteRepository.save(cliente);
    }

    private void atualizarRegistrosRelacionados(Cliente cliente, ClienteListReqDTO clienteListReqDTO) {
        associarParceiroAEmails(cliente, cliente.getParceiro());
        associarParceiroATelefones(cliente, cliente.getParceiro());
        associarParceiroAEnderecos(cliente, cliente.getParceiro());
    }


    private Cliente inserirParceiro(Cliente cliente, Parceiro parceiro) {
        associarParceiroAEmails(cliente, parceiro);
        associarParceiroATelefones(cliente, parceiro);
        associarParceiroAEnderecos(cliente, parceiro);
        logger.info("Parceiro inserido com sucesso: {}", cliente.getNome());
        return cliente;
    }

    private void associarParceiroAEmails(Cliente cliente, Parceiro parceiro) {
        if (cliente.getEmails() != null) {
            cliente.getEmails().forEach(email -> {
                logger.info("Inserindo email: {}", email.getEmail());
                email.setParceiro(parceiro);
                email.setCliente(cliente);
                email.setStAtivo("S");
            });
        }
    }

    private void associarParceiroATelefones(Cliente cliente, Parceiro parceiro) {
        if (cliente.getTelefones() != null) {
            cliente.getTelefones().forEach(telefone -> {
                logger.info("Inserindo telefone: {}", telefone.getNumero());
                telefone.setParceiro(parceiro);
                telefone.setCliente(cliente);
                telefone.setStAtivo("S");
            });
        }
    }

    private void associarParceiroAEnderecos(Cliente cliente, Parceiro parceiro) {
        if (cliente.getEnderecos() != null) {
            cliente.getEnderecos().forEach(endereco -> {
                logger.info("Inserindo endereço: {}", endereco.getLogradouro());
                endereco.setParceiro(parceiro);
                endereco.setCliente(cliente);
                endereco.setStAtivo("S");
            });
        }
    }

}
