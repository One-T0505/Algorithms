package LeetCode;

import java.util.HashMap;
import java.util.HashSet;

/**
 * ymy
 * 2023/3/31 - 10 : 39
 **/

// 给定一种规律 pattern 和一个字符串 s ，判断 s 是否遵循相同的规律。
// 这里的 遵循 指完全匹配，例如， pattern 里的每个字母和字符串 s 中的每个非空单词之间存在着双向连接的对应规律。

public class WordPattern {

    public static boolean wordPattern(String pattern, String s) {
        if (pattern == null || pattern.length() == 0 || s == null || s.length() == 0)
            return false;
        char[] pat = pattern.toCharArray();
        String[] words = s.split(" ");
        if (pat.length != words.length)
            return false;
        HashMap<Character, String> cToS = new HashMap<>();
        HashSet<String> isMapped = new HashSet<>();
        for (int i = 0; i < pat.length; i++) {
            if (cToS.containsKey(pat[i]) && cToS.get(pat[i]).equals(words[i]))
                continue;
            if (cToS.containsKey(pat[i]) && !cToS.get(pat[i]).equals(words[i]))
                return false;
            if (isMapped.contains(words[i]))
                return false;
            cToS.put(pat[i], words[i]);
            isMapped.add(words[i]);
        }
        return true;
    }


    public static void main(String[] args) {
        String pattern = "aaaa";
        String s = "dog dog dog dog";
        System.out.println(wordPattern(pattern, s));
    }
}
