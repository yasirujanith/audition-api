package com.audition.configuration;

import com.audition.configuration.interceptor.TracingInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final TracingInterceptor tracingInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(tracingInterceptor);
    }
}
