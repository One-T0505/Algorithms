package LeetCode;

/**
 * ymy
 * 2023/3/29 - 21 : 02
 **/

// leetCode540
// 给你一个仅由整数组成的有序数组，其中每个元素都会出现两次，唯有一个数只会出现一次。
// 请你找出并返回只出现一次的那个数。你设计的解决方案必须满足 O(log n) 时间复杂度和 O(1) 空间复杂度。

public class SingleElemInSortedArray {

    // 如果要求时间复杂度为O(n)  那就直接异或。但是时间复杂度为O(logn) 并且有序，那就只能二分
    // 假设只出现一次的元素位于下标 x，由于其余每个元素都出现两次，因此下标 x 的左边和右边都有偶数个元素，数组的长度
    // 是奇数。由于数组是有序的，因此数组中相同的元素一定相邻。对于下标 x 左边的下标 y，如果 nums[y]=nums[y+1]，
    // 则 y 一定是偶数；对于下标 x 右边的下标 z，如果 nums[z]=nums[z+1]，则 z 一定是奇数。由于下标 x 是相同元素
    // 的开始下标的奇偶性的分界，因此可以使用二分查找的方法寻找下标 x。
    // 初始时，二分查找的左边界是 0，右边界是数组的最大下标。每次取左右边界的平均值 mid 作为待判断的下标，根据 mid
    // 的奇偶性决定和左边或右边的相邻元素比较：
    //   1.如果 mid 是偶数，则比较 nums[mid] 和 nums[mid+1] 是否相等
    //   2.如果 mid 是奇数，则比较 nums[mid−1] 和 nums[mid] 是否相等
    // 如果上述比较相邻元素的结果是相等，则 mid < x，调整左边界，否则 mid ≥ x，调整右边界。调整边界之后继续二分查找，
    // 直到确定下标 x 的值.
    public static int singleNonDuplicate(int[] nums) {
        if (nums == null || (nums.length & 1) == 0)
            return Integer.MIN_VALUE;
        int L = 0, R = nums.length - 1;
        while (L < R){
            int mid = L + ((R - L) >> 1);
            // 这里有便捷写法：如果 mid 为偶数，那么 mid ^ 1 == mid + 1
            // 如果 mid 为奇数，那么 mid ^ 1 == mid - 1
            if (nums[mid] == nums[mid ^ 1])
                L = mid + 1;
            else
                R = mid;
        }
        return nums[L];
    }


    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 3, 3, 4, 4, 8, 8};
        System.out.println(singleNonDuplicate(arr));
    }
}
