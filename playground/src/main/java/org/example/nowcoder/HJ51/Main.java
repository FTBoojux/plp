package org.example.nowcoder.HJ51;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            int n = sc.nextInt();
            int[] vals = new int[n];
            for(int i = 0; i < n; ++i){
                vals[i] = sc.nextInt();
            }
            int k = sc.nextInt();
            System.out.println(vals[n-k]);
        }
    }
}
