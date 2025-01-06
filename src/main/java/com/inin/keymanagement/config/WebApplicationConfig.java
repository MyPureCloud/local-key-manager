package com.inin.keymanagement.config;

import com.inin.keymanagement.services.HawkAuthentication;
import com.inin.keymanagement.services.RequestLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebApplicationConfig implements WebMvcConfigurer {

    @Autowired
    private HawkAuthentication authService;

    @Autowired
    private RequestLoggingService requestLoggingService;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(new AuthenticationInterceptor(authService, requestLoggingService));
    }
}