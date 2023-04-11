package LeetCode;

/**
 * ymy
 * 2023/4/11 - 13 : 07
 **/


// leetCode307
// 给你一个数组 nums ，请你完成两类查询。其中一类查询要求 更新 数组 nums 下标对应的值
// 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（ 包含 ）的nums元素的 和 ，其中 left <= right
// 实现 NumArray 类：
//  1.NumArray(int[] nums) 用整数数组 nums 初始化对象
//  2.void update(int index, int val) 将 nums[index] 的值 更新 为 val
//  3.int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（ 包含 ）的nums元素的和

// 1 <= nums.length <= 3 * 10^4
// -100 <= nums[i] <= 100
// 0 <= index < nums.length
// -100 <= val <= 100
// 0 <= left <= right < nums.length
// 调用 update 和 sumRange 方法次数不大于 3 * 10^4


public class NumArray {

    public int[] copy;
    public int[] tree;
    public int N;

    public NumArray(int[] nums) {
        N = nums.length + 1;
        copy = new int[N];
        tree = new int[N];
        System.arraycopy(nums, 0, copy, 1, N - 1);
        // 构造出累加和数组
        for (int i = 1; i < N; i++) {
            int mostRight = i & (~i + 1);
            for (int j = i - mostRight + 1; j <= i; j++) {
                tree[i] += copy[j];
            }
        }
    }

    public void update(int index, int val) {
        int i = index + 1;
        int diff = val - copy[i];
        copy[i] = val;
        while (i < N){
            tree[i] += diff;
            i += i & -i;
        }
    }

    public int sumRange(int left, int right) {
        if (left > right || left < 0 || right >= N - 1)
            return Integer.MIN_VALUE;
        return preSum(right + 1) - (left == 0 ? 0 : preSum(left));
    }


    private int preSum(int index){
        if (index < 1 || index >= N)
            return Integer.MIN_VALUE;
        int res = 0;
        while (index != 0){
            res += tree[index];
            // 减去最右侧的1。 也可以简化为：index -= index & (-index)   -index == ~index + 1
            index -= index & (~index + 1);
        }
        return res;
    }


    public static void main(String[] args) {
        int[] arr = {7, 2, 7, 2, 0};
        NumArray a = new NumArray(arr);
        a.update(4, 6);
        a.update(0, 2);
        a.update(0, 9);
        System.out.println(a.sumRange(4, 4));
        a.update(3, 8);
        System.out.println(a.sumRange(0, 4));
        a.update(4, 1);
        System.out.println(a.sumRange(0, 3));
        System.out.println(a.sumRange(0, 4));
        a.update(0, 4);

    }
}
