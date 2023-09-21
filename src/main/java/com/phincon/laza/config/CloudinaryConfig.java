package com.phincon.laza.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Value("${com.phincon.laza.cloudinary.api-secret}")
    private String apiSecret;

    @Value("${com.phincon.laza.cloudinary.api-key}")
    private String apiKey;

    @Value("${com.phincon.laza.cloudinary.cloud-name}")
    private String cloudName;

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary instance = new Cloudinary(
                ObjectUtils.asMap("api_secret", apiSecret,
                        "cloud_name", cloudName,
                        "api_sign_request", "sha256",
                        "api_key", apiKey,
                        "secure", true));
        return instance;
    }
}
