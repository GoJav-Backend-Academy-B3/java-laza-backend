package com.phincon.laza.config;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidtransConfig {

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Value("${midtrans.client.key}")
    private String MIDTRANS_CLIENT_KEY;

    @Bean
    public MidtransCoreApi MidtransCoreApi() {
       Config coreApiConfigOptions = Config.builder()
                .setServerKey(MIDTRANS_SERVER_KEY)
                .setClientKey(MIDTRANS_CLIENT_KEY)
                .build();
       return new ConfigFactory(coreApiConfigOptions).getCoreApi();
    }

}
