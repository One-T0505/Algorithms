package GreatOffer.TopInterviewQ;

// 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
// 请注意 ，必须在不复制数组的情况下原地对数组进行操作。比如：
// 输入: nums = [0,1,0,3,12]
// 输出:        [1,3,12,0,0]

public class _0283_MoveZeros {

    public static void moveZeroes(int[] nums) {
        if (nums == null || nums.length == 0)
            return;
        int L = 0;
        int R = nums.length - 1;
        while (R >= 0 && nums[R] == 0)
            R--;
        while (L < R) {
            if (nums[L] == 0)
                exchange(nums, L, L, R--);
            else
                L++;
;        }
    }



    // 将 L~M 和 M+1～R 这两段的元素互换位置
    private static void exchange(int[] nums, int L, int M, int R) {
        reverse(nums, L, M);
        reverse(nums, M + 1, R);
        reverse(nums, L, R);

    }

    private static void reverse(int[] nums, int L, int R) {
        while (L < R){
            int tmp = nums[L];
            nums[L++] = nums[R];
            nums[R--] = tmp;
        }
    }
    // ----------------------------------------------------------------------------------------





    // 方法2
    public static void moveZeroes2(int[] nums) {
        if (nums == null || nums.length == 0)
            return;
        int L = 0;
        int R = nums.length - 1;
        while (L < R) {
            if (nums[L] == 0)
                bubble(nums, L);
            else
                L++;
        }
    }

    private static void bubble(int[] nums, int i) {
        while (i < nums.length - 1){
            int tmp = nums[i];
            nums[i] = nums[i + 1];
            nums[i + 1] = tmp;
        }
    }
    // ----------------------------------------------------------------------------------------



    // 方法3  利用荷兰国旗模型
    public static void moveZeroes3(int[] nums) {
        if (nums == null || nums.length == 0)
            return;
        int L = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0){
                int tmp = nums[i];
                nums[i] = nums[L + 1];
                nums[++L] = tmp;
            }
        }
    }

}
