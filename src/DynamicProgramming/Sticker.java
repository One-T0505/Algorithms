package DynamicProgramming;

import java.util.HashMap;

public class Sticker {
    // 给定一个字符串str， 给定一个字符串类型的数组arr。arr里的每一个字符串，代表一张贴纸，你可以把单个字符剪开使用，
    // 目的是拼出str来。返回需要至少多少张贴纸可以完成这个任务。
    // 例子: str= "babac"，arr = {"ba",","abcd"}
    // 至少需要两张贴纸"ba"和"abcd"， 因为使用这两张贴纸，把每一个字符单独剪开，含有2个a、2个b、1个c。
    // 是可以拼出str的。所以返回2。
    // 其实可以发现：给的目标字符串是什么不重要，只需要把它拆出来分别需要哪些字符以及个数；字符串数组中的贴纸是什么也不重要，只需要
    // 把它完全剪开看它能分别提供什么字符以及数量。

    public static int mainProcess(String[] stickers, String target){
        int N = stickers.length;
        int [][] map = new int[N][26];  // 0~A，1～B
        // 将所有贴纸能提供的字符以及数量存放在map数组中，每一行表示一个数组
        for (int i = 0; i < stickers.length; i++){
            char[] chars = stickers[i].toCharArray();
            for (char c : chars)
                map[i][c - 'a']++;
        }
        // dp傻缓存，如果要组装的字符串target已经算过了，直接返回dp中的值
        // key 表示要组装的字符串， value 表示需要用到的最少数量的贴纸
        HashMap<String, Integer> dp = new HashMap<>();
        dp.put("", 0);
        return process(dp, map, target);
    }

    // 若返回值为-1，则map中的贴纸怎么都无法组出rest
    private static int process(HashMap<String, Integer> dp, int[][] map, String rest) {
        if (dp.containsKey(rest))
            return dp.get(rest);
        int res = Integer.MAX_VALUE;
        int N = map.length;  // 贴纸数量
        // 将剩余字符串也转换为词频表达的方式
        int[] restMap = new int[26];
        char[] restChars = rest.toCharArray();
        for (char c : restChars)
            restMap[c - 'a']++;

        // 枚举让每一个贴纸作为组装rest的第一个贴纸
        for (int i = 0; i < N; i++) {
            // 如果该贴纸连rest的第一个字符都满足不了那就直接结束此轮循环， 否则递归时会栈溢出
            if (map[i][restChars[0] - 'a'] == 0)
                continue;

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 26; j++) {
                // 说明rest需要这个字符
                if (restMap[j] > 0) {
                    // 当前字符串可提供('a' + j)多少个？ 如果不够rest使用，那么在sb中补上还差多少个
                    for (int k = 0; k < Math.max(0, restMap[j] - map[i][j]); k++) {
                        sb.append((char) ('a' + j));
                    }
                }
            }
            // 执行到这里时，sb就组装出了rest使用了一个map[i]字符串后，还需要的所有元素，但顺序已经和之前不同了
            String nextRest = sb.toString();
            int nextRes = process(dp, map, nextRest);
            // 如果 == -1，说明没办法组装，并且res的值不会被修改；这是方法中唯一会修改res值的地方
            if (nextRes != -1)
                res = Math.min(res, nextRes + 1); // +1 是因为第一张贴纸没算进去
        }

        dp.put(rest, res == Integer.MAX_VALUE ? -1 : res);
        return dp.get(rest);
    }

}
