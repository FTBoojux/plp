package org.example.leetcode.minimumTeachings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution {
    public int minimumTeachings(int n, int[][] languages, int[][] friendships) {
        Set<Integer> poepleNeedToLearnLanguage = new HashSet<>();
        for (int[] friendship : friendships) {
            Set<Integer> firstPeoplesLanguage = new HashSet<>();
            boolean hasSameLanguage = false;
            for (int language : languages[friendship[0] - 1]) {
                firstPeoplesLanguage.add(language);
            }
            for (int language : languages[friendship[1] - 1]) {
                if (firstPeoplesLanguage.contains(language)) {
                    hasSameLanguage = true;
                    break;
                }
            }
            if (!hasSameLanguage) {
                poepleNeedToLearnLanguage.add(friendship[0]-1);
                poepleNeedToLearnLanguage.add(friendship[1]-1);
            }
        }
        int max = 0;
        int[] cnt = new int[n];
        for (Integer people : poepleNeedToLearnLanguage) {
            for (int language : languages[people]) {
                ++cnt[language-1];
                max = Math.max(max,cnt[language-1]);
            }
        }
        return poepleNeedToLearnLanguage.size()-max;
    }
}