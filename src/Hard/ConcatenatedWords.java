package Hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// leetCode472
// 给你一个不含重复单词的字符串数组 words ，请你找出并返回 words 中的所有连接词 。
// 连接词定义为：一个完全由给定数组中的至少两个较短单词组成的字符串。
// 也就是说数组里有一些单词是可以通过其他单词拼接得到的。
// 比如：words = [cat, dog, catdogcat]   输出：catdogcat  因为可以用cat dog 拼接得到
// 可以重复使用一个单词

// 数据规模
// 1 <= words.length <= 10^4
// 0 <= words[i].length <= 30
// words[i] 仅由小写字母组成
// 0 <= sum(words[i].length) <= 10^5

public class ConcatenatedWords {

    public static int[] dp = new int[1000];  // 用0表示没算过。-1表示不可行  1表示可行


    // 要使用前缀树。
    public static class Node {
        public boolean end;
        public Node[] nexts;

        public Node() {
            end = false;
            nexts = new Node[26];
        }
    }

    private static void insert(Node root, char[] s) {
        for (char c : s) {
            int path = c - 'a';
            if (root.nexts[path] == null)
                root.nexts[path] = new Node();
            root = root.nexts[path];
        }
        root.end = true;
    }
    // ==================================================================================


    // 主方法
    public static List<String> findAllConcatenatedWordsInADict(String[] words) {
        ArrayList<String> res = new ArrayList<>();
        // 长度 <= 2 不可能有答案的  因为每个单词都不相同
        if (words == null || words.length < 3)
            return res;
        // words中至少有三个元素
        // 按照字符串长度递增顺序排序，遍历到某个字符串时，如果可以被之前的字符串分解，那就不加进前缀树；
        // 如果不能被分解，就加入前缀树
        // 如果你可以被分解，那能分解你的那些字符串肯定长度比你短，已经进树了，完全可以用他们代替你，把你再加进去
        // 只会让树变得冗余
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        Node root = new Node();
        for (String word : words) {
            char[] chs = word.toCharArray();
            // 空字符串""天然可以被分解，详情请看下面的递归函数的base case
            // 所以要对长度筛选
            if (chs.length > 0 && canSplit(chs, root, 0))
                res.add(word);
            else // 不能被分解就加入树里
                insert(root, chs);
        }
        return res;
    }


    // 这是该题的核心。该方法用于判断s是否能被树中已有的字符串分解。
    // i表示来到了s的哪一位，现在要对第i位做决策了
    private static boolean canSplit(char[] s, Node cur, int i) {
        boolean res = false;
        if (i == s.length)
            res = true;
        else { // 还有字符
            Node c = cur;
            // 枚举i～N-1的前缀做为一个字符串看能否被分解
            for (int end = i; end < s.length; end++) {
                int path = s[end] - 'a';
                if (c.nexts[path] == null)
                    break;
                c = c.nexts[path];
                // 这里递归传入的参数依然是root，因为字符串可以复用
                if (c.end && canSplit(s, cur, end + 1)) {
                    res = true;
                    // 如果找打了一个答案，就不用继续往后枚举更长的前缀作为单词了，没必要了
                    break;
                }
            }
        }
        return res;
    }


    // 上面的递归只有一个可变参数，可以改成一维记忆化搜索

    public static List<String> findAllConcatenatedWordsInADict2(String[] words) {
        List<String> ans = new ArrayList<>();
        if (words == null || words.length < 3) {
            return ans;
        }
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        Node root = new Node();
        for (String str : words) {
            char[] s = str.toCharArray();
            Arrays.fill(dp, 0, s.length + 1, 0);
            if (s.length > 0 && canSplit2(s, root, 0, dp)) {
                ans.add(str);
            } else {
                insert(root, s);
            }
        }
        return ans;
    }

    private static boolean canSplit2(char[] s, Node r, int i, int[] dp) {
        if (dp[i] != 0) {
            return dp[i] == 1;
        }
        boolean ans = false;
        if (i == s.length) {
            ans = true;
        } else {
            Node c = r;
            for (int end = i; end < s.length; end++) {
                int path = s[end] - 'a';
                if (c.nexts[path] == null) {
                    break;
                }
                c = c.nexts[path];
                if (c.end && canSplit2(s, r, end + 1, dp)) {
                    ans = true;
                    break;
                }
            }
        }
        dp[i] = ans ? 1 : -1;
        return ans;
    }

}
