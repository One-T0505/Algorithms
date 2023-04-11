package Basic.Tree.BinaryTree;

// 给定一个二叉搜索树的根节点 root ，和一个整数 k ，请你设计一个算法查找其中第 k 个最小元素（从 1 开始计数）。

import utils.TreeNode;

import java.util.PriorityQueue;
import java.util.Stack;

public class _0230_Code {


    // 传统方法，任意一种遍历方式 + 大根堆
    // 但是这种方法没有利用到二叉搜索树的特性
    public static int kthSmallest(TreeNode root, int k) {
        if (root == null || k < 1)
            return Integer.MAX_VALUE;
        // 大根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
        traverse(root, heap, k);
        return heap.peek();
    }

    private static void traverse(TreeNode x, PriorityQueue<Integer> heap, int k) {
        if (x == null)
            return;
        traverse(x.left, heap, k);
        if (heap.size() < k)
            heap.add(x.val);
        else if (heap.size() == k && x.val < heap.peek()) {
            heap.poll();
            heap.add(x.val);
        }
        traverse(x.right, heap, k);
    }
    // ===============================================================================================




    // 方法2   尝试利用二叉搜索树的特性  因为二叉搜索树中序遍历的结果是一个递增数组
    // 所以我们只需要遍历前k个结点即可   应该还记得之前讲的非递归实现二叉树的中序遍历吧？
    // 递归的话，我们无法精确判断何时访问到了第k小的结点，而迭代可以
    public static int kthSmallest2(TreeNode root, int k) {
        if (root == null || k < 1)
            return Integer.MAX_VALUE;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode x = root;
        while (x != null || !stack.isEmpty()){
            while (x != null){
                stack.push(x);
                x = x.left;
            }
            // 此时x已经来到了null，并且整个左侧沿线的路径都存放到了栈中
            x = stack.pop();
            if (--k == 0)
                break;
            x = x.right;
        }
        return x.val;
    }

}
