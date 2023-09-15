package com.phincon.laza.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    public static LocalDateTime convertDateTime(String datetime){
        // Define a DateTimeFormatter to parse the string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        String newString = datetime.substring(0,23);

        // Replace 'Z' with '' and parse the string
        return LocalDateTime.parse(newString.replace("Z", ""), formatter);
    }



}
