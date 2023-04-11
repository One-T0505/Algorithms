package LeetCode;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * ymy
 * 2023/3/23 - 13 : 54
 **/

// leetCode6
// 将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。
// 比如输入字符串为 "PAYPALISHIRING" 行数为 3 时，排列如下：

public class ZigzagConversion {

    // 这个题意比较难理解，建议去看下题目描述。
    public static String convert(String s, int numRows) {
        if (s == null || s.length() == 0 || numRows <= 1)
            return s;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int unit = (numRows << 1) - 2;
        TreeMap<Integer, ArrayList<Character>> dp = new TreeMap<>();
        for (int i = 0; i < N; i++) {
            int mod = i % unit;
            int target = mod < numRows ? mod : unit - mod;
            if (!dp.containsKey(target))
                dp.put(target, new ArrayList<>());
            dp.get(target).add(chs[i]);
        }

        StringBuilder sb = new StringBuilder();
        for (int key : dp.keySet()){
            ArrayList<Character> list = dp.get(key);
            for (char c : list)
                sb.append(c);
        }
        return sb.toString();
    }




    public static String convert2(String s, int numRows) {
        if (s == null || s.length() == 0 || numRows <= 1)
            return s;
        char[] chs = s.toCharArray();
        int N = chs.length;
        char[] res = new char[N];
        boolean[] visited = new boolean[N];
        int p = 0;
        int unit = (numRows << 1) - 2;
        for (int k = 0; k < numRows; k++){
            for (int i = 0; i < N; i++) {
                if (!visited[i] && (i % unit == k) || (i % unit == unit - k)){
                    res[p++] = chs[i];
                    visited[i] = true;
                }
            }
        }
        return String.valueOf(res);
    }



    // 最优解
    public static String convert3(String s, int numRows) {
        if (s == null || s.length() == 0 || numRows <= 1)
            return s;
        char[] chs = s.toCharArray();
        int N = chs.length;
        char[] res = new char[N];
        int[] pos = new int[numRows]; // pos[i]表示调整后的第i行有多少个字符
        int unit = (numRows << 1) - 2;
        int mod = 0;
        int target = 0;
        for (int i = 0; i < N; i++) {
            mod = i % unit;
            target = mod < numRows ? mod : unit - mod;
            pos[target]++;
        }
        // 现在我们要改造pos数组，让pos[i]表示第i行应该从res的上面位置开始填。比如第0行有5个元素，那么
        // pos[1]就应该为5
        int pre = pos[0];
        pos[0] = 0;
        for (int i = 1; i < numRows; i++) {
            int tmp = pos[i];
            pos[i] = pos[i - 1] + pre;
            pre = tmp;
        }

        for (int i = 0; i < N; i++) {
            mod = i % unit;
            target = mod < numRows ? mod : unit - mod;
            res[pos[target]++] = chs[i];
        }
        return String.valueOf(res);
    }


    public static void main(String[] args) {
        String s = "PAYPALISHIRING";
        int numRows = 4;
        System.out.println(convert(s, numRows).equals("PINALSIGYAHRPI"));
    }
}
