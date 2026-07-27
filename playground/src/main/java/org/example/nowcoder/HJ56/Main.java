package org.example.nowcoder.HJ56;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if(n <= 6){
            System.out.println(1);
        }else if(n <= 28){
            System.out.println(2);
        }else if(n <= 496){
            System.out.println(3);
        }else if (n <= 8128){
            System.out.println(4);
        }
    }
}
