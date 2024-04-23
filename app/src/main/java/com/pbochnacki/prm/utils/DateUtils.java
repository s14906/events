package com.pbochnacki.prm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static String dateFormat = "dd/MM/yyyy";
    private static SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);


    public static void prepareCalendar(Calendar cal, int year, int month, int day) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
    }

    public static String formatDate(Date date) {
        return sdf.format(date);
    }

    public static Date parseDate(String date){
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(date + " cannot be parsed.");
        }
    }

}
