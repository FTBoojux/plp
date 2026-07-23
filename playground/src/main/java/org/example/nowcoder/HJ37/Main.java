package org.example.nowcoder.HJ37;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        System.out.println(fib(num));
    }
    public static int fib(int num) {
        if(num == 1) {
            return 1;
        }
        if(num == 2) {
            return 1;
        }
        int a = 1, b = 1;
        for(int i = 2; i < num; ++i) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }
}
