package Basic;

// 这道题左老师没讲，是我自己做leetcode的，1790题
// 给你长度相等的两个字符串 s1 和 s2 。一次字符串交换操作的步骤如下：选出某个字符串中的两个下标（不必不同），
// 并交换这两个下标所对应的字符。如果对其中一个字符串执行最多一次字符串交换就可以使两个字符串相等，
// 返回 true ；否则，返回 false 。

import java.util.ArrayList;

public class StringSwap {

    // 计数统计
    // 题目要求其中一个字符串执行最多一次字符交换使得两个字符串相等，意味着两个字符串中最多只存在两个位置 i,j
    // 处字符不相等，此时我们交换 i,j 处字符可使其相等。设两个字符串分别为 s1,s2.
    // 1.如果两个字符串 s1,s2 相等，则不需要进行交换即可满足相等；
    // 2.如果两个字符串 s1,s2 不相等，字符串一定存在两个位置 i,j 处的字符不相等，需要交换 i,j 处字符使其相等，
    //   此时一定满足 s1[i] = s2[j], s1[j] = s2[i]
    // 3.如果两个字符串中只存在一个或大于两个位置的字符不相等，则此时无法通过一次交换使其相等；

    public static boolean isWorthySwapV2(String s1, String s2){
        if (s1 == null || s2 == null || s1.length() != s2.length())
            return false;
        ArrayList<Integer> diff = new ArrayList<>();
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        int N = chars1.length;
        for (int i = 0; i < N; i++) {
            if (chars1[i] != chars2[i])
                diff.add(i);
            if (diff.size() > 2)
                return false;
        }
        if (diff.isEmpty())
            return true;
        if (diff.size() != 2)
            return false;
        return chars2[diff.get(0)] == chars1[diff.get(1)] && chars2[diff.get(1)] == chars1[diff.get(0)];
    }
    // ==================================================================================================


    public static void main(String[] args) {
        String s1 = "bank";
        String s2 = "kanb";
        System.out.println(isWorthySwapV2(s1, s2));
    }
}
