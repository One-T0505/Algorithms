package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import utils.TreeNode;

// 给你二叉树的根节点 root，返回其节点值的锯齿形层序遍历。
// (即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行)。

public class _0103_ZigzagLevelOrder {

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null)
            return res;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int size = 0;   // size记录每一层有多少个结点
        boolean fromLeft = true;  // 每一次是从左到右还是从右到左打印
        while (!queue.isEmpty()) {
            size = queue.size();  // 刚进循环时，size就变成了1，这表示第一层的结点数，只有一个根结点
            ArrayList<Integer> level = new ArrayList<>();  // 记录每一层有哪些结点
            for (int i = 0; i < size; i++) {
                TreeNode cur = fromLeft ? queue.pollFirst() : queue.pollLast();
                level.add(cur.val);
                if (fromLeft) {
                    if (cur.left != null)
                        queue.addLast(cur.left);
                    if (cur.right != null)
                        queue.addLast(cur.right);
                } else {  // 这个顺序不能变，必须是先右再左
                    if (cur.right != null)
                        queue.addFirst(cur.right);
                    if (cur.left != null)
                        queue.addFirst(cur.left);
                }
            }
            res.add(level);
            fromLeft = !fromLeft;
        }
        return res;
    }
    // 这道题有个非常重要的技巧，就是在层序遍历树时，把每层的数量记录，一层一层地处理结点。这道题目是链式层次打印
    // 树，在处理过程中，我们需要维持一个原则：就是让队列从头到尾存放的结点顺序，和树中某层从左到右的顺序一致。
    // 这是基本原则。保持这个核心原则，那么只需要在队列的结点是从头还是尾出队，出队结点的孩子按照什么顺序从哪头进队
    // 来考虑。

}
