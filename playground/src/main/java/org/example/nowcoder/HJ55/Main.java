package org.example.nowcoder.HJ55;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int cnt = 0;
        for(int i = 1; i < num; ++i){
            if(i%7 == 0 || String.valueOf(i).contains("7")){
                ++cnt;
            }
        }
        System.out.println(cnt);
    }
}
