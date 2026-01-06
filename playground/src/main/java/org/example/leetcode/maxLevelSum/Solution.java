package org.example.leetcode.maxLevelSum;

import org.example.utils.TreeNode;

import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    public int maxLevelSum(TreeNode root) {
        Queue<TreeNode> queue = new ArrayDeque<>();
        int maxSum = root.val-1;
        int minLevel = -1;
        queue.add(root);
        int level = 0;
        while (!queue.isEmpty()){
            ++level;
            int sumOfLevel = 0;
            for(int i = queue.size(); i > 0; --i) {
                TreeNode cur = queue.poll();
                sumOfLevel += cur.val;
                if(cur.left != null){
                    queue.add(cur.left);
                }
                if(cur.right != null){
                    queue.add(cur.right);
                }
            }
            if(sumOfLevel > maxSum){
                minLevel = level;
                maxSum = sumOfLevel;
            }
        }
        return minLevel;
    }
}