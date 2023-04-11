package Basic.SlidingWindow;

// 给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。

import java.util.Arrays;

public class _0003_Code {

    // 思路：用滑动窗口+左老师讲的欠债还钱表
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0)
            return 0;
        if (s.length() == 1)
            return 1;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int res = 1;
        int L = 0, R = 0;
        // 词频统计表
        int[] dp = new int[256];
        while (R < N){
            while (R < N && dp[chs[R]] == 0) // 当前字符没出现过
                dp[chs[R++]]++;
            // 此时R来到了第一个重复出现的地方 或者直接越界
            res = Math.max(res, R - L);
            if (R == N)
                break;
            while (L < R && chs[L] != chs[R]) // 没删除到那个R因此停止的重复字符
                dp[chs[L++]]--;
            dp[chs[L]]--;
            if (L < R)
                L++;
        }

        return res;
    }



    // 动态规划
    // 既然是选子串，那就要回想起之前选子串的一些思维定式：这里，我们从左至右遍历每个字符，以每个字符作为尾部，向左
    // 找出不能再扩的位置。遍历下来之后，最大的长度就是结果。假设i-1位置已经找到了最远位置j，也就是说j～i-1是没有重复字符的，
    // 并且j-1位置的字符必然在j～i-1出现过；那么此时我们来决定i的最左位置是多少。i的最左位置受两个因素影响：i-1位置的最左位置、
    // i位置字符前一次出现的位置。 x表示i位置的字符上一次出现的位置，如果是图1的情况，那么i位置最左扩的位置也是j；如果是
    // 图2的情况，那么i位置最左能扩到x+1。弄清楚两种情况后，转移方程就出来了，就可以写动态规划了。并且后一个元素只依赖前一个
    // 元素，所以只需要有限几个元素就可以完成缓存，不需要和字符串一样大的数组来做缓存.
    //
    //     x    j     i-1  i           j   x  i-1  i
    //          |______|               |_______|
    //             图1                     图2

    public static int longestNoRepeatSubString(String s){
        if (s == null || s.length() < 1)
            return 0;
        char[] chars = s.toCharArray();
        // 256可以表示所有字符，lastPos[i]=v 表示i字符上次出现在字符串的v位置
        int[] lastPos = new int[256];
        Arrays.fill(lastPos, -1);  // 为什么都设置为-1，下面会解释
        int N = chars.length;
        int pre = 1;
        int cur;
        int res = 0;
        // 字符串的第一个字符上次出现在字符串的0号位置
        lastPos[chars[0]] = 0;
        for (int i = 1; i < N; i++) { // 为啥从1开始，因为0位置向左扩最多到自己，就是1的长度，所以从1开始，并且pre=1
            // lastPos[chars[i]]如果是-1，就表示没出现过，可以扩到最左0位置；从0～i的长度刚好是：i+1 --> i-(-1)
            // 如果不是-1，那就刚好算出其长度
            int p1 = i - lastPos[chars[i]];
            // p2就表示上图2的情况下，i位置扩出的长度，然后两个取最小值
            int p2 = pre + 1;
            cur = Math.min(p1, p2);
            pre = cur;
            res = Math.max(res, cur);
            lastPos[chars[i]] = i;
        }
        return res;
    }


    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("au"));
    }
}
