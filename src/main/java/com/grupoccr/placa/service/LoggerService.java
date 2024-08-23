package com.grupoccr.placa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoccr.placa.model.dto.PlacaRespDTO;
import com.grupoccr.placa.model.entity.Logger;
import com.grupoccr.placa.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoggerService {

    private final LoggerRepository loggerRepository;

    @Autowired
    public LoggerService(LoggerRepository loggerRepository) {
        this.loggerRepository = loggerRepository;
    }

    public Logger saveLogger(HttpServletRequest request, PlacaRespDTO response) {
        Logger logger = new Logger();
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // Captura os cabeçalhos
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headersMap.put(headerName, wrappedRequest.getHeader(headerName));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String headersJson;
        try {
            headersJson = objectMapper.writeValueAsString(headersMap);
        } catch (JsonProcessingException e) {
            headersJson = "{}"; // Valor padrão em caso de erro
            e.printStackTrace(); // Log do erro
        }

        logger.setHeader(headersJson);

        // Captura o corpo da requisição
        String payload;
        try {
            payload = new String(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            payload = "{}"; // Valor padrão em caso de erro
            e.printStackTrace(); // Log do erro
        }

        logger.setRequest(payload);
        logger.setResponse(response.toString()); // Exemplo de preenchimento do campo 'response'
        // Preencha os outros campos necessários...

        return loggerRepository.save(logger);
    }
}
