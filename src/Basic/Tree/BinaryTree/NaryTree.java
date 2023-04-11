package Basic.Tree.BinaryTree;

import utils.TreeNode;

import java.util.ArrayList;
import java.util.List;

// leetCode431
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


    // 传入的参数是上级结点的孩子列表，该方法作用是将上级结点的各个子树转化成一个标准二叉树，并返回根结点，
    // 上游会让这个上级结点的left指针接住返回的根结点。
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
