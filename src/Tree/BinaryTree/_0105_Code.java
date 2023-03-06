package Tree.BinaryTree;

// leetCode105
// 给定两个整数数组 preorder 和 inorder，其中 preorder 是二叉树的先序遍历，inorder 是同一棵树的中
// 序遍历，请构造二叉树并返回其根节点。

import Tree.TreeNode;
import java.util.HashMap;

public class _0105_Code {
    // 这个是为了方便定位元素位置的。我们都是通过顺序选择先序数组的数作为根，然后去中序数组中确定该根元素的位置
    // 再将其分为左子树和右子树。
    public HashMap<Integer, Integer> map;
    public int N;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        N = preorder.length;
        map = new HashMap<>();
        // key:先序序列的某个元素  value: 该元素出现在中序数组的位置
        for (int k : preorder) {
            for (int j = 0; j < N; j++) {
                if (inorder[j] == k) {
                    map.put(k, j);
                    break;
                }
            }
        }
        return f(preorder, 0, N - 1, inorder, 0, N - 1);
    }

    // 当我们在中序数组中确定某部分是左子树的元素时，该范围的长度在先序数组中依然是等长的。
    // 比如:
    // preorder = [8, 6, 7, 9, 4, 1, 3, 5]
    //  inorder = [6, 9, 7, 8, 1, 4, 5, 3]
    // 先将preorder的首个元素作为根，会发现其在中序数组的3位置，那么0～2位置就是8作为根的左子树所有的结点
    // 其长度为3，对应到preorder中就是1～3; 4~7对应的就是8作为根的右子树部分，其对应到先序数组中就是4~7
    private TreeNode f(int[] preorder, int L, int R, int[] inorder, int l, int r) {
        if (L > R)
            return null;
        TreeNode root = new TreeNode(preorder[L]);
        int pos = map.get(preorder[L]);
        int leftSize = pos - l;
        int rightSize = r - pos;
        root.left = f(preorder, L + 1, L  + leftSize, inorder, l, pos - 1);
        root.right = f(preorder, L + leftSize + 1, R, inorder, pos + 1, r);
        return root;
    }
}
