package org.example.leetcode.rotateGrid;

import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    public int[][] rotateGrid(int[][] grid, int k) {
        int height =  grid.length;
        int width = grid[0].length;
        int[][] res = new int[height][width];
        for (int i = 0; 2*i < Math.min(height, width); ++i) {
            rotateGrid(grid, res, i, k);
        }
        return res;
    }
    private void rotateGrid(int[][] grid, int[][] res, int pos, int k) {
        int height = grid.length;
        int width = grid[0].length;
        int perimeter = 2 * (height - pos * 2 + width - pos * 2 - 2);
        k %= perimeter;
        Queue<Integer> queue = new ArrayDeque<>();
        for(int i = 0 + pos; i + pos < width; ++i) {
            queue.add(grid[pos][i]);
        }
        for(int i = pos+1; i + pos < height; ++i){
            queue.add(grid[i][width-1-pos]);
        }
        for(int i = width - 1 - pos - 1; i >= pos; --i) {
            queue.add(grid[height - 1 - pos][i]);
        }
        for(int i = height - 1 - pos - 1; i > pos; --i) {
            queue.add(grid[i][pos]);
        }
        for(int i = 0; i < k; ++i) {
            queue.add(queue.poll());
        }
        for(int i = 0 + pos; i + pos < width; ++i) {
            res[pos][i] = queue.poll();
        }
        for(int i = pos + 1; i + pos < height; ++i){
            res[i][width-1-pos] = queue.poll();
        }
        for(int i = width - 1 - pos - 1 ; i >= pos; --i) {
            res[height - 1 - pos][i] = queue.poll();
        }
        for(int i = height - 1 - pos - 1; i > pos; --i) {
            res[i][pos] = queue.poll();
        }
    }
}