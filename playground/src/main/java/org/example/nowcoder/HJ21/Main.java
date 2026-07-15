package org.example.nowcoder.HJ21;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String password = in.nextLine();
        System.out.println(convert(password));
    }
    private static String convert(String password) {
        StringBuilder sb = new StringBuilder();
        for(char ch : password.toCharArray()){
            if(Character.isLowerCase(ch)){
                sb.append(convertLowerCase(ch));
            }else if(Character.isUpperCase(ch)){
                // convert to lower case and add 1 and MOD A
                ch = (char)(((Character.toLowerCase(ch) - 'a' + 1) % 26) + 'a');
                sb.append(ch);
            }else{
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    private static char convertLowerCase(char ch){
        switch (ch) {
            // convert to number by 3x3 grid keyboard
            case 'a' : return '2';
            case 'b' : return '2';
            case 'c' : return '2';
            case 'd' : return '3';
            case 'e' : return '3';
            case 'f' : return '3';
            case 'g' : return '4';
            case 'h' : return '4';
            case 'i' : return '4';
            case 'j' : return '5';
            case 'k' : return '5';
            case 'l' : return '5';
            case 'm' : return '6';
            case 'n' : return '6';
            case 'o' : return '6';
            case 'p' : return '7';
            case 'q' : return '7';
            case 'r' : return '7';
            case 's' : return '7';
            case 't' : return '8';
            case 'u' : return '8';
            case 'v' : return '8';
            case 'w' : return '9';
            case 'x' : return '9';
            case 'y' : return '9';
            case 'z' : return '9';
        }
        throw new RuntimeException();
    }
}
