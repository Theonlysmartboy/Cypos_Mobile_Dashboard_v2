package com.cybene.cyposdashboard.utils;

import java.util.regex.Pattern;

public class KeyGenerator {
    private Pattern pattern;

    public static String generateKey(int length){
        StringBuilder key = new StringBuilder();
        for(int i = 0; i<length; i++){
            key.append(generateRandom(4));
            if (i<length-1)
                key.append("-");
        }
        return key.toString();
    }
    // function to generate a random string of length n
    public static String generateRandom(int n)
    {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString .charAt(index));
        }
        return sb.toString();
    }
}
