package org.example.nowcoder.HJ42;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int x = Math.min(a, b);
        int y = Math.max(a, b);
        System.out.println(y + y-x);
    }
}
