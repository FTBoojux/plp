package org.example.nowcoder.HJ22;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){
            int number = sc.nextInt();
            if(number == 0){
                return;
            }
            solve(number);
        }
    }

    private static void solve(int number) {
        int ans = 0;
        while(number > 2){
            ans += number/3;
            number = (number/3)+number%3;
        }
        System.out.println(ans + (number == 2 ? 1 : 0));
    }
}
