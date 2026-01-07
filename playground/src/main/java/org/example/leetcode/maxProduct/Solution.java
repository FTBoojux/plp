package org.example.leetcode.maxProduct;

import org.example.utils.TreeNode;

import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    private long ans = 0;
    long sum = 0;
    public int maxProduct(TreeNode root) {
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            sum += cur.val;
            if(cur.left != null) {
                queue.add(cur.left);
            }
            if(cur.right != null) {
                queue.add(cur.right);
            }
        }
        postOrderTraverse(root);
        return (int) (ans % 1_000_000_007);
    }
    private long postOrderTraverse(TreeNode root){
        long cur = root.val;
        if(root.left == null && root.right == null) {
            return cur;
        }
        if(root.left != null) {
            long leftSum = postOrderTraverse(root.left);
            ans = Math.max(ans,(sum-leftSum) * leftSum);
            cur += leftSum;
        }
        if(root.right != null) {
            long rightSum = postOrderTraverse(root.right);
            ans = Math.max(ans,(sum-rightSum) * rightSum);
            cur += rightSum;
        }
        return cur;
    }
}