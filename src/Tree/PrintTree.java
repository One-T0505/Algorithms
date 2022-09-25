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
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(Math.max(0, len)));
        return sb.toString();
    }

    public static void printT(TreeNode root){
        if (root == null)
            return;
        printIn(root, "", 1, 9);
    }

    private static void printIn(TreeNode cur, String direct, int height, int len) {
        if (cur == null)
            return;
        printIn(cur.right, "↙", height + 1, len);

        String res = blankSpace(direct + cur.val, height, len);
        System.out.println(res);

        printIn(cur.left, "↖", height + 1, len);
    }

    private static String blankSpace(String val, int height, int len) {
        int valLen = val.length();
        int left = (len - valLen) >> 1;
        int right = len - valLen - left;
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(left + (height - 1) * len)).append(val).append(" ".repeat(right));
        return builder.toString();
    }


    public static void main(String[] args) {
        TreeNode n4 = new TreeNode(4, null, null);
        TreeNode n3 = new TreeNode(3, null, null);
        TreeNode n2 = new TreeNode(2, n3, n4);
        TreeNode n6 = new TreeNode(6, null, null);
        TreeNode n7 = new TreeNode(7, null, null);
        TreeNode n5 = new TreeNode(5, n6, n7);
        TreeNode root = new TreeNode(1, n2, n5);
        PrintTree.printT(root);
    }
}
