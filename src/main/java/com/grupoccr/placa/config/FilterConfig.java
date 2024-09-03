package com.grupoccr.placa.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    //metodo responsavel por registrar o filtro de log quando a aplicação subir
    @Bean
    public FilterRegistrationBean<LogInterceptor> loggingFilter(LogInterceptor logInterceptor) {
        FilterRegistrationBean<LogInterceptor> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(logInterceptor);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
