package org.example.codeforce.goodbye2025.A;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        int t = in.nextInt();
        Solution solution = new Solution();
        while (t-- > 0) {
            solution.solve(in);
        }
    }

    public void solve(Scanner in) {
        String s = in.next();
        int countY = 0;
        for (char ch : s.toCharArray()) {
            if (ch == 'Y') {
                countY++;
            }
        }
        if (countY > 1) {
            System.out.println("NO");
        } else {
            System.out.println("YES");
        }
    }
}
