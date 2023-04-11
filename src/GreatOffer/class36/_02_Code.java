package GreatOffer.class36;

import java.util.HashMap;

// 来自京东
// 把一个01字符串切成多个部分，要求每一部分的0和1比例一样，同时要求尽可能多的划分
// 比如: 01010101
// 01 01 01 01 这是一种切法，0和1比例为1:1
// 0101 0101 也是一种切法，0和1比例为1 : 1
// 两种切法都符合要求，但是那么尽可能多的划分为第一种切法， 部分数为4
// 比如: 00001111
// 只有一种切法就是 00001111 整体作为一块，那么尽可能多的划分，部分数为1
// 给定一个01字符串str，假设长度为N,要求返回一个长度为N的数组ans
// 其中ans[i] = str[0...i] 这个前缀串，要求每一部分的0和1比例一样，同时要求尽可能多的划分下，部分数是多少
// 输入: str: = "010100001"  输出:ans=[1,1,1,2,1,2,1,1,3]

public class _02_Code {

    // 思路：首先遍历字符串，分别统计0和1的数量，并将其比例约成最简形式，假设最简形式用a表示。想想看这个答案数组
    // 如果某个前缀可以切分的话，那么每部分的0和1的比例必然等于全局比例a。假设全体可以切分成若干比例相等的部分，假设
    // 每部分比例为b，那么每一部分的比例都为b，最后全局比例为a，那么b一定等于a。
    // 所以分析到这里，我们得到了全局比例a，然后就要去每个位置查询前缀串的比例，看最终有几个前缀串的比例为a，
    // 如果有k个比例为a，那么全局就可以分成k个部分。
    // 比例这个事情和之前讲过的直线共点问题一样，让哈希表套哈希表，key表示最简形式分子，val对应一张哈希表，
    // 该哈希表的key是当前分子下的分母，而val则是该最简形式的比例出现了多少次。

    public static int[] split(int[] arr) {
        // key : 分子   val : 属于key的分母表
        HashMap<Integer, HashMap<Integer, Integer>> dp = new HashMap<>();
        int N = arr.length;
        int[] res = new int[N];
        int zero = 0;
        int one = 0;
        for (int i = 0; i < N; i++) {
            zero += arr[i] == 1 ? 0 : 1;
            one += arr[i] == 1 ? 1 : 0;
            // 说明从0..i都全部是一种数字，那么最优化分就是将其单独字符分成一份，所以一共可以分成i+1份
            if (zero == 0 || one == 0)
                res[i] = i + 1;
            else {  // 都不为0
                int gcd = gcd(zero, one);
                int a = zero / gcd;  // 最简形式分子
                int b = one / gcd;  // 最简形式分母
                if (!dp.containsKey(a))
                    dp.put(a, new HashMap<>());
                dp.get(a).put(b, dp.get(a).getOrDefault(b, 0) + 1);
                res[i] = dp.get(a).get(b);
            }
        }
        return res;
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
