package Tree;

import java.util.ArrayList;

// 一些基于非传统二叉树结点的题目
public class SpecialBinaryTree {

    // 1.该二叉树结点多了一个parent域用于指向树结构中的父结点。给定该二叉树中任意一个结点，返回其中序遍历下的后继结点,
    // 若无后继返回null
    // 思路：判断当前结点是否有右子树：1> 若有，则返回该右子树中最左的结点；2> 若无，则向上追溯父结点，直到回溯到某个结点后，
    //      发现该结点是其父的左孩子，，那么返回该结点的父结点，若一直向上回溯却没能找到一个结点是其父结点的左孩子，则该结点
    //      一定是整棵树最右的结点，返回null
    public static Node getInOrderSuccessor(Node cur) {
        if (cur == null)
            return null;
        if (cur.right != null)
            return getMostLeft(cur.right);
        else {
            while (cur.parent != null && cur != cur.parent.left)
                cur = cur.parent;
            return cur.parent;
        }
    }

    private static Node getMostLeft(Node node) {
        if (node == null)
            return node;
        while (node.left != null)
            node = node.left;
        return node;
    }

    // 问题1的暴力解法：先根据该结点的parent不断向上回溯找到root，直接中序遍历，找到该结点的后继
    public static Node getInOrderSuccessor2(Node node){
        if (node == null)
            return null;
        Node root = node;
        while (root.parent != null)
            root = root.parent;
        ArrayList<Node> nodes = new ArrayList<>();
        inOrder(root, nodes);
        return nodes.indexOf(node) == nodes.size() - 1 ?  null : nodes.get(nodes.indexOf(node) + 1);
    }

    private static void inOrder(Node root, ArrayList<Node> nodes) {
        if (root == null)
            return;
        inOrder(root.left, nodes);
        nodes.add(root);
        inOrder(root.right, nodes);
    }


    // 2.有了问题1的铺垫第2题就很好解决。第2题要求用同样的Node结点，返回其中序遍历的前驱结点。
    //   思路：如果当前结点有左子树，则返回该左子树中最右的结点；如果当前结点无左子树，则向上回溯，如果回溯时发现某个结点是其父结点的
    //        右孩子，则返回该结点的父结点，如果一直回溯却没有发现一个结点是其父结点的右孩子，则无前驱。
    public static Node getInOrderPredecessor(Node node){
        if (node == null)
            return null;
        if (node.left != null)
            return getMostRight(node.left);
        else {
            Node parent = node.parent;
            while (parent != null && node != parent.right){
                node = parent;
                parent = node.parent;
            }
            return parent;
        }
    }

    private static Node getMostRight(Node cur) {
        if (cur == null)
            return null;
        while (cur.right != null)
            cur = cur.right;
        return cur;
    }


    public static void main(String[] args) {
        Node n3 = new Node(5);
        Node n1 = new Node(6, n3, null, null);
        Node n4 = new Node(7);
        Node n2 = new Node(3, n1, n4, null);
        Node n5 = new Node(12);
        Node n6 = new Node(9, n5, null, null);
        Node root = new Node(10, n2, n6, null);
        n3.parent = n1;
        n1.parent = n2;
        n4.parent = n2;
        n5.parent = n6;
        n2.parent = root;
        n6.parent = root;
        Node node = getInOrderSuccessor(root);
        Node node1 = getInOrderSuccessor2(root);
        System.out.println(node == null ? node : node.val);
        System.out.println(node1 == null ? node1 : node1.val);

        Node predecessor = getInOrderPredecessor(n6);
        System.out.println(predecessor == null ? predecessor : predecessor.val);
    }
}

class Node{
    public int val;
    public Node left;
    public Node right;
    public Node parent;

    public Node(int val) {
        this.val = val;
    }

    public Node(int val, Node left, Node right, Node parent) {
        this.val = val;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }
}
