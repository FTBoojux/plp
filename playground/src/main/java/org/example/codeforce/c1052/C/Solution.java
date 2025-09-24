package org.example.codeforce.c1052.C;

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
        in.nextLine();
        char[] s = in.nextLine().toCharArray();
        int[] ans = new int[n];
        if ((s[0] == '0' && s[1] == '1') || (s[n-1] == '0' && s[n-2] == '1')) {
            System.out.println("NO");
            return;
        }
        for (int i = 0; i < n; i++) {
            ans[i] = i+1;
        }
        for (int i = 0; i < n; i++) {
            if (s[i] == '1') {
                continue;
            }
            if ( i > 0 && s[i] == '0' && s[i-1] == '1' && s[i+1] == '1') {
                System.out.println("NO");
                return;
            }
            if (s[i] == '0' && i+1 < n && s[i+1] == '0' ) {
                int tmp = ans[i+1];
                ans[i+1] = ans[i];
                ans[i] = tmp;
            }
        }
        System.out.println("YES");
        for (int an : ans) {
            System.out.print(an);
            System.out.print(' ');
        }
        System.out.println();
    }
}
