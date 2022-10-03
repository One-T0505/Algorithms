package Sort;
import utils.arrays;
public class MergeSort {

    //    递归实现归并排序的主过程
    public static void mergeSortV1(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        process1(arr, 0, arr.length - 1);
    }

    // 递归过程
    private static void process1(int[] arr, int L, int R){
        if (L==R)
            return;
        int mid = L + ((R - L) >> 1);    //等价于： mid=(L+R)/2  这么写是为了防溢出
        process1(arr, L, mid);
        process1(arr,mid + 1, R);
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
    public static void mergeSortV2(int[] arr){
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
            if (mergeGroup > (N >> 1))
                break;
            mergeGroup <<= 1;
        }
    }


    // 1.小和问题：假如有一个数组 {5, 2, 7, 10, 4, 8}，遍历每个元素，当前元素之前比自己小的所有元素累加求和就是该元素的
    //   小和。eg：5的小和为0，因为他前面没有比他小的；2的小和为0；7的小和为5+2=7；10的小和为14；4的小和为2；8的小和
    //   为18；数组的小和就是各个元素的小和累加。
    public static int lessSum(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        return process2(arr, 0, arr.length - 1);
    }

    public static int process2(int[] arr, int l, int r) {
        if (l == r)
            return 0;
        int mid = l + ((r - l) >> 1);
        return process2(arr, l, mid) + process2(arr, mid + 1, r) + merge2(arr, l, mid, r);
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

    // 2.逆序对问题：给一个数组{5, 2, 7, 10, 4, 8}，元素5有2个逆序对：(5,2)、(5,4)；元素2没有；元素7有1个逆序对；
    //   不难发现，逆序对就是看你后面有多少个数比你小。各个元素的逆序对之和就是该数组的逆序对数。求一个数组的逆序对数。
    //   该问题和上面的小和问题正好相反：小和问题算的是后面有多少个比自己大的元素，逆序对问题算的是后面有多少个比自己小的元素
    public static int reversePair(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        return process3(arr,0, arr.length - 1);
    }

    public static int process3(int[] arr, int l, int r) {
        if (l == r)
            return 0;
        int mid = l + ((r - l) >> 1);
        return process3(arr, l, mid) + process3(arr, mid + 1, r) +merge3(arr, l, mid, r);
    }

    public static int merge3(int[] arr, int L, int M, int R) {
        int [] help = new int[R - L + 1];
        int left = M, right = R, index = help.length - 1, num = 0;
        // 此时是逆序完成help数组
        while ((left >= L) && (right >= M + 1)){
            num += arr[left] > arr[right] ? right - M : 0;
            help[index--] = arr[left] <= arr[right] ? arr[right--] : arr[left--];
        }
        while (left >= L)
            help[index--] = arr[left--];
        while (right >= M + 1)
            help[index--] = arr[right--];
        System.arraycopy(help, 0, arr, L, help.length);
        return num;
    }

    // 3.一个数组arr，对每个元素M求：M的右边有多少个元素，是 M > 2 * 该元素。算出该数组中符合的所有数量。
    //   该方法的主函数和递归调用和之前的一摸一样，只是merge过程有点不一样
    public static int merge4(int[] arr, int L, int M, int R){
        // 单独处理
        int res = 0, right = M + 1;
        for (int left = L; left <= M; left++) {
            while (right <= R && arr[left] > (arr[right] << 1))
                right++;
            res += right - M - 1;
        }

        // 拷贝的操作单独写。没有将两个过程写在一起
        merge(arr, L, M, R);
        return res;
    }

    // 4.给定一个数组arr,两个整数lower和upper, 返回arr中有多少个子数组的累加和在[lower,upper]范围上

    // 暴力解法：先确定每一个子数组，然后在数组上遍历求和。时间复杂度：O(N3)
    public static int limitedSumV1(int[] arr, int lower, int upper){
        if (arr == null || arr.length == 0 || lower > upper)
            return 0;
        int res = 0;
        for (int L = 0; L < arr.length; L++) {
            for (int R = L; R < arr.length; R++) {
                int sum = 0;
                for (int k = L; k <= R; k++) {
                    sum += arr[k];
                }
                if (sum >= lower && sum <= upper)
                    res++;
            }
        }
        return res;
    }

    // 开辟一个同样大小的辅助数组help用于记录累加和，help[i]表示arr[0] + ... + arr[i]的累加和.优化的地方在于每次确定
    // 了一个子数组边界后，不用再遍历一遍算累加和了。假如此时确定了子数组边界为[L, R] 那么只需要让help[R] - help[L - 1]
    // 即可得到答案。时间复杂度为：O(N2) 空间复杂度：O(N)
    public static int limitedSumV2(int[] arr, int lower, int upper){
        if (arr == null || arr.length == 0 || lower > upper)
            return 0;
        // 准备辅助的累加和数组help
        int[] help = new int[arr.length];
        help[0] = arr[0];
        for (int i = 1; i < help.length; i++)
            help[i] = help[i - 1] + arr[i];

        int res = 0;
        for (int L = 0; L < arr.length; L++) {
            for (int R = L; R < arr.length; R++) {
                int sum = L == 0 ? help[R] : help[R] - help[L - 1];
                if (sum >= lower && sum <= upper)
                    res++;
            }
        }
        return res;
    }

    // 利用归并排序。这里只需要前缀和数组，已经和原数组无关了。准备好和愿数组等长的累加和数组help，help[i] = arr[0] + ... + arr[i]
    // eg：help = [4, 6, 8, 5, 2, 9]  range = [3, 7]   这里要学习一个思想：
    // help[5]=9，表示的是数组中从0～5的累加和为9，想知道在这里面以5为终点的子数组是否有符合该range的，也就是说：
    // arr[0] + ... + arr[5] = 9, 假设以5结尾的某个子数组的范围符合range，就是：3<=arr[x] +...+ arr[5]<=7
    // 那么剩下的和应该满足：2<=arr[0] +...+arr[x-1]<=6,  而arr[0] +...+arr[x-1] = help[x-1].
    // 所以当我们有了前缀和数组help后，问题可以简化为：
    //   对每个元素help[i]，我们想找寻在原数组中以i元素结尾的子数组有多少个是符合[lower, upper]
    // 进一步简化：
    //   在前缀和数组中，help[i]左边有多少个元素满足：help[i] - help[j] <=
    public static int limitedSumV3(int[] arr, int lower, int upper){
        if (arr == null || arr.length == 0 || lower > upper)
            return 0;
        // 准备辅助的累加和数组help
        int[] help = new int[arr.length];
        help[0] = arr[0];
        for (int i = 1; i < help.length; i++)
            help[i] = help[i - 1] + arr[i];
        return process4(help, 0, help.length - 1, lower, upper);
    }

    private static int process4(int[] help, int L, int R, int lower, int upper) {
        if (L == R)
            return help[L] >= lower && help[L] <= upper ? 1 : 0;
        int M = L + ((R - L) >> 1);
        return process4(help, L, M, lower, upper) + process4(help, M + 1, R, lower, upper) +
                merge5(help, L, M, R, lower, upper);
    }

    private static int merge5(int[] help, int L, int M, int R, int lower, int upper) {
        int res = 0;
        int leftBound = L, rightBound = L;
        for (int right = M + 1; right <= R; right++) {
            int min = help[right] - upper;
            int max = help[right] - lower;
            while (leftBound <= M && help[leftBound] < min)
                leftBound++;
            while (rightBound <= M && help[rightBound] <= max)
                rightBound++;
            res += Math.max(0, rightBound - leftBound);
        }
        merge(help, L, M, R);
        return res;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            int[] src = arrays.generateRandomArray(20, 50);
            int lower = arrays.generateRandomNum(50);
            int upper;
            do {
                upper = arrays.generateRandomNum(50);
            }while (upper < lower);
            int correct = limitedSumV2(src, lower, upper);
            int error = limitedSumV3(src, lower, upper);
            if (correct != error){
                System.out.println("failed!");
                System.out.println("range: [" + lower + ", " + upper + "]");
                arrays.printArray(src);
                System.out.println("正确答案：" + correct);
                System.out.println("错误答案：" + error);
                return;
            }
        }
        System.out.println("AC");
//        int[] src = {8, 39, 24};
//        System.out.println(limitedSumV3(src, 48, 49));
    }
}
