package org.example.nowcoder.HJ7;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String decimal = in.nextLine();
        BigDecimal bigDecimal = new BigDecimal(decimal);
        bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
        System.out.println(bigDecimal);
    }
}
