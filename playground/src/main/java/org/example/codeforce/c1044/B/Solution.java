package org.example.codeforce.c1044.B;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        int n = in.nextInt();
        List<Integer> g = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            g.add(in.nextInt());
        }
        g.sort(Comparator.comparingInt(i->-i));
        long ans = 0;
        for (int i = 0; i < n; i+=2) {
            ans += g.get(i);
        }
        System.out.println(ans);
    }
}
