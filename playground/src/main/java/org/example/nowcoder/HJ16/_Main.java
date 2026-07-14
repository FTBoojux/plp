package org.example.nowcoder.HJ16;

import java.util.Scanner;

public class _Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        n /=10;
        int[][] shoppingList = new int[n][m];
        for (int i = 0; i < m; i++) {
            int v = sc.nextInt();
            int w = sc.nextInt();
            int q = sc.nextInt();
            if(q == 0){
                shoppingList[i][0] = v/10;
                shoppingList[i][1] = w;
            } else if(shoppingList[q][2] == 0){
                shoppingList[q][2] = v/10;

            }
        }
    }
}
