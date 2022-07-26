package Sort;

public class QuickSort {
    // 给一个数组arr和一个整数num，把 <=num的数放在数组的左边， >num的数凡在数组的右边，左右两边的内部不要求有序
    // 要求：空间复杂度为O(1)，时间复杂度为O(N)

    // 思路：一开始另左边区域的索引为 -1，从头遍历元素，如果 <=num，则和索引的下一个位置的元素交换，左边区域扩大一位；
    // 如果 >num，则跳过
    public static void partiton_two(int[] arr, int num){
        int bound = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] <= num){
                int tmp = arr[i];
                arr[i] = arr[bound + 1];
                arr[bound + 1] = tmp;
                bound++;
            }
        }
    }

    // 给一个数组arr和一个整数num，把 <num的数放在数组的左边，=num的放中间， >num的数凡在数组的右边
    // 左右两边的内部不要求有序
    // 思路：此时左右两边设两个边界 left=-1, right=arr.length,遍历数组,会有三种逻辑：
    //      1.arr[i]=num i++
    //      2.arr[i]<num 该元素与left右边一个元素交换，left++, i++
    //      3.arr[i]>num 该元素与right左边一个元素交换，right--, i留在原地，因为新换到i位置的元素可能 =num,
    //        也可能<num,所以还需再判断一次
    public static void partition_three(int[] arr, int num){
        int left = -1, right = arr.length, index = 0;
        while (index < right){
            if (arr[index] == num)
                index++;
            else if (arr[index] < num) {
                swap(arr, index++, ++left);
            }else
                swap(arr, index, --right);
        }
    }

    // 荷兰国旗的partition问题和上面的非常一样，也是分成3个区域 < = >，该方法接收三个参数：arr、L、R；
    // num是arr[R],并且要求返回中间相等区域的头尾索引.
    public static int[] Holland(int[] arr, int l, int r){
        int left = l - 1, right = r, index = l;
        while (index < right){
            if (arr[index] == arr[r])
                index++;
            else if (arr[index] < arr[r]) {
                swap(arr, index++, ++left);
            }else
                swap(arr, index, --right);
        }
        swap(arr, right++, r);
        return new int[] {left + 1, index};
    }

    // 利用荷兰国旗的问题完成快速排序
    public static void quickSort(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        process(arr, 0, arr.length - 1);
    }

    private static void process(int[] arr, int l, int r) {
        if (l >= r)
            return;
        int[] equalAreas = Holland(arr, l, r);
        process(arr, l, equalAreas[0] - 1);
        process(arr, equalAreas[1] + 1, r);
    }

    // 快速排序最终版
    public static void quickSort_v2(int[] arr, int l, int r){
        if (arr == null || arr.length < 2)
            return;
        if (l < r){
            int mid = partition(arr, l, r);
            quickSort_v2(arr, l, mid - 1);
            quickSort_v2(arr, mid + 1, r);
        }
    }

    private static int partition(int[] arr, int l, int r) {
        int pivot = arr[l];
        int front = l, rear = r;
        while (front < rear){
            while (arr[rear] > pivot && front < rear)
                rear--;
            arr[front] = arr[rear];
            while (arr[front] < pivot && front < rear)
                front++;
            arr[rear] = arr[front];
        }
        arr[front] = pivot;
        return front;
    }


    public static void swap(int[] arr, int l, int r){
        int tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;
    }


    public static void main(String[] args) {
        int[] arr = {10, 6, 3, 7, 15, 12, 2, 8, 1, 11, 13};
//        int[] holland = QuickSort.Holland(arr, 0, arr.length - 1);
//        for (int i = 0; i < arr.length; i++) {
//            System.out.print(arr[i] + "\t");
//        }
//        System.out.println();
//        for (int i = 0; i < holland.length; i++) {
//            System.out.print(holland[i] + "\t");
//        }
//        System.out.println();
        QuickSort.quickSort_v2(arr, 0 ,arr.length - 1);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "\t");
        }
        System.out.println();
    }

}
