package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.List;

// 给定一个字符串 s 和一个字符串字典 wordDict ，在字符串 s 中增加空格来构建一个句子，使得句子中所有的单词都在
// 词典中。以任意顺序返回所有这些可能的句子。
// 注意：词典中的同一个单词可能在分段中被重复使用多次。

public class _0140_WordBreakII {

    // 这是139题的升级版，需要用到139题的解法，并且依然需要用到前缀树
    public static List<String> wordBreak(String s, List<String> wordDict) {
        char[] chs = s.toCharArray();
        PrefixTree tree = new PrefixTree();
        for (String word : wordDict)
            tree.insert(word);
        // 该方法其实就是139题的方法，返回一个dp表，dp[i]表示从i..N-1能否拆分成wordDict里的单词
        boolean[] dp = getDp(chs, tree.root);
        ArrayList<String> path = new ArrayList<>();
        ArrayList<String> res = new ArrayList<>();

        f(chs, 0, tree.root, dp, path, res);
        return res;
    }

    private static boolean[] getDp(char[] chs, Node root) {
        int N = chs.length;
        boolean[] dp = new boolean[N + 1];
        dp[N] = true;
        for (int i = N - 1; i >= 0; i--) {
            Node cur = root;
            for (int j = i; j < N; j++) {
                int path = chs[j] - 'a';
                if (cur.nexts[path] == null)
                    break;
                cur = cur.nexts[path];
                if (cur.end && dp[j + 1]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp;
    }


    // 最核心的递归方法
    private static void f(char[] chs, int i, Node root, boolean[] dp, ArrayList<String> path,
                          ArrayList<String> res) {
        if (i == chs.length) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < path.size() - 1; j++)
                sb.append(path.get(j)).append(" ");
            sb.append(path.get(path.size() - 1));
            res.add(sb.toString());
        } else {
            Node cur = root;
            for (int j = i; j < chs.length; j++) {
                int road = chs[j] - 'a';
                if (cur.nexts[road] == null)
                    break;
                cur = cur.nexts[road];
                if (cur.end && dp[j + 1]) {
                    path.add(cur.path);
                    f(chs, j + 1, root, dp, path, res);
                    path.remove(path.size() - 1);
                }
            }
        }
    }
    // =========================================================================================




    // 前缀树结构
    public static class Node {
        public int pass;
        public boolean end;
        public String path;
        public Node[] nexts;

        public Node() {
            pass = 0;
            end = false;
            path = null;
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
            cur.path = word;
        }
    }
    // =========================================================================================





    public static void main(String[] args) {
        String s = "catsanddog";
        ArrayList<String> dict = new ArrayList<>();
        dict.add("cat");
        dict.add("cats");
        dict.add("and");
        dict.add("sand");
        dict.add("dog");
        wordBreak(s, dict);
    }
}
