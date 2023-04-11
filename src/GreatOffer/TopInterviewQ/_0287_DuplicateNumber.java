package GreatOffer.TopInterviewQ;


// 给定一个包含 n + 1 个整数的数组 nums ，其数字都在 [1, n] 范围内（包括 1 和 n），可知至少存在一个重复的整数。
// 假设 nums 只有一个重复的整数 ，返回 这个重复的数 。
// 你设计的解决方案必须不修改数组 nums, 且只用常量级 O(1) 的额外空间。

public class _0287_DuplicateNumber {

    // 该题目用到的是之前讲过的：在单链表中寻找第一个入环结点的问题。解法是使用快慢指针，快指针每次走两个，慢指针
    // 每次走一个，当两个指针相遇时，让快指针返回原点，并且，此时让快慢指针每次都走一个，下次相遇时就是入环的第一个结点。
    // 本题目用到了上述思想。在一个数组中，一开始快慢指针都从0位置出发，下次跳的位置就是当前位置上的值。比如此时指针在0位置，
    // nums[0]=2，那么指针下次就跳到2位置。
    public int findDuplicate(int[] nums) {
        if (nums == null || nums.length < 2)
            return -1;
        int slow = nums[0];
        int fast = nums[nums[0]];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[nums[fast]];
        }
        fast = 0;
        while (slow != fast) {
            fast = nums[fast];
            slow = nums[slow];
        }
        return slow;
    }
}
