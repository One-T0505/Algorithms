package GreatOffer.class42;

import utils.arrays;

import java.util.Arrays;

// 给定一个全部由0、1组成的数组。1表示活细胞，0表示死细胞。如果0旁边只有一个1，那么下一轮，该死细胞复活。
// 两端如果有死细胞，其旁边有一个活细胞也可以在下一轮复活。如果死细胞在数组非两端位置，且两边都是活细胞，
// 那么该死细胞下一轮不能复活，依然维持死亡状态。所有的活细胞下一轮依然维持活状态。
// 给你一个数组arr，再给你一个轮数M，经过M轮次迭代后，arr是怎样的，请在原地修改arr的值
// 数组长度 <= 10^6    M <= 10^10

public class _01_Code {

    // 暴力解法
    public static void cell(int[] arr, int M) {
        if (arr == null || arr.length < 2 || M < 1)
            return;
        int N = arr.length;
        int[] help = new int[N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (arr[j] == 1)
                    help[j] = 1;
                else {
                    if (j == 0) {
                        if (arr[j] == 0 && arr[j + 1] == 1)
                            help[j] = 1;
                    } else if (j == N - 1) {
                        if (arr[j] == 0 && arr[j - 1] == 1)
                            help[j] = 1;
                    } else {
                        if (arr[j] == 0 && (arr[j + 1] == 1 ^ arr[j - 1] == 1))
                            help[j] = 1;
                    }
                }
            }
            System.arraycopy(help, 0, arr, 0, N);
            Arrays.fill(help, 0);
        }
    }


    // 根据数据量M可知，我们不可能真的一轮一轮地修改arr，直到M轮。所以一定有什么规律或者思路
    // 可以不用真的遍历到M轮就可以知道arr的值
    // 如果数组是：arr = {1, 0, 0, 1, 0, 0, 1, 1, 1, 0}    我们需要从左遍历一遍 然后 从右遍历一遍
    // 找出每个元素左侧和右侧距离最近的1在什么位置，包括自己。
    // arr[3]左侧最近的1就是自己 右侧最近的1也是自己
    // arr[5]左侧最近的1是3位置 右侧最近的1是6位置  所以该位置的0在经过1轮之后就可以被感染

    public static void cell2(int[] arr, int M) {
        if (arr == null || arr.length < 2 || M < 1)
            return;
        int N = arr.length;
        int[] left = new int[N];
        left[0] = arr[0] == 1 ? 0 : -1;
        for (int i = 1; i < N; i++) {
            left[i] = arr[i] == 1 ? i : left[i - 1];
        }
        int[] right = new int[N];
        right[N - 1] = arr[N - 1] == 1 ? 0 : -1;
        for (int i = N - 2; i >= 0; i--) {
            right[i] = arr[i] == 1 ? i : right[i + 1];
        }
        for (int i = 0; i < N; i++) {
            // 如果left[i] == right[i] 说明arr[i]==1  1永远都是1不会变
            if (arr[i] == 0) {
                int need = M + 1;
                if (left[i] == -1 ^ right[i] == -1)
                    need = left[i] == -1 ? right[i] - i : i - left[i];
                else if (left[i] != -1 && right[i] != -1)
                    need = Math.min(i - left[i], right[i] - i);
                arr[i] = need == M + 1 ? 0 : 1;
            }
        }
    }


    public static void main(String[] args) {
        int[] arr = {1, 0, 0, 1, 0, 0, 1, 1, 1, 0};
        cell(arr, 1);
        arrays.printArray(arr);
        // 1 0 0 1 0 0 1 1 1 0
        // 1 1 1 1 1
    }

}
