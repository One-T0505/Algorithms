package Tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTreeTraverse {

    // 先序遍历
    public void preOrder(TreeNode root) {
        if (root == null)
            return;
        System.out.print(root.val + "\t");
        preOrder(root.left);
        preOrder(root.right);
    }

    // 中序遍历
    public void inOrder(TreeNode root) {
        if (root == null)
            return;
        inOrder(root.left);
        System.out.print(root.val + "\t");
        inOrder(root.right);
    }

    // 后序遍历
    public void posOrder(TreeNode root) {
        if (root == null)
            return;
        posOrder(root.left);
        posOrder(root.right);
        System.out.print(root.val + "\t");
    }

    // 非递归方式实现先序遍历
    public void preOrderUnderStack(TreeNode root) {
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
    public void inOrderUnderStack(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            while (!stack.isEmpty() || root != null) {
                if (root != null) {
                    stack.push(root);
                    root = root.left;
                } else {
                    root = stack.pop();
                    System.out.print(root.val + "\t");
                    root = root.right;
                }
            }
        }
        System.out.println();
    }

    // 非递归方式实现后序遍历
    public void posOrderUnderStack(TreeNode root){
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
    public void posOrderUnderStack2(TreeNode root){
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.push(root);
            TreeNode tmp = null;
            while (!stack.isEmpty()) {
                tmp = stack.peek();
                if (tmp.left != null && root != tmp.left && root != tmp.right)
                    stack.push(tmp.left);
                else if (tmp.right != null && root != tmp.right)
                    stack.push(tmp.right);
                else {
                    System.out.print(stack.pop().val + "\t");
                    root = tmp;
                }
            }
        }
        System.out.println();
    }

    // 层次遍历
    public void levelOrder(TreeNode root) {
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

    // 统计一个二叉树的层次的最大宽度
    public int maxWidthUnderMap(TreeNode root) {
        if (root == null)
            return 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        //k为结点，v为层级
        HashMap<TreeNode, Integer> hashMap = new HashMap<>();
        hashMap.put(root, 1);

        // curlevel表示当前正在统计哪一层，curLevelNodes 表示当前层的结点数，max 表示层的最大结点数
        int curlevel = 1, curLevelNodes = 0, max = 0;
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            int curNodeLevel = hashMap.get(cur);
            if (cur.left != null) {
                hashMap.put(cur.left, curNodeLevel + 1);
                queue.add(cur.left);
            }
            if (cur.right != null) {
                hashMap.put(cur.right, curNodeLevel + 1);
                queue.add(cur.right);
            }

            if (curNodeLevel == curlevel){
                curLevelNodes++;
            }else {
                curlevel++;
                max = Math.max(curLevelNodes, max);
                curLevelNodes = 1;
            }
        }
        max = Math.max(max, curLevelNodes);
        return max;
    }

    //按照先序顺序序列化二叉树的主方法
    public Queue<String> preOrderSerialize(TreeNode root) {
        Queue<String> res = new LinkedList<>();
        preSerialize(root, res);
        return res;
    }

    //按照先序顺序序列化二叉树的核心方法
    private void preSerialize(TreeNode root, Queue<String> res) {
        if (root == null)
            res.add(null);
        else {
            res.add(String.valueOf(root.val));
            preSerialize(root.left, res);
            preSerialize(root.right, res);
        }
    }

    // 接收一个按照先序顺序序列化的 list，将其反序列化构建成一棵二叉树
    public TreeNode preOrderUnserialize(Queue<String> list){
        if (list == null || list.size() == 0)
            return null;
        return preUnserialize(list);
    }

    //中序、后序方式的序列化与反序列化与先序差不多，这里讲一下层次遍历的方式
    public Queue<String> levelOrderSerialize(TreeNode root) {
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
    public TreeNode levelorderUnserialize(Queue<String> list) {
        if (list == null || list.size() == 0)
            return null;
        TreeNode root = generateNode(list.poll());
        Queue<TreeNode> queue = new LinkedList<>();
        if (root != null)
            queue.add(root);

        TreeNode tmp =null;
        while (!queue.isEmpty()) {
            tmp = queue.poll();
            tmp.left = generateNode(list.poll());
            tmp.right = generateNode(list.poll());
            if (tmp.left != null)
                queue.add(tmp.left);
            if (tmp.right != null)
                queue.add(tmp.right);
        }
        return root;
    }

    public TreeNode generateNode(String poll) {
        if (poll == null)
            return null;
        return new TreeNode(Integer.valueOf(poll));
    }

    public TreeNode preUnserialize(Queue<String> list) {
        String value = list.poll();
        if (value == null)
            return null;
        TreeNode root = new TreeNode(Integer.valueOf(value));
        root.left = preUnserialize(list);
        root.right = preUnserialize(list);
        return root;
    }

    public static void main(String[] args) {
        BinaryTreeTraverse tree = new BinaryTreeTraverse();
        TreeNode n4 = new TreeNode(4, null, null);
        TreeNode n3 = new TreeNode(3, null, n4);
        TreeNode n2 = new TreeNode(2, n3, null);
        TreeNode n8 = new TreeNode(8, null, null);
        TreeNode n9 = new TreeNode(9, null, null);
        TreeNode n6 = new TreeNode(6, n8, null);
        TreeNode n7 = new TreeNode(7, n9, null);
        TreeNode n5 = new TreeNode(5, n6, n7);
        TreeNode root = new TreeNode(1, n2, n5);

        tree.preOrder(root);
        System.out.println();
        tree.preOrderUnderStack(root);

        tree.inOrder(root);
        System.out.println();
        tree.inOrderUnderStack(root);

        tree.posOrder(root);
        System.out.println();
        tree.posOrderUnderStack(root);
        tree.posOrderUnderStack2(root);

        tree.levelOrder(root);

        System.out.println(tree.maxWidthUnderMap(root));
    }
}
