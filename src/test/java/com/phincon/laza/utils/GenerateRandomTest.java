package com.phincon.laza.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateRandomTest {
    @Test
    public void testToken() {
        String token = GenerateRandom.token();
        assertNotNull(token);
        assertEquals(32, token.length());
    }

    @Test
    public void testCode() {
        String code = GenerateRandom.code();
        assertNotNull(code);
        assertTrue(code.matches("\\d{4}"));
    }
}
