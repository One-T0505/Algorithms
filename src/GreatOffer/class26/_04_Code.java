package GreatOffer.class26;

import java.util.*;

// 按字典 wordList 完成从单词 beginWord 到单词 endWord 转化，一个表示此过程的转换序列是形式上像
// beginWord -> s1 -> s2 -> ... -> sk 这样的单词序列，并满足：
//  1.每对相邻的单词之间仅有单个字母不同。
//  2.转换过程中的每个单词 si（1 <= i <= k）必须是字典 wordList 中的单词。注意，beginWord 不必是字典 wordList
//    中的单词。
//  3.sk == endWord
// 给你两个单词 beginWord 和 endWord ，以及一个字典 wordList 。请你找出并返回所有从 beginWord 到 endWord 的
// 最短转换序列 ，如果不存在这样的转换序列，返回一个空列表。每个序列都应该以单词列表 [beginWord, s1, s2, ..., sk]
// 的形式返回。

// 1 <= beginWord.length <= 5
// endWord.length == beginWord.length
// 1 <= wordList.length <= 500
// wordList[i].length == beginWord.length
// beginWord、endWord 和 wordList[i] 由小写英文字母组成
// beginWord != endWord
// wordList 中的所有单词互不相同

public class _04_Code {

    // 该题目隐含了一个信息：wordList里的所有单词、beginWord长度都一样。
    // 先生成一个哈希表  key：某个单词   value：一个列表，每个元素都是wordList里和key对应单词仅有一个字母不同的单词。
    // 这个任务就存在优化。比较暴力的做法是：遍历每个单词，让它去和剩下的所有去比较，然后逐位对比。假设wordList长度为N，
    // 每个单词长度为k，那么该方法时间复杂度为O(N^2*k)
    // 优化的方法是：把所有元素丢到哈希集合里，然后遍历每个单词，因为单词长度固定为k，且每位固定都是小写英文字母，所以
    // 遍历每位，让其变成a,b,..,z，看集合中是否存在，在集合中查找长度为k的一个字符串的时间复杂度为O(k)，这里是不是有
    // 疑惑，因为之前讲过哈希表的增删改查时间复杂度都是O(1)！！！
    // 确实，因为之前处理的都是int型或者long，这些数据在内存中分配的位数是固定的，我们要算哈希值，首先要遍历完整个数据，
    // 但是因为int型或者long长度，所以时间复杂度为O(1)；但是字符串就不行了，长度不固定，所以至少要遍历完字符串才能算出
    // 哈希值，再去表里查，所以这个优化的方法时间复杂度为：O(26*k*k)-->O(k^2)  再根据题目给出的数据规模，
    // 该方法会快很多


    // 主方法
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        // 因为题目说beginWord不必在列表里，就是说列表里一开始可能有也可能没有beginWord，我们先把beginWord
        // 加进列表里，因为后面利用该列表生成领接表时会把列表所有元素放在集合里过滤
        wordList.add(beginWord);
        // 生成领接表
        HashMap<String, List<String>> neighbors = getNeighbors(wordList);
        // 生成距离表
        HashMap<String, Integer> fromDistance = getDistance(beginWord, neighbors);
        List<List<String>> res = new ArrayList<>();
        // 先查看下距离表中，是否存在endWord，如果不存在就说明肯定到不了
        if (!fromDistance.containsKey(endWord))
            return res;
        HashMap<String, Integer> toDistance = getDistance(endWord, neighbors);
        LinkedList<String> path = new LinkedList<>();
        getShortestPaths(beginWord, endWord, neighbors, fromDistance, toDistance, path, res);
        return res;
    }



    private static HashMap<String, List<String>> getNeighbors(List<String> wordList) {
        HashSet<String> words = new HashSet<>(wordList);
        HashMap<String, List<String>> neighbors = new HashMap<>();
        for (String s : wordList) {
            neighbors.put(s, findNeighbors(s, words));
        }
        return neighbors;
    }



    // 找出s只需要变动一个字符就能得到的在words中的单词，将所有这样的单词放在一个List中返回
    private static List<String> findNeighbors(String s, HashSet<String> words) {
        ArrayList<String> res = new ArrayList<>();
        char[] chs = s.toCharArray();
        for (char cur = 'a'; cur <= 'z'; cur++) {
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] != cur) {
                    char tmp = chs[i];
                    chs[i] = cur;
                    if (words.contains(String.valueOf(chs)))
                        res.add(String.valueOf(chs));
                    chs[i] = tmp;
                }
            }
        }
        return res;
    }
    // 上面的两个方法仅仅完成了第一步，生成所有的领接信息
    // ============================================================================================


    // 得到了最终的neighbors信息后，它的结构非常类似图的结构，比如：
    //         bbc —— bbd
    //       /       /
    //     abc -- abd
    //       \    /
    //        abe
    // 我们现在就要生成一张距离表，key：某个字符串  value：距离beginWord的距离
    // 如果beginWord == abc， 那么bbd和它的距离为2     生成距离表的方法就是最经典的图的宽度优先遍历

    private static HashMap<String, Integer> getDistance(String beginWord,
                                                        HashMap<String, List<String>> neighbors) {
        HashMap<String, Integer> distance = new HashMap<>();
        // 自己到自己的距离为0
        distance.put(beginWord, 0);
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);
        HashSet<String> set = new HashSet<>();
        set.add(beginWord);
        while (!queue.isEmpty()) {
            String cur = queue.poll();
            for (String next : neighbors.get(cur)) {
                if (!set.contains(next)) {
                    distance.put(next, distance.get(cur) + 1);
                    queue.add(next);
                    set.add(next);
                }
            }
        }
        return distance;
    }
    // 完成了上面两个小任务后，才能开始主方法
    // =========================================================================================



    // 生成最短接龙路径
    // cur 表示当前变到了哪个单词, end是固定参数就是endWord
    private static void getShortestPaths(String cur, String endWord,
            HashMap<String, List<String>> neighbors, HashMap<String, Integer> fromDistance,
            HashMap<String, Integer> toDistance, LinkedList<String> path, List<List<String>> res) {
        path.add(cur);
        if (endWord.equals(cur))
            res.add(new ArrayList<>(path));
        else {
            for (String next : neighbors.get(cur)) {
                // 从每一个单词有很多邻居，有些邻居是指导我们走向end的，有些则不是，我们必须要保证每一步
                // 走得的确是在前行，才能找到最短路径。假如：begin是abc，end是xyz，查询距离表可知距离为3
                //     xbc —— xbd —— xyd
                //   /    \          /           我们下一步能走向哪里是依靠neighbors这个表的，这个表
                // abc     xbz —— xyz            包含的是双向信息，也就是说当我们从abc查询邻居表后，我们
                //  |       |                    可以走向xbc、abf；根据abf查询邻居表时，还会查到abf的邻居
                // abf  —— abz                   也有abc，这样就找不到最短路径了，所以我们要依靠距离表，保证
                //                               来到的每一个点确实离起点又远了一步，这样的点才有必要走，否则
                // 就不走，这样能极大地加速。
                if (fromDistance.get(next) == fromDistance.get(cur) + 1 &&
                        toDistance.get(next) == toDistance.get(cur) - 1)
                    getShortestPaths(next, endWord, neighbors, fromDistance, toDistance, path, res);
            }
        }
        path.pollLast();
    }





    public static void main(String[] args) {
        String[] wordList = {"hot", "dot", "dog", "lot", "log", "cog"};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(wordList));
    }

}
