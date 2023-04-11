package LeetCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * ymy
 * 2023/3/25 - 12 : 12
 **/

// leetCode205
// 给定两个字符串 s 和 t ，判断它们是否是同构的。
// 如果 s 中的字符可以按某种映射关系替换得到 t ，那么这两个字符串是同构的。
// 每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。不同字符不能映射到同一个字符上，
// 相同字符只能映射到同一个字符上，字符可以映射到自己本身。

public class IsomorphicStrings {

    // 这道题目和之前做过的一道题目很类似，但是稍微简单一点。
    // 这道题目的题意写的不是很清楚，但是官方给了例子：
    //   输入：s = "paper", t = "title"
    //   输出：true
    // s中的e映射到了t中的l，但是来到r时，此时又让r映射到了e，并且结果返回的是true，这说明题目说的映射不是双向映射
    // 而是单向映射，只要s中不存在这种映射，就不算。
    public static boolean isIsomorphic(String s, String t) {
        if (s == null || t == null || s.length() != t.length() || s.length() == 0)
            return false;
        // 执行到这里说明 s和t的长度相等并且不为0
        char[] ori = s.toCharArray();
        char[] des = t.toCharArray();
        // 主要作用是记录s中哪些字符有映射
        int[] dp = new int[256];
        Arrays.fill(dp, -1);
        // 这个的主要作用是记录t中哪些字符被映射了
        int[] isMapped = new int[256];
        Arrays.fill(isMapped, -1);
        return dfs(ori, des, 0, dp, isMapped);
    }

    private static boolean dfs(char[] ori, char[] des, int i, int[] dp, int[] isMapped) {
        if (i == ori.length)
            return true;
        // 执行到这里说明两个字符串都还没用完
        // 这个if表明，s之前就有相同的字符到别的映射了
        if (dp[ori[i]] != -1 && dp[ori[i]] != des[i])
            return false;
        // 这个if表明，虽然s[i]没有映射，但是目标字符t[j]已经被别的字符映射了
        if (dp[ori[i]] == -1 && isMapped[des[i]] != -1)
            return false;

        if (dp[ori[i]] == -1){
            dp[ori[i]] = des[i];
            isMapped[des[i]] = 1;
        }
        return dfs(ori, des, i + 1, dp, isMapped);
    }


    public static void main(String[] args) {
    }
}
