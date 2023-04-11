package GreatOffer.TopInterviewQ;


// 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
// 注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。

public class _0242_ValidAnagram {

    public boolean isAnagram(String s, String t) {
        if ((s == null ^ t == null))
            return false;
        if (s == null)  // 两个都为空
            return true;
        // 两个都不为空
        if (s.length() != t.length())
            return false;
        // 两个都不为空且长度相等
        char[] ori = s.toCharArray();
        int[] counter = new int[256];
        for (char c : ori)
            counter[c]++;
        char[] des = t.toCharArray();
        for (char c : des) {
            if (--counter[c] < 0)
                return false;
        }
        return true;
    }
}
