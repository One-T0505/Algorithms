package LeetCode;

import utils.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ymy
 * 2023/4/3 - 12 : 40
 **/


// leetCode449  二叉搜索树的序列化与反序列化


public class SerializeBST {

    public static String serialize(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        preOrder(root, list);
        // 核心思想掌握就行了，这里的返回值的范围为啥要去掉头尾两个位置？是因为题目定制化的原因，因为题目给的
        // 树序列化后之后是这样的 [2, 1, 3] 头尾有括号，并且逗号之后还有空格
        return list.toString().substring(1, list.toString().length() - 1);
    }



    public static TreeNode deserialize(String data) {
        if (data.isEmpty())
            return null;
        String[] parts = data.split(", ");
        LinkedList<Integer> queue = new LinkedList<>();
        int N = parts.length;
        for (String part : parts) {
            queue.addLast(Integer.parseInt(part));
        }
        return construct(queue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static TreeNode construct(LinkedList<Integer> queue, int lower, int upper) {
        if (queue.isEmpty() || queue.getFirst() < lower || queue.getFirst() > upper)
            return null;
        int val = queue.pollFirst();
        TreeNode root = new TreeNode(val);
        root.left = construct(queue, lower, val);
        root.right = construct(queue, val, upper);
        return root;
    }


    private static void preOrder(TreeNode cur, List<Integer> list) {
        if (cur == null)
            return;
        list.add(cur.val);
        preOrder(cur.left, list);
        preOrder(cur.right, list);
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(2);
        root.left = new TreeNode(1);
        root.right = new TreeNode(3);
        System.out.println(serialize(root));
    }
}
