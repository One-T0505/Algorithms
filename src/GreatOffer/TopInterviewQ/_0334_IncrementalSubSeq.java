package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ymy
 * 2023/3/14 - 20 : 10
 **/

// 给你一个整数数组 nums ，判断这个数组中是否存在长度为 3 的递增子序列。
// 如果存在这样的三元组下标 (i, j, k) 且满足 i < j < k ，使得 nums[i] < nums[j] < nums[k] ，返回 true ；
// 否则，返回 false。

public class _0334_IncrementalSubSeq {


    // 用两次单调栈，一次是搜集每个元素左边是否有比自己小的元素；第二次是找每个元素右边是否元素比自己大，
    // 如果一个位置同时具备这两个条件，那就可以找到一个递增三元子序列
    public static boolean increasingTriplet(int[] nums) {
        if (nums == null || nums.length < 3)
            return false;
        int N = nums.length;
        Stack<List<Integer>> leftLessS = new Stack<>();
        Stack<List<Integer>> rightMoreS = new Stack<>();
        boolean[] leftInfo = new boolean[N];
        boolean[] rightInfo = new boolean[N];
        // 先完成第一个单调栈 leftLessS。用 leftLessS 可以完成 leftInfo。leftInfo[i] == true 表示
        // nums[i] 左边有比自己小的
        for (int i = 0; i < N; i++) {
            while (!leftLessS.isEmpty() && nums[leftLessS.peek().get(0)] > nums[i]){
                List<Integer> pop = leftLessS.pop();
                for (int pos : pop)
                    leftInfo[pos] = !leftLessS.isEmpty();
            }
            if (leftLessS.isEmpty() || nums[leftLessS.peek().get(0)] < nums[i]){
                ArrayList<Integer> cur = new ArrayList<>();
                cur.add(i);
                leftLessS.push(cur);
            } else {
                leftLessS.peek().add(i);
            }
        }
        while (!leftLessS.isEmpty()){
            List<Integer> pop = leftLessS.pop();
            for (int pos : pop)
                leftInfo[pos] = !leftLessS.isEmpty();
        }

        // 再完成第二个单调栈 rightMoreS。用 rightMoreS 可以完成 rightInfo。rightInfo[i] == true 表示
        // nums[i] 右边有比自己大的
        for (int i = 0; i < N; i++) {
            while (!rightMoreS.isEmpty() && nums[rightMoreS.peek().get(0)] < nums[i]){
                List<Integer> pop = rightMoreS.pop();
                for (int pos : pop){
                    rightInfo[pos] = true;
                    if (leftInfo[pos])
                        return true;
                }
            }
            if (rightMoreS.isEmpty() || nums[rightMoreS.peek().get(0)] > nums[i]){
                ArrayList<Integer> cur = new ArrayList<>();
                cur.add(i);
                rightMoreS.push(cur);
            } else {
                rightMoreS.peek().add(i);
            }
        }


        for (int i = 0; i < N; i++) {
            if (leftInfo[i] && rightInfo[i])
                return true;
        }
        return false;
    }




    // 方法1太复杂了，尝试使用后缀数组 ends，ends[i]表示目前找到的长度为i+1的递增子序列中最小的结尾。
    public boolean increasingTriplet2(int[] nums) {
        if (nums == null || nums.length < 3)
            return false;
        int N = nums.length;
        int[] ends = new int[N];
        int R = 0;
        ends[0] = nums[0];
        for (int i = 1; i < N; i++) {
            int pos = search(ends, 0, R, nums[i]);
            if (pos == -1){
                ends[++R] = nums[i];
                if (R == 2)
                    return true;
            }
            else
                ends[pos] = nums[i];
        }
        return false;
    }



    // 在ends[L..R]上找到>=t的最左的位置
    private int search(int[] ends, int L, int R, int t) {
        int pos = -1;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (ends[mid] >= t){
                pos = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return pos;
    }
    // 方法2的时间复杂度和空间复杂度都为 O(N)





    // 贪心，可以做到让空间复杂度O(1)
    // 从左到右遍历数组 nums，遍历过程中维护两个变量 first和 second，分别表示递增的三元子序列中的第一个数
    // 和第二个数，任何时候都有 first < second。
    // 初始时，first = nums[0]，second = +∞。对于 1 ≤ i < n，当遍历到下标 i 时，令 num = nums[i]
    // 进行如下操作：
    //  1.如果 num > second，则找到了一个递增的三元子序列，返回 true；
    //  2.否则，如果 num > first，则将 second 的值更新为 num；
    //  3.否则，将 first 的值更新为 num。
    // 如果遍历结束时没有找到递增的三元子序列，返回 false。
    // 上述做法的贪心思想是：为了找到递增的三元子序列，first 和 second 应该尽可能地小，此时找到递增的三元子序列的
    // 可能性更大。
    public boolean increasingTriplet3(int[] nums) {
        if (nums == null || nums.length < 3)
            return false;
        int N = nums.length;
        int first = nums[0];
        int second = Integer.MAX_VALUE;
        for (int i = 1; i < N; i++) {
            if (nums[i] > second)
                return true;
            else if (nums[i] > first) {
                second = nums[i];
            } else
                first = nums[i];
        }
        return false;
    }
}
