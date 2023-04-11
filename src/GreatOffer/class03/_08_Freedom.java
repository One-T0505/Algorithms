package class03;

// leetCode514
// 给定一个字符串ring，表示刻在老式转盘式的座机表盘上的编码；给定另一个字符串key，表示需要拼写的关键词。您需要算出
// 能够拼写关键词中所有字符的最少步数。最初，ring的第一个字符与 12:00 方向对齐。您需要顺时针或逆时针旋转 ring 以
// 使key 的一个字符在 12:00 方向对齐，然后按下中心按钮，以此逐个拼写完key中的所有字符。旋转ring拼出 key 字符
// key[i]的阶段中：您可以将ring顺时针或逆时针旋转一个位置，计为1步。旋转的最终目的是将字符串ring的一个字符与
// 12:00 方向对齐，并且这个字符必须等于字符key[i]。如果字符key[i]已经对齐到12:00方向，您需要按下中心按钮进行拼写，
// 这也将算作1步。按完之后，您可以开始拼写key的下一个字符（下一阶段）, 直至完成所有拼写。


// 1 <= ring.length, key.length <= 100
// ring 和 key 只包含小写英文字母
// 保证 字符串 key 一定可以由字符串  ring 旋转拼出

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class _08_Freedom {

    // 暴力递归版
    public static int findRotateStepsV1(String ring, String key) {
        if (ring == null || ring.length() == 0 || key == null || key.length() == 0)
            return 0;
        char[] aim = key.toCharArray();
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        HashSet<Character> set = new HashSet<>();
        int N = ring.length();
        // 填写map表
        for (int i = 0; i < N; i++) {
            char cur = ring.charAt(i);
            set.add(cur);
            if (!map.containsKey(cur))
                map.put(cur, new ArrayList<>());
            map.get(cur).add(i);
        }
        // 如果aim中有字符是ring中不包含的，那就不可能搞定这个字符串，直接返回。但是题目说了肯定可以完成，所以我们
        // 不需要写下面这段代码。写上去只是为了训练思维的严密
        for (char c : aim) {
            if (!set.contains(c))
                return 0;
        }
        return process1(0, 0, aim, map, N);
    }


    // pre：转盘上，指针上一次指着的位置
    // index：现在要搞定目标字符串aim中的哪一个
    // map：key表示一种字符   value是一个数组，表示该字符在转盘的哪些位置，因为一种字符可能在ring中多次出现所以需要数组
    // N：ring的长度
    // 该方法返回：搞定index...及其后面所有字符需要的最少步数。
    public static int process1(int pre, int index, char[] aim, HashMap<Character, ArrayList<Integer>> map,
                               int N) {
        if (index == aim.length)
            return 0;
        // 还有字符需要处理
        char cur = aim[index];
        int res = Integer.MAX_VALUE;
        for (int pos : map.get(cur)) {
            res = Math.min(dial(pre, pos, N) + 1 + process1(pos, index + 1, aim, map, N), res);
        }
        return res;
    }


    // 给一个ring字符串，即便有重复字符也不要紧，就把它想像成一个首尾相连的环 0～N-1位置。那么将转盘从i位置转到j位置
    // 的最短距离是有固定公式的，要么逆时针要么顺时针。这个功能就是下面的方法。size表示转盘的大小，也就是ring的长度
    private static int dial(int i, int j, int size) {
        return Math.min(Math.abs(i - j), Math.min(i, j) + size - Math.max(i, j));
    }
    // ======================================================================================================


    // 记忆化搜索
    public static int findRotateStepsV2(String ring, String key) {
        if (ring == null || ring.length() == 0 || key == null || key.length() == 0)
            return 0;
        char[] chars = ring.toCharArray();
        char[] aim = key.toCharArray();
        int N = aim.length;
        int M = chars.length;
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        HashSet<Character> set = new HashSet<>();
        // 填写map表
        for (int i = 0; i < M; i++) {
            set.add(chars[i]);
            if (!map.containsKey(chars[i]))
                map.put(chars[i], new ArrayList<>());
            map.get(chars[i]).add(i);
        }
        // 如果aim中有字符是ring中不包含的，那就不可能搞定这个字符串，直接返回
        for (char c : aim) {
            if (!set.contains(c))
                return 0;
        }
        int[][] cache = new int[N + 1][M];
        for (int i = 0; i <= N; i++)
            Arrays.fill(cache[i], -1);
        return process2(0, 0, aim, map, M, cache);
    }

    private static int process2(int pre, int index, char[] aim, HashMap<Character, ArrayList<Integer>> map,
                                int M, int[][] cache) {
        if (cache[index][pre] != -1)
            return cache[index][pre];
        int res = Integer.MAX_VALUE;
        if (index == aim.length)
            res = 0;
        else { // 还有字符需要处理
            for (int pos : map.get(aim[index])) {
                res = Math.min(dial(pre, pos, M) + 1 + process2(pos, index + 1, aim, map, M, cache), res);
            }
        }
        cache[index][pre] = res;
        return res;
    }
    // ======================================================================================================


    // 动态规划    可变参数只有pre和index，所以缓存表二维即可，并且范围可以确定且不大
    public static int findRotateStepsV3(String ring, String key) {
        if (ring == null || ring.length() == 0 || key == null || key.length() == 0)
            return 0;
        char[] chars = ring.toCharArray();
        char[] aim = key.toCharArray();
        int N = aim.length;
        int M = chars.length;
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        HashSet<Character> set = new HashSet<>();
        // 填写map表
        for (int i = 0; i < M; i++) {
            set.add(chars[i]);
            if (!map.containsKey(chars[i]))
                map.put(chars[i], new ArrayList<>());
            map.get(chars[i]).add(i);
        }
        // 如果aim中有字符是ring中不包含的，那就不可能搞定这个字符串，直接返回
        for (char c : aim) {
            if (!set.contains(c))
                return 0;
        }
        // 行表示处理到aim哪个字符，列表示指针上一次的指向
        int[][] cache = new int[N + 1][M];
        for (int i = N - 1; i >= 0; i--) {
            for (int j = M - 1; j >= 0; j--) {
                int res = Integer.MAX_VALUE;
                for (int pos : map.get(aim[i]))
                    res = Math.min(dial(j, pos, M) + 1 + cache[i + 1][pos], res);
                cache[i][j] = res;
            }
        }
        return cache[0][0];
    }
}
