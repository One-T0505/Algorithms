package UnionFind;

// 给定一个方阵M，行数代表有多少个人，M[i][j] = 1，表示i和j两个人互相认识，并且有M[j][i] = 1.
// 并且有M[i][i] = 1,自己肯定认识自己。所以M必然是一个对称矩阵，请问这里有多少个朋友圈子。
// 注意：0认识2，0认识4，他们三个就可以构成圈子，不用让2也认识4。只要一个人认识圈子里的一个人就可以加入该圈子

public class FriendCircle {
    // 这里用改良的并查集结构，用数组替换之前用的哈希表，这样更快

    static class UnionFind {
        public int[] roots; // roots[i]表示i的代表结点
        public int[] sizes; // 如果i是集合的代表结点，那么sizes[i]表示其集合的大小，否则无意义
        public int[] help;  // 这里的help就相当于用哈希表实现并查集时申请的栈，在findRoot时用于路径压缩的
        public int circles; // 朋友圈的数量

        public UnionFind(int N) { // N表示有多少个人
            roots = new int[N];
            sizes = new int[N];
            help = new int[N];
            circles = N;   // 一开始每一个人都是一个圈子
        }

        private int findRoot(int i){
             int index = 0;
             while (i != roots[i]){
                 i = roots[i];
                 help[index++] = i; // 将沿途的结点都放在help中记录着
             }
             // 找到i的代表结点了
            for (index -= 1; index >= 0; index--)
                roots[help[index]] = i;
            return i;
        }

        public void union(int i, int j){
            int root1 = findRoot(i);
            int root2 = findRoot(j);
            if (root1 != root2) {
                if (sizes[root1] >= sizes[root2]){
                    sizes[root1] += sizes[root2];
                    roots[root2] = root1;
                } else {
                    sizes[root2] += sizes[root1];
                    roots[root1] =root2;
                }
                circles--;
            }
        }
    }


    // 主方法
    public static int findFriendCircles(int[][] M){
        int N = M.length;
        UnionFind unionFind = new UnionFind(N);
        // 只用遍历上半部分矩阵，不包含对角线
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (M[i][j] == 1)
                    unionFind.union(i, j);
            }
        }
        return unionFind.circles;
    }
}
