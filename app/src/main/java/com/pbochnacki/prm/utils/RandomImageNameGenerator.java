package com.pbochnacki.prm.utils;

import java.util.UUID;

public class RandomImageNameGenerator {
    private static String imageFileName;

    public static void generateRandomImageFileName(){
        imageFileName = String.valueOf(UUID.randomUUID());
    }

    public static String getImageFileName() {
        return imageFileName;
    }
}
