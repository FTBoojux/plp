package org.example.leetcode.latestDayToCross;

import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    private static final int[][] MOVE = {{0,-1},{0,1},{-1,0},{1,0}};
    public int latestDayToCross(int row, int col, int[][] cells) {
        int days = cells.length;
        int[][] matrix = new int[row][col];
        for(int i = 0; i < days; ++i){
            int x = cells[i][0]-1,
                    y = cells[i][1]-1;
            matrix[x][y] = i+1;
        }
        int left = 0,
                right = days;
        while (left <= right) {
            int mid = (right-left)/2+left;
            if(canCross(matrix,mid)) {
                left = mid+1;
            } else {
                right = mid-1;
            }
        }
        return left-1;
    }
    public boolean canCross(int[][] matrix, int days) {
        int rows = matrix.length,
                cols = matrix[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < cols; i++) {
            if (matrix[0][i] > days) {
                queue.add(new int[]{0,i});
            }
        }
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            if (cur[0] == rows-1) {
                return true;
            }
            for (int[] move : MOVE) {
                int nx = cur[0] + move[0],
                        ny = cur[1] + move[1];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols
                    && !visited[nx][ny] && matrix[nx][ny] > days
                ) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx,ny});
                }
            }
        }
        return false;
    }
}