package GreatOffer.TopInterviewQ;


// 给你一个字符串 s 和一个整数 k ，请你找出 s 中的最长子串， 要求该子串中的每一字符出现次数都不少于 k。
// 返回这一子串的长度。

public class _0395_LongestSubstringEqualTime {

    // 这道题目不可以使用滑动窗口，因为随着滑动窗口逐步变大后，并不是窗口内的东西越容易达标。比如：[a b c c c b a]
    // k==3， 那么答案是：[c c c] 窗口变大后反而不达标了

    // 现在把整体流程制定成这样：因为全部都是小写英文字母，所以我们最大的流程是，遍历一次寻找出只包含1种字符且达标的
    // 最长子串，再遍历一次，寻找出包含2种字符且达标的最长子串。。。。再遍历一次，寻找只包含26种字符且达标的最长子串。
    // 最终的答案一定在其中。

    public int longestSubstring(String s, int k) {
        if (s == null || s.length() == 0 || k < 1)
            return 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int res = 0;
        // 最大的循环  在每轮循环中，都需要完成一次滑动窗口的流程，主要的思路就是：
        // 在当前L固定的情况下，一直让R向右扩，直到出现窗口内出现的字符种类超过aim了，这就是停止的时机，
        // 然后检查当前[L,R]内达标的字符是否匹配上aim，匹配上了，就可以收集答案。
        for (int aim = 1; aim <= 26; aim++) {
            int[] counter = new int[26];
            int R = -1;   // 窗口右边界
            int satisfied = 0;  // 窗口内，已经有几种字符出现的次数>=k了
            int kinds = 0;    // 窗口内，已经出现了多少种字符了
            for (int L = 0; L < N; L++) {  // 尝试每一个左边界
                while (R + 1 < N && !(kinds == aim && counter[chs[R + 1] - 'a'] == 0)) {
                    R++;
                    if (counter[chs[R] - 'a'] == 0)
                        kinds++;
                    if (counter[chs[R] - 'a'] == k - 1)
                        satisfied++;
                    counter[chs[R] - 'a']++;
                }
                // 执行到这里时，此时的R来到了当前L为左边界下的第一个非法位置
                if (satisfied == aim)
                    res = Math.max(res, R - L + 1);
                // 此时要让chars[L]移出窗口了，所以要删除chars[L]的信息
                if (counter[chs[L] - 'a'] == 1)
                    kinds--;
                if (counter[chs[L] - 'a'] == k)
                    satisfied--;
                counter[chs[L] - 'a']--;
            }
        }
        return res;
    }
}
