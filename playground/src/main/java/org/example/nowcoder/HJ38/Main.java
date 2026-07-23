package org.example.nowcoder.HJ38;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        float h = sc.nextFloat();
        float ans = 0;
        for(int i = 0; i < 5; ++i){
            ans += h * 2;
            if (i == 0){
                ans -= h;
            }
            h /=2;
        }
        System.out.println(ans);
        System.out.println(h);
    }
}
