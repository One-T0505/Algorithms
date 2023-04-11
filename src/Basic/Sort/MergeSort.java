package Basic.Sort;
import utils.arrays;

import java.util.ArrayList;
import java.util.List;

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
        System.arraycopy(help, 0, arr, L, R - L + 1);
    }
    // ===============================================================================================




    // 非递归方法实现归并排序
    public static void mergeSortV2(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        int N = arr.length;
        int unit = 1;  // 可以看做一个最小合并单位，每次合并都是左右两个mergeGroup一起合并
        while (unit < N) { // log N
            // 当前左组的，第一个位置
            int L = 0;
            while (L < N) {
                if (L + unit >= N) // 如果连合并的左组都凑不齐，该阶段的合并就可以结束了，让unit翻倍
                    break;
                // 这里不用验证 L + unit >= N 上面的if已经判断过了
                int M = L + unit - 1;
                int R = (M + unit) >= N ? N - 1 : M + unit; // 数组的最后，有可能凑不齐合并的右组
                merge(arr, L, M, R);
                L = R + 1;
            }
            // 防止一个数很大时，再✖2就会溢出
            if (unit > (N >> 1))
                break;
            unit <<= 1;
        }
    }
    // ===============================================================================================





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
            // 计算小和有两种方式：
            //  1.最容易想到的，就是遍历每个元素，假如当前来到c元素，那么我们就遍历c的左边的小和，这样的视角
            //    是计算一个数有多少小和
            //  2.遍历每个元素，假如当前来到元素c，遍历c的右侧，看当前元素c总共能为多少个数提供小和。这样的视角
            //    是计算每个元素作为小和能提供多少。
            // 一个是别人能给他多少小和，一个是他能给别人多少小和，我们采用的是后者。
            // 该句表示：arr[left]可以为哪些元素提供小和；这两部分合并之后，arr[left]一定在哪些元素之前
            // 为什么是以左半部分的数为主线来求小和呢？
            // 废话，右边的数在左边的数的右侧，小和是要求左边有哪些数小于自己。
            // 当两个组开始合并时，我们计算了当前左部分的每个数，对当前右侧的数产生了多少小和，等合并完之后，相对次序
            // 会变化，但是无所谓，因为我们要将合并好的这组，继续和新的更大的右部分去计算小和，我们的目标是右侧的，
            // 所以左侧内部是什么顺序都没关系，反正都在右侧的左边就是了。
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
    // ================================================================================================




    // 2.逆序对问题：给一个数组{5, 2, 7, 10, 4, 8}，元素5有2个逆序对：(5,2)、(5,4)；元素2没有；元素7有1个逆序对；
    //   不难发现，逆序对就是看你后面有多少个数比你小。各个元素的逆序对之和就是该数组的逆序对数。求一个数组的逆序对数。
    //   该问题和上面的小和问题正好相反：小和问题算的是后面有多少个比自己大的元素，逆序对问题算的是后面有多少个比自己小的元素
    //   leetCode315 题和这个一模一样，只不过问法变成了：计算右侧小于当前元素的个数。这不是一样的吗？
    //   并且这道题加了点难度，要算的不是总和了，而是要具体到每一个元素右侧有多少个元素小于当前元素。可参考 315 题。
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
            help[index--] = arr[left] > arr[right] ? arr[left--] : arr[right--];
        }
        while (left >= L)
            help[index--] = arr[left--];
        while (right >= M + 1)
            help[index--] = arr[right--];
        System.arraycopy(help, 0, arr, L, help.length);
        return num;
    }
    // ================================================================================================






    // 4.一个数组arr，对每个元素M求：M的右边有多少个元素，是 M > 2 * 该元素。算出该数组中符合的所有数量。
    //   该方法的主函数和递归调用和之前的一摸一样，只是merge过程有点不一样
    public static int merge4(int[] arr, int L, int M, int R){
        // 单独处理
        int res = 0, right = M + 1;
        for (int left = L; left <= M; left++) {
            // 不回退技巧  每次左边换了一个新元素后，不用把right重置为M+1
            // 因为左边的元素arr[i]越来越大，右边小于arr[i]/2的元素数量也会越来越多，并且小于arr[i-1]/2的元素
            // 必然也小于arr[i]/2
            while (right <= R && arr[left] > (arr[right] << 1))
                right++;
            res += right - M - 1;
        }

        // 拷贝的操作单独写。没有将两个过程写在一起
        merge(arr, L, M, R);
        return res;
    }
    // ================================================================================================





    // 4.leetCode315
    //   给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： counts[i] 的值
    //   是 nums[i] 右侧小于 nums[i] 的元素的数量。
    //   这道题用归并排序或者增强版有序表都可以实现，这里只写了归并排序的解法

    // 这里要将数组元素包一层，带着自己初始时在数组的下标一起玩，因为归并排序最终会将原始位置打乱。
    public static class Node {
        public int val;
        public int index;

        public Node(int val, int index) {
            this.val = val;
            this.index = index;
        }
    }


    // 主方法   整体思路和逆序对问题一模一样
    public List<Integer> countSmaller(int[] nums) {
        ArrayList<Integer> res = new ArrayList<>();
        if (nums == null)
            return res;
        int N = nums.length;
        for (int i = 0; i < N; i++)
            res.add(0);
        if (N < 2)
            return res;
        Node[] nodes = new Node[N];
        for (int i = 0; i < N; i++)
            nodes[i] = new Node(nums[i], i);
        f(nodes, 0, N - 1, res);
        return res;
    }

    private void f(Node[] arr, int L, int R, ArrayList<Integer> res) {
        if (L == R)
            return;
        int mid = L + ((R - L) >> 1);
        f(arr, L, mid, res);
        f(arr, mid + 1, R, res);
        mergeSum(arr, L, mid, R, res);
    }

    private void mergeSum(Node[] arr, int L, int mid, int R, ArrayList<Integer> res) {
        Node[] help = new Node[R - L + 1];
        int i = help.length - 1;
        int p1 = mid, p2 = R;
        while (p1 >= L && p2 > mid){
            if (arr[p1].val > arr[p2].val)
                res.set(arr[p1].index, res.get(arr[p1].index) + p2 - mid);
            help[i--] = arr[p1].val > arr[p2].val ? arr[p1--] : arr[p2--];
        }
        while (p1 >= L)
            help[i--] = arr[p1--];
        while (p2 > mid)
            help[i--] = arr[p2--];

        System.arraycopy(help, 0, arr, L, help.length);
    }
    // ================================================================================================






    // 5.给定一个数组arr,两个整数lower和upper, 返回arr中有多少个子数组的累加和在[lower,upper]范围上

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


    // 假如sum[i]==100 即0~i的累加和为100，目标范围是[60, 90]  我们的目标是以每个元素结尾去寻找符合要求的子数组。
    // 所以以i为子数组结尾，有多少个子数组的累加和能在该范围上呢？
    // 假设在0～i-1上有一个位置j，其前缀累加和为sum[j]   如果 100-90 <= sum[j] <= 100-60
    // 那么我们就可以说找到了一个以i结尾的子数组j+1~i的累加和在[60,90]上了。这种方法叫间接求法。
    // 理解了上面的思路之后，下面的方法就好理解了：
    // 当有个部分需要合并时，比如: [3, 5, 10, 12]   [5, 7, 9, 13]    这些部分在之前合并时该计算的都已经计算了，现在
    // 要来讲这两个部分合并时新产生的答案。右部分是我们需要遍历的，每个元素都要作为上述的子数组结尾元素，然后看左边哪个元素
    // 的前缀累加和符合 sum[i] - upper ~ sum[i] - lower
    private static int merge5(int[] help, int L, int M, int R, int lower, int upper) {
        int res = 0;
        int leftBound = L, rightBound = L;
        for (int right = M + 1; right <= R; right++) {
            int min = help[right] - upper;
            int max = help[right] - lower;
            while (leftBound <= M && help[leftBound] < min)
                leftBound++;
            // leftBound 现在的位置是第一个前缀和 >= min 的位置
            while (rightBound <= M && help[rightBound] <= max)
                rightBound++;
            // rightBound 现在的位置是第一个前缀和 >= max 的位置
            // 所以leftBound～rightbound-1的每个位置i的前缀和，即 0~i的累加和都符合要求
            // 那也就是说 leftBound～rightbound-1 有多少个，就能对应找出以right为结尾的子数组累加和符合范围
            res += Math.max(0, rightBound - leftBound);
        }
        merge(help, L, M, R);
        return res;
    }
    // ================================================================================================






    // for test
    private static void test(int testTime, int maxSize, int maxVal){
        for (int i = 0; i < testTime; i++) {
            int[] src = arrays.randomNoNegativeArr(maxSize, maxVal);
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
    }


    public static void main(String[] args) {
        int[] arr = {6, 2, 4, 9, 3, 1, 7};
        System.out.println(lessSum(arr));
    }
}
