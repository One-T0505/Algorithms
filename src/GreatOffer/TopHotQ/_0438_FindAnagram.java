package GreatOffer.TopHotQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 给定两个字符串 s 和 p，找到 s 中所有 p 的异位词的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
// 异位词: 指由相同字母重排列形成的字符串（包括相同的字符串）。

public class _0438_FindAnagram {

    // 这个要用到：滑动窗口下的欠债还钱模型
    public List<Integer> findAnagrams(String s, String p) {
        ArrayList<Integer> res = new ArrayList<>();
        if (s == null || p == null || s.length() < p.length())
            return res;
        char[] ori = s.toCharArray();
        int N = ori.length;
        char[] pst = p.toCharArray();
        int M = pst.length;
        HashMap<Character, Integer> dp = new HashMap<>();   // 欠债表
        for (char c : pst) {
            dp.put(c, dp.getOrDefault(c, 0) + 1);
        }
        int all = M;
        // 先让窗口先形成一个M-1的大小，因为在之后的流程里，R向右扩一个，L就要向右扩一个，但是事先得先形成一个窗口
        int R = 0;
        while (R < M - 1) {
            if (dp.containsKey(ori[R])) {
                int count = dp.get(ori[R]);
                if (count > 0)
                    all--;
                dp.put(ori[R], count - 1);
            }
            R++;
        }
        // 正式的窗口流程，右边扩充一个，左边退掉一个  从上面的循环退出，R==M-1
        for (int L = 0; R < N; L++, R++) {
            // 先让R位置的字符进来
            if (dp.containsKey(ori[R])) {
                int count = dp.get(ori[R]);
                if (count > 0)
                    all--;
                dp.put(ori[R], count - 1);
            }
            // 每次新来的字符处理完后，窗口大小就是M，要检查是否可以收集答案
            if (all == 0)
                res.add(L);
            // 再退掉L位置字符
            if (dp.containsKey(ori[L])) {
                int count = dp.get(ori[L]);
                if (count >= 0)
                    all++;
                dp.put(ori[L], count + 1);
            }
        }
        return res;
    }

}
