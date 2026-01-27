package org.example.leetcode.minCost;

import java.util.*;

class Solution {
    public int minCost(int n, int[][] edges) {
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int start = edge[0],
                    end = edge[1],
                    weight = edge[2];
            graph.get(start).add(new int[]{end,weight});
            graph.get(end).add(new int[]{start, weight*2});
        }
        int[] minCost = new int[n];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(0);
        minCost[0] = 0;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            List<int[]> neighbours = graph.get(cur);
            for (int[] neighbour : neighbours) {
                int cost = minCost[cur] + neighbour[1];
                if (cost < minCost[neighbour[0]]) {
                    minCost[neighbour[0]] = cost;
                    queue.add(neighbour[0]);
                }
            }
        }
        return minCost[n-1] == Integer.MAX_VALUE ? -1 : minCost[n-1];
    }
}