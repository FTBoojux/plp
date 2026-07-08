package org.example.nowcoder.HJ11;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        StringBuilder sb = new StringBuilder(line);
        System.out.println(sb.reverse());
    }
}
