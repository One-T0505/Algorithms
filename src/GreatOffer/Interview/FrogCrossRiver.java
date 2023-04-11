package GreatOffer.Interview;

import java.util.HashMap;
import java.util.HashSet;

// 一只青蛙想要过河。假定河流被等分为若干个单元格，并且在每一个单元格内都有可能放有一块石子（也有可能没有）。
// 青蛙可以跳上石子，但是不可以跳入水中。给你石子的位置列表 stones（用单元格序号升序表示）， 请判定青蛙能
// 否成功过河（即能否在最后一步跳至最后一块石子上）。开始时， 青蛙默认已站在第一块石子上，并可以假定它第一步
// 只能跳跃 1 个单位（即只能从单元格 1 跳至单元格 2 ）
// 如果青蛙上一步跳跃了 k 个单位，那么它接下来的跳跃距离只能选择为 k - 1、k 或 k + 1 个单位。 另请注意，
// 青蛙只能向前方（终点的方向）跳跃。

public class FrogCrossRiver {

    public static boolean canCross(int[] stones) {
        HashSet<Integer> set = new HashSet<>();
        for (int s : stones)
            set.add(s);
        // 这个是记忆化缓存的dp表
        HashMap<Integer, HashMap<Integer, Boolean>> dp = new HashMap<>();
        return jump(1, 1, stones[stones.length - 1], set, dp);
    }


    // cur表示当前的位置  pre表示来跳多少步到的cur  end是固定参数 是最后一块石头的位置
    // set是固定参数，里面存放的是所有石头的位置
    // dp是记忆化缓存，里面存放的是所有递归的结果。key: 表示cur的所有可能性   每个key对应一个哈希表
    // 哈希表的key对应的是pre这个参数，value表示的是 (cur, pre)这种组合下能否成功过河
    // 该方法返回：以pre的方式来到cur，能否最终成功过河
    private static boolean jump(int cur, int pre, int end, HashSet<Integer> set,
                                HashMap<Integer, HashMap<Integer, Boolean>> dp) {
        if (cur == end)
            return true;
        if (!set.contains(cur))  // 如果当前位置不在集合里，说明cur表示在河里
            return false;
        // 执行到这里说明此时cur是在一个石头的位置，并且cur不是终点的石头

        if (dp.containsKey(cur) && dp.get(cur).containsKey(pre)) // 如果这种情况已经跑过了，则直接返回结果
            return dp.get(cur).get(pre);

        boolean res = (pre > 1 && jump(cur + pre - 1, pre - 1, end, set, dp)) ||
                jump(cur + pre, pre, end, set, dp) ||
                jump(cur + pre + 1, pre + 1, end, set, dp);
        // 返回结果前先存储答案
        if (!dp.containsKey(cur))
            dp.put(cur, new HashMap<>());
        if (!dp.get(cur).containsKey(pre))
            dp.get(cur).put(pre, res);

        return res;
    }
}
