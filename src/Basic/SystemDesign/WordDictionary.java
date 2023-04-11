package Basic.SystemDesign;

import java.util.HashSet;

/**
 * ymy
 * 2023/3/31 - 19 : 49
 **/

// leetCode211  这个不是老师讲的
// 请你设计一个数据结构，支持 添加新单词 和 查找字符串是否与任何先前添加的字符串匹配 。
// 实现词典类 WordDictionary ：
//  1.WordDictionary() 初始化词典对象
//  2.void addWord(word) 将 word 添加到数据结构中，之后可以对它进行匹配
//  3.bool search(word) 如果数据结构中存在字符串与 word 匹配，则返回 true ；否则，返回  false 。
//    word 中可能包含一些 '.' ，每个 . 都可以表示任何一个字母。


// 1 <= word.length <= 25
// addWord 中的 word 由小写英文字母组成
// search 中的 word 由 '.' 或小写英文字母组成
// 最多调用 10^4 次 addWord 和 search


public class WordDictionary {

    // 前缀树
    private class Node {
        public boolean end;
        public Node[] nexts;

        public Node(){
            end = false;
            nexts = new Node[26];
        }
    }

    private Node root;
    private HashSet<String> dp; // 记录已经加入了哪些单词


    public WordDictionary() {
        root = new Node();
        dp = new HashSet<>();
    }


    // 保证给的word全部都只是小写英文字母
    public void addWord(String word) {
        if (dp.contains(word))
            return;
        Node cur = root;
        char[] chs = word.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            int path = chs[i] - 'a';
            if (cur.nexts[path] == null)
                cur.nexts[path] = new Node();
            cur = cur.nexts[path];
        }
        cur.end = true;
    }


    public boolean search(String word) {
        if (word == null || word.length() == 0)
            return false;
        // 如果word全部都是字母，那么下面的检查就可以很快检查出来。如果包含匹配符 '.' 那就得用下面的递归
        if (dp.contains(word))
            return true;
        char[] t = word.toCharArray();
        return dfs(t, 0, root);
    }

    private boolean dfs(char[] t, int i, Node cur) {
        if (i == t.length)
            return cur.end;
        if (cur == null)
            return false;
        if (t[i] == '.'){
            boolean res = false;
            for (int j = 0; j < 26; j++) {
                if (cur.nexts[j] != null){
                    res |= dfs(t, i + 1, cur.nexts[j]);
                    if (res)
                        return true;
                }
            }
            return false;
        } else {
            if (cur.nexts[t[i] - 'a'] == null)
                return false;
            return dfs(t, i + 1, cur.nexts[t[i] - 'a']);
        }
    }




}
