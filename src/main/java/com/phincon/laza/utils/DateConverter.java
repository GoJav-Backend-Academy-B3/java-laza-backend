package com.phincon.laza.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    public static LocalDateTime convertDateTime(String datetime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the string into a LocalDateTime object
       return LocalDateTime.parse(datetime, formatter);

    }
}
