package LeetCode;

/**
 * ymy
 * 2023/3/24 - 19 : 34
 **/

// leetCode80
// 给你一个有序数组 nums ，请你原地删除重复出现的元素，使得出现次数超过两次的元素只出现两次 ，返回删除后数组的新长度。
// 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。

public class RemoveDuplicatesII {

    public static int removeDuplicates(int[] nums) {
        if (nums == null)
            return -1;
        if (nums.length < 3)
            return nums.length;
        int N = nums.length;
        // 用双指针。fast指向的是实际遍历到的位置，而slow指向的是真正被保存下来的元素的位置
        // slow-1 位置就是上一个被保存的元素，如果 nums[slow - 2] != nums[fast] 就说明fast不可能和slow-1重复
        // 因为一种元素最多保存两次。
        int slow = 2, fast = 2;
        while (fast < N){
            if (nums[slow - 2] != nums[fast]){
                nums[slow++] = nums[fast];
            }
            fast++;
        }
        return slow;
    }


    public static void main(String[] args) {
        int[] arr = {0, 0, 1, 1, 1, 1, 2, 3, 3};
        int N = removeDuplicates(arr);
        for (int i = 0; i < N; i++) {
            System.out.print(arr[i] + "\t");
        }
        System.out.println();
    }
}
