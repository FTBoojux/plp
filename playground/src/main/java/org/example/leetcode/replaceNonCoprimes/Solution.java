package org.example.leetcode.replaceNonCoprimes;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<Integer> replaceNonCoprimes(int[] nums) {
        List<Integer> stack = new ArrayList<>();
        for (int num : nums) {
            while (!stack.isEmpty() && gcd(num,stack.getLast()) > 1) {
                num = lcm(num,stack.removeLast());
            }
            stack.add(num);
        }
        return stack;
    }

    private int lcm(int num1, int num2) {
        return num1 / gcd(num1,num2) * num2;
    }

    private int gcd(int num1, int num2) {
        while (num1 > 0) {
            int tmp = num1;
            num1 = num2 % num1;
            num2 = tmp;
        }
        return num2;
    }
}
