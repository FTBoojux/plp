package org.example.codeforce.BlackboardGame;

import java.util.Scanner;

public class BlackboardGame {
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        int t = in.nextInt();
        Solution solution = new Solution();
        while (t-- > 0) {
            solution.solve(in);
        }
    }

    public static class Solution {
        public void solve(Scanner in) {
            int n = in.nextInt();
            if(n % 4 == 0){
                System.out.println("BOB");
            }else{
                System.out.println("ALICE");
            }
        }
    }
}
