package Tree;

// 二叉树的Mirrors遍历
// 二叉树的递归遍历：时间复杂度为O(N) 空间复杂度为O(logN)  而Mirrors遍历可以做到：时间复杂度O(N)  空间复杂度为O(1)
// morris序是独立于前序、中序、后序的一种遍历序列，由morris序列可以处理成前序、中序、后序
// 利用morris序能很方便地处理一些问题
public class Morris {
    // 来到一棵树的某个结点（一开始是根结点），记做 cur
    // 1. cur没有左子树， cur = cur.right;
    // 2. cur有左子树，找到左子树上的最右结点 mostRight
    //    1> 如果 mostRight 的右指针指向 null，mostRight.right = cur; cur = cur.left;
    //    2> 如果 mostRight 的右指针指向 cur， mostRight.right = null; cur = cur.right;

    public static void morris(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            TreeNode mostRight = cur.left;
            if (mostRight != null) {  // 若cur有左子树
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                }
            }
            // 执行该行代码有两种情况：1.若cur一开始就没有左子树；2.从else字句执行之后
            cur = cur.right;
        }
    }

    public static void morrisPreOrder(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            TreeNode mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                if (mostRight.right == null){
                    mostRight.right = cur;
                    System.out.print(cur.val + "\t"); // 新加的代码
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            } else  // 新加的代码
                System.out.print(cur.val + "\t");
            cur = cur.right;
        }
    }

    public static void morrisInOrder(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            TreeNode mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            System.out.print(cur.val + "\t"); // 新加的代码
            cur = cur.right;
        }
    }

    public static void morrisPosOrder(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            TreeNode mostRight = cur.left;
            if (mostRight != null) {  // 若cur有左子树
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                    // 要实现morris序下的后序遍历，需要每次逆序打印cur结点左子树的右边界。这里不可以用栈，
                    // 否则空间复杂度就不是O(1)
                    printEdge(cur.left);
                }
            }
            // 执行该行代码有两种情况：1.若cur一开始就没有左子树；2.从else字句执行之后
            cur = cur.right;
        }
        printEdge(root); // 最后还要将整棵树的右边界逆序打印一下
    }

    // 先将右边界的一条链逆序，打印完之后再恢复。就是单链表逆序的原理
    private static void printEdge(TreeNode root) {
        TreeNode head = reverseEdge(root);
        TreeNode cur = head;
        while (cur != null){
            System.out.print(cur.val + "\t");
            cur = cur.right;
        }
        reverseEdge(head);  // 再还原
    }


    // 返回链表逆转后新的头结点，该头结点在逆序之前是尾结点
    private static TreeNode reverseEdge(TreeNode root) {
        TreeNode pre = null, next = null;
        while (root != null){
            next = root.right;
            root.right = pre;
            pre = root;
            root = next;
        }
        return pre;
    }

    // 利用morris遍历序判断某棵树是否为二叉排序树
    public static boolean isBST(TreeNode root){
        if (root == null)
            return true;
        TreeNode cur = root;
        Integer pre = null;
        while (cur != null){
            TreeNode mostRight = cur.left;
            if (mostRight != null) {  // 若cur有左子树
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                }
            }
            // 只需要在中序遍历逻辑下，判断当前值是否比前驱大，若不成立则不是二叉搜索树
            if (pre != null && pre >= cur.val)
                return false;
            pre = cur.val;
            cur = cur.right;
        }
        return true;
    }

    // 树的最小高度：所有叶结点中距离根最近的高度。先用最暴力的版本
    // 思路： 以x为根结点的树，若有左子树，求出左子树的最小高度；若有右子树，求出右子树的最小高度；
    // 整棵树的最小高度 = Math.min(左子树的最小高度, 右子树的最小高度)+1;


    // 该算法目前还有问题
    public static int minLength(TreeNode root){
        if (root == null)
            return 0;
        return process(root);
    }
    private static int process(TreeNode root){
        if (root.left == null && root.right == null)
            return 1;
        int leftLength = Integer.MAX_VALUE;
        if (root.left != null)
            leftLength = process(root.left);
        int rightLength = Integer.MAX_VALUE;
        if (root.right != null)
            rightLength = process(root.right);
        return Math.min(leftLength, rightLength) + 1;
    }

    public static void main(String[] args) {
        TreeNode n2 = new TreeNode(2);
        TreeNode n4 = new TreeNode(4);
        TreeNode n7 = new TreeNode(7);
        TreeNode n5 = new TreeNode(5, n2, n4);
        TreeNode n8 = new TreeNode(8, n7, null);
        TreeNode root = new TreeNode(3, n5, n8);
        morrisPreOrder(root);
        System.out.println();
        morrisInOrder(root);
        System.out.println();
        morrisPosOrder(root);
        System.out.println();
        System.out.println(isBST(root));
        System.out.println(minLength(root));
    }

}
