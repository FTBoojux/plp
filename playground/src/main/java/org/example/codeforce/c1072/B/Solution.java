package org.example.codeforce.c1072.B;

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
        int s = in.nextInt(),
                k = in.nextInt(),
                m = in.nextInt();
        System.out.println(getAns(s,k,m));
    }
    public int getAns(int s, int k, int m) {
        // 处于上部分的沙子数量有两种情况
        // 1.全部的沙子 s
        // 2.等于持续时间的沙子 k
        // 2的情况只有一种
        // 持续时间小于于全部沙子的时间，而且此时沙漏是倒置
        // 对于情况1，时间就是s-k
        // 对于情况2，flips必须为奇数，且k < s
        int flips = m / k;
        int remain = m % k;
        if(flips % 2 == 1 && k < s) {
            return Math.max(0,k-remain);
        }
        return Math.max(0,s-remain);
    }
}
