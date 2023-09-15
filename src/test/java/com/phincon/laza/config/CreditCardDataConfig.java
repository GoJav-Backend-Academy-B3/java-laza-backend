package com.phincon.laza.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.phincon.laza.model.entity.CreditCard;


@TestConfiguration
public class CreditCardDataConfig {

    @Bean
    @Qualifier("cc.all")
    public List<CreditCard> creditCardAll() {
        return Arrays.asList(
            new CreditCard("cc01", "2739952966392831", 2, 25, null),
            new CreditCard("cc02", "2503234676183548", 3, 24, null),
            new CreditCard("cc03", "3881845703890574", 10, 24, null)
        );
    }

    @Bean
    @Qualifier("cc.one")
    public CreditCard creditCardOne() {
        return new CreditCard("cc009", "5391887052246895", 4, 26, null);
    }
}
