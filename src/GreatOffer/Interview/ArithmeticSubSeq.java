package GreatOffer.Interview;

import java.util.ArrayList;
import java.util.HashMap;

// 给你一个整数数组 nums ，返回 nums 中所有等差子序列的数目。
// 如果一个序列中至少有三个元素，并且任意两个相邻元素之差相同，则称该序列为等差序列。
// 例如，[1, 3, 5, 7, 9]、[7, 7, 7, 7] 和 [3, -1, -5, -9] 都是等差序列。
// 再例如，[1, 1, 2, 5, 7] 不是等差序列。
// 题目数据保证答案是一个 32 bit 整数。

// 数据规模
// 1  <= nums.length <= 1000
// -2^31 <= nums[i] <= 2^31 - 1

public class ArithmeticSubSeq {

    // 对于每个元素我们都需要制作一张哈希表，为的是存储以当前元素为子序列结尾的等差序列有几个。具体是这样的：
    // key: 某个差值   value: 以当前元素作为子序列结尾，并且差值为key的等差序列有几个
    // 不过这里的等差序列的长度只用>=2即可
    // 当来到i位置时，和前面的元素j做差得到一个差值diff，然后在j的表里查询差值为diff的子序列有多少个，假如为k个，
    // 那么就应该在i的表里填上(diff, k + 1) 这条记录。
    public static int numberOfArithmeticSlices(int[] nums) {
        int N = nums.length;
        // 每个元素都对应一张哈希表
        ArrayList<HashMap<Integer, Integer>> dp = new ArrayList<>();
        int res = 0;
        for (int i = 0; i < N; i++) {
            // 每到一个新元素就首先把表加进去
            dp.add(new HashMap<>());
            for (int j = i - 1; j >= 0; j--) {
                //  -2^31 <= nums[i] <= 2^31 - 1  所以差值可能溢出
                long diff = (long) nums[i] - (long) nums[j];
                if (diff <= Integer.MIN_VALUE || diff >= Integer.MAX_VALUE)
                    continue;
                int dif = (int) diff;
                // 查询j的表里以diff为差值的等差子序列有几个，表里存放的都是长度>=2的
                int count = dp.get(j).getOrDefault(dif, 0);
                // count个子序列都是长度>=2的，所以每一个子序列在最后拼上nums[i]后，都是长度>=3的,这才符合题意
                res += count;
                // 但是存放表里的却要是长度>=2的，所以才要+1
                // 比如 nums = [1, 3, 5, 7]  5对应的表应该是: (2, 2)  (4, 1)  差值为2的子序列有两个:
                //      [3, 5]  [1, 3, 5]
                //      来到7时，和5得到一个差值2，去查5的表里差值为2的序列为2，所以应该在7的表里添加：(2, 3)
                //      [1, 3, 5, 7]  [3, 5, 7]  [5, 7]
                // 前面的这坨 dp.get(i).getOrDefault(dif, 0)  只是表里本来有的，要累加
                // 比如 nums = [4, 4, 9]  9和1位置的4得到差值5，所以会修改9的表里key为5的这条记录，
                //     当9和下一个0位置的4也得到一个差值5，所以依然会修改9的表里key为5的记录，但是不能直接覆盖
                //     应该累加。
                dp.get(i).put(dif, dp.get(i).getOrDefault(dif, 0) + count + 1);
            }
        }
        return res;
    }
}
