package com.phincon.laza.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@Configuration
public class RajaongkirConfig {

    @Value("${rajaongkir.key}")
    private String RAJAONGKIR_KEY;

    public HttpEntity<String> headerConfig(String... params){
        HttpHeaders headers = new HttpHeaders();
        headers.add("key", RAJAONGKIR_KEY);

        if (params[0] != ""){
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("content-type", "application/x-www-form-urlencoded");
            HttpEntity<String> entity = new HttpEntity<>(params[0],headers);
            return entity;
        }
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return entity;
    }
}
