package org.example.leetcode.Spreadsheet;

public class Spreadsheet {
    int[][] sheet;
    int rows;
    public Spreadsheet(int rows) {
        sheet = new int[26][rows];
        this.rows = rows;
    }

    public void setCell(String cell, int value) {
        int cols = cell.charAt(0)-'A';
        int rows = Integer.parseInt(cell.substring(1));
        sheet[cols][rows] = value;
    }

    public void resetCell(String cell) {
        setCell(cell,0);
    }

    public int getValue(String formula) {
        int index = formula.indexOf('+');
        String param1 = formula.substring(1,index);
        String param2 = formula.substring(index+1);
        int num1 = getCell(param1);
        int num2 = getCell(param2);
        return num1 + num2;
    }
    private int getCell(String param){
        if (Character.isDigit(param.charAt(0))) {
            return Integer.parseInt(param);
        }
        int cols = param.charAt(0)-'A';
        int rows = Integer.parseInt(param.substring(1));
        return sheet[cols][rows];
    }
}

/**
 * Your Spreadsheet object will be instantiated and called as such:
 * Spreadsheet obj = new Spreadsheet(rows);
 * obj.setCell(cell,value);
 * obj.resetCell(cell);
 * int param_3 = obj.getValue(formula);
 */