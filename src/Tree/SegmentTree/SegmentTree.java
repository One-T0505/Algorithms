package Tree.SegmentTree;
import utils.arrays;

// 线段树 SegmentTree
// 假如有一个很长的int数组，我们需要频繁地对区间进行操作，比如：查询3~584号位置的累加和；将33～279位置的值全部更新为12；
// 将1043～772位置的值全部加上7......  当然这种用遍历很容易做到，但是时间复杂度是O(N);
// 对区间频繁地做某种操作时就可以使用线段树，他可以将所有这样的操作的时间复杂度降为：O(logN)

// 实现：先用一个比较短的数组来模拟一下线段树的机制。 假设 arr=[8, 2, 5, 4, 7, 3, 6, 9] 长度为8，因为会用到数组模拟堆时
//      推测自己父亲或者子结点的位置，所以我们需要将原数组复刻一份，但是是从索引1开始。这样就得到：
//      copy = [0, 8, 2, 5, 4, 7, 3, 6, 9]  现在就让copy当作原数组。我们还需要一个数组sum，这个数组就是用来记录某个区
//      间的累加和。我们想在sum中模拟的效果如下图所示：
//
//                [1,8]                                     sum中每个元素都是某个区间的累加和；原数组copy中有效元素
//              /       \                                   是1～8位，一般默认sum[1]就是整棵树的根元素，那sum[1]
//           [1,4]       [5,8]                              就是整个数组的累加和。如果整个数组有奇数个，那么无所谓
//         /    \         /   \                             左边多1还是右边多1。i号位置的左孩子在 2*i，右孩子在
//      [1,2]  [3,4]    [5,6]  [7,8]                        2*i+1， sum[i] = sum[2*i] + sum[2*i+1]，概念和
//     /  \     / \     /  \    /  \                        数组模拟完全二叉树类似。
//    [1] [2] [3] [4]  [5] [6] [7] [8]
//
// 那么要开辟多大的sum数组呢？ 当原数组个数为2的N次方时sum的长度是最省的，如上图所示，刚好需要 2*N 个，0号位置也要算进去。
// 但是我们开辟的sum要保证任何情况下都够用，所以得分析原数组最差的情况，那就是 2的N次方+1 个时sum是最浪费的。假设N=5时，
// sum如下图所示：
//            [1,5]
//          /       \                      本来原数组只有5个，但是为了构成一个完全二叉树，sum数组0号位没用也得加上，
//       [1,2]      [3,5]  3               刚好是 15 + 1 =16  ，也就是说原数组有N个的时候，sum大小为4*N时，一定够用。
//     /    \       /   \
//    [1]   [2] 3  [3]  [4,5]
//                      /   \
//                    [4]   [5]
//
// 累加和数组sum定义完后，还需要一个和sum等长的lazy数组，这个数组用于记录懒信息，刚好一个位置对应一个区间。下面介绍懒信息的
// 机制。我们用arr来模拟。假如现在来了一个操作是：将[2~5]范围上的数全部加3。首先来到根结点[1,5]，发现[2,5]，有部分在根结点
// 的左孩子[1,2]，又有一部分是在[3,5]上的，所以根结点就需要将该任务传递给左右孩子；[1,2]发现其左孩子压根不在范围内，所以不会给
// [1]发送任务；右孩子[2]是完全在目标范围内的，当某个区间全部都在目标范围内时，就打住，在该区间记录懒信息，因为[2]是sum[5],
// 所以就让 lazy[5]=3，表示5号位置对应区间内的数全部加3；再看[3,5]，刚好全部包含在[2,5],所以就打住，由[3,5]收集懒信息，
// 于是 lazy[3]=3. 这样这个操作就完成了。
//
// 当下一个操作——将[3,5]上的数都加2，当我们从根开始判断到其右孩子时发现范围刚好被目标范围涵盖，于是在这里停住开始收集
// 懒信息，但是发现此时 lazy[3]!=0，说明此前有过操作，那么此时[3,5]还不是叶子结点，所以就将之前记录的懒信息仅下发一层，即
// 将之前的懒信息下发给自己的两个孩子，于是lazy[6]=3, lazy[7]=3，并且此时就可以把[3,5]之前的懒信息抹除了，并填上新的
// 懒信息。

// 现在再说一下update操作，就是将某一个范围内的值都设为某个数。假如将[3,5]区间全部设置为2，但是发现此时[3,5]对应的lazy信息
// lazy[3] != 0，说明此前有过一些累加操作，那么此时就将lazy[3] = 0，因为不管前面发生了多少次累加，只要有一次update操作，
// 前面所有的操作全废。为了记录update的懒信息，同样需要一个lazy1数组，但是这里和add操作有所不同，在add操作里0不会产生歧义，
// 但在update操作中，因为数组默认初始值都是0，如果lazy1[3]==0, 表示把对应区间所有值都设为0，还是说就是初始值，没有任何操作？
// 所以为了保证lazy1信息的有效性，还需要一个布尔型数组，来标记对应的lazy1中某个元素的值是不是真的是有效信息。

// 如果同时发现 lazy1[i] != 0 && lazy[i] != 0，说明i对应的区间上一定是先有update操作，才有后来的add操作，因为如果是update
// 在后，那么前面的累加信息会被清空。

public class SegmentTree {
    // 最开始的数组是从0下标开始，处理后的数组是从1开始，所以长度会有变化，之后用到的辅助数组都是以这个新长度N为基础的
    public int N;
    public int[] copy;   // 原数组处理后的数组
    public int[] sum;    // 累加和数组
    public int[] lazyA;    // 记录累加操作懒信息的数组
    public int[] lazyU;    // 记录更新操作懒信息的数组
    public boolean[] effect;    // 记录更新操作是否有效的数组

    public SegmentTree(int[] arr){
        N = arr.length + 1;
        copy = new int[N];
        // 将原数组拷贝，并从1下标开始，0位不用
        System.arraycopy(arr, 0, copy, 1, N - 1);
        sum = new int[N << 2];   // 4N 保证最坏情况也能装得下
        lazyA = new int[N << 2];   // 4N 保证最坏情况也能装得下
        lazyU = new int[N << 2];   // 4N 保证最坏情况也能装得下
        effect = new boolean[N << 2];   // 4N 保证最坏情况也能装得下
    }



    // 利用copy数组，构造出sum数组中的累加和信息.
    // 在copy[l~r]范围上建立sum数组，pos表示该范围对应的结点应该在sum中哪个下标
    // 主函数调用该方法时应该这么调用：build(1, N, 1)   1~N就是对整个copy数组建立，对应的根的位置当然是填在sum[1]啦
    public void build(int l, int r, int pos){
        if (l == r){  // 说明是叶子结点，那就直接将元素值填上
            sum[pos] = copy[l];
            return;
        }
        int mid = l + ((r - l) >> 1);
        build(l, mid, pos << 1);    // 去建立自己的左子树，左子树的根的位置是 2*pos，就是pos左孩子的位置
        build(mid + 1, r, pos << 1 | 1);  // 去建立自己的右子树 pos << 1 | 1 == 2*pos+1
        sum[pos] = sum[pos << 1] + sum[pos << 1 | 1];
    }

    // 累加操作
    // L、R表示任务的范围，C表示要加多少
    // l、r表示当前来到了树上哪个区间范围，pos表示该区间对应于sum数组中的位置，那么对应的lazyA中也是这个位置
    public void add(int L, int R, int C, int l, int r, int pos){
        // 当前任务把这个区间全包了
        if (L <= l && r <= R){
            sum[pos] += C * (r - l + 1);   // 该区间的累加和就要加上相应的值
            lazyA[pos] += C;      // 并把信息收集起来，不用再往下传；这里是累加，因为还有可能有之前的信息
            return;
        }
        // 当前任务没有全包这个区间，说明这个区间有些部分是不需要做该操作的
        int mid = l + ((r - l) >> 1);
        distribute(pos, mid - l + 1, r - mid);
        if (L <= mid)
            add(L, R, C, l, mid, pos << 1);
        if (R >= mid + 1)
            add(L, R, C, mid + 1, r, pos << 1 | 1);
        sum[pos] = sum[pos << 1] + sum[pos << 1 | 1];
    }

    // 更新操作
    // L、R表示任务的范围，C表示设置为多少
    // l、r表示当前来到了树上哪个区间范围，pos表示该区间对应于sum数组中的位置，那么对应的lazyU、effect中也是这个位置
    public void update(int L, int R, int C, int l, int r, int pos){
        // 任务将当前区间全包了
        if (L <= l && r <= R){
            effect[pos] = true;  // 设置该位置为有效位
            lazyU[pos] = C;
            sum[pos] = (r - l + 1) * C;
            lazyA[pos] = 0;  // 将累加信息清空
            return;
        }
        // 任务没办法全包当前区间，说明这个区间有些部分是不需要做该操作的
        int mid = l + ((r - l) >> 1);
        distribute(pos, mid - l + 1, r - mid);
        if (L <= mid)
            update(L, R, C, l, mid, pos << 1);
        if (R >= mid + 1)
            update(L, R, C, mid + 1, r, pos << 1 | 1);
        sum[pos] = sum[pos << 1] + sum[pos << 1 | 1];
    }


    // 查询操作
    public long query(int L, int R, int l, int r, int pos){
        if (L <= l && R >= r)
            return sum[pos];
        int mid = l + ((r - l) >> 1);
        distribute(pos, mid - l + 1, r - mid);
        long res = 0;
        if (L <= mid)
            res += query(L, R, l, mid, pos << 1);
        if (R >= mid + 1)
            res += query(L, R, mid + 1, r, pos << 1 | 1);
        return res;
    }


    // 之前的，所有懒增加，和懒更新，从父范围，发给左右两个子范围
    // 分发策略是什么
    // ln表示左子树元素结点个数，rn表示右子树结点个数
    // 一定要先做更新操作的分发
    private void distribute(int pos, int ln, int rn) {
        // 更新操作的分发
        if (effect[pos]){
            effect[pos << 1] = true;
            effect[pos << 1 | 1] = true;
            lazyU[pos << 1] = lazyU[pos];
            lazyU[pos << 1 | 1] = lazyU[pos];
            lazyA[pos << 1] = 0;
            lazyA[pos << 1 | 1] = 0;
            sum[pos << 1] = lazyU[pos] * ln;
            sum[pos << 1 | 1] = lazyU[pos] * rn;
            effect[pos] = false;  // 分发下去之后，就清空自己的有效位信息
        }
        // 累加操作的检查
        if (lazyA[pos] != 0){
            lazyA[pos << 1] += lazyA[pos];
            sum[pos << 1] += lazyA[pos] * ln;
            lazyA[pos << 1 | 1] += lazyA[pos];
            sum[pos << 1 | 1] += lazyA[pos] * rn;
            lazyA[pos] = 0;
        }
    }
    // ===========================================================================================================


    // 用最暴力的结构实现上述操作
    static class PlanB{
        public int[] copy;

        public PlanB(int[] origin){
            copy = new int[origin.length + 1];
            System.arraycopy(origin, 0, copy, 1, origin.length);
        }

        public void add(int L, int R, int C){
            for (int i = L; i <= R; i++) {
                copy[i] += C;
            }
        }

        public void update(int L, int R, int C){
            for (int i = L; i <= R; i++) {
                copy[i] = C;
            }
        }

        public long query(int L, int R){
            long res = 0;
            for (int i = L; i <= R; i++) {
                res += copy[i];
            }
            return res;
        }
    }

    // 功能测试
    // testTimes表示测试次数   addOrUpdate表示一次测试中执行累加和更新一共的次数  query表示查询的次数
    public static boolean functionTest(int testTimes, int maxSize, int maxVal, int addOrUpdate, int query){
        for (int i = 0; i < testTimes; i++) {
            int[] ori = arrays.randomNoNegativeArr(maxSize, maxVal);
            PlanB planB = new PlanB(ori);
            SegmentTree segmentTree = new SegmentTree(ori);
            int N = ori.length;
            segmentTree.build(1, N, 1);
            for (int j = 0; j < addOrUpdate; j++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                int C = (int) ((maxVal + 1) * Math.random());
                if (Math.random() < 0.5){
                    segmentTree.add(L, R, C, 1, N, 1);
                    planB.add(L, R, C);
                }else {
                    segmentTree.update(L, R, C, 1, N, 1);
                    planB.update(L, R, C);
                }
            }

            for (int j = 0; j < query; j++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                long res1 = segmentTree.query(L, R, 1, N, 1);
                long res2 = planB.query(L, R);
                if (res1 != res2)
                    return false;
            }
        }
        return true;
    }


    // 性能测试
    public static void performanceTest(int maxSize, int maxVal, int addOrUpdate, int query){
        int[] arr = arrays.fixedLenArray(maxSize, maxVal);
        int N = arr.length;
        SegmentTree tree = new SegmentTree(arr);
        tree.build(1, N, 1);
        PlanB planB = new PlanB(arr);
        long start = System.currentTimeMillis();
        for (int i = 0; i < addOrUpdate; i++) {
            int num1 = (int) (Math.random() * N) + 1;
            int num2 = (int) (Math.random() * N) + 1;
            int L = Math.min(num1, num2);
            int R = Math.max(num1, num2);
            int C = (int) ((maxVal + 1) * Math.random());
            if (Math.random() < 0.5)
                planB.add(L, R, C);
            else
                planB.update(L, R, C);
        }
        long end = System.currentTimeMillis();
        System.out.println("暴力方法的累加和更新时间：" + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < addOrUpdate; i++) {
            int num1 = (int) (Math.random() * N) + 1;
            int num2 = (int) (Math.random() * N) + 1;
            int L = Math.min(num1, num2);
            int R = Math.max(num1, num2);
            int C = (int) ((maxVal + 1) * Math.random());
            if (Math.random() < 0.5)
                tree.add(L, R, C, 1, N, 1);
            else
                tree.update(L, R, C, 1, N, 1);
        }
        end = System.currentTimeMillis();
        System.out.println("线段树的累加和更新时间：" + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < query; i++) {
            int num1 = (int) (Math.random() * N) + 1;
            int num2 = (int) (Math.random() * N) + 1;
            int L = Math.min(num1, num2);
            int R = Math.max(num1, num2);
            planB.query(L, R);
        }
        end = System.currentTimeMillis();
        System.out.println("暴力方法的查询时间：" + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < query; i++) {
            int num1 = (int) (Math.random() * N) + 1;
            int num2 = (int) (Math.random() * N) + 1;
            int L = Math.min(num1, num2);
            int R = Math.max(num1, num2);
            tree.query(L, R, 1, N, 1);
        }
        end = System.currentTimeMillis();
        System.out.println("线段树的查询时间：" + (end - start) + " ms");


    }

    public static void main(String[] args) {
        boolean res = functionTest(10000, 20, 100, 20, 10);
        System.out.println(res);
        performanceTest(100000, 10000, 50000, 50000);
    }
}


// 总结：线段树一般应用在什么地方？ 线段树一般应用于范围上密集操作的场合，并且如果有了左子树信息和右子树信息，不能通过常数时间
//      加工出自己的信息就不能使用线段树。拿到左右子树的信息后只需要做简单的处理就能完成的才行。