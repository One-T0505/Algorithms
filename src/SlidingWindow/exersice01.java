package SlidingWindow;

import java.util.Arrays;
import java.util.LinkedList;
import utils.arrays;

// 假设一个固定大小为W的窗口，依次划过arr,返回每一次滑出状况的最大值
// 例如：arr= [4,3,5,4,3,3,6,7], W= 3  返回: [5,5,5,4,6,7]
public class exersice01 {

    // w表示窗口大小. 假如数组长度为N，窗口大小为W，则会返回 N-W+1 个结果
    public static int[] getMax(int[] arr, int w){
        if (arr == null || arr.length == 0 || w < 1)
            return null;
        if (arr.length < w){
            Arrays.sort(arr);
            return new int[]{arr[arr.length - 1]};
        }
        LinkedList<Integer> list = new LinkedList<>();
        int[] res = new int[arr.length - w + 1];
        int index = 0;
        for (int R = 0; R < arr.length; R++) {
            // 第一个元素进来时，list为空直接跳过while循环
            while (!list.isEmpty() && arr[R] >= arr[list.peekLast()]){
                list.pollLast();
            }
            list.addLast(R);
            // R - w 表示的是要过期的元素下标，如果要过期的元素下标正好是list头部，那就出队列
            if (list.peekFirst() == R - w)
                list.pollFirst();
            if (R >= w - 1) // R >= w - 1 时，这个窗口才形成，才开始收集答案
                res[index++] = arr[list.peekFirst()];
        }
        return res;
    }

    // 对数器
    public static int[] verify(int[] arr, int w){
        if (arr == null || arr.length == 0 || w < 1)
            return null;
        if (arr.length < w){ // 如果数组长度小于窗口，那就返回数组最大元素
            Arrays.sort(arr);
            return new int[]{arr[arr.length - 1]};
        }
        int[] res = new int[arr.length - w + 1];
        int index = 0;
        for (int L = 0, R = w - 1; R < arr.length; L++, R++) {
            int max = Integer.MIN_VALUE;
            for (int i = L; i <= R; i++) {
                max = Math.max(max, arr[i]);
            }
            res[index++] = max;
        }
        return res;
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            int[] array = arrays.generateRandomArray(10, 50);
            int[] res1 = getMax(array, 3);
            int[] res2 = verify(array, 3);
            if (!arrays.isSameArray(res1, res2)) {
                arrays.printArray(array);
                arrays.printArray(res1);
                arrays.printArray(res2);
                System.out.println("===========================================");
                return;
            }
        }
        System.out.println("AC");
    }
}
