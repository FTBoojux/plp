package org.example.utils;

import java.util.Random;

public final class StringUtils {
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String alphanumeric = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new Random(System.currentTimeMillis());
    private StringUtils(){

    }
    public static String randomStringFromAlphabet(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }
    public static String randomStringFromAlphanumeric(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }
        return stringBuilder.toString();
    }
    public static String randomString(int length){
        return randomStringFromAlphanumeric(length);
    }

    /**
     * return a random string whose length is between 0 and 32
     * @return randomString
     */
    public static String randomString(){
        return randomStringFromAlphanumeric(random.nextInt(1,32));
    }

    public static boolean equals(String str1, String str2){
        if(str1 == null && str2 == null){
            return true;
        }
        if(str1 == null || str2 == null){
            return false;
        }
        if(str1.isEmpty() && str2.isEmpty()){
            return true;
        }
        if(str1.isEmpty() || str2.isEmpty()){
            return false;
        }
        return str1.equals(str2);
    }
}
