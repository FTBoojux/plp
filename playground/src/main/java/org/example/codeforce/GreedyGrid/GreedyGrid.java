package org.example.codeforce.GreedyGrid;

import java.util.Scanner;

public class GreedyGrid {
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        int t = in.nextInt();
        GreedyGrid solution = new GreedyGrid();
        while (t-- > 0) {
            solution.solve(in);
        }
    }

    public void solve(Scanner in) {
        int n = in.nextInt(),
                m = in.nextInt();
        if(n == 1 || m == 1 || (n == 2 && m == 2)){
            System.out.println("NO");
        }else{
            System.out.println("YES");
        }
    }
}
