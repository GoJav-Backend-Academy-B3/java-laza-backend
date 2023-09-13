package com.phincon.laza.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    public static LocalDateTime convertDateTime(String datetime){

        // Define a DateTimeFormatter to parse the string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        // Replace 'Z' with '' and parse the string
        return LocalDateTime.parse(datetime.replace("Z", ""), formatter);
    }
}
