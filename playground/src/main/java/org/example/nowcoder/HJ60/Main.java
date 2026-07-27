package org.example.nowcoder.HJ60;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for(int i =n/2; i > 0; --i){
            int j = n - i;
            if(isPrime(i) && isPrime(j)){
                System.out.println(i);
                System.out.println(j);
                return;
            }
        }
    }
    public static boolean isPrime(int num){
        if(num <= 3){
            return true;
        }
        for(int i = 2; i * i <= num; ++i){
            if(num % i == 0) {
                return false;
            }
        }
        return true;
    }
}