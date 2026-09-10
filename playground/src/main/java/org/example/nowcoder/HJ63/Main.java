package org.example.nowcoder.HJ63;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String DNA = in.next();
        int n = in.nextInt();
        int index = -1;
        float max_gc_ratio = 0;
        int gc_count = 0;
        for(int left = 0,right = 0;right < DNA.length(); ++right){
            char cur = DNA.charAt(right);
            if (cur == 'G' || cur == 'C') {
               ++gc_count;
            }
            if(right-left+1 < n){
                continue;
            }
            while(right-left+1 > n){
                char first_char = DNA.charAt(left);
                if(first_char == 'G' || first_char == 'C'){
                    --gc_count;
                }
                ++left;
            }
            float gc_ratio = (float) gc_count /n;
            if(gc_ratio > max_gc_ratio){
                max_gc_ratio = gc_ratio;
                index = left;
            }
        }
        System.out.println(DNA.substring(index, index + n));
    }
}
