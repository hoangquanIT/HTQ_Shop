package com.quanht.utils;

import java.util.Random;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";

    public static String generatePassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Add one lowercase letter
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));

        // Add one uppercase letter
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));

        // Add one number
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));

        // Add remaining characters randomly
        for (int i = 0; i < 3; i++) {
            String characters = LOWERCASE + UPPERCASE + NUMBERS;
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

}
