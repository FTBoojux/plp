package org.example.codeforce.SubmissionisAllYouNeed;

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
        int ans = 0;
        int n = in.nextInt();
        while(n-- > 0){
            int s = in.nextInt();
            ans += Math.max(s,1);
        }
        System.out.println(ans);
    }
}
