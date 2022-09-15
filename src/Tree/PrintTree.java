package Tree;

public class PrintTree {
    // 打印一棵二叉树：将二叉树的结构逆时针旋转90度在终端打印出
    public static void printTree(TreeNode root){
        printReverseInOrder(root, 0, 15);
    }

    private static void printReverseInOrder(TreeNode root, int height, int len) {
        if (root == null)
            return;
        printReverseInOrder(root.right, height + 1, len);

        String value = String.valueOf(root.val);
        int lenM = value.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        value = getBlankSpace(lenL) + value + getBlankSpace(lenR);
        System.out.println(getBlankSpace(height * len) + value);

        printReverseInOrder(root.left, height + 1, len);
    }

    private static String getBlankSpace(int len) {
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < len; i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }


    public static void main(String[] args) {
        TreeNode n4 = new TreeNode(4, null, null);
        TreeNode n3 = new TreeNode(3, null, null);
        TreeNode n2 = new TreeNode(2, n3, n4);
        TreeNode n6 = new TreeNode(6, null, null);
        TreeNode n7 = new TreeNode(7, null, null);
        TreeNode n5 = new TreeNode(5, n6, n7);
        TreeNode root = new TreeNode(1, n2, n5);
        PrintTree.printTree(root);
    }
}
