import utils.arrays;

import java.util.Arrays;

public class BinarySearch {

    // 传统二分搜索法：若target不在有序数组arr中，返回-1；若存在则返回索引
    public static int binarySearch(int[] arr, int target){
        if (arr == null || arr.length == 0)
            return -1;
        int L = 0, R = arr.length - 1;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (arr[mid] == target)
                return mid;
            else if (arr[mid] > target) {
                R = mid - 1;
            } else {
                L = mid + 1;
            }
        }
        return -1;
    }


    // 给一个有序数组，数组中允许有重复的值，给一个数target，找出arr中>=target的最左的位置，若不存在则返回-1。
    // 也可以求<=target 的最右的位置
    public static int mostLeftEag(int[] arr, int target){
        int L = 0, R = arr.length - 1;
        int index = -1;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (arr[mid] >= target)
                R = mid - 1;
            else
                L = mid + 1;
        }
        return index;
    }



    // 给一个无序数组，里面没有重复的元素，都是不一样的元素。我们定义一个局部最小值：若该元素不是头尾元素，如果它
    // 比左右两边的元素都小，则该元素为局部最小；若该元素为头尾元素，则只需要比它相邻的一个元素小就是局部最小。
    // 返回该数组中的任何一个局部最小值的索引。

    // 该问题说明 二分搜索不一定非得要求数组有序！！！！！
    public static int localMin(int[] arr){
        if (arr == null || arr.length < 2)
            return -1;
        if (arr[0] < arr[1])
            return 0;
        if (arr[arr.length - 1] < arr[arr.length - 2])
            return arr.length - 1;
        int L = 1, R = arr.length - 2;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (arr[mid] > arr[mid - 1])
                R = mid - 1;
            else if (arr[mid] > arr[mid + 1]) {
                L = mid + 1;
            } else
                return mid;
        }
        return -1;
    }

    public static void main(String[] args) {
        // 用对数器验证自己写的二叉搜索法是否正确
        int maxSize = 20, maxVal = 200;
        for (int i = 0; i < 100000; i++) {
            int target = (int) ((maxVal + 1) * Math.random());
            int[] src = arrays.generateRandomArray(maxSize, maxVal);
            int[] copy = arrays.copyArray(src);
            if (binarySearch(src, target) >= 0 && Arrays.binarySearch(src, target) >= 0 &&
                    binarySearch(src, target) != Arrays.binarySearch(src, target)){
                arrays.printArray(src);
                System.out.println(target);
                System.out.println("failed!");
                return;
            }
        }
        System.out.println("AC");
    }
}
