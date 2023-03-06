package DynamicProgramming;

import java.util.HashMap;

// 给定一个字符串str， 给定一个字符串类型的数组arr。arr里的每一个字符串，代表一张贴纸，你可以把单个字符剪开使用，
// 目的是拼出str来。返回需要至少多少张贴纸可以完成这个任务。
// 例子: str= "babac"，arr = {"ba",","abcd"}
// 至少需要两张贴纸"ba"和"abcd"， 因为使用这两张贴纸，把每一个字符单独剪开，含有2个a、2个b、1个c。
// 是可以拼出str的。所以返回2。
// 其实可以发现：给的目标字符串是什么不重要，只需要把它拆出来分别需要哪些字符以及个数；字符串数组中的贴纸是什么也不重要，
// 只需要把它完全剪开看它能分别提供什么字符以及数量。
public class Sticker {

    // 暴力递归尝试可能性
    public static int stickerV1(String[] stickers, String target){
        if (stickers == null)
            return -1;
        if (target == null || target.length() == 0)
            return 0;
        int res = process1(stickers, target);
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    // 该方法表示：用stickers这些贴纸，拼出target最少需要几张贴纸
    private static int process1(String[] stickers, String target) {
        if (target.length() == 0)
            return 0;
        int minus = Integer.MAX_VALUE;
        // 每一张贴纸都可以做第一张贴纸
        for (String sticker : stickers){
            String rest = restStr(target, sticker);
            // 如果目标字符串用了一张贴纸后，剩余字符串的长度和之前一样，就说明该贴纸并不能提供有效字符，直接跳过
            if (rest.length() != target.length()) // 只有不相等时才说明贴纸有效
                minus = Math.min(minus, process1(stickers, rest));
        }
        // minus == Integer.MAX_VALUE 说明所有贴纸都没用；如果有用就加1，因为在尝试谁做第一张贴纸时并没有算上
        // 所以返回的minus的值只有可能是：Integer.MAX_VALUE 或者 剩下用的最少贴纸数+1
        return minus + (minus == Integer.MAX_VALUE ? 0 : 1);
    }

    private static String restStr(String target, String sticker) {
        char[] des = target.toCharArray();
        char[] src = sticker.toCharArray();
        int[] counter = new int[26];  // 计数器，0--a, 25--z 统计目标字符串中一共有多少种字符，每种有多少个
        for (char c : des)
            counter[c - 'a']++;
        for (char c : src){
            if (counter[c - 'a'] > 0)
                counter[c - 'a']--;
        }
        StringBuilder res = new StringBuilder("");
        for (int i = 0; i < counter.length; i++) {
            if (counter[i] != 0)
                res.append(String.valueOf((char) (i + 'a')).repeat(counter[i]));
        }
        return res.toString();
    }
    // ==============================================================================================


    // 对上面的方法做一定优化，并剪枝。但是还没到动态规划,这个方法很有必要。这里用一种特殊的方式来表示贴纸。
    // 申请一个二维数组int stickers[N][26] 来记录所有贴纸的词频即可。
    public static int stickerV2(String[] stickers, String target){
        if (stickers == null)
            return -1;
        if (target == null || target.length() == 0)
            return 0;
        int N = stickers.length;
        int[][] allStickers = new int[N][26];
        // 制作所有贴纸的词频，这样就可以用一个二维矩阵代替字符串数组
        for (int i = 0; i < N; i++) {
            char[] sticker = stickers[i].toCharArray();
            for (char c : sticker)
                allStickers[i][c - 'a']++;
        }
        int res = process2(allStickers, target);
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    // 每一种贴纸都有无穷张，返回搞定target的最少张数
    private static int process2(int[][] allStickers, String target) {
        if (target.length() == 0)
            return 0;
        char[] targetChars = target.toCharArray();
        int[] targetCounter = new int[26];
        for (char c : targetChars)
            targetCounter[c - 'a']++;
        int N = allStickers.length, minus = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            int[] curSticker = allStickers[i]; // 尝试第一张贴纸是谁
            // 最关键的优化(重要的剪枝!这一步也是贪心!)
            // 如果目标字符串的第一个字符，该贴纸有，那再继续，否则直接不做; 肯定是可以避免一些不必要的递归的
            // 贪心策略也可以是别的
            if (curSticker[targetChars[0] - 'a'] > 0){
                StringBuilder sb = new StringBuilder("");
                for (int j = 0; j < 26; j++) {
                    if (targetCounter[j] > 0){
                        // 贴纸提供的字符数量最多=目标字符串中的，如果多于，也只能减到0
                        int nums = Math.max(0, targetCounter[j] - curSticker[j]);
                        sb.append(String.valueOf((char) (j + 'a')).repeat(nums));
                    }
                }
                String rest = sb.toString();
                minus = Math.min(minus, process2(allStickers, rest));
            }
        }
        return minus + (minus == Integer.MAX_VALUE ? 0 : 1);
    }
    // ==============================================================================================


    // 根据上面的递归可以发现，可变参数target是个字符串，可能性太多，不像之前的题目那样申请数组或矩阵就能搞定。
    // 所以这道题改成动态规划不现实，优化到记忆化搜索即可
    public static int stickerV3(String[] stickers, String target){
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
    private static int process(HashMap<String, Integer> cache, int[][] map, String rest) {
        if (cache.containsKey(rest))
            return cache.get(rest);
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
                    sb.append(String.valueOf((char) ('a' + j)).repeat(Math.max(0, restMap[j] - map[i][j])));
                }
            }
            // 执行到这里时，sb就组装出了rest使用了一个map[i]字符串后，还需要的所有元素，但顺序已经和之前不同了
            String nextRest = sb.toString();
            int nextRes = process(cache, map, nextRest);
            // 如果 == -1，说明没办法组装，并且res的值不会被修改；这是方法中唯一会修改res值的地方
            if (nextRes != -1)
                res = Math.min(res, nextRes + 1); // +1 是因为第一张贴纸没算进去
        }
        cache.put(rest, res == Integer.MAX_VALUE ? -1 : res);
        return cache.get(rest);
    }

    public static void main(String[] args) {
        String[] stickers = {"ababc", "b", "ca"};
        String target = "cacbacbbaaac";
        System.out.println(stickerV1(stickers, target));
        System.out.println(stickerV2(stickers, target));
        System.out.println(stickerV3(stickers, target));

    }

}
