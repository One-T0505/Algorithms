package Tree.BinaryTree;

import Tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

// 多叉树与标准二叉树的相互转换
//
//              1                              1
//           /  ｜  \      encode            /
//          3   2    4      <==>            3
//         / \             decode          / \
//        5   6                           5   2
//                                         \   \
//                                          6   4
//

public class NaryTree {

    static class NaryNode {
        public int val;
        public List<NaryNode> children;

        public NaryNode(int val) {
            this.val = val;
        }

        public NaryNode(int val, List<NaryNode> children) {
            this.val = val;
            this.children = children;
        }
    }

    // 将多叉树转换为二叉树，并返回根结点
    public TreeNode encode(NaryNode root) {
        if (root == null)
            return null;
        TreeNode res = new TreeNode(root.val);
        res.left = en(root.children);
        return res;
    }

    private TreeNode en(List<NaryNode> children) {
        TreeNode head = null, cur = null;
        for (NaryNode child : children) {
            TreeNode curNode = new TreeNode(child.val);
            if (head == null)
                head = curNode;
            else
                cur.right = curNode;
            cur = curNode;
            cur.left = en(child.children);
        }
        return head;
    }

    public NaryNode decode(TreeNode root) {
        if (root == null)
            return null;
        return new NaryNode(root.val, de(root.left));
    }

    private List<NaryNode> de(TreeNode firstChild) {
        ArrayList<NaryNode> children = new ArrayList<>();
        while (firstChild != null) {
            NaryNode cur = new NaryNode(firstChild.val, de(firstChild.left));
            children.add(cur);
            firstChild = firstChild.right;
        }
        return children;
    }
}
