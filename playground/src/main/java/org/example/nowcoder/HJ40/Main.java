package org.example.nowcoder.HJ40;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int alphabetic = 0;
        int whitespace = 0;
        int number = 0;
        int others = 0;
        for(char ch : s.toCharArray()) {
            if(Character.isAlphabetic(ch)) {
                ++alphabetic;
            }else if(Character.isWhitespace(ch)){
                ++whitespace;
            }else if(Character.isDigit(ch)){
                ++number;
            }else{
                ++others;
            }
        }
        System.out.println(alphabetic);
        System.out.println(whitespace);
        System.out.println(number);
        System.out.println(others);
    }
}
