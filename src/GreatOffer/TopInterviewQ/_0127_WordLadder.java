package GreatOffer.TopInterviewQ;

import java.util.*;

// 字典 wordList 中从单词 beginWord 和 endWord 的转换序列是一个按下述规格形成的序列
// beginWord -> s1 -> s2 -> ... -> sk： 每一对相邻的单词只差一个字母。
// 对于 1 <= i <= k 时，每个 si 都在 wordList 中。注意， beginWord 不需要在 wordList 中。
// sk == endWord
// 给你两个单词 beginWord 和 endWord 和一个字典 wordList，返回从 beginWord 到 endWord 的最短转换序列
// 中的单词数目。如果不存在这样的转换序列，返回 0。

public class _0127_WordLadder {


    public int ladderLength(String beginWord, String endWord, List<String> wordList) {

        wordList.add(beginWord);

        // key 为某个字符串   value是一个数组，里面放的每个字符串都是字典中和key只需要转换一个字母就能得到的
        HashMap<String, List<String>> nexts = getNexts(wordList);

        // key为某个字符串  value表示该字符串和 beginWord 的距离，就是 beginWord 需要转换几个字母才能得到这个key
        HashMap<String, Integer> distance = new HashMap<>();
        distance.put(beginWord, 0);
        HashSet<String> set = new HashSet<>();
        set.add(beginWord);
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        while (!queue.isEmpty()) {
            String cur = queue.poll();
            int dis = distance.get(cur);
            for (String next : nexts.get(cur)) {
                if (next.equals(endWord))
                    return dis + 1;
                if (!set.contains(next)) {
                    set.add(next);
                    queue.add(next);
                    distance.put(next, dis + 1);
                }
            }
        }
        return 0;
    }


    private HashMap<String, List<String>> getNexts(List<String> wordList) {

        HashSet<String> set = new HashSet<>(wordList);

        HashMap<String, List<String>> res = new HashMap<>();

        for (String word : wordList)
            res.put(word, getNext(word, set));

        return res;
    }


    // 将 dict 中只和 word 需要转一个字母就可以得到的单词全部找出来
    private List<String> getNext(String word, HashSet<String> dict) {
        ArrayList<String> res = new ArrayList<>();
        char[] chs = word.toCharArray();

        for (int i = 0; i < chs.length; i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != chs[i]) {
                    char tmp = chs[i];
                    chs[i] = c;
                    if (dict.contains(String.valueOf(chs)))
                        res.add(String.valueOf(chs));
                    chs[i] = tmp;
                }
            }
        }
        return res;
    }
}
