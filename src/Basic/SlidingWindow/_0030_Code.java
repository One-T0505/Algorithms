package Basic.SlidingWindow;

// leetCode438题的进阶版
// 给定一个字符串 s 和一个字符串数组 words。words 中所有字符串长度相同。
// s 中的串联子串是指一个包含 words 中所有字符串以任意顺序排列连接起来的子串。
// 例如，如果 words = ["ab","cd","ef"]， 那么 "abcdef"， "abefcd"，"cdabef"， "cdefab"，
// "efabcd"， 和 "efcdab" 都是串联子串。 "acdbef" 不是串联子串，因为他不是任何 words 排列的连接。
// 返回所有串联字串在 s 中的开始索引。你可以以任意顺序返回答案。

import java.util.ArrayList;
import java.util.List;

public class _0030_Code {


    // 欠债还钱模型
    public List<Integer> findSubstring(String s, String[] words) {
        int wordNum = words.length;
        int wordLen = words[0].length();
        int allLen = wordLen * wordNum;
        if (s == null || s.length() < allLen)
            return null;
        ArrayList<Integer> res = new ArrayList<>();

        return res;
    }
}
