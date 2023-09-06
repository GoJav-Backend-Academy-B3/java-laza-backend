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
        int min = 100000;
        int max = 999999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.valueOf(randomNumber);
    }
}
