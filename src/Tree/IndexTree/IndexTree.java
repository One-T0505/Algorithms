package Tree.IndexTree;

import utils.arrays;

// 回想一下之前学过的累加和数组，可以很方便地查询某个范围的累加和。但是这种简单的累加和数组无法很好地应对数组中某个元素
// 变化的情况，一旦某个元素改变了，那么累加和数组中很多元素都需要重新计算，如果这种改变经常出现，那么代价就会很大。
// 所以引入了IndexTree，它可以很好地应对这种变化。SegmentTree也可以达到同样的效果，但是改动起来很麻烦，最重要的是，
// 当我们扩展到二维、三维时，SegmentTree就会非常麻烦，但是IndexTree改写起来就非常容易，所以这就是IndexTree的必要性。
// 下面来介绍一维情况下的IndexTree。

// IndexTree的累加和数组中每个元素不是从头到自己的累加和，每个元素的管辖范围有所变化。这里我们默认原数组从1下标开始。
//      1  2  3  4  5  6  7  8  9  10  11  12  13  14  15  16
//
// arr  5  1  7  3  2  8  6  4  -2  9   1  -3   6  8   -5   1
//
// 对于IndexTree中累加和数组，每个元素的管辖范围如下：
//  1   2    3    4    5    6    7     8     9     10     11      12    13     14     15     16
//  1  010  011  100  101  110  111  01000 01001  01010  01011  01100  01101  01110  01111  10000
//  1  1~2   3   1~4   5   5~6   7    1~8    9    9~10    11     9~12   13    13~14   15    1~16
//
// 发现了什么规律？ 将位置写成二进制形式后再看。可以发现任何一个位置i的二进制形式下，取出最右边的第一个1，假设为x，
// 那么i管辖的范围就是：i-x+1 ～ i. 比如：12-->01100，取出最右边第一个1，00100=4，所以12号管辖的是：12-4+1 ~ 12
//
// 相比于传统的累加和数组，IndexTree改进了每个元素的管辖范围，当某个元素被修改时，这样就只有很少的元素需要改动。
// 假如arr[3]变化了，那么sum数组需要改变哪些元素？ 需要改变3、4、8、16，如果是传统累加和，则需要改变3～16
// 可以发现任何一个位置的元素被修改后，需要改变的元素位置就是自己和所有的2的次幂位置的元素。如何用代码表示？
// i位置值被修改了，提取i最右侧的1，加上i就是被影响的位置，循环，直到越界。比如：3-->011, 提取出最右侧1就是001，相加
// i就变成了100，4号位就是被影响的，再提取出最右侧的1就是100，相加就是1000，所以8号位也要修改，然后就是16号。
//
// 随便给一个i，想求1～i的累加和怎么做？ 把i的二进制写出来，从右到左，逐一把1抹除，然后拿出对应未知的值累加即可。
// 比如i=11，想求1～11的累加和，
// 11-->1011，先拿出自己sum[11]，然后抹除最右的1就成了1010，所以拿出sum[10]，再抹除最右的1就成1000，所以再拿出sum[8]，
// 再抹除最右的1就成0，所以停止，所以1～11的累加和就是：sum[11] + sum[10] + sum[8].
// 有了这个规律，就可以求任意范围的累加和了 比如5～14 = 1～14 - 1～4

public class IndexTree {
    public int[] copy;
    public int[] tree;
    public int N;

    public IndexTree(int[] ori) {
        N = ori.length + 1;
        copy = new int[N];
        tree = new int[N];
        System.arraycopy(ori, 0, copy, 1, ori.length);
        // 构造出累加和数组
        for (int i = 1; i < N; i++) {
            int mostRight = i & (~i + 1);
            for (int j = i - mostRight + 1; j <= i; j++) {
                tree[i] += copy[j];
            }
        }
    }

    // 返回L～R范围的累加和
    public int sum(int L, int R){
        if (L > R || L < 1 || R >= N)
            return Integer.MIN_VALUE;
        return preSum(R) - (L == 1 ? 0 : preSum(L - 1));
    }

    // 返回1～index的累加和
    private int preSum(int index){
        if (index < 1 || index >= N)
            return Integer.MIN_VALUE;
        int res = 0;
        while (index != 0){
            res += tree[index];
            // 减去最右侧的1。 也可以简化为：index -= index & (-index)   -index == ~index + 1
            index -= index & (~index + 1);
        }
        return res;
    }

    // 让原数组copy[index]上的值加C，相应地要改变树上所有可能受到影响的位置
    public void add(int index, int C){
        while (index < N){
            tree[index] += C;
            index += index & -index;
        }
    }

    static class PlanB{
        public int[] copy;
        public int N;

        public PlanB(int[] ori) {
            N = ori.length + 1;
            copy = new int[N];
            System.arraycopy(ori, 0, copy, 1, N - 1);
        }

        public int sum(int L, int R){
            if (L > R || L < 1 || R >= N)
                return Integer.MIN_VALUE;
            int res = 0;
            for (int i = L; i <= R; i++)
                res += copy[i];
            return res;
        }

        public void add(int index, int C){
            copy[index] += C;
        }
    }


    public static void main(String[] args) {
        int addTime = 50;
        int addLimit = 100;
        int queryTime = 30;
        int maxSize = 20;
        int maxVal = 100;
        int testTime = 2000000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[] ori = arrays.randomNoNegativeArr(maxSize, maxVal);
            IndexTree tree = new IndexTree(ori);
            PlanB planB = new PlanB(ori);
            int N = ori.length;
            for (int j = 0; j < addTime; j++) {
                int index = (int) (Math.random() * N) + 1;
                int C = (int) (Math.random() * addLimit) + 1;
                tree.add(index, C);
                planB.add(index, C);
            }
            for (int j = 0; j < queryTime; j++) {
                int num1 =(int) (Math.random() * N) + 1;
                int num2 =(int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                if (tree.sum(L, R) != planB.sum(L, R)){
                    System.out.println("Failed");
                    return;
                }
            }
        }
        System.out.println("AC");
    }
}
