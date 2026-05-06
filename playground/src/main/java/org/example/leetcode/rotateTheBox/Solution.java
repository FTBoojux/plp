package org.example.leetcode.rotateTheBox;

import java.util.Arrays;

class Solution {
    public char[][] rotateTheBox(char[][] boxGrid) {
        for(int i = 0; i < boxGrid.length; i++){
            for(int left = 0, right = 0; right < boxGrid[i].length; ++right) {
                if(boxGrid[i][right] == '*' || right == boxGrid[i].length - 1) {
                    int end = boxGrid[i][right] == '*' ? right : right+1;
                    Arrays.sort(boxGrid[i], left, end);
                    reverse(boxGrid[i], left, end-1);
                    left = right + 1;
                }
            }
        }
        return rotate(boxGrid);
    }

    private void reverse(char[] chars, int left, int end) {
        while (left < end) {
            char temp = chars[left];
            chars[left] = chars[end];
            chars[end] = temp;
            left++;
            end--;
        }
    }

    public char[][] rotate(char[][] boxGrid) {
        int originHeight = boxGrid.length;
        int originWidth = boxGrid[0].length;
        char[][] result = new char[originWidth][originHeight];
        for (int i = 0; i < originHeight; i++) {
            for (int j = 0; j < originWidth; j++) {
                result[j][originHeight - i - 1] = boxGrid[i][j];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // boxGrid = [["#",".","*","."],["#","#","*","."]]
        Solution solution = new Solution();
        char[][] boxGrid = new char[][]{
                {'#','.','*','.'},{'#','#','*','.'}
        };
        char[][] result = solution.rotateTheBox(boxGrid);
        System.out.println(Arrays.deepToString(result));
        // boxGrid = [["#",".","#"]]
        boxGrid = new char[][]{
                {'#','.','#'}
        };
        result = solution.rotateTheBox(boxGrid);
        System.out.println(Arrays.deepToString(result));
    }
}