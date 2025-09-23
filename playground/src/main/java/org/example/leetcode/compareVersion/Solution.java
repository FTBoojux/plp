package org.example.leetcode.compareVersion;

public class Solution {
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int i1 = 0, i2 = 0;
        while(i1 < v1.length && i2 < v2.length) {
            int n1 = Integer.parseInt(v1[i1]);
            int n2 = Integer.parseInt(v2[i2]);
            ++i1;
            ++i2;
            if (n1 > n2) {
                return 1;
            } else if (n1 < n2) {
                return -1;
            }
        }
        while (i1 < v1.length) {
            if (Integer.parseInt(v1[i1]) > 0) {
                return 1;
            }
            ++i1;
        }
        while (i2 < v2.length) {
            if (Integer.parseInt(v2[i2]) > 0) {
                return -1;
            }
            ++i2;
        }
        return 0;
    }
}