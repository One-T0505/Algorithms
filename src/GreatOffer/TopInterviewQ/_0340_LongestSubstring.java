package GreatOffer.TopInterviewQ;


// 给定一个字符串s，定义达标字符串：如果该字符串中包含的字符种类数不超过k种，那么该字符串称为达标串。
// 找出s中达标的子串中，长度最长的子串，返回其长度。

public class _0340_LongestSubstring {

    // 这个题目是包含潜在的单调性的，因为包含的字符越多，那么包含的字符种类数不可能变少，所以可以使用滑动窗口。
    public static int longestSubstringWithAtMostKChar(String s, int k) {
        if (s == null || s.length() == 0 || k < 1)
            return 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int[] counter = new int[256];
        int kinds = 0;
        int R = 0;
        int res = 0;
        // 枚举窗口左边界
        for (int L = 0; L < N; L++) {
            // R 窗口的右边界  下面这么一长串条件都是在说R什么情况下可以向右扩
            while (R < N && ((kinds < k) || (kinds == k && counter[chs[R]] > 0))) {
                kinds += counter[chs[R]] == 0 ? 1 : 0;
                counter[chs[R++]]++;
            }
            // 执行到这里时，说明以L作为左边界时，R已经扩到了最远处，可以结算答案了
            res = Math.max(res, R - L);
            // 此时要让左边界不断退
            // 如果当前L处的字符只有一个，那么种类数要减1，因为L向右移动后，这个种类的字符就没了
            kinds -= counter[chs[L]] == 1 ? 1 : 0;
            counter[chs[L]]--;
        }
        return res;
    }
}
