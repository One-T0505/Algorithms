package Basic.UnionFind;

// n 对情侣坐在连续排列的 2n 个座位上，想要牵到对方的手。
// 人和座位由一个整数数组 row 表示，其中 row[i] 是坐在第 i 个座位上的人的 ID。情侣们按顺序编号，第一对是 (0, 1)，
// 第二对是 (2, 3)，以此类推，最后一对是 (2n-2, 2n-1)。
// 返回最少交换座位的次数，以便每对情侣可以并肩坐在一起。 每次交换可选择任意两人，让他们站起来交换座位。

public class _0765_Code {

    // 思路
    // 两对交错的情侣 (x, y)  (x', y')  至少要交换1次;
    // 三对交错的情侣 (a, b)  (b', c)  (c', a')  至少要交换2次   4对交错的情侣至少要交换3次 ...
    // 我们的首要目的是找齐所有的连通分量，像上面的两对就是一个连通分量，而第二行的三对也是一个连通分量
    // 因为每个联通分量里，只需要知道该联通分量里涉及几对情侣，就可以知道该连通分量全部牵手成功至少需要交换几次。
    // 假如一个连通分量里涉及5对情侣，那么至少交换4次。如果一共有n对情侣，其中有2对默认位置坐的就是对的，剩余n-2
    // 对情侣构成了x个联通分量，那么总体至少需要： (n - 2) - x
    // 找连通分量可以使用并查集


    // 主方法
    public static int minSwapsCouples(int[] row) {
        int len = row.length;
        int N = len >> 1; // 情侣对数
        UnionFind uf = new UnionFind(N);
        // 根据题意可知，如果编号/2 的数值相等，就可以断定两个人属于一对情侣。比如：(2, 3) 就属于1号情侣
        // 但是最终不需要2、3两个人做到2、3两个位置上，这两个人只要坐的是同一张椅子即可，并且2在前还是3在前都可以
        // 只需要这两个人坐在一个椅子内即可。就比如：3坐8，2坐9也是可以的。
        // 每个椅子都是以偶数位开始。
        for (int i = 0; i < len; i += 2) {
            // row[i] >> 1   row[i + 1] >> 1   将这两个人所属的情侣编号传入，看他们是否为1对，
            // 如果为1对，那么他们坐的位置就不要移动，如果不是1对，就需要加入相应的联通分量
            uf.union(row[i] >> 1, row[i + 1] >> 1);
        }
        // 为什么是总情侣对数  -  并查集里的连通分量数目 ?
        // 假设一共有N对情侣, 如果我们把一对坐对位置的情侣也算做一个联通分量，只包含自己这一对的情侣编号，
        // 为什么可以这么看？因为上面说过了，一个连通分量要想全部牵手成功，至少需要交换连通分量里的情侣对数 - 1次。
        // 只有1对的情侣，只包含自己，所以需要 1 - 1次，刚好0次，符合这个规律，所以我们可以把一对坐在一起的情侣看做
        // 一个联通分量。如果最后分析完了，这里有 N1, N2, ... Nm  个连通分量，N1表示第1个连通分量里有几对情侣。
        // 那么总共需要：
        // (N1 - 1) + (N2 - 1) + ... + (Nm - 1) == (N1 + N2 + ... + Nm) - m  ==  N - m 次
        // m 刚好是联通分量数目  N 是总情侣对数
        return N - uf.count;
    }


    public static class UnionFind {
        public int N; // 总的情侣对数
        public int[] roots;
        public int[] help;
        public int count; // 记录联通分量数目

        public UnionFind(int n) {
            N = n;
            roots = new int[N];
            for (int i = 0; i < N; i++) {
                roots[i] = i;
            }
            help = new int[N];
            count = N;
        }


        public void union(int x, int y) { // 这里的x、y都是情侣编号
            int r1 = findRoot(x);
            int r2 = findRoot(y);

            // 说明这两个情侣编号属于不同的集合，即不同的连通分量
            // 但是既然能传入这两个参数，就说明这两个编号坐在同一个椅子内
            if (r1 != r2){
                roots[r1] = r2;  // 合并在一个集合内
                // count初始为N，总情侣对数
                count--; // 每吸纳一个新的情侣编号进入集合，连通分量数目就必然减少一个
            }
            // 如果 r1 == r2  要不就是两个人默认就是一对情侣，且刚好坐在了同一个椅子内；
            // 要不就是，这两个情侣编号不一样，但是已经在同一个集合内了
            // 都不用做处理
        }

        private int findRoot(int x) {
            int i = 0;
            while (x != roots[x]){
                help[i++] = x;
                x = roots[x];
            }
            while (i > 0){
                roots[help[--i]] = x;
            }
            return x;
        }
    }
}
