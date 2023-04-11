package GreatOffer.TopHotQ;

import utils.TreeNode;

import java.util.HashMap;

// 给定一个二叉树的根节点 root ，和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的路径的数目。
// 路径不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
//
//           -1               如果目标是8，则应该返回3。因为有3条路径：5->3   5->2->1   -3->11
//         /    \
//        5      -3
//      /   \      \
//     3     2      11
//    / \     \
//   6  -2     1

public class _0437_PathSumIII {

    // 思路：因为在树中，路径的方向是单一的，所以这和在数组中求累加和的方法一样。回忆一下在数组中的做法：先做一个
    // 从0..i的累加和数组sum，然后依次让每个元素作为子数组的结尾元素，如果想找以i元素作为结尾的子数组的累加和是否达到
    // 目标，其实就是在找i之前是否存在一个j，使得sum[j] = sum[i] - 目标。
    // 在二叉树中也一样，每次到一个结点时都将从根到自己的路径和添加到辅助结构中，并在结构中查询是否自己的累加和与
    // 目标值的差值是否存在。

    // 这道题和问题原型还是有一点区别的：
    //  1.这道题目是统计数量，所以用哈希表做辅助结构时，key是某个累加和，value就是该累加和出现了多少次
    //    并且当遍历退出某个结点后，还要删除自己的记录，因为遍历到其他结点时也是用这个结构，但是路径是不相通的，
    //    如果别的结点误用了别的信息，而导致产生了多余的答案就会错误，所以每个结点退出时都要清除掉自己的信息。
    //    比如。借用上面的图，target==2，当来到-2时会在表里添加5这个累加和，如果不清除掉，当来到1时，会记录自己的
    //    累加和7，并会在表里查询是否存在7-2==5，发现表里还真存在，那么就会认为存在一条以1为路径终点的路径使得累加和
    //    等于target，而实际上是不存在的。

    public static int pathSum(TreeNode root, int targetSum) {
        if (root == null)
            return 0;
        // key：累加和  val : 出现了多少次
        HashMap<Long, Integer> dp = new HashMap<>();
        // 如果target==4，而根结点就是4，那么根结点自己就是一条路径，我们需要在dp中预先存放(0,1)这条记录，如果碰
        // 到了刚才的这种情况，会去表里查询是否存在0这个累加和，一看，表里有，次数是1，然后就会将这个结果正确记录下
        // 来；如果表里没这个记录，碰到上面这种情况，去表里查询是否有0，表里是空的，于是认为不存在，所以就会漏答案
        // 而这条记录又不会影响接下来的计算
        // 并且，当真的找到一条路径等于target时，会去表里查询是否存在0这个记录，如果不预先添加，当找到了路径时也会被
        // 漏掉
        dp.put(0L, 1);
        return f(root, targetSum, 0, dp);
    }


    // 以x为根的树上有多少条路径满足t
    // pre是从根到x的父结点已经积攒了多少累加和
    private static int f(TreeNode x, int t, long pre, HashMap<Long, Integer> dp) {
        if (x == null)
            return 0;
        long cur = pre + x.val;
        int res = 0;
        if (dp.containsKey(cur - t))
            res = dp.get(cur - t);
        dp.put(cur, dp.getOrDefault(cur, 0) + 1);

        res += f(x.left, t, cur, dp);
        res += f(x.right, t, cur, dp);
        // 左递归和右递归都跑完了，此时要退出当前结点x了，所以要清除x的记录
        if (dp.get(cur) == 1) // 这个cur是由于x造成的
            dp.remove(cur);
        else
            dp.put(cur, dp.get(cur) - 1);
        return res;
    }



    public static void main(String[] args) {
        TreeNode root = new TreeNode(-1);
        root.left = new TreeNode(5);
        root.right = new TreeNode(-3);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(2);
        root.right.right = new TreeNode(11);
        root.left.left.left = new TreeNode(6);
        root.left.left.right = new TreeNode(-2);
        root.left.right.right = new TreeNode(1);

        int t = 8;
        System.out.println(pathSum(root, t));
    }
}
