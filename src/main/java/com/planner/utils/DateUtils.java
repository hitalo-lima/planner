package com.planner.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("A data passada não pode ser nula");
        }

        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static LocalDateTime parseISODateTime(String isoDateTime) {
        if (isoDateTime == null) {
            throw new IllegalArgumentException("A data passada não pode ser nula");
        }
        return LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static boolean isDateBetween(LocalDateTime dateToCheck, LocalDateTime startDate, LocalDateTime endDate) {
        if (dateToCheck == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("As datas passadas não podem ser nulas");
        }

        return !dateToCheck.isBefore(startDate) && !dateToCheck.isAfter(endDate);
    }
}
