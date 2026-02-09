package org.example.leetcode.balanceBST;

import org.example.utils.TreeNode;

import java.util.ArrayList;
import java.util.List;

class Solution {
    public TreeNode balanceBST(TreeNode root) {
        List<Integer> inorder = new ArrayList<>();
        inorderTraverse(inorder,root);
        int length = inorder.size();
        return buildBalancedBST(inorder,0,length-1);
    }

    private TreeNode buildBalancedBST(List<Integer> inorder, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = (start + end) / 2;
        TreeNode left = buildBalancedBST(inorder, start, mid-1);
        TreeNode right = buildBalancedBST(inorder, mid+1, end);
        return new TreeNode(inorder.get(mid), left, right);
    }

    private void inorderTraverse(List<Integer> inorder, TreeNode root) {
        if (root == null) {
            return;
        }
        inorderTraverse(inorder, root.left);
        inorder.add(root.val);
        inorderTraverse(inorder, root.right);
    }
}