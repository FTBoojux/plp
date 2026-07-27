package org.example.nowcoder.HJ57;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String num1 = in.nextLine();
        String num2 = in.nextLine();
        System.out.println(new BigInteger(num1).add(new BigInteger(num2)));
    }
}
