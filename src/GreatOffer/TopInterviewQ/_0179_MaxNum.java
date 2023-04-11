package GreatOffer.TopInterviewQ;

import java.util.Arrays;
import java.util.Comparator;

/**
 * ymy
 * 2023/3/13 - 21 : 31
 **/

// 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
// 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
// 输入：nums = [3,30,34,5,9]   输出："9534330"

// 1 <= nums.length <= 100
// 0 <= nums[i] <= 10^9

public class _0179_MaxNum {

    public String largestNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return "";
        }
        // 将每个数字转换成字符串
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        // 自定义比较规则
        Arrays.sort(strs, new Comparator<String>() {
            public int compare(String s1, String s2) {
                String order1 = s1 + s2;
                String order2 = s2 + s1;
                return order2.compareTo(order1);
            }
        });
        // 特殊情况处理
        if (strs[0].equals("0")) {
            return "0";
        }
        // 将排序后的字符串连接起来
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }
}
