package GreatOffer.TopHotQ;

// 整数数组的一个排列就是将其所有成员以序列或线性顺序排列。
// 例如，arr = [1,2,3]，以下这些都可以视作 arr 的排列：[1,2,3]、[1,3,2]、[3,1,2]、[2,3,1] 。
// 整数数组的下一个排列是指其整数的下一个字典序更大的排列。更正式地，如果数组的所有排列根据其字典顺序从小到大
// 排列在一个容器中，那么数组的下一个排列就是在这个有序容器中排在它后面的那个排列。如果不存在下一个更大的排列，
// 那么这个数组必须重排为字典序最小的排列（即，其元素按升序排列）。
// 例如，arr = [1,2,3] 的下一个排列是 [1,3,2] 。
// 类似地，arr = [2,3,1] 的下一个排列是 [3,1,2] 。
// 而 arr = [3,2,1] 的下一个排列是 [1,2,3] ，因为 [3,2,1] 不存在一个字典序更大的排列。
// 给你一个整数数组 nums ，找出 nums 的下一个排列。
// 必须原地修改，只允许使用额外常数空间。

public class _0031_NextPermutation {

    // 首先判断一个数组是为否其最大字典序排列：从右往左遍历，如果一直没有降序出现，那就说明是最大排列，
    // 把当前数组逆序后返回 比如：[7, 7, 5, 4, 4, 2, 1, 1]
    // 如果在某个位置出现了第一个降序，先标记为i位置, 比如：[? ? ? ? ?, 5, 9, 6, 5, 5, 4, 4, 2, 1, 1]
    //                                                          i
    // 我们可以得到这些结论：当前字符串的下一个字典序排列，i位置左边?处的元素都不需要动
    // 左边不动，以i位置为5的情况下，其右边已经挖掘到了最大值了，因为都是降序的。那么当前排列的下一个排列应该是：
    // 左边一模一样，i位置应该是其右侧中>i且最小的元素(可以用二分找)，也就是上面数组中的6，把i位置的5和6交换，
    // 然后将右侧的 9 5 5 5 4 4 4 2 1 1 逆序后即可，也就是说i位置换了一个稍微比当前i位置大的元素，然后右侧
    // 变成了最小的排列

    public static void nextPermutation(int[] nums) {
        if (nums == null || nums.length < 2)
            return;
        int N = nums.length;
        int pos = -1;
        for (int i = N - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                pos = i;
                break;
            }
        }
        // 说明整个数组的排列已经是最大的了
        if (pos == -1)
            reverse(nums, 0, N - 1);
        else {
            int aim = mostRightG(nums, pos + 1, N - 1, nums[pos]);
            swap(nums, pos, aim);
            reverse(nums, pos + 1, N - 1);
        }
    }


    private static void reverse(int[] nums, int L, int R) {
        while (L < R) {
            swap(nums, L++, R--);
        }
    }


    // 在L..R上找出>t且最右的位置  L..R是降序排序
    private static int mostRightG(int[] nums, int L, int R, int t) {
        int res = L;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (nums[mid] > t) {
                res = mid;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        return res;
    }


    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

}
