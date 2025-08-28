package org.example.leetcode.SortMatrixbyDiagonals3446;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Solution {
    public int[][] sortMatrix(int[][] grid) {
        int m = grid.length,
                n = grid[0].length;
        int[][] ans = new int[m][n];
        PriorityQueue<Integer> maxPq = new PriorityQueue<>(Comparator.comparingInt(Integer::intValue));
        PriorityQueue<Integer> minPq = new PriorityQueue<>(Comparator.comparingInt(i->-i));
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m && i+j < n; j++) {
                minPq.add(grid[j][i+j]);
            }
            for (int j = 0; j < m && i + j < n; j++) {
                ans[j][i+j] = minPq.poll();
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n && i + j < m; j++) {
                maxPq.add(grid[i+j][j]);
            }
            for (int j = 0; j < n && i + j < m; j++) {
                ans[i+j][j] = maxPq.poll();
            }
        }
        return ans;
    }
}