package class07;


import java.util.Arrays;
import java.util.HashSet;


// 本题与leetCode139 140 属于同一系列
// 假设所有字符都是小写字母。arr是去重的单词表,每个单词都不是空字符串且可以使用任意次。
// 使用arr中的单词有多少种拼接成字符串str的方式。返回方法数。

public class ConcatenateString {

    public static int concatenateStringV1(String str, String[] words) {
        if (str == null || str.equals(""))
            return 0;
        HashSet<String> set = new HashSet<>(Arrays.asList(words));
        return process(str, 0, set);
    }


    // 所有的可分解字符串，都已经放在了set中
    // str[i....] 能够被set中的贴纸分解的话，返回分解的方法数
    private static int process(String str, int i, HashSet<String> set) {
        if (i == str.length())
            return 1;
        int res = 0;
        for (int end = i; end < str.length(); end++) {
            String pre = str.substring(i, end + 1);
            if (set.contains(pre))
                res += process(str, end + 1, set);
        }
        return res;
    }
    // 可以发现，该递归只有一个可变参数index，并且每个index，都需要枚举后面所有可能组成的word，所有时间复杂度至少是
    // O(N^2)，但是好像说substring这个方法也是O(N)，所以时间复杂度来到O(N^3)。
    // ====================================================================================================


    // 用动态规划和前缀树来优化。用前缀树来存储每一个word，这样的话，来到一个位置index，开始枚举后续可能组成单词时，就可以
    // 在树上找。不用像枚举一样，一定要遍历到最后，前缀树上找的时候只要没能匹配上就不会接着往下找很多错误的可能，所以会极大地
    // 优化。因为每次查询后续所有可能时都只会固定查询前缀树上最长那个单词字符数量的次数，也不需要截取子串了，所以时间复杂度一下
    // 就来到了O(N).

    // 主方法
    public static int concatenateStringV2(String str, String[] words) {
        if (str == null || str.equals(""))
            return 0;
        PrefixTree tree = new PrefixTree();
        for (String word : words)
            tree.add(word);
        char[] chars = str.toCharArray();
        int N = chars.length;
        int[] cache = new int[N + 1];
        cache[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            Node cur = tree.root;
            for (int end = i; end < N; end++) {  // 以i作为开始，逐一寻找
                int path = chars[end] - 'a';
                if (cur.nexts[path] == null)  // 如果为空，后面的不可能有，直接退出
                    break;
                cur = cur.nexts[path];
                if (cur.end)    // 沿途上有这个单词，那么可以加上结果
                    cache[i] += cache[end + 1];
            }
        }
        return cache[0];
    }

    public static void main(String[] args) {
        String str = "aabacacacaaba";
        String[] words = {"a", "aa", "aab", "aac", "ac", "acca"};
        System.out.println(concatenateStringV1(str, words));
        System.out.println(concatenateStringV2(str, words));

    }

    public static class Node {
        public Node[] nexts;
        public boolean end;

        public Node() {
            nexts = new Node[26];  // 因为题目指明了都是小写字母
            end = false;
        }
    }

    public static class PrefixTree {
        public Node root = new Node();


        public void add(String word) {
            char[] chars = word.toCharArray();
            Node cur = root;
            for (char c : chars) {
                int path = c - 'a';
                if (cur.nexts[path] == null)
                    cur.nexts[path] = new Node();
                cur = cur.nexts[path];
            }
            cur.end = true;
        }
    }
}
