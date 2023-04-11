package GreatOffer.TopInterviewQ;


// 给你一个 升序排列的数组 nums，请你原地删除重复出现的元素，使每个元素只出现一次，返回删除后数组的新长度。
// 元素的相对顺序应该保持一致。由于在某些语言中不能改变数组的长度，所以必须将结果放在数组nums的第一部分。更
// 规范地说，如果在删除重复项之后有 k 个元素，那么 nums 的前 k 个元素应该保存最终结果。
// 将最终结果插入 nums 的前 k 个位置后返回 k 。
// 不要使用额外的空间，你必须在原地修改输入数组 并在使用 O(1) 额外空间的条件下完成。


public class _0026_RemoveDuplicates {


    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        if (nums.length == 1)
            return 1;
        int N = nums.length;
        int size = 1;  // nums[0]必然是一个
        int x = nums[0];
        for (int i = 1; i < N; i++) {
            if (nums[i] != x) {
                nums[size++] = nums[i];
                x = nums[i];
            }
        }
        return size;
    }
}
