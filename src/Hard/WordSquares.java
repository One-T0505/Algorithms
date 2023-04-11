package Hard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// leetCode425
// 给定一个字符串数组words，其长度为N，给定一个字符串长度M，表示字符串数组中每个字符串的长度为M。规定一定有 N==M
// 也就是说，如果M==4，那么words长度为4，且里面每个字符串长度都为4。
// 比如words == ["ball", "area", "lead", "lady"]  请找出所有拼接矩阵的方式，使得矩阵的第i行和第i列都是words[i]
//
// 0   b  a  l  l    看这个矩阵第0行是 ball 第0列也是ball  第2行是 lead  第2列也是 lead
// 1   a  r  e  a
// 2   l  e  a  d
// 3   l  a  d  y
//
// 不要求words中每个字符串都出现，某一个字符串可以使用多次，请返回所有符合要求的矩阵
// 返回的形式用链表即可：如上的矩阵就可以以 ball -> area -> lead -> lady 的形式返回
// 输入: words = ["abat","baba","atan","atal"]
// 输出: [["baba","abat","baba","atal"],["baba","abat","baba","atan"]]
// 解释：
// 可以看到输出里，有两个链表，代表两个单词方阵
// 第一个如下：
// b a b a
// a b a t
// b a b a
// a t a l
// 这个方阵里没有atan，因为不要求全部单词都在方阵里
// 第二个如下：
// b a b a
// a b a t
// b a b a
// a t a n
// 这个方阵里没有atal，因为不要求全部单词都在方阵里


public class WordSquares {

    // 我们要做的第一件事就是将所有单词的所有不重复前缀全部找出来。比如给的是[abc, abd, bbd]
    // 那么我们需要做一张哈希表
    // (a, [abc, abd])  (ab, [abc, abd])  (abc, [abc])
    // (abd, [abd])
    // (b, [bbd])  (bb, [bbd])  (bbd, [bbd])
    // key 表示某个前缀   value 表示以该前缀的字符串有哪些
    // 注意：这个表里还要加上一条很重要的记录： ("", [abc, abd, bbd])
    // 表示前缀为空的字符串是所有   我们在填写矩阵时，第0行第0列要填的字符串可以是任意的，因为其无限制
    // 比如：
    //      b  a  l  l      当填入第一个单词后，第二个单词就不能随心所欲地填了，其必须满足前缀是a，才可以当作第二个单词
    //      a  r  e  a      当填入第二个单词后，第三个单词就必须满足前缀是 le
    //      l  e            所以我们总结出来一个规律，当要填第i行和第i列的单词时，该单词的前缀必须是 从填入的0～i-1
    //      l  a            单词的第i个字符的拼接。
    // 单独添加这条记录就是为了可以一起融进for循环，不用单独判断边界情况
    public static List<List<String>> wordSquares(String[] words) {
        // 这个N既表示words的长度，也表示所有字符串的长度
        int N = words[0].length();
        HashMap<String, List<String>> dp = new HashMap<>();
        for (String word : words) {
            for (int end = 0; end <= N; end++) {
                String prefix = word.substring(0, end);
                if (!dp.containsKey(prefix))
                    dp.put(prefix, new ArrayList<>());
                dp.get(prefix).add(word);
            }
        }
        // 上面的都是制作前缀表
        List<List<String>> res = new ArrayList<>();
        f(0, N, dp, new LinkedList<>(), res);
        return res;
    }

    // i, 当前填到第i号单词，从0开始，填到N-1
    // dp 前缀所拥有的单词
    // path, 之前填过的单词, 0...i-1填过的
    // res, 收集答案
    private static void f(int i, int N, HashMap<String, List<String>> dp,
                          LinkedList<String> path, List<List<String>> res) {
        if (i == N) {
            res.add(new ArrayList<>(path));
        } else {
            // 先求0～i-1以填好的单词对i的限制
            StringBuilder limit = new StringBuilder();
            for (String word : path) {
                limit.append(word.charAt(i));
            }
            // 填入的第i单词的前缀必须是prefix
            String prefix = limit.toString();
            if (dp.containsKey(prefix)) {
                for (String satisfied : dp.get(prefix)) {
                    path.add(satisfied);
                    f(i + 1, N, dp, path, res);
                    path.pollLast();
                }
            }
        }
    }

}
