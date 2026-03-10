package org.example.codeforce.c1085.A;

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
        int n = in.nextInt();
        String s = in.next();
        char[] charArray = s.toCharArray();
        int max = 0, min = 0;
        for(int i = 0; i < n; ++i) {
            if(i > 0 && i < n-1 && charArray[i-1] == '1' && charArray[i+1] == '1') {
                charArray[i] = '1';
            }
            if (charArray[i] == '1') {
                ++max;
            }
        }
        for(int i = 0; i < n; ++i) {
            if(i > 0 && i < n-1 && charArray[i-1] == '1' && charArray[i+1] == '1') {
                charArray[i] = '0';
            }
            if (charArray[i] == '1') {
                ++min;
            }
        }
        System.out.println(min + " " + max);
    }
}
