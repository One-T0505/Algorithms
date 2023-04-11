package Basic.Tree.BinaryTree;

//给你一棵完全二叉树的根节点root，求出该树的节点个数。
// 完全二叉树的定义如下：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，
// 并且最下面一层的节点都集中在该层最左边的若干位置。若最底层为第 h 层，则该层包含 1 ~ 2^h 个节点。

import utils.TreeNode;
import java.util.ArrayList;

public class _0222_Code {


    // 最容易想到的方法1  但是这个方法并没有利用到完全二叉树这一特性
    public static int countNodes(TreeNode root) {
        if (root == null)
            return 0;
        return countNodes(root.left) + countNodes(root.right) + 1;
    }


    // 方法2  DFS
    public static int countNodes2(TreeNode root) {
        if (root == null)
            return 0;
        ArrayList<Integer> level = new ArrayList<>();
        dfs(root, 0 , level);
        int res = 0;
        for (int e : level)
            res += e;
        return res;
    }

    private static void dfs(TreeNode cur, int level, ArrayList<Integer> dp) {
        if (cur == null)
            return;
        if (level < dp.size())
            dp.set(level, dp.get(level) + 1);
        else
            dp.add(1);
        dfs(cur.left, level + 1, dp);
        dfs(cur.right, level + 1, dp);
    }



    // 方法3 是利用了完全二叉树特性的最优方法
    // 规定根结点在第0层。不管怎么样，该完全二叉树的最左的结点一定位于最低层，我们先找到最左的结点left，
    // 如果left所处的层级在h层，那也就是说这棵完全二叉树的总结点数在：[2^h, 2^(h+1) - 1]
    // 我们就把完全二叉树的第0～h层全部填满成满二叉树，我说的是想象，并不是真的填，编号从1开始，一直到2^(h+1)-1
    // 0～h-1层的结点数我们可以根据公式计算得出，现在只用计算最底层第h层有多少结点即可。
    // 我们现在就可以二分了，因为已经确定了结点总数量的上下界了，并且最后第h层所有结点包含虚拟结点的编号就对应着
    // 2^h+1～2^(h+1)-1，注意：第h层的第一个就是我们一开始找的哪个最左的结点left，所以它可以不考虑在内，从它的下一个
    // 开始考虑即可。于是我们就可以在这些编号里二分，然后查询该树上是否存在该结点了。

    // 比如二分除了一个编号k，如何快速确定它在完全二叉树上是否存在呢？ 举个例子
    // 比如k==11  先把编号写成二进制：1011  其最高位的1省略掉不算，从次高位开始，从根结点开始。
    // 如果当前位为1，表示从当前结点向右孩子滑动；如果当前位为0，表示从当前结点向左孩子滑动。
    //
    //         7                           1                    如果完全二叉树如左图所示，右边是编号图
    //       /   \                    /        \                我们想查11号结点，其二进制 1011
    //      4     6                 2           3               最高位的1不管，从次高位开始。0，所以从根
    //     / \   / \      -->     /   \       /   \             向左孩子滑动来到4，然后两次滑向右孩子，发现为
    //    8  3  2   5            4     5      6    7            null，说明11号结点不存在，那么结点数量的
    //   /                      / \                             上下界就可以更新了。
    //  1                      8   9 10 11  12 13 14 15
    //
    public static int countNodes3(TreeNode root) {
        if (root == null)
            return 0;
        int level = 0;
        TreeNode mostLeft = root;
        while (mostLeft.left != null){
            level++;
            mostLeft = mostLeft.left;
        }
        // 这里还是把最低层的第一个结点包含在内一起计算了，我发现这样写代码好写一些
        int L = 1 << level;
        int R = (1 << (level + 1)) - 1;
        int res = L;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (exist(root, level, mid)){
                res = mid;
                L = mid + 1;
            }
            else
                R = mid - 1;
        }
        return res;
    }

    private static boolean exist(TreeNode root, int level, int num) {
        int offset = 1 << (level - 1);
        while (offset != 0){
            int path = num & offset;
            offset >>= 1;
            root = path == 0 ? root.left : root.right;
            if (root == null)
                return false;
        }
        return true;
    }


}
