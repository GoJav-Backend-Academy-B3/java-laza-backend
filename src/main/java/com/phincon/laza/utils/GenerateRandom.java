package com.phincon.laza.utils;

import java.util.Random;
import java.util.UUID;

public class GenerateRandom {
    public static String token() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String code() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.valueOf(randomNumber);
    }

    public static String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            sb.append(digit);
        }

        return sb.toString();
    }

    public static String username(String email) {
        String[] split = email.split("@");

        return split[0].replaceAll("[^a-zA-Z0-9]", "_");
    }
}
