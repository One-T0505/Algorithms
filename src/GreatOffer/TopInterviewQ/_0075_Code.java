package GreatOffer.TopInterviewQ;

// 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums ，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、
// 白色、蓝色顺序排列。我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
// 必须在不使用库内置的 sort 函数的情况下解决这个问题。

public class _0075_Code {

    // 这就是荷兰国旗分治模型
    public static void sortColors(int[] nums) {
        if (nums == null || nums.length < 2)
            return;
        int L = -1, R = nums.length;
        int i = 0;
        while (i < R){
            if (nums[i] == 0)
                swap(nums, ++L, i++);
            else if (nums[i] == 1) {
                i++;
            } else
                swap(nums, --R, i);
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
