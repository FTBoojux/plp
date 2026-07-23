package org.example.nowcoder.HJ41;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int[] m = new int[num];
        int[] x = new int[num];
        for (int i = 0; i < num; i++) {
            m[i] = sc.nextInt();
        }
        for(int i = 0; i < num; ++i) {
            x[i] = sc.nextInt();
        }
        Set<Integer> set = new HashSet<>();
        set.add(0);
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < x[i]; j++) {
                Set<Integer> copyOf = new HashSet<>(set);
                for (Integer integer : copyOf) {
                    set.add(integer + m[i]);
                }
            }
        }
        System.out.println(set.size());
    }
}
