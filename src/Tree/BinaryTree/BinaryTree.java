package Tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {

    // 先序遍历
    public static void preOrder(TreeNode root) {
        if (root == null)
            return;
        System.out.print(root.val + "\t");
        preOrder(root.left);
        preOrder(root.right);
    }

    // 中序遍历
    public static void inOrder(TreeNode root) {
        if (root == null)
            return;
        inOrder(root.left);
        System.out.print(root.val + "\t");
        inOrder(root.right);
    }

    // 后序遍历
    public static void posOrder(TreeNode root) {
        if (root == null)
            return;
        posOrder(root.left);
        posOrder(root.right);
        System.out.print(root.val + "\t");
    }

    // 非递归方式实现先序遍历
    public static void preOrderUnderStack(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                root = stack.pop();
                System.out.print(root.val + "\t");
                if (root.right != null)
                    stack.push(root.right);
                if (root.left != null)
                    stack.push(root.left);
            }
        }
        System.out.println();
    }

    // 非递归方式实现中序遍历
    public static void inOrderUnderStack(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            TreeNode cur = root;
            while (!stack.isEmpty() || cur != null) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    System.out.print(cur.val + "\t");
                    cur = cur.right;
                }
            }
        }
        System.out.println();
    }

    // 非递归方式实现后序遍历
    public static void posOrderUnderStack(TreeNode root){
        if (root != null){
            Stack<TreeNode> stack1 = new Stack<>();
            Stack<TreeNode> stack2 = new Stack<>();
            stack1.push(root);
            while (!stack1.isEmpty()) {
                root = stack1.pop();
                stack2.push(root);
                if (root.left != null)
                    stack1.push(root.left);
                if (root.right != null)
                    stack1.push(root.right);
            }
            while (!stack2.isEmpty())
                System.out.print(stack2.pop().val + "\t");
        }
        System.out.println();
    }

    // 只用一个栈实现非递归方式的后序遍历
    public static void posOrderUnderStack2(TreeNode root){
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode cur = stack.peek();
                if (cur.left != null && root != cur.left && root != cur.right)
                    stack.push(cur.left);
                else if (cur.right != null && root != cur.right)
                    stack.push(cur.right);
                else {
                    System.out.print(stack.pop().val + "\t");
                    root = cur;
                }
            }
        }
        System.out.println();
    }

    // 层次遍历
    public static void levelOrder(TreeNode root) {
        if (root == null)
            return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            root = queue.poll();
            System.out.print(root.val + "\t");
            if (root.left != null)
                queue.add(root.left);
            if (root.right != null)
                queue.add(root.right);
        }
        System.out.println();
    }

    // 统计一个二叉树的层次的最大宽度，利用hashmap和队列实现
    public static int maxWidthV1(TreeNode root){
        if (root == null)
            return 0;
        Queue<TreeNode> queue = new LinkedList<>();
        HashMap<TreeNode, Integer> levelMap = new HashMap<>();  // 记录结点在树中的高度
        int level = 1;  // 记录当前正在统计哪一层的结点数
        int nodes = 0; // 当前层已统计的结点数
        int max = 0;  // 记录最多结点数

        queue.add(root);
        levelMap.put(root, level);

        while (!queue.isEmpty()){
            TreeNode cur = queue.poll();
            Integer curLevel = levelMap.get(cur);  // 当前结点的层
            if (cur.left != null){
                queue.add(cur.left);
                levelMap.put(cur.left, level + 1);
            }
            if (cur.right != null){
                queue.add(cur.right);
                levelMap.put(cur.right, level + 1);
            }
            if (curLevel == level){
                nodes++;
            }else {
                max = Math.max(max, nodes);
                level++;
                nodes = 1; // 如果进入了else里，就说明已经有一个结点了
            }
        }
        return Math.max(max, nodes);  // 还需要再比较一次，因为最后一层的结点在队列中是不会进入else代码块中的
    }

    // 统计一个二叉树的层次的最大宽度，只用队列实现
    public static int maxWidthV2(TreeNode root){
        if (root == null)
            return 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        TreeNode curEnd = root;  // 用于记录当前层的最后一个结点
        TreeNode nextEnd = null; // 用于记录下一层最后一个结点
        int max = 0;
        int nodes = 0;  // 记录当前层已经统计的结点数量
        while (!queue.isEmpty()){
            TreeNode cur = queue.poll();
            if (cur.left != null){
                queue.add(cur.left);
                nextEnd = cur.left;  // 动态记录下一层的最后一个结点
            }
            if (cur.right != null){
                queue.add(cur.right);
                nextEnd = cur.right; // 动态记录下一层的最后一个结点
            }
            nodes++;
            if (cur == curEnd){
                max = Math.max(max, nodes);
                nodes = 0;
                curEnd = nextEnd;
            }
        }
        return max;
    }

    // --------------------------------------------------------------------------------------------------------
    //按照先序顺序序列化二叉树的主方法. 中序遍历是没办法序列化的，因为有歧义
    public static Queue<String> preOrderSerialize(TreeNode root) {
        Queue<String> res = new LinkedList<>();
        preSerialize(root, res);
        return res;
    }

    //按照先序顺序序列化二叉树的核心方法
    private static void preSerialize(TreeNode root, Queue<String> res) {
        if (root == null)
            res.add(null);
        else {
            res.add(String.valueOf(root.val));
            preSerialize(root.left, res);
            preSerialize(root.right, res);
        }
    }

    // 接收一个按照先序顺序序列化的 list，将其反序列化构建成一棵二叉树
    public static TreeNode preOrderUnserialize(Queue<String> list){
        if (list == null || list.size() == 0)
            return null;
        return preUnserialize(list);
    }

    public static TreeNode preUnserialize(Queue<String> list) {
        String value = list.poll();
        if (value == null)
            return null;
        TreeNode root = new TreeNode(Integer.parseInt(value));
        root.left = preUnserialize(list);
        root.right = preUnserialize(list);
        return root;
    }

    //中序、后序方式的序列化与反序列化与先序差不多，这里讲一下层次遍历的方式
    public static Queue<String> levelOrderSerialize(TreeNode root) {
        //存储序列化的结果
        Queue<String> res = new LinkedList<>();
        if (root == null)
            res.add(null);
        else {
            res.add(String.valueOf(root.val));
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                root = queue.poll();
                if (root.left != null) {
                    queue.add(root.left);
                    res.add(String.valueOf(root.left.val));
                }else
                    res.add(null);
                if (root.right != null) {
                    queue.add(root.right);
                    res.add(String.valueOf(root.right.val));
                }else
                    res.add(null);
            }
        }
        return res;
    }

    // 层次遍历的反序列化
    public static TreeNode levelOrderUnserialize(Queue<String> list) {
        if (list == null || list.size() == 0)
            return null;
        TreeNode root = generateNode(list.poll());
        Queue<TreeNode> queue = new LinkedList<>();
        if (root != null)
            queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            cur.left = generateNode(list.poll());
            cur.right = generateNode(list.poll());
            if (cur.left != null)
                queue.add(cur.left);
            if (cur.right != null)
                queue.add(cur.right);
        }
        return root;
    }

    private static TreeNode generateNode(String poll) {
        if (poll == null)
            return null;
        return new TreeNode(Integer.parseInt(poll));
    }
    // --------------------------------------------------------------------------------------------------------

}
