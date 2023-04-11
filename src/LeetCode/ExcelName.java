package LeetCode;

/**
 * ymy
 * 2023/3/25 - 10 : 58
 **/

// leetCode168
// 给你一个整数 columnNumber ，返回它在 Excel 表中相对应的列名称。  A -> 1   B -> 2   AA -> 27   AB -> 28

public class ExcelName {


    // 别看这道题标的是简单，但实际没那么容易
    // 这道题目必须去看官方题解，认真看
    public static String convertToTitle(int columnNumber) {
        StringBuilder sb = new StringBuilder();
        while (columnNumber > 0){
            int a = (columnNumber - 1) % 26 + 1;
            sb.append((char) (a - 1 + 'A'));
            columnNumber = (columnNumber - a) / 26;
        }
        return sb.reverse().toString();
    }


    public static void main(String[] args) {
        System.out.println(convertToTitle(28));
    }
}
