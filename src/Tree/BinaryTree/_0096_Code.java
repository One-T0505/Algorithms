package Tree.BinaryTree;

// leetCode96
// 给你一个整数 n ，求恰由 n 个节点组成且节点值从 1 到 n 互不相同的二叉搜索树有多少种？
// 返回满足题意的二叉搜索树的种数。

public class _0096_Code {

    // 这个可以用动态规划做。
    // n==0表示整棵树为空，所以形态数为1；n==1时只有一个根结点，所以也是1。
    // n > 1时，其实就是枚举1～n让每个数i做根，然后让1～i-1做i的左子树，i+1～n做i的右子树。
    public int numTrees(int n) {
        int[] G = new int[n + 1];
        G[0] = 1;
        G[1] = 1;

        for (int i = 2; i <= n; ++i) {
            for (int j = 1; j <= i; ++j) {
                G[i] += G[j - 1] * G[i - j];
            }
        }
        return G[n];
    }
}
