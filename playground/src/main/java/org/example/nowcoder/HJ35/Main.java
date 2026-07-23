package org.example.nowcoder.HJ35;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int num = 1;
        int[][] arr = new int[n][n];
        for (int[] ints : arr) {
            Arrays.fill(ints, -1);
        }
        for (int i = 0; i < n; ++i) {
            for(int j = 0; j <= i; ++j) {
                arr[i-j][j] = num;
                ++num;
            }
        }
        for (int[] ints : arr) {
            for(int i = 0; i < ints.length; ++i){
                if(ints[i] == -1){
                    break;
                }else{
                    System.out.print(ints[i]);
                    if(!(i == ints.length-1 || ints[i+1] == -1)) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }
}
