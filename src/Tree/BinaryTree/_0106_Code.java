package Tree.BinaryTree;

// leetCode106
// 给定两个整数数组 inorder 和 postorder ，其中 inorder 是二叉树的中序遍历， postorder 是同一棵树
// 的后序遍历，请你构造并返回这颗二叉树。

import Tree.TreeNode;
import java.util.HashMap;

public class _0106_Code {

    // 先去看懂105题

    public HashMap<Integer, Integer> map;
    public int N;

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        N = inorder.length;
        map = new HashMap<>();
        // key:先序序列的某个元素  value: 该元素出现在中序数组的位置
        for (int k : postorder) {
            for (int j = 0; j < N; j++) {
                if (inorder[j] == k) {
                    map.put(k, j);
                    break;
                }
            }
        }
        return f(postorder, 0, N - 1, inorder, 0, N - 1);
    }

    private TreeNode f(int[] postorder, int L, int R, int[] inorder, int l, int r) {
        if (L > R)
            return null;
        TreeNode root = new TreeNode(postorder[R]);
        int pos = map.get(postorder[R]);
        int leftSize = pos - l;
        int rightSize = r - pos;
        root.left = f(postorder, L, L + leftSize - 1, inorder, l, pos - 1);
        root.right = f(postorder, L + leftSize, R - 1, inorder, pos + 1, r);
        return root;
    }
}
