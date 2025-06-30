package org.example.LetterHome;//package org.example.LongestHarmoniousSubsequence594;

import java.util.Scanner;

public class LetterHome {
    public static void main(String[] args) {
        int t;
        Solution solution = new Solution();
        Scanner sc = new Scanner(System.in);
        t = sc.nextInt();
        while (t-- > 0) {
            solution.solve(sc);
        }
    }
}

