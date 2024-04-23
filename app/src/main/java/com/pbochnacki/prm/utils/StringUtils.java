package com.pbochnacki.prm.utils;

public class StringUtils {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.equals("");
    }

    public static boolean longerThan(String input, int value) {
        return input.length() >= value;
    }
}

