package org.example.nowcoder.HJ4;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            sb.append(line.charAt(i));
            if(sb.length() == 8){
                System.out.println(sb);
                sb.setLength(0);
            }
        }
        while(!sb.isEmpty() && sb.length() < 8){
            sb.append('0');
        }
        if(sb.length() == 8){
            System.out.println(sb);
        }
    }
}
