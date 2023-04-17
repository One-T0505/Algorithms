package Basic.Sort.Exercise;

// leetCode703
// 设计一个找到数据流中第 k 大元素的类（class）。注意是排序后的第 k 大元素，不是第 k 个不同的元素。
// 请实现 KthLargest 类：
//  1.KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象。
//  2.int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第 k 大的元素。

// 1 <= k <= 10^4
// 0 <= nums.length <= 10^4
// -10^4 <= nums[i] <= 10^4
// -10^4 <= val <= 10^4
// 最多调用 add 方法 10^4 次
// 题目数据保证，在查找第 k 大元素时，数组中至少有 k 个元素

import java.util.PriorityQueue;

public class KthLargest {

    public PriorityQueue<Integer> heap;
    public int limit;

    public KthLargest(int k, int[] nums) {
        limit = k;
        heap = new PriorityQueue<>();
        for (int num : nums) {
            if (heap.size() < limit || num > heap.peek())
                heap.add(num);
            if (heap.size() > limit)
                heap.poll();
        }
    }

    // 题目说，在查找第k大的元素时，数组保证至少有k个元素了，所以此时堆里必然是有k个元素的
    public int add(int val) {
        if (heap.size() < limit){
            heap.add(val);
        } else if (heap.size() == limit && val > heap.peek()) {
            heap.poll();
            heap.add(val);
        }
        return heap.peek();
    }
}
