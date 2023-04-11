package GreatOffer.class24;


// 给定一个字符串，每种字符只能留一个，返回字典序最小的结果。
// 比如 s="bbaccb"  a b c都只能留一个，如果某种字符有多个，只能选择某个位置的保留,
// 保留结果可以是：bac  acb  所以返回acb

public class _06_Code {

    // 这道题目是一个纯贪心的题目。流程是这样的：
    // 还是先做一张s的字符统计表，然后遍历每个字符，在统计表中减去次数，直到有一个字符的数量变为0的时候，那么
    // 从0..C当前位置就是可选区，在可选取里选择ASCII码最小的那个字符，然后将其左边的所有字符都删除，右边到最后
    // 是一个区域。举个例子：
    //   0 1 2 3 4 5 6 7 8
    //   b a a c b c b a a                      a  4    b  3   c  2
    //
    // 当来到5时，出现了第一个记录为0的时刻，那么0..5就是可选区，选择里面ASCII码最小的a，选哪个位置的a都可以，比如选择
    // 2位置的a，删除左边所有的字符，并把右边的所有a删除，那么新的字符串就是：
    //
    // 2 3 4 5 6        那么新的统计表就是： a  1    b  2    c  2
    // a c b c b
    //
    // 然后循环下去，直到左右边界相等.


    public String removeDuplicateLetters(String s) {
        if (s == null || s.length() < 2)
            return s;
        char[] chars = s.toCharArray();
        int N = chars.length;
        int[] counter = new int[256];
        for (char c : chars) counter[c]++;

        int minASCIndex = 0;
        for (int i = 0; i < N; i++) {
            minASCIndex = chars[minASCIndex] > chars[i] ? i : minASCIndex;
            if (--counter[chars[i]] == 0)
                break;
        }
        return chars[minASCIndex] + removeDuplicateLetters(s.substring(minASCIndex + 1).
                replaceAll(String.valueOf(chars[minASCIndex]), ""));
    }


}
