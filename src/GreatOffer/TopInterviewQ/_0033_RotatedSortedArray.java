package GreatOffer.TopInterviewQ;


// 整数数组 nums 按升序排列，数组中的值互不相同。
// 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了旋转，使数组变为
// [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标从 0 开始计数)。
// 例如， [0,1,2,4,5,6,7] 在下标 3 处经旋转后可能变为 [4,5,6,7,0,1,2] 。
// 给你旋转后的数组 nums 和一个整数 target，如果 nums 中存在这个目标值 target，则返回它的下标，否则返回 -1。
// 你必须设计一个时间复杂度为 O(log n) 的算法解决此问题。

public class _0033_RotatedSortedArray {

    // 时间复杂度要求到 O(logN)  那必然是二分了。我们把出现降序的结点称为断点。比如：[1, 2, 3, 4, 5, 6, 7]
    // 旋转成了 [5, 6, 7, 1, 2, 3, 4]  那么1就是断点。
    public static int search(int[] nums, int target) {
        if (nums == null || nums.length == 0)
            return -1;
        int L = 0, R = nums.length - 1;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (nums[mid] == target)
                return mid;
            if (nums[L] <= nums[mid]){ // 说明断点在mid后面，L～mid还是有序的
                if (nums[L] <= target && target < nums[mid])
                    R = mid - 1;
                else
                    L = mid + 1;
            } else {
                if (nums[mid] < target && target <= nums[R])
                    L = mid + 1;
                else
                    R = mid - 1;
            }
        }
        return -1;
    }
    // -----------------------------------------------------------------------------------------
    // 本题的进阶版是leetCode 81 题，那个数组中会有重复值




    // leetCode81  进阶版
    // 已知存在一个按非降序排列的整数数组 nums ，数组中的值不必互不相同。
    // 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了旋转 ，使数组变为
    // [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始 计数）。
    // 例如， [0,1,2,4,4,4,5,6,6,7] 在下标 5 处经旋转后可能变为 [4,5,6,6,7,0,1,2,4,4] 。
    // 给你旋转后的数组 nums 和一个整数 target ，请你编写一个函数来判断给定的目标值是否存在于数组中。如果 nums 中
    // 存在这个目标值 target ，则返回 true ，否则返回 false 。
    // 你必须尽可能减少整个操作步骤。

    public static boolean search2(int[] nums, int target) {
        if (nums == null || nums.length == 0)
            return false;
        int L = 0, R = nums.length - 1;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (nums[mid] == target)
                return true;
            // 当三者相等时，在此时的区间上是无法进行判断的。
            if (nums[L] == nums[mid] && nums[mid] == nums[R]){
                // 如果一直碰到相同的值，就不断往后移动
                while (L != mid && nums[L] == nums[mid])
                    L++;
                // 跳出while有两种情况: 1.nums[L] != nums[mid]  2. L == mid
                // 说明nums[L..mid]这一段都是相等，且不等于 target 的值，所以我们需要在 mid+1～R 上继续寻找
                if (L == mid){
                    L = mid + 1;
                    continue;
                }
            }
            // 执行到这里说明，三者不全相等，并且 nums[mid] != target
            // 并且 nums[L] != nums[mid]   因为跳过了 continue 说明 L != mid  为什么 L != mid 就可以
            // 跳出while，那肯定是因为 找到了 nums[L] != nums[mid]
            if (nums[L] != nums[mid]){
                // 这种情况下，说明断点一定在 mid+1~R 上。
                if (nums[L] < nums[mid]){
                    // 在上面的条件下，如果 target 在以下范围内，那么右侧区间就可以丢弃了，因为右侧不可能出现target
                    //
                    // 比如: 1 2 3 4 5 ...|...   target==3
                    //      L       M  左   右
                    // 那么左是本来就在5后面的，所以都>=5，而右是在1之前被转移过来的，所以都<=1，所以不包含3
                     if (target >= nums[L] && target < nums[mid])
                         R = mid - 1;
                     else
                         L = mid + 1;
                } else { // nums[L] > nums[mid]  那么断点必然在L～mid
                    if (target > nums[mid] && target <= nums[R])
                        L = mid + 1;
                    else
                        R = mid - 1;
                }
            } else { // nums[L] == nums[mid] != nums[R]
                if (nums[mid] < nums[R]){
                    if (target > nums[mid] && target <= nums[R])
                        L = mid + 1;
                    else
                        R = mid - 1;
                } else { // nums[mid] > nums[R]
                    if (target >= nums[L] && target < nums[mid])
                        R = mid - 1;
                    else
                        L = mid + 1;
                }
            }
        }
        return false;
    }
}
