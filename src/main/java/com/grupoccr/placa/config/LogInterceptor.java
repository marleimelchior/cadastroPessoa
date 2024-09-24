package com.grupoccr.placa.config;

import com.grupoccr.placa.model.entity.Logger;
import com.grupoccr.placa.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.UUID;

@Component
public class LogInterceptor extends OncePerRequestFilter {

    @Autowired
    private LoggerRepository loggerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        // Envolva a solicitação e a resposta para capturar o corpo de cada um
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;
            //montar o objeto de log
            Logger logger = new Logger();
            logger.setUuid(UUID.randomUUID().toString());
            logger.setDataRequest(new Timestamp(startTime));
            logger.setTimeRequest(new BigDecimal(executeTime));
            logger.setType(request.getMethod());
            logger.setUrl(request.getRequestURI());
            logger.setStatusCode(response.getStatus());

            // capturar o conteúdo dos headers
            Enumeration<String> headerNames = request.getHeaderNames();
            StringBuilder headers = new StringBuilder();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
            }
            logger.setHeader(headers.toString().trim());

            // Capturar o conteúdo do corpo da requisição
            String requestBody = new String(wrappedRequest.getContentAsByteArray());
            requestBody = requestBody.replaceAll("\u0000", "");
            // Capturar parâmetros da URL
            String queryString = request.getQueryString();
            String requestParams = (queryString == null ? "" : "?" + queryString);

            // Concatenar o corpo da requisição e os parâmetros da URL
            if (!requestParams.isEmpty()) {
                requestBody += " (" + requestParams + ")";
            }

            logger.setRequest(requestBody.isEmpty() ? "{}" : requestBody);

            // Capturar o conteúdo do corpo da resposta
            String responseBody = new String(wrappedResponse.getContentAsByteArray());
            responseBody = responseBody.replaceAll("\u0000", "");
            logger.setResponse(responseBody.isEmpty() ? "[]" : responseBody);

            loggerRepository.save(logger);
            // Certifique-se de copiar o conteúdo da resposta de volta ao cliente
            wrappedResponse.copyBodyToResponse();
        }
    }
}
