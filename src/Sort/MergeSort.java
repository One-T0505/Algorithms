package Sort;
import utils.arrays;
public class MergeSort {

    //    递归实现归并排序的主过程
    public static void mergeSort(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        process_1(arr, 0, arr.length - 1);
    }

    // 递归过程
    private static void process_1(int[] arr, int L, int R){
        if (L==R)
            return;
        int mid = L + ((R - L) >> 1);    //等价于： mid=(L+R)/2  这么写是为了防溢出
        process_1(arr, L, mid);
        process_1(arr,mid + 1, R);
        merge(arr, L, mid, R);
    }

    //    归并过程, L~mid，mid+1~R 已经分别有序了，现在要让 L~R 整体有序
    public static void merge(int[] arr, int L, int mid, int R){
        int[] help = new int[R - L + 1];
        int index = 0, left = L, right = mid + 1;
        while (left <= mid && right <= R){
            help[index++] = arr[left] <= arr[right] ? arr[left++] : arr[right++];
        }
        while (left <= mid)
            help[index++] = arr[left++];
        while (right <= R)
            help[index++] = arr[right++];
        for (index = 0; index < help.length; index++)
            arr[L + index] = help[index];

    }

    // 非递归方法实现归并排序
    public static void mergeSort_v2(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        int N = arr.length;
        int mergeGroup = 1;  // 可以看做一个最小合并单位，每次合并都是左右两个mergeGroup一起合并
        while (mergeGroup < N) { // log N
            // 当前左组的，第一个位置
            int L = 0;
            while (L < N) {
                if (L + mergeGroup >= N)
                    break;
                int M = L + mergeGroup - 1;
                int R = (M + mergeGroup) >= N ? N - 1 : M + mergeGroup;
                merge(arr, L, M, R);
                L = R + 1;
            }
            // 防止一个数很大时，再✖2就会溢出
            if (mergeGroup > N / 2)
                break;
            mergeGroup <<= 1;
        }
    }


    // 小和问题：假如有一个数组 {5, 2, 7, 10, 4, 8}，遍历每个元素，当前元素之前比自己小的所有元素累加求和就是该元素的
    // 小和。eg：5的小和为0，因为他前面没有比他小的；2的小和为0；7的小和为5+2=7；10的小和为14；4的小和为2；8的小和
    // 为18；数组的小和就是各个元素的小和累加。
    public static int lessSum(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        return process_2(arr, 0, arr.length - 1);
    }

    public static int process_2(int[] arr, int l, int r) {
        if (l == r)
            return 0;
        int mid = l + ((r - l) >> 1);
        return process_2(arr, l, mid) + process_2(arr, mid + 1, r) + merge2(arr, l, mid, r);
    }


    public static int merge2(int[] arr, int l, int m, int r){
        int[] help = new int[r - l + 1];
        int sum = 0, index = 0;
        int left = l, right = m + 1;
        while (left <= m && right <= r){
            sum += arr[left] < arr[right] ? arr[left] * (r - right + 1) : 0;
            help[index++] = arr[left] < arr[right] ? arr[left++] : arr[right++];
        }
        while (left <= m)
            help[index++] =arr[left++];
        while (right <= r)
            help[index++] = arr[right++];
        System.arraycopy(help, 0, arr, l, help.length);
        return sum;
    }

    // 逆序对问题：给一个数组{5, 2, 7, 10, 4, 8}，元素5有2个逆序对：(5,2)、(5,4)；元素2没有；元素7有1个逆序对；
    // 不难发现，逆序对就是看你后面有多少个数比你小。各个元素的逆序对之和就是该数组的逆序对数。求一个数组的逆序对数。
    // 该问题和上面的小和问题正好相反：小和问题算的是后面有多少个比自己大的元素，逆序对问题算的是后面有多少个比自己小的元素
    public static int reversePair(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        return process_3(arr,0, arr.length - 1);
    }

    public static int process_3(int[] arr, int l, int r) {
        if (l == r)
            return 0;
        int mid = l + ((r - l) >> 1);
        return process_3(arr, l, mid) + process_3(arr, mid + 1, r) +merge3(arr, l, mid, r);
    }

    public static int merge3(int[] arr, int l, int mid, int r) {
        int [] help = new int[r - l + 1];
        int p1 = mid, p2 = r, index = help.length - 1, num = 0;
        while ((p1 >= l) && (p2 >= mid + 1)){
            num += arr[p1] > arr[p2] ? p2 - mid : 0;
            help[index--] = arr[p1] <= arr[p2] ? arr[p2--] : arr[p1--];
        }
        while (p1 >= l)
            help[index--] = arr[p1--];
        while (p2 >= mid + 1)
            help[index--] = arr[p2--];
        System.arraycopy(help, 0, arr, l, help.length);
        return num;
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            int[] src = arrays.generateRandomArray(20, 100);
            int[] copy = arrays.copyArray(src);
            mergeSort(src);
            mergeSort_v2(copy);
            if (src.length >= 2 && copy.length >= 2 && src.length == copy.length){
                int len = src.length;
                for (int j = 0; j < len; j++) {
                    if (src[j] != copy[j]){
                        System.out.println("failed!");
                        arrays.printArray(src);
                        arrays.printArray(copy);
                        break;
                    }
                }
            }
        }
    }
}
