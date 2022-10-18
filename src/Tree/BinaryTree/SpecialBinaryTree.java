package Tree.BinaryTree;

import Tree.BiDirectNode;

import java.util.ArrayList;

// 一些基于非传统二叉树结点的题目
public class SpecialBinaryTree {

    // 1.该二叉树结点多了一个parent域用于指向树结构中的父结点。给定该二叉树中任意一个结点，返回其中序遍历下的后继结点,
    // 若无后继返回null
    // 思路：判断当前结点是否有右子树：1> 若有，则返回该右子树中最左的结点；2> 若无，则向上追溯父结点，直到回溯到某个结点后，
    //      发现该结点是其父的左孩子，，那么返回该结点的父结点，若一直向上回溯却没能找到一个结点是其父结点的左孩子，则该结点
    //      一定是整棵树最右的结点，返回null
    public static BiDirectNode getInOrderSuccessor(BiDirectNode cur) {
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

    private static BiDirectNode getMostLeft(BiDirectNode node) {
        if (node == null)
            return node;
        while (node.left != null)
            node = node.left;
        return node;
    }

    // 问题1的暴力解法：先根据该结点的parent不断向上回溯找到root，直接中序遍历，找到该结点的后继
    public static BiDirectNode getInOrderSuccessor2(BiDirectNode node){
        if (node == null)
            return null;
        BiDirectNode root = node;
        while (root.parent != null)
            root = root.parent;
        ArrayList<BiDirectNode> nodes = new ArrayList<>();
        inOrder(root, nodes);
        return nodes.indexOf(node) == nodes.size() - 1 ?  null : nodes.get(nodes.indexOf(node) + 1);
    }

    private static void inOrder(BiDirectNode root, ArrayList<BiDirectNode> nodes) {
        if (root == null)
            return;
        inOrder(root.left, nodes);
        nodes.add(root);
        inOrder(root.right, nodes);
    }


    // 2.有了问题1的铺垫第2题就很好解决。第2题要求用同样的Node结点，返回其中序遍历的前驱结点。
    //   思路：如果当前结点有左子树，则返回该左子树中最右的结点；如果当前结点无左子树，则向上回溯，如果回溯时发现某个结点是其父结点的
    //        右孩子，则返回该结点的父结点，如果一直回溯却没有发现一个结点是其父结点的右孩子，则无前驱。
    public static BiDirectNode getInOrderPredecessor(BiDirectNode node){
        if (node == null)
            return null;
        if (node.left != null)
            return getMostRight(node.left);
        else {
            BiDirectNode parent = node.parent;
            while (parent != null && node != parent.right){
                node = parent;
                parent = node.parent;
            }
            return parent;
        }
    }

    private static BiDirectNode getMostRight(BiDirectNode cur) {
        if (cur == null)
            return null;
        while (cur.right != null)
            cur = cur.right;
        return cur;
    }


    public static void main(String[] args) {
        BiDirectNode n3 = new BiDirectNode(5);
        BiDirectNode n1 = new BiDirectNode(6, n3, null, null);
        BiDirectNode n4 = new BiDirectNode(7);
        BiDirectNode n2 = new BiDirectNode(3, n1, n4, null);
        BiDirectNode n5 = new BiDirectNode(12);
        BiDirectNode n6 = new BiDirectNode(9, n5, null, null);
        BiDirectNode root = new BiDirectNode(10, n2, n6, null);
        n3.parent = n1;
        n1.parent = n2;
        n4.parent = n2;
        n5.parent = n6;
        n2.parent = root;
        n6.parent = root;
        BiDirectNode node = getInOrderSuccessor(n1);
        BiDirectNode node1 = getInOrderSuccessor2(n1);
        System.out.println(node == null ? node : node.val);
        System.out.println(node1 == null ? node1 : node1.val);
    }
}

