package SlidingWindow;

// DNA序列 由一系列核苷酸组成，缩写为 'A', 'C', 'G' 和 'T'。例如，"ACGAATTCCG" 是一个 DNA序列。在研究 DNA 时，
// 识别 DNA 中的重复序列非常有用。给定一个表示 DNA序列的字符串 s ，返回所有在 DNA 分子中出现不止一次的长度为 10 的
// 序列(子字符串)。你可以按任意顺序返回答案。

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class _0187_Code {

    // 常规方法就不说了。这里实现的是最优解：哈希表 + 滑动窗口 + 位运算
    // 由于 s 中只含有 4 种字符，我们可以将每个字符用 2 个比特表示，即：
    // A --> 00   C --> 01  G --> 10  T --> 11
    // 如此一来，一个长为 10 的字符串就可以用 20 个比特表示，而一个 int有32个比特，足够容纳该字符串，
    // 因此我们可以将 s 的每个长为 10 的子串用一个 int 整数表示（只用低20位）。每个整数都对应着一个唯一的字符串，
    // 因此我们可以将方法一中的哈希表改为存储每个长为 10 的子串的整数表示。
    // 如果我们对每个长为 10 的子串都单独计算其整数表示，那么时间复杂度仍然和传统方法一样为 O(NL)。为了优化时间复杂度，
    // 我们可以用一个大小固定为 10 的滑动窗口来计算子串的整数表示。设当前滑动窗口对应的整数表示为 x，当我们要计算下
    // 一个子串时，就将滑动窗口向右移动一位，此时会有一个新的字符进入窗口，以及窗口最左边的字符离开窗口，
    // 这些操作对应的位运算，按计算顺序表示如下：
    //  1. x << 2   移出两个空位给新进来的字符使用
    //  2. (x << 2) | chs[i]    将新来的字符插进去
    //  3. ((x << 2) | chs[i]) & ((1 << 20) - 1)   因为只用管低位20位，所以移出的那个字符的比特位超过了20位，
    //     就用掩码将高位12位全部清0
    // 这里规定一个字符串最左边的字符对应的是二进制中的最低两位
    public static List<String> findRepeatedDnaSequences(String s) {
        ArrayList<String> list = new ArrayList<>();
        if(s == null || s.length() < 10)
            return list;
        // 频次表，记录某个字符串出现的次数，补过字符串是以int数据给出的
        HashMap<Integer, Integer> fre = new HashMap<>();
        HashMap<Character, Integer> dp = new HashMap<>();
        dp.put('A', 0);
        dp.put('C', 1);
        dp.put('G', 2);
        dp.put('T', 3);
        char[] chs = s.toCharArray();
        int N = chs.length;
        int mask = (1 << 20) - 1;  // 掩码
        int x = 0;
        for (int i = 0; i < 9; i++) {
            x = (x << 2) | dp.get(chs[i]);
        }
        for(int i = 9; i < N; i++){
            x = ((x << 2) | dp.get(chs[i])) & mask;
            fre.put(x, fre.getOrDefault(x, 0) + 1);
            if (fre.get(x) == 2)
                list.add(String.valueOf(chs, i - 9, 10));
        }
        return list;
    }


    public static void main(String[] args) {
        String s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        System.out.println(findRepeatedDnaSequences(s));
    }
}
