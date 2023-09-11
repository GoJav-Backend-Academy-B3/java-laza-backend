package com.phincon.laza.config;

import com.xendit.XenditClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XenditConfig {
    @Bean
    public XenditClient xenditClient() {
        return new XenditClient.Builder()
                .setApikey("xnd_development_ARKvHGZFnj1BlGLgUn6jVFJauA6MmY6dbcwxUOYU8s8fyhbQ5zyaszBcRo17j0cf")
                .build();
    }

}
