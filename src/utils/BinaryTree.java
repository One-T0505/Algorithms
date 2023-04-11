package utils;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree {



    // 要求生成的树中，每个结点的值都不一样。规定值都为非负数，将给的数组按照层次遍历建树
    // 数组中为负数表示该结点为null，不允许数组第一个元素就为负数。
    public static TreeNode generateRandomBinaryTree(int[] arr){
        if (arr == null || arr.length == 0)
            return null;
        Queue<Integer> list = new LinkedList<>();
        for (int elem : arr)
            list.add(elem);
        return process(list);
    }

    private static TreeNode process(Queue<Integer> e) {
        if (e.isEmpty())
            return null;
        Integer val = e.poll();
        if (val < 0)
            return null;
        TreeNode root = new TreeNode(val);
        root.left = process(e);
        root.right = process(e);
        return root;
    }

    public static void main(String[] args) {
        int[] arr = {2, 7, -3, -10};
        TreeNode root = generateRandomBinaryTree(arr);
        Basic.Tree.BinaryTree.BinaryTree.preOrder(root);
    }
}
