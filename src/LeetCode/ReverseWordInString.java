package LeetCode;

/**
 * ymy
 * 2023/4/3 - 19 : 47
 **/


// leetCode151  反转字符串中的单词
// 给你一个字符串 s ，请你反转字符串中 单词 的顺序。
// 单词 是由非空格字符组成的字符串。s 中使用至少一个空格将字符串中的 单词 分隔开。
// 返回 单词 顺序颠倒且 单词 之间用单个空格连接的结果字符串。
// 注意：输入字符串 s中可能会存在前导空格、尾随空格或者单词间的多个空格。返回的结果字符串中，单词间应当仅用单个
//      空格分隔，且不包含任何额外的空格。


// 1 <= s.length <= 10^4
// s 包含英文大小写字母、数字和空格 ' '
// s 中 至少存在一个 单词

public class ReverseWordInString {

    public static String reverseWords(String s) {
        if (s == null || s.length() == 0)
            return "";
        char[] chs = s.toCharArray();
        int start = 0, end = chs.length - 1;
        while (chs[start] == ' ')
            start++;
        while (chs[end] == ' ')
            end--;
        // start end 跳过前导和尾缀的空格

        StringBuilder res = new StringBuilder("");
        StringBuilder sb = new StringBuilder("");
        int i = start;
        while (i <= end){
            if (chs[i] == ' '){
                if (i > start && chs[i - 1] != ' '){
                    res.append(sb.reverse()).append(" ");
                    sb.replace(0, sb.length(), ""); // 全部清空
                }
            } else
                sb.append(chs[i]);
            i++;
        }
        if (sb.length() > 0)
            res.append(sb.reverse());
        return res.reverse().toString();
    }


    public static void main(String[] args) {
        String s = "   the   sky  is blue  ";
        System.out.println(reverseWords(s));
    }
}
