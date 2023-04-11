package LeetCode;

/**
 * ymy
 * 2023/3/31 - 19 : 40
 **/

// 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
// 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
// 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。

public class RemoveElement {

    public static int removeElement(int[] nums, int val) {
        if (nums == null || nums.length == 0)
            return 0;
        int len = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val)
                nums[len++] = nums[i];
        }
        return len;
    }


    public static void main(String[] args) {
        int[] arr = {3, 2, 2, 3};
        int val = 3;
        int len = removeElement(arr, val);
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            System.out.print(arr[i] + "\t");
        }
        System.out.println();
    }
}
