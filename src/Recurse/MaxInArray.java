package Recurse;

// 为了便于了解递归，这里用一个入门的问题来引入递归
// 求一个数组中的最大值，其实遍历一遍就可以了，这里非要用递归，只是为了熟悉这个过程
public class MaxInArray {

    public static int max(int[] arr){
        if (arr == null || arr.length == 0)
            throw new RuntimeException("不存在最大值");
        return process(arr, 0, arr.length - 1);
    }

    private static int process(int[] arr, int L, int R) {
        if (L == R)
            return arr[L];
        int mid = L + ((R - L) >> 1);
        int leftMax = process(arr, L, mid);
        int rightMax = process(arr, mid + 1, R);
        return Math.max(leftMax, rightMax);
    }

    public static void main(String[] args) {
        int[] arr = {12, 5, 30, 16, 32};
        System.out.println(max(arr));
    }
}
