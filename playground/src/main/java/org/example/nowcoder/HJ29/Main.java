package org.example.nowcoder.HJ29;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            sb.append(encode(ch));
        }
        System.out.println(sb);
        String t = sc.nextLine();
        sb = new StringBuilder();
        for (char ch : t.toCharArray()) {
            sb.append(decode(ch));
        }
        System.out.println(sb);
    }

    private static char encode(char ch) {
        if (Character.isAlphabetic(ch)) {
            if (Character.isUpperCase(ch)) {
                ch = (char) ('a' + (ch - 'A' + 1) % 26);
            } else {
                ch = (char) ('A' + (ch - 'a' + 1) % 26);
            }
        }
        if(Character.isDigit(ch)){
            ch = (char)('0' + (ch - '0' + 1) % 10);
        }
        return ch;
    }
    private static char decode(char ch) {
        if (Character.isAlphabetic(ch)) {
            if (Character.isUpperCase(ch)) {
                ch = (char) ('a' + (ch - 'A' - 1 + 26) % 26);
            } else {
                ch = (char) ('A' + (ch - 'a' - 1 + 26) % 26);
            }
        }
        if(Character.isDigit(ch)){
            ch = (char)('0' + (ch - '0' - 1 + 10) % 10);
        }
        return ch;
    }
}
