package GreatOffer.class22;

import java.util.HashMap;

// 给定一个没有重复值的正整数数组，要求找到两个集合，使其两个集合内所有的累加和相等，使累加和尽量大。
// 两个集合不一定要完全包含整个数组。返回能找到的最大累加和。
// eg：[1, 2, 3, 5]  两个集合可以是：{1, 2}  {3}  累加和相等，都为3；但是 {2, 3} {5} 这样的两个集合累加和也相等
// 并且和为5，比3更大，所以结果返回5。

public class _05_Code {

    // 这里需要用到哈希表，key：两个集合累加和的差值的绝对值  value：在key对应的差值下较小的那个集合的累加和
    // 比如：(3, 5)  表示两个集合的累加和差值为3，较小的那个集合的累加和为5，那么另一个集合的累加和就是8
    // 最后返回哈希表中key为0的值就是结果。
    // 一开始的时候需要在哈希表中提前存放(0,0)这条记录，一开始没有遍历数组时，两个集合都是空的。
    // 那如何使循环运转起来呢？
    // 假设进行到某个时刻时，哈希表dp中的记录有：(3, 4) (1, 5) (7, 2)   当前遍历到的数组元素为3
    // 我们需要另一个哈希表tmp来先拷贝dp中所有的老纪录，然后遍历每条记录，当取出(3,4)这条记录时，我们应该将其
    // 还原为两个集合的累加和：[7,4]，然后当前元素可以加到任意一个集合里，如果加到前面就形成：[10,4]，那么存入dp
    // 的记录就是(6,4),如果dp中不存在这个key，那么可以直接加入，如果存在，那就比较value的值，如果不能使其提高那就不修改；
    // 3还可以加到后面的集合中形成：(7, 7),然后操作和前面一样。对于每条记录都需要把当前元素尝试加入每个集合里，看形成的新
    // 纪录能否添加到dp中去。一开始（0，0）这条记录的value值很小，但是随着循环进行，value上的0会被不断提高。
    // tmp其实就是把老记录搬过去，然后创造出新纪录添加到dp保存的老记录里。

    public static int maxSum(int[] arr) {
        if (arr == null || arr.length < 3)
            return 0;
        int N = arr.length;
        HashMap<Integer, Integer> dp = new HashMap<>(), tmp;
        // 先置记录
        dp.put(0, 0);
        for (int elem : arr) {
            // 先将老记录全部拷贝
            tmp = new HashMap<>(dp);
            // 遍历所有差值
            for (int diff : tmp.keySet()) {
                // 得到较小集合的累加和
                int minSetSum = tmp.get(diff);
                // 情况1
                // 先将当前元素加到较大集合里，那么差值就会持续扩大，必然是正数。  diff + elem 就是新的差值
                // 将新生成的记录放回dp时还要检查dp中是否已经存在这个key了
                dp.put(diff + elem, Math.max(minSetSum, dp.getOrDefault(diff + elem, 0)));
                // 情况2
                // 把当前元素加到较小的集合里，那么差值会变小，有可能会变成负数，所以要记得加绝对值
                // existed 表示新的key对应的value值，如果dp中存在新的key，那么existed就是对应的实际的值；如果不存在，
                // 那么existed就是0
                int existed = dp.getOrDefault(Math.abs(diff - elem), 0);
                // 下面是重新计算那个较小的集合累加和是多少
                if (diff >= elem)
                    dp.put(diff - elem, Math.max(minSetSum + elem, existed));
                else
                    dp.put(elem - diff, Math.max(diff + minSetSum, existed));
            }
        }
        return dp.get(0);
    }
}
