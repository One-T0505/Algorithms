package GreatOffer.TopHotQ;

import java.util.ArrayList;
import java.util.List;

// 给你一个字符串 s 。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。
// 注意，划分结果需要满足：将所有划分结果按顺序连接，得到的字符串仍然是 s 。
// 返回一个表示每个字符串片段的长度的列表。
// "abbacdeedekgkgk"  最优化分是：abba  c  deede  kgkgk  所以返回结果：[4, 1, 5, 5]

public class _0763_PartitionLabels {

    // 需要用到哈希表，key是字符  value是该字符在s中出现的最右的位置
    // 0  1  2  3  4  5  6  7  8  9 10 11 12 13
    // a  b  c  a  b  a  c  c  c  e  f  g  z  g  给定字符串如左边所示  并且哈希表如右边所示
    //
    // a   5     c  8    f  10    z  12      当来到0位置时查询表得知a最右出现在5，所以第1刀切的位置R不可能
    // b   4     e  9    g  13               在5之前了；来到1位置，查表发现b最右出现在4位置，<R，所以R不更新
    //                                       来到2位置的c查表知道c最右出现在8位置，>R，更新R为8，接下来都不会
    //                                       更新R，直到来到9位置时，发现已经超过R了，那么就可以确定第一刀切在
    //                                       8位置，此时的9位置已经是下一块的开始了。

    public List<Integer> partitionLabels(String s) {
        char[] chs = s.toCharArray();
        int N = chs.length;
        // 辅助结构，保存每种字符出现的最右的位置
        int[] dp = new int[26];
        for (int i = 0; i < N; i++)
            dp[chs[i] - 'a'] = i;
        ArrayList<Integer> res = new ArrayList<>();
        // 先把第一个字符的最右位置暂当要切的位置
        int R = dp[chs[0] - 'a'];
        int L = 0;
        for (int i = 1; i < N; i++) {
            if (i > R) {
                res.add(R - L + 1);
                L = i;
            }
            R = Math.max(R, dp[chs[i] - 'a']);
        }
        res.add(R - L + 1);
        return res;
    }
}
