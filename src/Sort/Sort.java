package Sort;

import utils.arrays;

public class Sort {

    //    在数组的L～R上做简单插入排序,
    public static void insertSort(int[] arr, int L, int R){
        if (arr == null || arr.length < 2 || L < 0 || R >= arr.length || L + 1 >= R)
            return;
        for (int i = L + 1; i <= R; i++) {
            for (int j = i - 1; j >= L && arr[j] > arr[j + 1]; j--)
                swap(arr, j, j + 1);
        }
    }

    // 选择排序
    public static void selectSort(int[] arr) {
        if (arr == null || arr.length < 2)
            return;
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                minIndex = arr[j] < arr[minIndex] ? j : minIndex;
            }
            swap(arr, i ,minIndex);
        }
    }

    //    冒泡排序
    public static void bubbleSort(int[] arr){
        for (int i = arr.length - 2; i >= 0; i--) {
            for (int j = 0; (j <= i) && (arr[j] > arr[j + 1]); j++) {
                swap(arr, j, j + 1);
            }
        }
    }

    private static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void bubbleSort_v2(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        for (int i = 0; i < arr.length - 1; i++) {
            int swapTimes = 0;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]){
                    swap(arr, j, j + 1);
                    swapTimes++;
                }
            }
            if (swapTimes == 0)
                break;
        }
    }

    public static void insertSort_v2(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] >= arr[i - 1])
                continue;
            else {
                for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--)
                    swap(arr, j, j + 1);
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {12, 5, 3, 21, 18, 1, 1};
        int[] arr2 = {2, 15, 23, 21, 38};
        insertSort(arr, 3, 6);
        arrays.printArray(arr);
    }
}
