package LeetCode;

import utils.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * ymy
 * 2023/4/9 - 15 : 39
 **/


// leetCode572
// 给你两棵二叉树 root 和 subRoot 。检验 root 中是否包含和 subRoot 具有相同结构和节点值的子树。如果存在，
// 返回 true ；否则，返回 false 。
// 二叉树 tree 的一棵子树包括 tree 的某个节点和这个节点的所有后代节点。tree 也可以看做它自身的一棵子树。

// root 树上的节点数量范围是 [1, 2000]
// subRoot 树上的节点数量范围是 [1, 1000]
// -10^4 <= root.val <= 10^4
// -10^4 <= subRoot.val <= 10^4


public class SubtreeOfAnother {


    // 常规方法
    public static boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if((root == null) ^ (subRoot == null))
            return false;
        if(root == null) // 都为空
            return true;
        return isSame(root.left, subRoot) || isSame(root, subRoot) || isSame(root.right, subRoot);
    }


    private static boolean isSame(TreeNode a, TreeNode b){
        if((a == null) ^ (b == null))
            return false;
        if(a == null) // 都为空
            return true;
        // 都不为空
        return (a.val == b.val) && isSame(a.left, b.left) && isSame(a.right, b.right);
    }
    // ----------------------------------------------------------------------------------------------





    // 优化方法
    // 这个方法需要我们先了解一个「小套路」：一棵子树上的点在深度优先搜索序列（即先序遍历）中是连续的。
    // 了解了这个「小套路」之后，我们可以确定解决这个问题的方向就是：把 s 和 t 先转换成深度优先搜索序列，
    // 然后看 t 的深度优先搜索序列是否是 s 的深度优先搜索序列的「子串」。
    // 假设 s 由两个点组成，1 是根，2 是 1 的左孩子；t 也由两个点组成，1 是根，2 是 1 的右孩子。这样
    // 一来 s 和 t 的深度优先搜索序列相同，可是 t 并不是 s 的某一棵子树。由此可见
    // 「s 的深度优先搜索序列包含 t 的深度优先搜索序列」 是 「t 是 s 子树」 的必要不充分条件，所以单纯这样做是不正确的。
    // 为了解决这个问题，我们可以引入两个空值 lNull 和 rNull，当一个节点的左孩子或者右孩子为空的时候，就插入这
    // 两个空值，这样深度优先搜索序列就唯一对应一棵树。处理完之后，就可以通过判断
    // 「s 的深度优先搜索序列包含 t 的深度优先搜索序列」 来判断答案。
    public static List<Integer> sOrder = new ArrayList<>();
    public static List<Integer> tOrder = new ArrayList<>();

    public static int max, lNull, rNull;

    public static boolean isSubtree2(TreeNode root, TreeNode subRoot){

        max = Integer.MIN_VALUE;
        getMax(root);
        getMax(subRoot);
        lNull = max + 1;
        rNull = max + 2;

        getDfsOrder(root, sOrder);
        getDfsOrder(subRoot, tOrder);

        return kmp();
    }


    private static void getMax(TreeNode cur) {
        if (cur == null)
            return;
        max = Math.max(max, cur.val);
        getMax(cur.left);
        getMax(cur.right);
    }


    private static void getDfsOrder(TreeNode cur, List<Integer> list) {
        if (cur == null)
            return;
        list.add(cur.val);
        if (cur.left != null)
            getDfsOrder(cur.left, list);
        else
            list.add(lNull);

        if (cur.right != null)
            getDfsOrder(cur.right, list);
        else
            list.add(rNull);
    }


    private static boolean kmp() {
        int sLen = sOrder.size(), tLen = tOrder.size();
        // 完成next数组
        int[] fail = new int[tLen];
        fail[0] = -1;
        // fail[1] = 0
        int j = 2;
        int p = fail[j - 1];
        while (j < tLen){
            if (tOrder.get(p).equals(tOrder.get(j - 1)))
                fail[j++] = ++p;
            else if (p > 0) {
                p = fail[p];
            } else
                fail[j++] = 0;
        }
        j = 0;
        int i = 0;
        while (i < sLen && j < tLen){
            if (sOrder.get(i).equals(tOrder.get(j))){
                i++;
                j++;
            } else if (fail[j] != -1)
                j = fail[j];
            else
                i++;
        }
        return j == tLen;
    }
    // --------------------------------------------------------------------------------------------


    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        TreeNode sub = new TreeNode(1);
        System.out.println(isSubtree2(root, sub));
    }
}
