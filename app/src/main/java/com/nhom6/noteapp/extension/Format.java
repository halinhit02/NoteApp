package com.nhom6.noteapp.extension;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public final class Format {
    public static String formatDateTimeToString(String time, String date) {
        try {
            String dateTimeString = time + "-" + date;

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("H:m-dd/MM/yyyy", Locale.getDefault());

            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, inputFormatter);

            return DateTimeFormatter.ofPattern(Constance.DATE_TIME_FORMAT, Locale.getDefault()).format(localDateTime);

        } catch (DateTimeParseException e) {
            return DateTimeFormatter
                    .ofPattern(Constance.DATE_TIME_FORMAT, Locale.getDefault())
                    .format(LocalDateTime.of(1970, 1, 1, 0, 0));
        }
    }

    // từ 2 String -> HH:mm-dd/MM/yyyy
    public static LocalDateTime formatDateTimeToDate(String time, String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("H:m-dd/MM/yyyy", Locale.getDefault());
        try {
            String dateTimeString = time + "-" + date;
            return LocalDateTime.parse(dateTimeString, inputFormatter);

        } catch (DateTimeParseException e) {
            return LocalDateTime.of(1970, 1, 1, 0, 0);
        }
    }

    //từ LocalDateTime -> thành 1 chuỗi dd/MM/yyyy
    public static String formatDate(LocalDateTime localDateTime) {
        return DateTimeFormatter
                .ofPattern(Constance.DATE_FORMAT, Locale.getDefault())
                .format(localDateTime);
    }

    public static String sdf(Date date) {
        SimpleDateFormat d = new SimpleDateFormat(Constance.DATE_FORMAT);
        return d.format(date);
    }

    public static String formattedTime(int hourOfDay, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
    }
}
