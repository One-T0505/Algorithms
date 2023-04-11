package GreatOffer.TopInterviewQ;


// 给你两个按非递减顺序排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n，分别表示 nums1 和 nums2 中的元素数目。
// 请你 合并 nums2 到 nums1 中，使合并后的数组同样按非递减顺序排列。
// 注意：最终，合并后数组不应由函数返回，而是存储在数组 nums1 中。为了应对这种情况，nums1 的初始长度为 m + n，
// 其中前 m 个元素表示应合并的元素，后 n 个元素为 0，应忽略。nums2 的长度为n。

public class _0088_MergeSortedArray {

    // 这个就是归并排序时已经排好序了，只是完成最后一步的拷贝问题。这道题目我们需要从后往前填充，并且相等时应该拷贝
    // nums1数组的元素，因为这样可以更快地释放空间，

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int pos = nums1.length - 1;
        while (m > 0 && n > 0) {
            nums1[pos--] = nums1[m - 1] >= nums2[n - 1] ? nums1[--m] : nums2[--n];
        }

        while (m > 0)
            nums1[pos--] = nums1[--m];

        while (n > 0)
            nums1[pos--] = nums2[--n];
    }
}
