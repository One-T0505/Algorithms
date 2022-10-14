import utils.arrays;
import Sort.Sort;

public class BFPRT {

    // 快排的改编版应用
    // 给一个无序的数组arr，找出数组中第k小的数，其中k>=1. 要求时间复杂度O(N), 额外空间复杂度O(1).

    // 用快排，每次将数组分成三个区域：小于区、等于区、大于区。按理说如果整个数组有序了，那么第k小的数肯定在arr[k-1]处，所以
    // 如果 等于区左边界 <= k - 1 <= 等于区右边界, 那么就结束了，直接返回arr[k-1]，毕竟等于区都一样，返回哪个都可以。
    // 否则，就去左边递归或者右边递归！！！！ 这里是和传统快排最大的不同，传统快排是左右两边都要递归，而现在左右两边只有
    // 一侧会递归。
    public static int findK(int[] arr, int k){
        if (arr == null || arr.length < k || k < 1)
            return Integer.MAX_VALUE;   // 表示无意义
        return process3(arr, 0, arr.length - 1, k - 1);
    }

    // index表示如果arr在L～R上有序后，应该返回的位置,并且index一定满足： L <= index <= R
    private static int process3(int[] arr, int L, int R, int index) {
        if (L == R)
            return arr[L];
        int pivot = L + (int) (Math.random() * (R - L + 1));
        arrays.swap(arr, pivot, R);
        int[] equalAreas = HollandFlag(arr, L, R);
        if (equalAreas[0] <= index && index <= equalAreas[1])
            return arr[index];
        else if (index < equalAreas[0]) {
            return process3(arr, L, equalAreas[0] - 1, index);
        } else
            return process3(arr, equalAreas[1] + 1, R, index);
    }

    // 非递归实现上述方法
    public static int findKV2(int[] arr, int k){
        if (arr == null || arr.length < k || k < 1)
            return Integer.MAX_VALUE;   // 表示无意义
        int L = 0, R = arr.length - 1;
        int pivot = 0;
        while (L <= R){
            pivot = L + (int) (Math.random() * (R - L + 1));
            arrays.swap(arr, pivot, R);
            int[] equalAreas = HollandFlag(arr, L, R);
            if (equalAreas[0] <= k - 1 && k - 1 <= equalAreas[1])
                return arr[k - 1];
            else if (k - 1 < equalAreas[0]) {
                R  = equalAreas[0] - 1;
            } else
                L = equalAreas[1] + 1;
        }
        return arr[k - 1];
    }

    // 默认以arr[R]为划分值，将数组排序成三个区域：小于arr[R]区  等于区  大于arr[R]；小于区和大于区内部不保证有序。
    // 并返回等于区的开头位置和结束位置。
    private static int[] HollandFlag(int[] arr, int L, int R) {
        if (L > R)
            return new int[] {-1, -1};
        if (L == R)
            return new int[] {L, R};
        int leftBound = L - 1, rightBound = R;
        int index = L;
        while (index < rightBound){
            if (arr[index] < arr[R])
                arrays.swap(arr, ++leftBound, index++);
            else if (arr[index] == arr[R])
                index++;
            else
                arrays.swap(arr, --rightBound, index);
        }
        arrays.swap(arr, R, rightBound++);
        return new int[] {leftBound + 1, rightBound - 1};
    }
    // =====================================================================================================



    // 上面的方法已经是解决该问题的最优时间复杂度和空间复杂度了。现在才开始进入BFPRT算法！！！！！！！！！！！
    // BFPRT也是用来解决在一个无序数组中找出第k小的数。它的流程几乎和上述方法一模一样，唯一的区别在于：在将数组分成
    // 三个区域的时候，传统算法是随机找一个数作为pivot的，这样有可能让左右两区不均匀，会出现最差情况，这种最差情况
    // 是我们无法避免的。
    // BFPRT最重要的贡献就在于，分区时挑选pivot非常讲究，这种挑选方法可以非常确定地规避这种最差情况，不管怎么样，每次
    // 递归都可以过滤掉至少 3/10 * N的元素。所以这个方法的地位很重要。下面先介绍下流程：
    //
    //                                                    |- 1>将数组每5个分成一个group，最后一组满不满都分成一组
    //                                                    |- 2>每个组内从小到大排序，选出中位数，偶数个的组选出上中点
    //  1.讲究地挑选出pivot  -------------------------------|- 3>挑选出的所有数组成数组m，从m中选出其中位数或上中点作为
    //                                                          pivot。
    //                                                    |-   （有没有发现这个问题和我们一开始的问题是一样的，
    //                                                           规模而且变小了所以，这里可递归调用）
    //
    //  2.分成三个区域
    //  3.若k-1在等于区中，返回
    //  4.若不在等于区中，左边或右边递归，重复执行2、3、4
    //
    //
    // BFPRT这是在挑选pivot时做了优化，为什么费那么大劲做优化，一定是有好处的。我们来分析下时间复杂度。
    //
    //       ●  ●  ●  ●
    //       ●  ●  ●  ●  ●                   如图，每一列就是一个group，最后一组只有4个元素。带箭头那行就是每组
    //  ->   ●  ●  ✯︎  ●  ●                   选出的中位数从而组成m，m的大小为 N/5, ✯就是m的中位数，也就是我们挑选出的
    //       ●  ●  ●  ●  ●                   pivot。那么现在回归到愿数组中，至少有多少个元素是比pivot大的？
    //       ●  ●  ●  ●  ●                  首先在m中，pivot后面的元素都比pivot大，就是 N/5 * 1/2 = N/10 个；
    //  组号  1  2  3  4  5                  这N/10个，每一个对应于自己的group中，都有3比自己大。
    //                                      所以整个数组中比pivot大的至少有：N/10 * 3 = 3/10 * N，所以比pivot
    //                                      小的最多是 7/10 * N，所以如果递归进入左循环的最差规模就是T(7/10 * N)，
    //                                     同样的方法可以算出进入右循环的最差规模也是T(7/10 * N)，所以每次都会至少淘汰
    //                                     3/10 * N. 因为时间复杂度的大O表示最差情况，所以我们用右边至少有多少个，就能
    //                                     间接求出左边最多的情况。
    // 这里的 3/10 * N 算的只是数量级，当然还有一些边界问题，比如最后一组只有2个的时候，m的长度需要 +1, -1操作，
    // 这些常数的规模就忽略不计。
    //
    // 相关问题：
    // 1.为什么要选取5作为group的大小？
    //   因为是5个人一起创造的这个方法，所以5这个设置是启发式的。group大小变了，那就不是3N/10了。

    public static int findKV3(int[] arr, int k){
        if (arr == null || arr.length < k || k < 1)
            return Integer.MAX_VALUE;   // 表示无意义
        return bfprt(arr, 0, arr.length - 1, k - 1); // 因为0对应第1小，所以k-1才是第k小的数
    }

    // 将arr在L～R上排序，并且返回index位置的数. 默认 L <= index <= R
    private static int bfprt(int[] arr, int L, int R, int index) {
        if (L == R)
            return arr[L];
        // medianOfMedians就是完整的挑选pivot的流程，该方法做的事：
        //  1.将arr上L～R上5个元素分成一组，并在组内排好序
        //  2.将组内中位数挑选出来组成新数组m
        //  3.从m中挑选出中位数，也就是最终的pivot返回
        int pivot = medianOfMedians(arr, L, R); // T(N/5) + O(N)
        // 下面的流程是不是和上面的一模一样
        int pos = -1;
        for (int i = 0; i < arr.length; i++) {  // O(N)
            if (arr[i] == pivot){
                pos = i;
                break;
            }
        }
        arrays.swap(arr, pos, R);
        int[] equalAreas = HollandFlag(arr, L, R);
        if (index >= equalAreas[0] && index <= equalAreas[1])
            return arr[index];
        else if (index < equalAreas[0])
            return bfprt(arr, L, equalAreas[0] - 1, index);  // T(7N/10)
        else
            return bfprt(arr, equalAreas[1] + 1, R, index);  // T(7N/10)
    }
    // 所以，BFPRT的递归复杂度为：T(N) = T(N/5) + T(7N/10) + O(N)
    // 虽然不能用Master公式直接得到，但是严格的数学证明表示：该递归式收敛于O(N)

    private static int medianOfMedians(int[] arr, int L, int R) {
        int length = R - L + 1;
        int offset = length % 5 == 0 ? 0 : 1;
        int[] medians = new int[length / 5 + offset];
        for (int group = 0; group < medians.length; group++) {
            int groupHead = L + group * 5;
            // Math.min(groupHead + 4, R) 保证最后一组也不越界
            medians[group] = getMedian(arr, groupHead, Math.min(groupHead + 4, R));
        }
        // 再从m中找出中位数。((medians.length + 1) >> 1) - 1 能保证奇数个返回中点，偶数个返回上中点，并且是索引
        return bfprt(medians, 0, medians.length - 1, ((medians.length + 1) >> 1) - 1);
    }

    private static int getMedian(int[] arr, int L, int R) {
        // 用插入排序即可，因为这个范围最多就5个数
        for (int i = L + 1; i <= R; i++) {
            for (int j = i - 1; j >= L && arr[j] > arr[j + 1]; j--)
                arrays.swap(arr, j, j + 1);
        }
        return arr[(L + R) >> 1];
    }
    // =====================================================================================================



    // 应用：给定一个无序数组arr中，给定一个正数k， 返回top k个最大的数，要求实现不同时间复杂度三个方法:
    //      1) O(N*logN)
    //      2) O(N + k*logN)
    //      3) O(N + k*logk)

    // 1. O(N*logN)   这个就是用最常规的归并排序、快速排序、堆排序就能完成
    // 2. O(N + k*logN)   这里可以用自底向上建堆的堆排序完成；首先自底向上建堆O(N)，然后弹出k个元素，每次弹出一个就要
    //    向下heapify，每次调整的代价为O(logN)
    // 3. O(N + k*logk)  首先生成一个大小为k的数组，已知第k大的数为X，先遍历一整遍原数组O(N)，只要比X大，就放到小数组中，
    //    对这个长度为k的数组排序O(k*logk)


    // =====================================================================================================
    public static void main(String[] args) {
        // 测试BFPRT
        for (int i = 0; i < 1000000; i++) {
            int maxSize = 20;
            int maxVal = 60;
            int[] arr = arrays.generateRandomArray(maxSize, maxVal);
            int k = ((int) (Math.random() * arr.length)) + 1;
            int res1 = findKV2(arr, k);
            int res2 = findKV3(arr, k);
            if (res1 != res2){
                System.out.println("Failed!");
                System.out.println(res1);
                System.out.println(res2);
                return;
            }
        }
        System.out.println("AC");
        // ===============================================================================================
    }
}
