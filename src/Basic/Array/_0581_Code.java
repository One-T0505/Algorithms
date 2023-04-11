package Basic.Array;

// 给定一个数组arr，只能对arr中的一个子数组排序,但是想让arr整体都有序。返回满足这一设定的
// 子数组中，最短的子数组是多长。eg：arr=[7, 6, 2, 1, 0, 8, 9]  只需要对[7, 6, 2, 1, 0]排序即可.

public class _0581_Code {

    // 思路：从左遍历数组，用一个变量max记录已经找到的最大值；若当前元素 < max，则用right记下位置，标记的意思
    // 就是如果要排序的话，这个位置肯定会发生变动；然后遍历完成后就能找到最右侧需要变动的位置；
    // 同样地，再逆向遍历一遍，找到最左侧会变动的位置left，left～right就是需要调整的。
    // eg：arr=[1, 2, 7, 4, 3, 6, 8, 9]
    //          0  1  2  3  4  5  6  7
    //  从左遍历一遍后，right=5; 这里的含义是：从6~7这两个元素是完全不用动的；再从右遍历一遍，得到left=2，
    //  这表示：从0~1 是已经排好序的，不用动的，所以从left～right才是需要排序的。

    public static int subSort(int[] nums){
        if (nums == null || nums.length < 2)
            return 0;
        int N = nums.length;
        int max = nums[0];
        int r = -1;
        for (int i = 1; i < N; i++) {
            if (nums[i] < max){
                r = i;
            } else
                max = Math.max(max, nums[i]);
        }
        int min = nums[N - 1];
        int l = N;
        for (int i = N- 2; i >= 0; i--) {
            if (nums[i] > min)
                l = i;
            else
                min = Math.min(min, nums[i]);
        }
        return r == -1 ? 0 : r - l + 1;
    }


    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 2, 2};
        System.out.println(subSort(arr));
    }
}
