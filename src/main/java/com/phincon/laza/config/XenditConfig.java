package com.phincon.laza.config;

import com.xendit.XenditClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XenditConfig {
    @Bean
    public XenditClient xenditClient() {
        return new XenditClient.Builder()
                .setApikey("xnd_public_development_Av89vIGkrNeQE2ZSKAGANGC70X6iTYt37DRUt3JNrIr7z8RGKoNGW5zmZHg4WqJ")
                .build();
    }

}
