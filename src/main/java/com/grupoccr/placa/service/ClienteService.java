package com.grupoccr.placa.service;

import com.grupoccr.placa.exception.ApplicationException;
import com.grupoccr.placa.exception.RegistroNaoEncontradoException;
import com.grupoccr.placa.model.dto.ClienteReqDTO;
import com.grupoccr.placa.model.dto.ClienteRespDTO;
import com.grupoccr.placa.model.dto.ClientesReqDTO;
import com.grupoccr.placa.model.entity.*;
import com.grupoccr.placa.model.mapper.ClienteMapper;
import com.grupoccr.placa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteEmailRepository clienteEmailRepository;

    @Autowired
    private ClienteTelefoneRepository clienteTelefoneRepository;

    @Autowired
    private ClienteEnderecoRepository clienteEnderecoRepository;

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Transactional
    public void incluir(ClienteReqDTO clienteReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = Optional.ofNullable(parceiroRepository.findByApiKey(apiKey).orElseThrow(() ->
                new RegistroNaoEncontradoException("API-KEY " + apiKey + "não cadastrada")));


        clienteReqDTO.setParceiro(parceiroOptional.get());
        Cliente cliente = clienteMapper.toEntity(clienteReqDTO);
        cliente = inserirParceiro(clienteMapper.toEntity(clienteReqDTO), parceiroOptional.get());

        clienteRepository.save(cliente);

    }

    @Transactional
    public ClienteRespDTO incluirLote(ClientesReqDTO clientesReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = parceiroRepository.findByApiKey(apiKey);

        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }

        Parceiro parceiro = parceiroOptional.get();
        int registrosSalvos = 0;

        for (ClienteReqDTO clienteReqDTO : clientesReqDTO.getPessoas()) {
            Cliente cliente = clienteMapper.toEntity(clienteReqDTO);
            cliente = inserirParceiro(cliente, parceiro);
            clienteRepository.save(cliente);
            registrosSalvos++;
        }

        ClienteRespDTO clienteRespDTO = new ClienteRespDTO();
        clienteRespDTO.setRegistrosSalvos(registrosSalvos);
        clienteRespDTO.setTotalRegistros(clientesReqDTO.getPessoas().size());
        clienteRespDTO.setRegistrosInvalidos(clientesReqDTO.getPessoas().size() - registrosSalvos);

        return clienteRespDTO;
    }

    @Transactional
    public ClienteRespDTO atualizar(String cpfCnpj, ClienteReqDTO clienteReqDTO, String apiKey) {

        Optional<Parceiro> parceiroOptional = parceiroRepository.findByApiKey(apiKey);

        if (parceiroOptional.isEmpty()) {
            throw new ApplicationException("API-KEY não cadastrada");
        }

        Parceiro parceiro = parceiroOptional.get();

        Cliente cliente = clienteRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new ApplicationException("Pessoa não encontrada"));

        clienteMapper.updateDtoToEntity(clienteReqDTO, cliente);
        cliente = inserirParceiro(cliente, parceiro);
        clienteRepository.save(cliente);

        ClienteRespDTO clienteRespDTO = new ClienteRespDTO();
        clienteRespDTO.setMensagem("Alterado com sucesso");

        return clienteRespDTO;
    }

    private Cliente inserirParceiro(Cliente cliente, Parceiro parceiro) {
        if (cliente.getEmails() != null) {
            for (ClienteEmail email : cliente.getEmails()) {
                email.setParceiro(parceiro);
                email.setCliente(cliente);
            }
        }

        if (cliente.getTelefones() != null) {
            for (ClienteTelefone telefone : cliente.getTelefones()) {
                telefone.setParceiro(parceiro);
                telefone.setCliente(cliente);
            }
        }

        if (cliente.getEnderecos() != null) {
            for (ClienteEndereco endereco : cliente.getEnderecos()) {
                endereco.setParceiro(parceiro);
                endereco.setCliente(cliente);
            }
        }

        return cliente;
    }
}
