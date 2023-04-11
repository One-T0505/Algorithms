package GreatOffer.class26;


// 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words， 返回所有二维网格上的单词。
// 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
// 同一个单元格内的字母在一个单词中不允许被重复使用。
//
// 输入：board = [["o","a","a","n"],
//               ["e","t","a","e"],
//               ["i","h","k","r"],
//               ["i","f","l","v"]],     words = ["oath","pea","eat","rain"]
// 输出：["eat","oath"]

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class _02_Code {

    // 先把words所有的单词建成前缀树，这样当我们来到一个结点想查询以该结点能否走出一个单词时，我们只需要
    // 在前缀树中看开头是否有这个字符就可以判断该字符能否当开头了，有了前缀树剧很方便了。

    // 主方法
    public List<String> findWords(char[][] board, String[] words) {
        if (board == null || board.length == 0 || words == null || words.length == 0)
            return null;
        Node root = new Node();  // 前缀树的根结点
        // 对原始words去重
        HashSet<String> uniqueWords = new HashSet<>();
        for (String word : words) {
            if (!uniqueWords.contains(word)) {
                build(root, word); // 将该单词加入前缀树
                uniqueWords.add(word);
            }
        }
        ArrayList<String> res = new ArrayList<>();
        // 沿途走过的字符收集到path中
        LinkedList<Character> path = new LinkedList<>();
        int N = board.length;
        int M = board[0].length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                f(board, i, j, path, root, res);
            }
        }
        return res;
    }
    // 前缀树结点封装
    // ==============================================================================================


    public static class Node {
        public Node[] nexts;
        public int pass;
        // 因为我们会对words先去重一遍，所以前缀树中某个结点是单词的结尾，最多只能是一个单词的结尾
        // 所以可以用布尔型变量记录end
        public boolean end;

        public Node() {
            nexts = new Node[26];
            pass = 0;
            end = false;
        }
    }


    // 将一个单词加入到前缀树中
    private void build(Node root, String word) {
        root.pass++;
        Node cur = root;
        char[] chs = word.toCharArray();
        for (char c : chs) {
            int path = c - 'a';
            if (cur.nexts[path] == null) {
                cur.nexts[path] = new Node();
            }
            cur = cur.nexts[path];
            cur.pass++;
        }
        cur.end = true;
    }

    // 该递归方法是整个算法的核心
    // 现在来到了board[i][j]这个字符，path表示以某个字符为起点时到当前位置走过的字符；如果当前位置就是
    // 起点，那么path为空；cur表示来到了哪个结点；这里需要注意下，如果board[i][j]=='k'，那么cur就是
    // 这个字符在前缀树中对应结点的父结点，因为我现在刚来到[i][j]这个位置，我还没有验证这个位置在前缀树中是否可走；
    // 就像主方法调用那样，位置确定了，但是传入的都是root，root不代表任何一个字符；res就是总结果。
    //
    // 该方法返回的是：从[i][j]位置出发搞定了多少个words中的单词  为什么这样设计返回值，下面会讲
    private int f(char[][] board, int i, int j, LinkedList<Character> path,
                  Node cur, ArrayList<String> res) {
        char c = board[i][j];
        if (c == 0)   // c == 0，说明走的是回头路，那么直接退出，因为搞定不了一个单词，所以返回0
            return 0;
        // (i,j)不是回头路，是没走过的路，但是还要验证在前缀树中是否可以走
        int index = c - 'a';
        // 说明该字符没办法搞定目标单词，pass == 0，说明当前结点之后沿路的答案都收集过了
        if (cur.nexts[index] == null || cur.nexts[index].pass == 0)
            return 0;
        // 执行到这里说明，没有走回头路且可以继续下去
        cur = cur.nexts[index];
        path.addLast(c);  // 当前字符有效
        int solved = 0;   // 从(i,j)位置出发，后续一共搞定了多少答案
        if (cur.end) { // 如果当前来到的点是结尾
            res.add(generatePath(path));
            // 标记当前结点已经不是结尾了，这样从别的路来到此结点时就不会收集已经收集过的答案了
            cur.end = false;
            solved++;
        }
        // 往上、下、左、右，四个方向尝试
        board[i][j] = 0;  // 将当前结点标记为0，如果谁走到这里表示是已经走过的了
        if (i > 0)
            solved += f(board, i - 1, j, path, cur, res);
        if (i < board.length - 1)
            solved += f(board, i + 1, j, path, cur, res);
        if (j > 0)
            solved += f(board, i, j - 1, path, cur, res);
        if (j < board[0].length - 1)
            solved += f(board, i, j + 1, path, cur, res);

        // 再还原现场
        board[i][j] = c;
        path.pollLast();
        // 这个和上面的 cur.nexts[index].pass == 0 是相互作用的，solved不可能超过
        // 当前结点的pass
        cur.pass -= solved;
        return solved;
    }

    // path中存储的字符已经是一个完成的单词了，现在要将其处理成一个字符串返回
    private String generatePath(LinkedList<Character> path) {
        char[] chs = new char[path.size()];
        int i = 0;
        for (char c : path)
            chs[i++] = c;
        return String.valueOf(chs);
    }


}
