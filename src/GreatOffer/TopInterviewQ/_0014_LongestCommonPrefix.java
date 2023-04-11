package GreatOffer.TopInterviewQ;

public class _0014_LongestCommonPrefix {


    // 思路：先把字符串数组第一个字符串整个当作公共前缀，然后后面的字符串依次和他比较
    // 这个前缀肯定会逐步缩短

    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            prefix = longestCommonPrefix(prefix, strs[i]);
            if (prefix.length() == 0)
                break;
        }
        return prefix;
    }

    private static String longestCommonPrefix(String prefix, String str) {
        int length = Math.min(prefix.length(), str.length());
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (prefix.charAt(i) == str.charAt(i))
                index++;
            else
                break;
        }
        return prefix.substring(0, index);
    }
    // ==========================================================================================


    public String longestCommonPrefix2(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        char[] chars = strs[0].toCharArray();
        int len = chars.length;  // 最长公共前缀长度不可能超过某个字符串的长度
        for (int i = 1; i < strs.length; i++) {
            char[] des = strs[i].toCharArray();
            int index = 0;
            while (index < chars.length && index < des.length) {
                if (chars[index] == des[index])
                    index++;
                else
                    break;
            }
            len = Math.min(len, index);
            if (len == 0)
                return "";
        }
        return strs[0].substring(0, len);
    }

}
