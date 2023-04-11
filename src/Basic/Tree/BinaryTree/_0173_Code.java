package Basic.Tree.BinaryTree;

// 二叉搜索树迭代器
// 实现一个二叉搜索树迭代器类 BSTIterator，表示一个按中序遍历二叉搜索树（BST）的迭代器：
//  1.BSTIterator(TreeNode root)  初始化 BSTIterator 类的一个对象。BST 的根节点 root 会作为构造函数的
//    一部分给出。指针应初始化为一个不存在于 BST 中的数字，且该数字小于 BST 中的任何元素。
//  2.boolean hasNext() 如果向指针右侧遍历存在数字，则返回 true ；否则返回 false 。
//  3.int next() 将指针向右移动，然后返回指针处的数字。
// 注意，指针初始化为一个不存在于 BST 中的数字，所以对 next() 的首次调用将返回 BST 中的最小元素。
// 你可以假设 next() 调用总是有效的，也就是说，当调用 next() 时，BST的中序遍历中至少存在一个下一个数字。

import utils.TreeNode;
import java.util.Stack;

public class _0173_Code {

    static class BSTIterator {

        Stack<TreeNode> stack;
        TreeNode pointer;

        public BSTIterator(TreeNode root) {
            stack = new Stack<>();
            pointer = root;
        }


        // 注意题目说的，每次调用next()都能保证确实还有下一个元素，所以我们不用去判断
        public int next() {
            while (pointer != null){
                stack.push(pointer);
                pointer = pointer.left;
            }
            pointer = stack.pop();
            // ret就是要返回的值，并且pointer指向了下一个结点
            int ret = pointer.val;
            pointer = pointer.right;
            return ret;
        }

        public boolean hasNext() {
            return pointer != null || !stack.isEmpty();
        }
    }
}
