package Tree.OrderedTree;

import utils.arrays;
import Sort.MergeSort;

// 这些练习是有序表的改写，因为有些题目是用系统提供的有序表完成不了的，所以需要自己手写。

// 下面这道题，在归并排序时讲过，可以去看MergeSort文件第4题，那会没学有序表，用的是一个非常复杂的方法改写的，现在
// 会了有序表，可以改写。
// 1.给定一个数组arr,两个整数lower和upper, 返回arr中有多少个子数组的累加和在[lower,upper]范围上

// 用SBTree改写。核心思路还是按照以某个元素结尾来找子数组。同样也需要一个累加和数组sum，表示从0～自己的累加和。
// 对于任意一个子数组，其结尾元素下标为i，从0～i的累加和为sum[i]，如果存在一个x在0～i范围内，并且：
// sum[i]-upper <= sum[x] <= sum[i]-lower，那么 x+1～i的累加和就是在[lower,upper]之间的；所以对于以i结尾的所有子数组，
// 我们要找的就是在0～i之间有多少个元素从0到自己的累加和是在[sum[i]-upper, sum[i]-lower]之间的。

// 想象这样一棵二叉排序树，我们遍历这个数组，来到一个元素arr[i]时，我们将其从0到i的累加和sum作为key，加入到这棵树中，并查询
// 这棵树上有多少个结点的key是在[sum[i]-upper, sum[i]-lower]之间的，就是我们想找的：以i结尾所有的累加和在[lower,upper]
// 之间的子数组。在这棵树中不需要直接提供这样的接口，我们可以实现有多少个结点是小于某个key的；比如我们要求树上有多少个
// 结点是在[4, 11]之间的，我们可以求有多少个结点<11，有多少个结点<4，然后相减即可。
// 这棵树和之前实现的SBTree有些不同，这棵树需要可以添加重复的key，因为数组中可能有负数和0，所以累加和不会一直递增，所以
// 添加进去的key不会一直递增；其次我们需要记录当前树中所有不同key的数量，还要记录总的数量，就是说重复的key也要计数。
// 这棵树不需要删除操作。

import java.util.HashSet;

// 给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper]
//（包含 lower 和 upper）之内的区间和的个数。区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元
// 素之和，包含 i 和 j (i ≤ j)。

public class _0327_Code {

    // 这道题是很经典的题目。方法1用了有序表的改写来完成。方法2就是之前归并排序里讲的方法。
    public static int countRangeSum(int[] arr, int lower, int upper){
        SBExtendedTree tree = new SBExtendedTree();
        long sum = 0;
        int res = 0;
        tree.put(0);
        for (int elem : arr) {
            sum += elem;
            long a = tree.lessKey(sum - lower + 1);
            long b = tree.lessKey(sum - upper);
            res += a - b;
            tree.put(sum);
        }
        return res;
    }

    // 结点定义
    public static class SBTNode{
        public long key;  // 表示累加和
        public SBTNode left;
        public SBTNode right;
        public int size;  // 不同的key的数量
        public int all;   // 所有的数量

        public SBTNode(long key) {
            this.key = key;
            size = 1;
            all = 1;
        }
    }

    // 整棵树的封装
    // ======================================================================================================
    public static class SBExtendedTree {
        private SBTNode root;
        public HashSet<Long> set = new HashSet<>(); // 用于记录添加过的key


        // 添加结点
        public void put(long sum){
            boolean contains = set.contains(sum);
            root = add(root, sum, contains);
            set.add(sum);
        }

        // 计算有多少个严格小于key的记录，这里的记录是指包含重复加入的结点数。<11的记录数量 == <=10的记录数量
        // 因为这里的key是int整数
        public long lessKey(long key){
            SBTNode cur = root;
            int res = 0;
            while (cur != null){
                if (cur.key < key){
                    res += cur.all - (cur.right == null ? 0 : cur.right.all);
                    cur = cur.right;
                } else if (cur.key > key) {
                    cur = cur.left;
                } else
                    return res + (cur.left == null ? 0 : cur.left.all);
            }
            return res;
        }

        // 计算比key大的记录有多少，不算相等的
        // 比如要计算 >11的，只需要用全部的 - <=11的。
        public long greaterKey(long key){
            return root == null ? 0 : root.all - lessKey(key + 1);
        }

        private SBTNode add(SBTNode cur, long key, boolean contains) {
            if (cur == null)
                return new SBTNode(key);
            else {
                cur.all++;
                if (key == cur.key)
                    return cur;
                else { // 还需要左滑或者右滑
                    if (!contains)
                        cur.size++;
                    if (key < cur.key)
                        cur.left = add(cur.left, key, contains);
                    else
                        cur.right = add(cur.right, key, contains);
                    return keepBalanced(cur);
                }
            }
        }

        // 调平衡
        private SBTNode keepBalanced(SBTNode cur){
            if (cur == null)
                return null;
            int leftS = cur.left == null ? 0 : cur.left.size;
            int leftLeftS = cur.left != null && cur.left.left != null ? cur.left.left.size : 0;
            int leftRightS = cur.left != null && cur.left.right != null ? cur.left.right.size : 0;
            int rightS = cur.right == null ? 0 : cur.right.size;
            int rightLeftS = cur.right != null && cur.right.left != null ? cur.right.left.size : 0;
            int rightRightS = cur.right != null && cur.right.right != null ? cur.right.right.size : 0;
            if (leftLeftS > rightS){ // LL
                cur = rightRotate(cur);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (leftRightS > rightS) { // LR
                cur.left = leftRotate(cur.left);
                cur = rightRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (rightRightS > leftS) { // RR
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur = keepBalanced(cur);
            } else if (rightLeftS > leftS){ // RL  最后一个条件判断不能直接写else，必须要严格判断
                cur.right = rightRotate(cur.right);
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            }
            return cur;
        }

        private SBTNode leftRotate(SBTNode cur) {
            // curSame表示当前结点的key添加了多少次
            int same = cur.all - (cur.left == null ? 0 : cur.left.all) -
                    (cur.right == null ? 0 : cur.right.all);
            SBTNode right = cur.right;
            cur.right = right.left;
            right.left = cur;
            // 修改size属性，先后顺序不能错
            right.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) + (cur.right == null ? 0 : cur.right.size) + 1;
            // 修改all属性，先后顺序不能错
            right.all = cur.all;
            cur.all = (cur.left == null ? 0 : cur.left.all) + (cur.right == null ? 0 : cur.right.all) + same;

            return right;
        }

        private SBTNode rightRotate(SBTNode cur) {
            // curSame表示当前结点的key添加了多少次
            int same = cur.all - (cur.left == null ? 0 : cur.left.all) -
                    (cur.right == null ? 0 : cur.right.all);
            SBTNode left = cur.left;
            cur.left = left.right;
            left.right = cur;
            // 修改size属性，先后顺序不能错
            left.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) + (cur.right == null ? 0 : cur.right.size) + 1;
            // 修改all属性，先后顺序不能错
            left.all = cur.all;
            cur.all = (cur.left == null ? 0 : cur.left.all) + (cur.right == null ? 0 : cur.right.all) + same;

            return left;
        }
    }
    // ======================================================================================================




    // 方法2 归并排序+窗口+前缀和
    public static int countRangeSum2(int[] arr, int lower, int upper){
        int N = arr.length;
        // 制作前缀和
        long[] preSum = new long[N + 1];
        preSum[1] = arr[0];
        for (int i = 2; i <= N; i++) {
            preSum[i] = preSum[i - 1] + arr[i - 1];
        }
        return f(preSum, 0, N, lower, upper);
    }

    private static int f(long[] preSum, int L, int R, int low, int up) {
        if (L == R)
            return 0;
        int mid = L + ((R - L) >> 1);
        return f(preSum, L, mid, low, up) + f(preSum, mid + 1, R, low, up) +
                merge(preSum, L, mid, R, low, up);
    }

    private static int merge(long[] preSum, int L, int M, int R, int low, int up) {
        int p = L, s = M + 1, e = M + 1;
        int res = 0;
        while (p <= M){
            while (s <= R && preSum[s] - preSum[p] < low)
                s++;
            while (e <= R && preSum[e] - preSum[p] <= up)
                e++;
            res += e - s;
            p++;
        }
        // 合并
        long[] sorted = new long[R - L + 1];
        s = L;
        e = M + 1;
        p = 0;
        while (s <= M && e <= R) {
            sorted[p++] = preSum[s] <= preSum[e] ? preSum[s++] : preSum[e++];
        }
        while (s <= M)
            sorted[p++] = preSum[s++];
        while (e <= R)
            sorted[p++] = preSum[e++];
        System.arraycopy(sorted, 0, preSum, L, R - L + 1);
        return res;
    }


    public static void test(int maxLen, int maxVal, int testTime){
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.randomNoNegativeArr(maxLen, maxVal);
            int lower = arrays.generateRandomNum(maxVal);
            int upper = lower + (int) (Math.random() * maxVal);
            int ans1 = countRangeSum(arr, lower, upper);
            int ans2 = MergeSort.limitedSumV3(arr, lower, upper);
            if (ans1 != ans2) {
                System.out.println("Failed");
                arrays.printArray(arr);
                System.out.println("范围是： [ " + lower + ", " + upper + " ]");
                System.out.println(ans1);
                System.out.println(ans2);
                return;
            }
        }
        System.out.println("AC");
    }

}
