package GreatOffer.Interview;

import java.util.HashMap;
import java.util.TreeSet;

// 给你一个长度为 2 * n 的整数数组。你需要将 nums 分成两个长度为 n 的数组，分别求出两个数组的和，
// 并最小化两个数组和之差的绝对值。nums 中每个元素都需要放入两个数组之一。
// 请你返回最小的数组和之差。
// 1 <= n <= 15
// nums.length == 2 * n
// -10^7 <= nums[i] <= 10^7

public class PickSum {

    // 由题目数据量发现数组长度最多为30，但是元素值的范围比较大，所以如果用背包模型，那需要一个二维表，表的列表示
    // 累加和，那么这个表就会非常大，因为数值比较大，这样是不可行的。
    // 可以使用分治，将数组分成两半，实现一个递归函数f，用于找出所有的累加和。

    public static int minimumDifference(int[] nums) {
        int N = nums.length;  // 必然是偶数
        int half = N >> 1;
        // 将左半部分所有的累加和做出来
        HashMap<Integer, TreeSet<Integer>> ldp = new HashMap<>();
        f(nums, 0, half, 0, 0, ldp);
        // 将右半部分所有的累加和做出来
        HashMap<Integer, TreeSet<Integer>> rdp = new HashMap<>();
        f(nums, half, N, 0, 0, rdp);
        // 统计数组累加和
        int sum = 0;
        for (int e : nums)
            sum += e;
        int res = Integer.MAX_VALUE;
        // 枚举所有情况：左侧选0个，右侧选N/2个   左侧选1个，右侧选N/2-1个
        // 总体思路就是左侧选一些，然后从右侧寻找最匹配的
        for (int leftNum : ldp.keySet()) {
            for (int leftSum : ldp.get(leftNum)) {
                // 如果数组长度为20，如果leftNum是4，那么就需要从右半部分选择6个数；
                // 如果数组累加和为100，左侧选的这4个数的累加和为80，那么我们就要去右侧rdp.get(6)里面去找<=-30中最大的数
                // 这样能保证从做右侧选出的10个数的累加和最接近50，这样就和剩余的没选的10个数的和差值最小了。
                // 当然也可以选择>=-30的，因为差值的绝对值，这里说明确了，下面就可以不用绝对值了
                Integer rightSum = rdp.get(half - leftNum).floor((sum >> 1) - leftSum);
                if (rightSum != null) { // 如果存在
                    int pickSum = leftSum + rightSum;
                    int restSum = sum - pickSum;
                    // 因为我们找的是<=-30的，所以pickSum <= 50  必然是restSum >= pickSum的
                    res = Math.min(res, restSum - pickSum);
                }
            }
        }
        return res;
    }


    // 如果arr长度为8   那么 就将其分为：0 1 2 3    4 5 6 7
    // f(arr, 0, 4)  [0,4)
    // f(arr, 4, 8)  [4,8)
    // arr[i....end-1]这个范围上，去做选择   pick挑了几个数！
    // sum  挑的这些数累加和是多少！
    // dp记录结果  HashMap<Integer, TreeSet<Integer>> dp
    // key -> 挑了几个数，比如挑了3个数，但是形成累加和可能有多个！  value -> 有序表，都记下来！
    // 整个过程，纯暴力！一半的数组长度最多为15， 2^15 -> 3万多，纯暴力跑完，依然很快！
    private static void f(int[] nums, int i, int end, int pick, int sum,
                          HashMap<Integer, TreeSet<Integer>> dp) {
        if (i == end) {
            if (!dp.containsKey(pick))
                dp.put(pick, new TreeSet<>());
            dp.get(pick).add(sum);
        } else {
            f(nums, i + 1, end, pick, sum, dp);  // 不选当前数
            f(nums, i + 1, end, pick + 1, sum + nums[i], dp);  // 选当前数
        }
    }


    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();
        set.add(15);
        set.add(6);
        set.add(9);
        System.out.println(set.floor(10));
    }
}
