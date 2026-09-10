package org.example.nowcoder.HJ62;
import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt(),
                b = in.nextInt();
        System.out.println(Integer.bitCount(a));
        System.out.println(Integer.bitCount(b));
    }
}
