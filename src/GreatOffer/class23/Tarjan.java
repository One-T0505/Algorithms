package GreatOffer.class23;

import java.util.HashSet;

// 给定数组father大小为N，表示一共有N个节点
// father[i] = j表示点i的父亲是点j，father表示的树一定是一棵树而不是森林
// queries是二维数组，大小为M*2， 每一个长度为2的数组都表示一条查询
// [4,9]表示想查询4和9之间的最低公共祖先.   [3,7],表示想查询3和7之间的最低公共祖先...
// tree和queries里面的所有值都一定在0~N-1之间
// 返回一个数组ans,大小为M，ans[i]表示 第i条查询的答案

public class Tarjan {

    // 暴力方法 时间复杂度O(M*N)
    // 需要用到一个集合；对于每个问题的两个结点，让该结点从自己到根的整条路径上的结点，都加入集合里，
    // 然后另一个结点也是这样的操作，第一个出现在集合里的结点就是最底公共祖先。因为要考虑最差情况，
    // 所以给的father数组构建成树可能是一条链。

    public static int[] lowestCommonAncestor(int[] father, int[][] queries) {
        if (father == null || father.length == 0 || queries == null || queries.length == 0 ||
                queries[0].length != 2)
            return null;
        int N = father.length;
        int M = queries.length;
        int[] res = new int[M];
        HashSet<Integer> set = new HashSet<>();
        int index = 0;
        for (int[] query : queries) {
            if (query[0] == query[1])
                res[index++] = query[0];
            else {
                int jump = query[0];
                while (father[jump] != jump) {
                    set.add(jump);
                    jump = father[jump];
                }
                // 加上最后的根结点
                set.add(jump);
                jump = query[1];
                while (!set.contains(jump))
                    jump = father[jump];
                res[index++] = jump;
                set.clear();
            }
        }
        return res;
    }
    // ==============================================================================================


    // 在线方法：树链剖分 就是之前讲过的那个树的重链结构
    // 为什么说这是在线方法呢？因为构建整个重链需要O(N)的时间复杂度，但是一旦这个结构建立好了之后，每次查询
    // 只需要O(logN)的时间复杂度，因为有了重链，不管树的形状是什么样的，树上任意一个结点到根只需要O(logN)
    // 任何时候来一个查询，都立马能查，唯一耗点时的就是一开始建树的过程。
    // 空间复杂度O(N), 支持在线查询，单次查询时间复杂度O(logN)
    // 如果有M次查询，时间复杂度O(N + M * logN)

    public static int[] lowestCommonAncestor2(int[] father, int[][] queries) {
        TreeChain tc = new TreeChain(father);
        int M = queries.length;
        int[] res = new int[M];
        for (int i = 0; i < M; i++) {
            if (queries[i][0] == queries[i][1])
                res[i] = queries[i][0];
            else
                res[i] = tc.lca(queries[i][0], queries[i][1]);
        }
        return res;
    }


    public static class TreeChain {
        // 用的都是平移之后的坐标
        private int N;
        private int root;
        private int[][] tree;  // 朴素树结构
        private int[] parent;  // 坐标平移之后的父结点数组
        private int[] depth;
        private int[] son;
        private int[] size;
        private int[] head;

        public TreeChain(int[] father) {
            init(father);
            dfs1(root, 0);
            dfs2(root, root);
        }


        private void init(int[] father) {
            N = father.length + 1;
            tree = new int[N][];
            parent = new int[N];
            depth = new int[N];
            son = new int[N];
            size = new int[N];
            head = new int[N--]; // N最终变回了father的真实长度
            // 为了完成朴素树结构，要统计每个结点有几个直接后代
            int[] succ = new int[N];
            for (int i = 0; i < N; i++) {
                if (father[i] == i)
                    root = i + 1;   // 平移后的根
                else
                    succ[father[i]]++;
            }
            tree[0] = new int[0];
            for (int i = 0; i < N; i++)
                tree[i + 1] = new int[succ[i]];
            for (int i = 0; i < N; i++) {
                if (i + 1 != root)
                    tree[father[i] + 1][--succ[father[i]]] = i + 1;
            }
        }


        // cur是当前结点，f是cur的父结点
        // 上面最开始调用该方法时传入的是：dfs1(root, 0)  所以depth[root] = depth[0] + 1
        // 因为所有成员变量数组的0号位置都是废弃的，初始值为0，刚好root的深度就变成了1，所以刚好对应上。

        // 该方法用于设置f的一个孩子结点cur的parent、depth、size属性  son属性需要自己设置
        // 你可以认为，运行一遍dfs1，那么cur和f的parent、depth、size，这三个关系就填好了
        private void dfs1(int cur, int f) {
            parent[cur] = f;
            depth[cur] = depth[f] + 1;
            size[cur] = 1;
            int maxSize = -1;
            for (int s : tree[cur]) {
                dfs1(s, cur);
                size[cur] += size[s];
                if (size[s] > maxSize) {
                    maxSize = size[s];
                    son[cur] = s;
                }
            }
        }


        // cur  表示当前结点    top  表示cur所在重链的头结点
        // dfs1方法把size数组填好了，这样我们就可以任意获取以某个结点为根的树的结点数量，这样就方便设置重儿子
        // 运行一遍该方法，cur和top关于head的属性就填好了
        private void dfs2(int cur, int top) {
            head[cur] = top;
            if (son[cur] != 0) { // 如果cur有儿子
                dfs2(son[cur], top);
                for (int s : tree[cur]) {
                    if (s != son[cur])
                        dfs2(s, s);
                }
            }
        }


        // 求a、b两个结点的最底公共祖先
        // 因为是public方法，所以外界调用时传入的坐标是真实的，并没有平移过的
        public int lca(int a, int b) {
            a++;
            b++;
            while (head[a] != head[b]) {
                if (depth[head[a]] < depth[head[b]])
                    b = parent[head[b]];
                else
                    a = parent[head[a]];
            }
            // -1表示返回真实的坐标
            return (depth[a] < depth[b] ? a : b) - 1;
        }
    }
    // -------------------------------------------------------------------------------------------




    // 离线查询 Tarjan + 并查集
    // 如果有M条查询，时间复杂度O(N + M) 但是必须把M条查询一次给全，不支持在线查询

    // Tarjan算法
    // 如果要用Tarjan算法，那么我们需要把问题给出的queries数组改造成别的辅助结构,左部分是题目给的queries的形式，
    // 右边是我们需要的形式：
    //
    // 0  [s, e]          k {s, f}      c {c}       f {k}
    // 1  [k, s]             1  3          2           3
    // 2  [c, c]
    // 3  [k, f]          e {s}         s {e, k}          这表示：关于k的问题有：(k,e) (k,s) (k,f)
    //                       0             0  1           下面的数字表示是第几个问题.并且一对问题要相互
    //                                                    记录。比如：(s,e)，那么就要在s的表里有e，e的表里有s
    //
    // 假设题目给出的father数组构造出的树如下图所示，我们用这个图来模拟Tarjan算法
    //
    //           a                 一开始并查集中，每个结点都是独立的集合，集合里只包含自己
    //         /   \               利用二叉树的递归遍历，如果一个结点访问过，那就打上tag。
    //        b      c             先来到a，问题列表里没有关于a的问题，来到b，d，都没有关于b、d的问题，
    //      /  \    /  \           来到f，发现有关于f的问题，去到f的表里查看只有k，并且k没被遍历过，所以这个问题
    //     d   y    k   s          先不解决，等到说明时候走到k的时候在解决。当一个问题的双方都被遍历过后，才解决
    //    /    \   / \             这个问题。当从f回到d时，让f和d这两个集合打上同样的tag——d，表示这两个集合的头结点
    //   f      t  z  e            从d来到b时，d的集合和b的集合打上同样的tag——b，所以本来：{b,d}->d, b和d的tag
    //
    // 是d，现在就变成了：{f,d,b}->b. 然后来到y、t，又回到y，当回到y的时候，y和t的集合打上tag——y，然后回到b，
    // y和b的集合打上tag——b。回到a，集合b和a打上a的tag；来到k的时候，查询k的表里有一个f是被访问过的，那么现在到了k，
    // 是时候该解决(f,k)这个问题了，那么答案就是此时f的tag——a。f和k的最底公共祖先确实是a。所以可以在ans填答案了，
    // 填完之后就可以在列表里删除这个问题了
    // 总体流程就是这样的。

    public static int[] Tarjan(int[] father, int[][] queries) {
        int N = father.length;
        int M = queries.length;
        int[] help = new int[N];  // 统计每个结点有几个儿子
        int root = 0;
        // 生成mt
        for (int i = 0; i < N; i++) {
            if (father[i] == i)
                root = i;
            else
                help[father[i]]++;
        }
        int[][] mt = new int[N][];
        for (int i = 0; i < N; i++)
            mt[i] = new int[help[i]];
        for (int i = 0; i < N; i++) {
            if (i != root)
                mt[father[i]][--help[father[i]]] = i;
        }
        // 到这里时，help整个数组全部为0了，又可以直接使用了
        // 生成mq和mi   在完成mq和mi的过程中，queries[i][0] == queries[i][1] 的情况我们直接跳过了
        // 因为这样的查询问题，结果就是queries[i][0]
        for (int[] query : queries) {
            if (query[0] != query[1]) {
                help[query[0]]++;
                help[query[1]]++;
            }
        }
        int[][] mq = new int[N][];
        int[][] mi = new int[N][];
        for (int i = 0; i < N; i++) {
            mq[i] = new int[help[i]];
            mi[i] = new int[help[i]];
        }
        for (int i = 0; i < M; i++) {
            if (queries[i][0] != queries[i][1]) {
                mq[queries[i][0]][--help[queries[i][0]]] = queries[i][1];
                mi[queries[i][0]][help[queries[i][0]]] = i;
                mq[queries[i][1]][--help[queries[i][1]]] = queries[i][0];
                mi[queries[i][1]][help[queries[i][1]]] = i;
            }
        }

        int[] res = new int[M];
        UnionFind uf = new UnionFind(N);
        f(root, mt, mq, mi, uf, res);
        // 最后再处理这一些简单查询
        for (int i = 0; i < M; i++) {
            if (queries[i][0] == queries[i][1])
                res[i] = queries[i][0];
        }
        return res;
    }


    // 这是Tarjan方法最核心的方法，主方法里主要是怎么生成对应的mt、mq、mi
    // 当前来到cur点
    // mt是整棵树cur下方有哪些直接后代  mt[cur] = {a,b,c,d}表示cur的孩子是abcd
    // mq是题目给出的queries转换出的我们需要的问题列表   cur有哪些问题：mq[cur] = {x,y,z}
    // 表示查询中包含：(cur, x)  (cur, y) (cur, z)
    // mi表示得到问题的答案应该填在ans的什么地方  和mq一一对应 如果 mi[cur] = {6, 12, 34} 那说明
    // (cur, x)这个查询是第6个查询。
    private static void f(int cur, int[][] mt, int[][] mq, int[][] mi, UnionFind uf, int[] res) {
        for (int next : mt[cur]) {
            f(next, mt, mq, mi, uf, res);
            uf.union(cur, next);  // 合并集合
            uf.setTag(cur, cur);  // 将这个集合打上tag
        }
        // 此时，cur在树中以下的位置的tag该打的都打好了，该合并的都合并好了，此时要解决和cur相关的问题了
        int[] q = mq[cur];
        int[] i = mi[cur];
        for (int k = 0; k < q.length; k++) {
            // cur和谁有问题
            int tag = uf.getTag(q[k]);
            if (tag != -1)
                res[i[k]] = tag;
        }
    }




    public static class UnionFind {
        private int[] root;  // 并查集里的根信息，查询某个结点所在集合的根结点
        private int[] size;  // 某个集合的大小，只有每个集合的根结点有必要保存这个信息
        private int[] tag;   // 当前结点所在集合的tag信息
        private int[] help;  // 用来将并查集扁平化的辅助数组

        public UnionFind(int N) {
            root = new int[N];
            size = new int[N];
            tag = new int[N];
            help = new int[N];
            for (int i = 0; i < N; i++) {
                root[i] = i;
                size[i] = 1;
                tag[i] = -1; // 表示没遍历过
            }
        }


        public void union(int i, int j) {
            int r1 = findRoot(i);
            int r2 = findRoot(j);
            if (r1 != r2) {
                int greater = size[r1] >= size[r2] ? r1 : r2;
                int less = greater == r1 ? r2 : r1;
                root[less] = greater;
                size[greater] += size[less];
            }
        }


        // 集合的某个元素是i，请把整个集合打上统一的标签，t
        public void setTag(int i, int t) {
            tag[findRoot(i)] = t;
        }

        // 集合的某个元素是i，请把整个集合的tag信息返回
        public int getTag(int i) {
            return tag[findRoot(i)];
        }


        private int findRoot(int i) {
            int index = 0;
            while (root[i] != i) {
                help[index++] = i;
                i = root[i];
            }
            while (index > 0)
                root[help[--index]] = i;

            return i;
        }
    }
    // =============================================================================================
}
