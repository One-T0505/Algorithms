package Sort;

import utils.arrays;

import java.util.Stack;

public class QuickSort {
    // 给一个数组arr和一个整数num，把 <=num的数放在数组的左边， >num的数放在数组的右边，左右两边的内部不要求有序
    // 要求：空间复杂度为O(1)，时间复杂度为O(N)

    // 思路：一开始另左边区域的边界bound为 -1，从头遍历元素，如果 <=num，则和bound的下一个位置的元素交换，左边区域扩大一位 bound++；
    // 如果 >num，则跳过
    public static void partitionTwo(int[] arr, int num){
        if (arr == null || arr.length == 0)
            return;
        int bound = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] <= num)
                arrays.swap(arr, ++bound, i);
        }
    }

    // 给一个数组arr和一个整数num，把 <num的数放在数组的左边，=num的放中间， >num的数凡在数组的右边
    // 左右两边的内部不要求有序
    // 思路：此时左右两边设两个边界 left=-1, right=arr.length,遍历数组,会有三种逻辑：
    //      1.arr[i]=num i++
    //      2.arr[i]<num 该元素与left右边一个元素交换，left++, i++
    //      3.arr[i]>num 该元素与right左边一个元素交换，right--, i留在原地，因为新换到i位置的元素可能 =num,
    //        也可能<num,所以还需再判断一次
    public static void partitionThree(int[] arr, int num){
        if (arr == null || arr.length == 0)
            return;
        int leftBound = -1, rightBound = arr.length;
        int index = 0;
        while (index < rightBound){
            if (arr[index] == num)
                index++;
            else if (arr[index] < num)
                arrays.swap(arr, ++leftBound, index++);
            else
                arrays.swap(arr, --rightBound, index);
        }
    }
    // ====================================================================================================


    // 前两个问题说明了如何partition的过程，接下来就要用不同的partition过程来实现快速排序
    // 快速排序初级版，每次只能搞定一个数
    public static void quickSortV1(int[] arr, int l, int r){
        if (arr == null || arr.length < 2)
            return;
        if (l < r){
            int mid = partition(arr, l, r);
            quickSortV1(arr, l, mid - 1);
            quickSortV1(arr, mid + 1, r);
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
    // ====================================================================================================


    // 荷兰国旗的partition问题和上面的非常一样，也是分成3个区域 < = >，该方法接收三个参数：arr、L、R；
    // num是arr[R],并且要求返回中间相等区域的头尾索引.
    public static int[] HollandFlag(int[] arr, int L, int R){
        if (L > R)
            return new int[] {-1, -1};
        if (L == R)
            return new int[] {L, R};
        int left = L - 1, right = R, index = L;
        while (index < right){
            if (arr[index] == arr[R])
                index++;
            else if (arr[index] < arr[R]) {
                arrays.swap(arr, index++, ++left);
            }else
                arrays.swap(arr, index, --right);
        }
        arrays.swap(arr, right++, R);
        return new int[] {left + 1, right - 1};
    }

    // 利用荷兰国旗的问题完成快速排序，实现快速排序的进化版
    // 每次都能将相等的一批数搞定
    public static void quickSortV2(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        process(arr, 0, arr.length - 1);
    }

    private static void process(int[] arr, int l, int r) {
        if (l >= r)
            return;
        int[] equalAreas = HollandFlag(arr, l, r);
        process(arr, l, equalAreas[0] - 1);
        process(arr, equalAreas[1] + 1, r);
    }
    // ====================================================================================================


    // 快排进化版的partition过程因为用到了荷兰国旗的分治，所以要优于初级版。如果是最差情况，数组中每一个元素都不相同
    // 并且最后区域都到了边界上，那时间复杂度就降为了O(N2).
    // 快排最终版的优化在于：每次随机在数组中选择一个数和arr[R]交换，然后用新的arr[R]作为partition的分界
    public static void quickSortV3(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        process2(arr, 0, arr.length - 1);
    }

    private static void process2(int[] arr, int L, int R) {
        if (L >= R)
            return;
        arrays.swap(arr, R, L + (int) (Math.random() * (R - L + 1)));
        int[] equalAreas = HollandFlag(arr, L, R);
        process2(arr, L, equalAreas[0] - 1);
        process2(arr, equalAreas[1] + 1, R);
    }
    // ====================================================================================================


    // 非递归改写随机快速排序
    public static void quickSortV3UnderStack(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        int N = arr.length;
        arrays.swap(arr, (int) (Math.random() * N), N - 1);
        int[] equalAreas = HollandFlag(arr, 0, N - 1);
        Stack<Record> records = new Stack<>();
        records.push(new Record(0, equalAreas[0] - 1));
        records.push(new Record(equalAreas[1] + 1, N - 1));
        while (!records.isEmpty()){
            Record cur = records.pop();
            arrays.swap(arr, cur.L + (int) (Math.random() * (cur.R - cur.L + 1)), cur.R);
            equalAreas = HollandFlag(arr, cur.L, cur.R);
            records.push(new Record(cur.L, equalAreas[0] - 1));
            records.push(new Record(equalAreas[1] + 1, cur.R));
        }
    }
    public static class Record{
        public int L;
        public int R;

        public Record(int l, int r) {
            L = l;
            R = r;
        }
    }
    // ====================================================================================================

}
