package org.example.nowcoder.HJ61;

import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int m = in.nextInt(),
                n = in.nextInt();
        System.out.println(countWays(m,n));
    }
    public static int countWays(int m, int n) {
//        1.
//苹果比盘子少 (m < n)：    既然苹果不够，至少有 n - m 个盘子一定是空的。所以这种情况等价于把 m 个苹果放到 m 个盘子里，即：    f(m, n) = f(m, m)
//2.
//苹果大于或等于盘子 (m >= n)：    分为两种互斥情况，将它们相加：
//◦
//至少有一个盘子空着：相当于把 m 个苹果放入 n - 1 个盘子，即 f(m, n - 1)。
//◦
//所有盘子都不空：我们可以先给每个盘子放 1 个苹果（消耗 n 个苹果），剩下 m - n 个苹果再放入 n 个盘子中，即 f(m - n, n)。
        if ( m == 0 ) {
            return 1;
        }
        if ( n == 0 ) {
            return 0;
        }
        if(m < n) {
            return countWays(m,m);
        }else{
            return countWays(m, n - 1) + countWays(m - n, n);
        }
    }
}