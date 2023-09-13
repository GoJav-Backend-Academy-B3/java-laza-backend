package com.phincon.laza.config;

import com.phincon.laza.interceptor.ImageUploadInterceptor;
import com.phincon.laza.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
        registry.addInterceptor(new ImageUploadInterceptor())
            .addPathPatterns(
                "/management/brands/**", 
                "/users/update", 
                "/product/**");  
    }
}
