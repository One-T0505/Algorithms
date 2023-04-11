package GreatOffer.TopInterviewQ;


// 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
// 你可以假设所有输入数组都可以得到满足题目要求的结果。要求：时间复杂度O(N) 空间复杂度O(1)

public class _0324_WiggleSortII {


    // 这道题目要求的复杂度决定了这是一个超难的题目。它需要两个前置问题基本模型：
    //  1.无序数组中找出第k小的数    这就是快速选择模型 (BFPRT 就是在该模型上优化的)  荷兰国旗问题
    //    也可以用堆，但是这道题目要求了空间复杂度为常数， 所以不能用堆
    //  2.完美洗牌问题。  将 L1 L2 L3 L4 R1 R2 R3 R4  调整成  R1 L1 R2 L2 R3 L3 R4 L4

    public static void wiggleSortII(int[] nums) {
        if (nums == null || nums.length < 2)
            return;
        int N = nums.length;
        // 第一步，先找出数组中 N/2 小的数，利用快速选择. 我们其实并不需要那个返回值，就是排完序后在 N/2 位置上的值，
        // 我们只是希望利用快速选择实现对数组的划分，左边是小于区，中间是等于区，右边是大于区
        quickSelect(nums, 0, N - 1, N >> 1);
        // 长度为偶数 完美洗牌就是只能用在偶数长度上的。但是完美洗牌洗完之后只能让数组变成 R1 L1 R2 L2 R3 L3 R4 L4
        // 这样是达不到题目要求的，我们还需要整体逆序一下，别问为什么，就是大量的实验结果表明的，经验主义。
        if ((N & 1) == 0){
            shuffle(nums, 0, N - 1);
            reverse(nums, 0, N - 1);
        } else // 长度为奇数  完美洗牌问题无法使用，所以我们将0位置的元素弃而不用，在1～N-1上使用完美洗牌
               // 这次洗完之后就不用逆序了，也是经验主义。
            shuffle(nums, 1, N - 1);

    }



    private static int quickSelect(int[] nums, int L, int R, int k) {
        while (L < R){
            int pivot = nums[L + (int) (Math.random() * (R - L + 1))];
            int[] equals = hollandFlags(nums, L, R, pivot);
            if (k >= equals[0] && k <= equals[1])
                return nums[k];
            else if (k < equals[0]) {
                R = equals[0] - 1;
            } else
                L = equals[1] + 1;
        }
        return nums[L];
    }


    // 基于荷兰国旗模型的快速选择。在nums[L..R]上选出如果排序的，在k位置的值
    private static int[] hollandFlags(int[] nums, int L, int R, int pivot) {
        // 此时 nums[R-1]位置就是pivot
        int less = L - 1, more = R + 1;
        while (L < more){
            if (nums[L] < pivot)
                swap(nums, ++less, L++);
            else if (nums[L] == pivot) {
                L++;
            } else
                swap(nums, L, --more);
        }
        return new int[] {less + 1, more - 1};
    }




    // 传入的范围必然是偶数个元素。现在要把 L1 L2 L3 L4 R1 R2 R3 R4  调整成  R1 L1 R2 L2 R3 L3 R4 L4
    private static void shuffle(int[] nums, int L, int R) {
        while (R - L + 1 > 0){
            int len = R - L + 1;
            int radix = 3;
            int k = 0;
            while (radix <= len + 1){
                radix *= 3;
                k++;
            }
            int M = L + ((R - L) >> 1);
            int half = (radix / 3 - 1) >> 1;
            exchange(nums, L + half, M, M + half);
            cycle(nums, L, half << 1, k);
            L += half << 1;
        }
    }



    private static void exchange(int[] nums, int L, int M, int R) {
        reverse(nums, L, M);
        reverse(nums, M + 1, R);
        reverse(nums, L, R);
    }



    private static void cycle(int[] nums, int L, int N, int k) {

        for (int i = 0, trigger = 1; i < k; trigger *= 3){
            int preVal = nums[L + trigger - 1];
            int cur = destination(trigger, N);
            while (cur != trigger){
                int tmp = nums[L + cur - 1];
                nums[L + cur - 1] = preVal;
                preVal = tmp;
                cur = destination(cur, N);
            }
            nums[L + cur - 1] = preVal;
        }
    }

    private static int destination(int i, int N) {
            return i <= (N >> 1) ? i << 1 : ((i - (N >> 1)) << 1) - 1;
    }


    private static void reverse(int[] nums, int L, int R) {
        while (L < R){
            int tmp = nums[R];
            nums[R--] = nums[L];
            nums[L++] = tmp;
        }
    }



    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
