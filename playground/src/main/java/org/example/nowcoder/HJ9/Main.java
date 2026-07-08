package org.example.nowcoder.HJ9;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner in  = new Scanner(System.in);
        int num = in.nextInt();
        Set<Integer> set = new HashSet<>();
        while(num > 0){
            int cur = num % 10;
            if(!set.contains(cur)){
                set.add(cur);
                System.out.print(cur);
            }
            num = num / 10;
        }
    }
}
