package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.List;

// 给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出s。
// 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。

public class _0139_WordBreak {

    // 主方法
    public static boolean wordBreak(String s, List<String> wordDict) {
        PrefixTree tree = new PrefixTree();
        for (String word : wordDict)
            tree.insert(word);
        char[] chs = s.toCharArray();
        Node cur = tree.root;
        return f(chs, 0, cur, tree);
    }

    private static boolean f(char[] chs, int i, Node cur, PrefixTree tree) {
        if (i == chs.length)
            return true;
        boolean res = false;
        for (int j = i; j < chs.length; j++) {
            int path = chs[j] - 'a';
            cur = cur.nexts[path];
            if (cur == null)
                break;
            // 有路
            if (cur.end)
                res |= f(chs, j + 1, tree.root, tree);
            if (res)
                break;
        }
        return res;
    }
    // =========================================================================================




    // 动态规划法
    public static boolean wordBreak2(String s, List<String> wordDict) {
        PrefixTree tree = new PrefixTree();
        for (String word : wordDict)
            tree.insert(word);
        char[] chs = s.toCharArray();
        int N = chs.length;
        boolean[] dp = new boolean[N + 1];
        dp[N] = true;
        for (int i = N - 1; i >= 0; i--) {
            Node cur = tree.root;
            for (int j = i; j < N; j++) {
                int path = chs[j] - 'a';
                cur = cur.nexts[path];
                if (cur == null)
                    break;
                if (cur.end)
                    dp[i] |= dp[j + 1];
                if (dp[i])
                    break;
            }
        }
        return dp[0];
    }


    // 刚才上面的问题只需要返回最终能否完成切分，现在将问题升级下，返回能切分的方法数。
    public static int wordBreak3(String s, List<String> wordDict) {
        PrefixTree tree = new PrefixTree();
        for (String word : wordDict)
            tree.insert(word);
        char[] chs = s.toCharArray();
        int N = chs.length;
        int[] dp = new int[N + 1];
        dp[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            Node cur = tree.root;
            for (int j = i; j < N; j++) {
                int path = chs[j] - 'a';
                cur = cur.nexts[path];
                if (cur == null)
                    break;
                if (cur.end)
                    dp[i] += dp[j + 1];
            }
        }
        return dp[0];
    }
    // ============================================================================================



    // 前缀树结构
    public static class Node {
        public int pass;
        public boolean end;
        public Node[] nexts;

        public Node() {
            pass = 0;
            end = false;
            nexts = new Node[26];
        }
    }

    public static class PrefixTree {
        public Node root;

        public PrefixTree() {
            root = new Node();
        }

        public void insert(String word) {
            char[] chars = word.toCharArray();
            Node cur = root;
            cur.pass++;
            for (char c : chars) {
                int path = c - 'a';
                if (cur.nexts[path] == null)
                    cur.nexts[path] = new Node();
                cur = cur.nexts[path];
                cur.pass++;
            }
            cur.end = true;
        }
    }
    // =========================================================================================


    public static void main(String[] args) {
        String s = "leetcode";
        ArrayList<String> dict = new ArrayList<>();
        dict.add("leet");
        dict.add("code");
        System.out.println(wordBreak(s, dict));
    }
}
