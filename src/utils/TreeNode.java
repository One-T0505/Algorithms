package utils;

/**
 * ymy
 * 2023/3/15 - 11 : 13
 **/

public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {}
    public TreeNode(int v) {
        val = v;
    }

    public TreeNode(int v, TreeNode l, TreeNode r) {
        val = v;
        left = l;
        right = r;
    }
}
