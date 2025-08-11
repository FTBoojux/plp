package org.example.leetcode.productQueries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    private static final int MOD = 1_000_000_007;
    public int[] productQueries(int n, int[][] queries) {
        int length = queries.length;
        List<Integer> bits = new ArrayList<>();
        int bit = 1;
        while(bit <= n){
            if((bit & n) > 0){
                bits.add(bit);
            }
            bit <<= 1;
        }
        int m = bits.size();
        int[][] cache = new int[m][m];
        for (int i = 0; i < m; i++) {
            int product = 1;
            for (int j = i; j < m; j++) {
                product = (int)((long)product*bits.get(j)%MOD);
                cache[i][j] = product;
            }
        }
        int[] ans = new int[length];
        for (int i = 0; i < length; i++) {
            ans[i] = cache[queries[i][0]][queries[i][1]];
        }
        return ans;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.productQueries(15, new int[][]{{0, 1}, {2, 2}, {0, 3}})));
    }
}