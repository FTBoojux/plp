package org.example.LetterHome;

import java.util.Scanner;

class Solution{

    public void solve(Scanner sc) {
        int n = sc.nextInt(),
                s = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        int left = arr[0],
                right = arr[n-1],
                ans = right - left;
        if(s <= left){
            ans = right - s;
        }else if(s >= right){
            ans = s - left;
        }else{
            ans += Math.min(right - s, s - left);
        }
        System.out.println(ans);
    }
}