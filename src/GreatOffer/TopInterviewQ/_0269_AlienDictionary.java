package GreatOffer.TopInterviewQ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

// 给一个字符串数组，其排列顺序可能是按照某种字典序排列的，也有可能是随机乱排的；如果存在某种序列规则，请以字符串形式
// 返回该新的规则，若不存在某种排序规则则返回空字符串。
// eg：["gd", "fdc", "lc"]   从这三个字符串的排列来看，我们可以确定地知道：g<f<l  所以返回gfl

public class _0269_AlienDictionary {

    // 实际上这道题目考察的依然是拓扑排序。我们两两比较字符串，将可以确定的字符大小关系按照图的形式连接起来，小的
    // 连大的；最后将整个图按照拓扑排序，看是否能全部输出

    public static String alienDictionary(String[] words) {
        if (words == null || words.length == 0)
            return "";
        int N = words.length;
        // 每个字符对应的入度
        HashMap<Character, Integer> map = new HashMap<>();
        // 一开始所有字符入度为0，边的关系是要靠比较大小才能确定的
        for (String word : words) {
            for (char c : word.toCharArray())
                map.put(c, 0); // 重复的字符会覆盖，所以map中记录了所有的不一样的字符
        }
        // key为某个字符，value对应的就是比key大的字符，这里说的大，是按照给出的字符串数组来决定的。
        HashMap<Character, HashSet<Character>> graph = new HashMap<>();
        // 两个相邻单词比大小
        for (int i = 0; i < N - 1; i++) {
            char[] cur = words[i].toCharArray();
            char[] next = words[i + 1].toCharArray();
            int len = Math.min(cur.length, next.length);  // 找出较短的那个
            int j = 0;
            for (; j < len; j++) {
                if (cur[j] != next[j]) { // 如果碰到了两个字符不等，那么后一个字符串的字符必然是大于当前字符串的
                    if (!graph.containsKey(cur[j]))
                        graph.put(cur[j], new HashSet<>());
                    if (!graph.get(cur[j]).contains(next[j])) {
                        graph.get(cur[j]).add(next[j]);
                        map.put(next[j], map.get(next[j]) + 1);
                    }
                    break;  // 一旦碰到两个字符不等，那么比大小就可以终止了
                }
            }
            // 从for循环出来，有可能是j==len了，也有可能是j还没碰到任何一个字符串的长度
            if (j < cur.length && j == next.length)
                return "";
        }
        // 从这里开始，图的关系已经建立好了，现在要开始跑拓扑排序算法了
        StringBuilder sb = new StringBuilder();
        Queue<Character> queue = new LinkedList<>();
        for (Character c : map.keySet()) {
            if (map.get(c) == 0)
                queue.add(c);
        }
        while (!queue.isEmpty()) {
            char poll = queue.poll();
            sb.append(poll);
            if (graph.containsKey(poll)) {
                for (char neighbor : graph.get(poll)) {
                    map.put(neighbor, map.get(neighbor) - 1);
                    if (map.get(neighbor) == 0)
                        queue.add(neighbor);
                }
            }
        }
        return sb.length() == map.size() ? sb.toString() : "";
    }
}
