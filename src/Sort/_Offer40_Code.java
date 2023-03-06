package Sort;

// leetCode剑指Offer40
// 输入整数数组 arr ，找出其中最小的 k 个数。例如，输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4。
// 0 <= k <= arr.length <= 10^4

import java.util.PriorityQueue;

public class _Offer40_Code {

    public static int[] getLeastNumbers(int[] arr, int k) {
        if (arr == null || arr.length < k)
            return null;
        int[] res = new int[k];
        if (k == 0)
            return res;
        int i = 0;
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
        for (int j = 0; j < k; j++) {
            heap.add(arr[j]);
        }
        for (int j = k; j < arr.length; j++) {
            if (arr[j] < heap.peek()) {
                heap.poll();
                heap.add(arr[j]);
            }
        }
        while (!heap.isEmpty()){
            res[i++] = heap.poll();
        }
        return res;
    }


    public static void main(String[] args) {
        int[] arr = {3, 2, 1};
        int[] res = getLeastNumbers(arr, 2);
    }
}
