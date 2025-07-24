package org.example.codeforce.DifficultContest;

import java.util.Arrays;
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
        String str = in.next();
        char[] charArray = str.toCharArray();
        Arrays.sort(charArray);
        StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            sb.insert(0,c);
        }
        System.out.println(sb.toString());
    }
}
