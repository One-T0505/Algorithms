package LeetCode;

/**
 * ymy
 * 2023/3/25 - 14 : 10
 **/

// leetCode258
// 给定一个非负整数 num，反复将各个位上的数字相加，直到结果为一位数。返回这个结果。

public class AddDigits {

    // 记得去看官方题目描述
    public static int addDigits(int num) {
        if (num < 10)
            return num;
        while (num >= 10){
            num = sum(num);
        }
        return num;
    }


    // 计算 num 各位之和
    private static int sum(int num) {
        int res = 0;
        while (num != 0){
            res += num % 10;
            num /= 10;
        }
        return res;
    }



    // 尝试看能不能用 O(1) 的时间复杂度来解决
    // 用打表法就能发现这个规律
    public static int addDigits2(int num) {
        return num == 0 ? 0 : (num % 9 == 0 ? 9 : num % 9);
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(i + ":\t" + addDigits(i));
        }
    }
}
