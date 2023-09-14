package com.phincon.laza.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class GenerateRandomTest {
    @Test
    public void testToken() {
        String token = GenerateRandom.token();
        assertNotNull(token);
        assertEquals(32, token.length());

        log.info("[COMPLETE] testing generate random token then correct");
    }

    @Test
    public void testCode() {
        String code = GenerateRandom.code();
        assertNotNull(code);
        assertEquals(4, code.length());

        log.info("[COMPLETE] testing generate random code then correct");
    }
}
