package com.phincon.laza.config;

import com.xendit.XenditClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XenditConfig {

    @Value("${xendit.api.key.secret}")
    private String XENDIT_API_KEY;
    @Bean
    public XenditClient xenditClient() {
        return new XenditClient.Builder()
                .setApikey(XENDIT_API_KEY)
                .build();
    }

}
