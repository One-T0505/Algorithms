package Basic.Tree;

// 给定数组father，大小为N，表示一共有N个节点
// father[i] = j 表示点i的父亲是点j，father表示的树一定是一棵树而不是森林
// 给定数组values，大小为N，values[i]=v 表示节点i的权值是v
// 实现如下4个方法，保证4个方法都很快:
//  1)让某个子树所有节点值加上v，入参: int head, int v
//  2)查询某个子树所有节点值的累加和，入参:int head
//  3)在树上从a到b的整条链上所有加上v,入参: int a, int b, int v
//  4)查询在树上从a到b的整条链上所有节点值的累加和，入参: int a, int b

public class TreeChain {

// 这是树链领域最经典的一道题。首先要引入一个概念：重儿子
// 该题目给定的树结构不一定是二叉树，可以是多叉树。一个结点的所有儿子中，哪个儿子作为根所在的子树有最多的结点，那么
// 该儿子就是当前结点的重儿子，其余都是轻儿子。如果有多个儿子都有相等且最多的子树结点，那么任意选择一个作为重儿子。
// 如下图所示，用直线连的结点就是重儿子，用曲线所连的结点就是轻儿子
//
//                     a
//                   /   ⎞              a的重儿子可以是b或c，因为以b为根的子树和以c为根的子树都是7个结点，
//                  b     c             所以随机选择了b作为a的重儿子，那么c就是轻儿子。
//                 /  ⎞     \           由重儿子组成的链就是重链，比如：a->b->d->e   g->h->j
//                d   g      j          c->j->l->n->o  f单独也是一条重链，只不过只有f这一个结点；k、m同理
//               /  ⎞  \    ⎛  \        所以共有6条重链。
//              e   f   h   k   l
//                     /       ⎛  \
//                    i        m   n
//                                /
//                               o
//
// 假设我们的结构中会将所有重链的头记录下来，那么该书上任意一个结点想要跳到根结点就会变得很快。比如m：
// m首先需要找到自己所在重链的头，就是自己，然后通过轻链找父来到l，再通过重链找头来到c，再通过轻链找父来到a。
// 任意一个结点来到根的时间复杂度为O(logN)
//
// 如果根据给定的father数组构建出来的树如下左图所示，其数字表示在数组的索引，我们根据深度优先遍历方式去标记轻重链
// 得到的顺序如右图所示，该数字表示访问顺序
//
//           4                    1               为什么要用DFS序给结点重新编号呢？这就是为了解决前两问
//          / \                 ⎛  \              前两问都是给一个结点，然后对其所在的整棵子树进行操作，
//         2   0                6   2             经过DFS序重新编号后，每一颗子树的序号都是连续的。
//        /   / \              /   / ⎞            以2为根的子树，能构成2～5；以6为根的子树能构成6～7，
//       3   5   6            7   3  5            所以问题就变成了在某个连续区间上操作，这就是线段树。
//          /                    /                我们只需要记录每个结点所在的子树有多少个结点就行了；比如
//         1                    4                 2所在子树有4个结点，而自己的编号为2，就能推出这颗子树的
//                                                编号是从2～5
//
// 这个DFS序还有另一个特点，就是每条重链的编号也是连续的 6->7  5  1->2->3->4  这个特点就是针对第3、4问的
// 下面以一个具体例子来说明重链的作用，左图是实际的数，右边是以DFS序对其编号
//
//               a                             1
//            /     \                        /    ⎞             比如想对e～k这条路径上的所有结点+5
//           b       h                      2      8            对着DFS序来做。e对应5，k对应12。要从
//         /  \     /  \                   /  ⎞   /  ⎞          两侧逐步向根汇聚。
//       c     f   i    k                 3   6  9   12
//     /  \   /     \    \               / ⎞  /   \    \
//    d    e g       j    m             4  5 7     10  13
//                  /                              /
//                 l                              11
//
// 5需要找到自己所在重链的位置，一直从自己向上到重链的头+5，发现5所在重链到头只有自己，所以5结点+5；然后轻链找父
// 来到3。3从自己一直到所在重链的头+5，也就是3～1，这3个结点+5，4是不用管的，所在重链的后面的部分是不用管的；这样
// 就到了根；再看右侧，右侧也是同样的道理，12所在重链从自己一直到头+5，就是12这个结点+5，然后轻链找父来到8，
// 8就是所在重链的头，于是只有8结点+5，然后来到1，所以两侧都汇聚到了根。上面的所有+5操作都可以用线段树完成。
// 如果两个结点在同一侧就更好解决了。


    private int time; // 时间戳，用来给DFS序编号用的
    private int N;    // 结点数量.原始father数组是从0～N-1；我们将其改成从1～N
    private int root;    // 根据给定的father数组确定出整棵树的根

    // 朴素树结构
    // 比如给定的father数组是这样的：[2 1 1 4 1],画出来就是这样的：
    //     1                                              2
    //    / \                                            / \
    //   2   4       我们需要将其编号改成从1开始的：         3   5
    //  /     \                                        /     \
    // 0       3                                      1       4
    //
    // tree[0]这个一维数组表示的是，现在编号为0的结点有哪些后代，新编号中无0这个编号，所以无后代。
    // tree[2] == [3, 5] ... tree[5] == [4]
    private int[][] tree;

    // 权重数组，如果之前values[0]==6,那么在val中就是：val[1]==6，平移了
    private int[] val;
    // 调整过坐标的father数组, 如果之前是 [2 1 1 4 1]   那现在就是：[0 3 2 2 5 2]  0不用了
    private int[] parent;
    // 深度数组，任何一个结点处于整棵树的第几层
    private int[] depth;
    // 重儿子数组  son[i]==0 说明i结点无后代   son[i]==j  说明i的重儿子是j
    private int[] son;
    // 子树结点数量数组  size[i]表示以i为根的整棵子树有多少个结点
    private int[] size;
    // 记录所有重链头结点的数组  head[i]表示i结点所在重链的头
    private int[] head;
    // DFS序编号数组  dfs[i]==j 表示i结点在DFS序编号为j
    private int[] dfs;
    // val数组是按正常顺序排列的，但是经过DFS编号后，位置就对应不起来了，dfsVal数组就是按照DFS编号重新排列了
    // 权重数组，这样才能使用线段树
    private int[] dfsVal;
    // 线段树
    private SegmentTree seg;


    // TreeChain初始化
    public TreeChain(int[] father, int[] values) {
        init(father, values);
        dfs1(root, 0);
        dfs2(root, root);
        seg = new SegmentTree(dfsVal);
        seg.build(1, N - 1, 1);
    }


    // 完成第一问   以用户视角来调用该方法传入的head编号还是从0开始的编号
    public void addSubtree(int head, int v) {
        // 所以先要让head平移成新编号
        head++;
        // dfs[head]就是DFS序下的编号，往后数size[head] - 1个连续结点，因为size还包含本身自己这个结点
        seg.add(dfs[head], dfs[head] + size[head] - 1, v, 1, N - 1, 1);
    }


    // 完成第二问
    public int querySubtree(int head) {
        head++;
        return seg.query(dfs[head], dfs[head] + size[head] - 1, 1, N - 1, 1);
    }


    // 完成第四问
    public int queryChain(int a, int b) {
        // 先都转换成平移坐标
        a++;
        b++;
        int res = 0;
        // 汇聚法 上面讲解的时候只着重分析了a、b两个结点在不同侧的时候，没有讲同侧的情况。
        // 即便a、b不在同一条重链上，也不一定会经过根结点，我们要找的是汇聚点。此时，有一个问题需要注意，就是谁先
        // 往上跳。假设有一种情况如下图所示：g不是根结点，只是沿途的一个结点
        //           g
        //          /  ⎞           目标点是x和y。y的深度大于x的深度，如果先让x先跳，那么就是x～g这些点加v，然后
        //         d   c           x直接就到了g的父结点；再让y跳，y来到了g，于是就错过了汇聚点g。
        //        /   /            这个地方可能没讲清楚，但是最终想说的跳的规则就是：
        //       x   w             每次判断x、y所在重链头部的深度，谁大(更底层)就谁跳，也有可能一个点可能
        //          /              连续跳几次，才会换到另一个点来跳
        //         y
        //
        // a b在同一条重链上的情况会直接跳过while循环
        while (head[a] != head[b]) {
            if (depth[head[a]] > depth[head[b]]) {
                res += seg.query(dfs[head[a]], dfs[a], 1, N - 1, 1);
                a = parent[head[a]];  // 跳到当前所在重链头部的父结点上
            } else {
                res += seg.query(dfs[head[b]], dfs[b], 1, N - 1, 1);
                b = parent[head[b]];  // 跳到当前所在重链头部的父结点上
            }
        }
        // 直接跳到这里，说明a、b同侧，并在同一个重链上，那么无非就是a在上或者b在上
        // 重链有一个很重要的特点就是重链上的结点DFS序编号是连续的
        if (depth[a] > depth[b]) // 说明a在下，b在上，那么b的DFS序编号更小
            res += seg.query(dfs[b], dfs[a], 1, N - 1, 1);
        else
            res += seg.query(dfs[a], dfs[b], 1, N - 1, 1);

        return res;
    }


    // 完成第三问
    public void addChain(int a, int b, int v) {
        a++;
        b++;
        while (head[a] != head[b]) {
            if (depth[head[a]] > depth[head[b]]) {
                seg.add(dfs[head[a]], dfs[a], v, 1, N - 1, 1);
                a = parent[head[a]];
            } else {
                seg.add(dfs[head[b]], dfs[b], v, 1, N - 1, 1);
                b = parent[head[b]];
            }
        }
        if (depth[a] > depth[b])
            seg.add(dfs[b], dfs[a], v, 1, N - 1, 1);
        else
            seg.add(dfs[a], dfs[b], v, 1, N - 1, 1);
    }


    // 该方法做了4件事情：
    //  1.原始的树 tree弄好了，可以从i这个点，找到下级的直接孩子
    //  2.上面的一大堆结构，准备好了空间，
    //  3.values 变换成了 val
    //  4.找到整棵树的根
    private void init(int[] father, int[] values) {
        time = 0;
        N = father.length + 1;
        tree = new int[N][]; // 每个一维数组不确定长度
        val = new int[N];
        parent = new int[N];
        depth = new int[N];
        son = new int[N];
        head = new int[N];
        size = new int[N];
        dfs = new int[N];
        dfsVal = new int[N];
        // 制作val
        System.arraycopy(values, 0, val, 1, N - 1);
        // 制作朴素树结构tree，那就要先知道每个结点有几个后代，这样才能生成一维数组
        // help就是用来统计每个结点有几个后代的
        int[] help = new int[N - 1];
        for (int i = 0; i < N - 1; i++) {
            if (father[i] == i)
                root = i + 1;   // 坐标要平移
            else
                help[father[i]]++;
        }
        tree[0] = new int[0]; // 0索引弃用了
        for (int i = 0; i < N - 1; i++)
            tree[i + 1] = new int[help[i]];
        // 到现在为止，tree的结构已经有了，现在要填数据了
        for (int i = 0; i < N - 1; i++) {
            if (i + 1 != root)
                tree[father[i] + 1][--help[father[i]]] = i + 1;
        }
    }


    // cur 当前结点    father  cur的父结点
    // 上游调用的是dfs1(root, 0)  所以在新的坐标下，让根的父结点指向了0
    // 该方法的作用就是根据father，设置好cur结点的相关属性  该方法修改了以下变量：
    // parent  depth  size  son
    private void dfs1(int cur, int father) {
        parent[cur] = father;
        depth[cur] = depth[father] + 1;
        size[cur] = 1;
        int maxSize = -1;
        // 遍历当前结点的所有直接后代  并且还会顺便找出cur结点的重儿子
        for (int s : tree[cur]) {
            dfs1(s, cur);
            size[cur] += size[s];
            if (size[s] > maxSize) {
                maxSize = size[s];
                son[cur] = s;
            }
        }
    }


    // dfs1完成了每个结点重儿子的寻找，所以dfs2才能利用上面的信息对其进行DFS序编号
    // dfs2会对以下的变量设置：
    // head  dfs  dfsVal
    // 至此，类的成员变量均已完成设置

    // cur  表示当前结点    top  表示cur所在重链的头结点
    private void dfs2(int cur, int top) {
        dfs[cur] = ++time;  // DFS编号
        head[cur] = top;    // 设置自己所在链的头部信息
        dfsVal[time] = val[cur];  // 设置DFS序对应的权值
        // 如果cur有儿子  son[cur]指的是cur的重儿子，因为0这个索引不会用到，如果不是0
        // 就说明cur有重儿子，那必然有儿子，该条件也可以换成：size[cur] > 1 也是等效的
        if (son[cur] != 0) {
            dfs2(son[cur], top);  // cur的重儿子所在重链的头部也依然是top
            for (int s : tree[cur]) { // 遍历所有儿子
                if (s != son[cur])  // 设置所有剩下的轻儿子
                    dfs2(s, s);    // 轻儿子所在重链的头部就是自己
            }
        }
    }


    // 线段树结构的封装
    // ========================================================================================
    private static class SegmentTree {
        private int N;
        private int[] arr;
        private int[] sum;
        private int[] lazy;

        public SegmentTree(int[] ori) {
            N = ori.length + 1;
            arr = new int[N];
            sum = new int[N << 2];
            lazy = new int[N << 2];
            System.arraycopy(ori, 0, arr, 1, N - 1);
        }


        public void build(int L, int R, int pos) {
            if (L == R) {
                sum[pos] = arr[L];
                return;
            }
            int mid = L + ((R - L) >> 1);
            build(L, mid, pos << 1);
            build(mid + 1, R, pos << 1 | 1);
            sum[pos] = sum[pos << 1] + sum[pos << 1 | 1];
        }


        public void add(int L, int R, int C, int l, int r, int pos) {
            if (L <= l && R >= r) {
                sum[pos] += C * (r - l + 1);
                lazy[pos] += C;
                return;
            }
            int mid = (l + (r - l) >> 1);
            distribute(pos, mid - L + 1, R - mid);
            if (L < mid)
                add(L, R, C, l, mid, pos << 1);
            if (R >= mid + 1)
                add(L, R, C, mid + 1, R, pos << 1 | 1);
            sum[pos] = sum[pos << 1] + sum[pos << 1 | 1];
        }


        public int query(int L, int R, int l, int r, int pos) {
            if (L <= l && R >= r)
                return sum[pos];
            int res = 0;
            int mid = l + ((r - l) >> 1);
            distribute(pos, mid - l + 1, r - mid);
            if (L <= mid)
                res += query(L, R, l, mid, pos << 1);
            if (R >= mid + 1)
                res += query(L, R, mid + 1, r, pos << 1 | 1);
            return res;
        }

        private void distribute(int pos, int ln, int rn) {
            if (lazy[pos] != 0) {
                lazy[pos << 1] += lazy[pos];
                sum[pos << 1] += lazy[pos] * ln;
                lazy[pos << 1 | 1] += lazy[pos];
                sum[pos << 1 | 1] += lazy[pos] * rn;
                lazy[pos] = 0;
            }
        }

    }

    // ========================================================================================


}
