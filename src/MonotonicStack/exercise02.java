package MonotonicStack;

// leetCode84
// 给定一个非负数组arr， 代表直方图 返回直方图的最大长方形面积。
// eg： arr=[3, 2, 6, 5, 4]   把它想像成一个统计图，每个元素就表示一个矩阵，而每个数值就决定了该矩阵的高度，
// 求这个统计图中矩形能选中最多的格子数.
//          __
//         |__|__
//         |__|__|__
//    __   |__|__|__|
//   |__|__|__|__|__|
//   |__|__|__|__|__|
//   |__|__|__|__|__|
//
// 思路：同样地，对每个元素找出：左边离自己最近且比自己小的边界left; 右边离自己最近且比自己小的边界right
//      那么[left+1, right-1]就是以自己为最小值的最宽阔的区域，用最小值*宽度就能算出能框选出的矩形包含最多的格子数

import java.util.ArrayList;

public class exercise02 {
    // 单调栈实现
    public static int findRectangle(int[] arr){
        int[][] areas = MonotonicStack.monotonicStackRepeat(arr);
        int res = Integer.MIN_VALUE;
        int N = arr.length;
        for (int i = 0; i < N; i++) {
            int left = areas[i][0] + 1;
            int right = areas[i][1] == -1 ? N - 1 : areas[i][1] - 1;
            int width = right -left + 1;
            res = Math.max(res, width * arr[i]);
            ArrayList<Integer>[] s = new ArrayList[5];
            s[2] = new ArrayList<>();
            s[2].add(5);
        }
        return res;
    }


    public static void main(String[] args) {
        int[] arr = {3, 2, 6, 5, 4};
        System.out.println(findRectangle(arr));
    }
}
