package com.inforsecure.fiuapi.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static String getCurrentTimeStamp(){
        return LocalDateTime.now().format(formatter);
    }

    public static String getPeriodicTimeStamp(int numOfMonths){
            return LocalDateTime.now().plusMonths(numOfMonths).format(formatter);
    }
}
