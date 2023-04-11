package Basic.Tree.BinaryTree;

import utils.TreeNode;

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

    // eg：
    //                  a                            morris遍历序列是：a b d b e a c f c g
    //                /   \                          可以发现，有左子树的结点都会到达2次。
    //               b     c                      如果在第一次到达每个节点就打印就能得到先序：a b d e c f g
    //             /  \   /  \                    如果在第二次到达每个节点就打印就能得到中序：d b e a f c g
    //            d    e f    g
    //
    // 用morris遍历序列处理成后序遍历序列需要额外处理，因为morris最多到达一个结点两次，而后序遍历是在第三次回到结点时输出。
    // 如果要用morris遍历序列加工出后序遍历序列需要这样做：在每一个有左子树的结点处，逆序打印其左子树的右边界，最后再逆序
    // 打印整棵树的右边界

    public static void morris(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            if (cur.left != null) {  // 若cur有左子树
                TreeNode mostRight = cur.left;
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                    cur = cur.right;
                }
            } else // cur没有左子树
                cur = cur.right;
        }
    }

    public static void morrisPreOrder(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            if (cur.left != null) {  // 若cur有左子树
                TreeNode mostRight = cur.left;
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    System.out.print(cur.val + "   ");
                    cur = cur.left;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                    cur = cur.right;
                }
            } else {// cur没有左子树
                System.out.print(cur.val + "   ");
                cur = cur.right;
            }
        }
    }

    public static void morrisInOrder(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            if (cur.left != null) {  // 若cur有左子树
                TreeNode mostRight = cur.left;
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                    System.out.print(cur.val + "   ");
                    cur = cur.right;
                }
            } else {// cur没有左子树
                System.out.print(cur.val + "   ");
                cur = cur.right;
            }
        }
    }

    public static void morrisPosOrder(TreeNode root){
        if (root == null)
            return;
        TreeNode cur = root;
        while (cur != null){
            if (cur.left != null) {  // 若cur有左子树
                TreeNode mostRight = cur.left;
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                    // 要实现morris序下的后序遍历，需要每次逆序打印有左子树的结点的左子树的右边界。这里不可以用栈，
                    // 否则空间复杂度就不是O(1)
                    printEdge(cur.left);
                    cur = cur.right;
                }
            } else {// cur没有左子树
                cur = cur.right;
            }
        }
        // 最后还要将整棵树的右边界逆序打印一下
        printEdge(root);
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
    // ==================================================================================================



    // 利用morris遍历序判断某棵树是否为二叉排序树
    public static boolean isBST(TreeNode root){
        if (root == null)
            return true;
        boolean res = true;
        int pre = Integer.MIN_VALUE;  // 记录前一个的值
        TreeNode cur = root;
        while (cur != null){
            if (cur.left != null) {  // 若cur有左子树
                TreeNode mostRight = cur.left;
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){
                    mostRight.right = cur;
                    pre = cur.val;   // if语句中了说明cur结点是第一次来到，此时把结点的值记录下来
                    cur = cur.left;
                } else {  // mostRight.right == cur
                    mostRight.right = null;
                    // 这里如果中了if为什么不能直接return false？ 很关键：因为morris在遍历过程中会修改树的结构
                    // 如果直接return了，可能树的结构已经和之前不一样了，所以不能中途返回，必须等morris遍历全部结束后
                    // 再返回想要的结果
                    if (pre != Integer.MIN_VALUE && pre >= cur.val)
                        res = false;
                    cur = cur.right;
                }
            } else {// cur没有左子树  没有左子树的结点只会来到一次，所以每次来到的时候就要记下
                pre = cur.val;
                cur = cur.right;
            }
        }
        return res;
    }
    // ==================================================================================================




    // 树的最小高度：所有叶结点中距离根最近的高度。先用最暴力的版本
    // 思路： 以x为根结点的树，若有左子树，求出左子树的最小高度；若有右子树，求出右子树的最小高度；
    // 整棵树的最小高度 = Math.min(左子树的最小高度, 右子树的最小高度)+1;

    public static int minHeightV1(TreeNode root){
        if (root == null)
            return 0;
        return process(root);
    }

    // 该方法返回以root为根结点的树的最小高度
    private static int process(TreeNode root){
        if (root.left == null && root.right == null)
            return 1;
        int leftHeight = Integer.MAX_VALUE;
        if (root.left != null)
            leftHeight = process(root.left);
        int rightHeight = Integer.MAX_VALUE;
        if (root.right != null)
            rightHeight = process(root.right);
        return Math.min(leftHeight, rightHeight) + 1;
    }


    // 用morris遍历求一棵树的最小高度.这里有两个难点需要解决：
    //  1.随便来到一个结点x，如何判断x的高度是多少？因为morris遍历时很可能会从 某个结点一下又回到之前的结点去。
    //    所以我们需要一个变量pre来记录当前结点x的上一个访问到的结点，如果 pre == x左子树的最右结点 就说明x是回溯了
    //    有了pre我们就可以分辨当前结点的高度是之前的高度+1还是说要减少。
    //
    //  2.如何判断哪些结点是叶结点？ 正常来说左右孩子都为null的结点就是叶结点，但是在morris遍历中会改变右指针的指向，
    //    会使得本来是叶结点的结点变成不是叶结点。
    //    所以，在morris遍历时，对于那些能回到自己两次的结点，在第二次回到自己时，自己子树中的结点肯定是修改好了指针指向，
    //    再判断自己左子树的最右结点是不是叶结点。 最后还要单独判断一下整棵树的最右结点是不是叶结点。因为morris遍历无法
    //    关注到整棵树的最右结点。
    //
    // 解决了这两个问题，只需要把所有叶子结点的高度取最小值即可。
    public static int minHeightV2(TreeNode root){
        if (root == null)
            return 0;
        TreeNode cur = root;
        int res = Integer.MAX_VALUE;
        int height = 0;
        while (cur != null){
            if (cur.left != null) {  // 若cur有左子树
                TreeNode mostRight = cur.left;
                int rightBound = 1;   // 记录当前结点左子树整个右边界有多少高度，最后回溯时方便算高度
                // 找到cur的左子树上真实的最右结点
                while (mostRight.right != null && mostRight.right != cur){
                    rightBound++;
                    mostRight = mostRight.right;
                }
                // 从while出来后，mostRight一定是cur左子树的最右结点
                if (mostRight.right == null){  // 说明是第一次到达cur
                    mostRight.right = cur;
                    cur = cur.left;
                    height++;  // height就可以正常加
                } else {  // mostRight.right == cur 说明是第二次到达cur
                    // 第二次到达cur时，mostRight的右指针已修改成成null，如果左孩子也是null，说明mostRight是叶子
                    if (mostRight.left == null)
                        res = Math.min(res, height);
                    mostRight.right = null;
                    cur = cur.right;
                    height -= rightBound;  // 高度回溯
                }
            } else {  // cur没有左子树
                cur = cur.right;
                height++;
            }
        }
        // 单独处理整棵树的最右结点
        int finalRight = 1;
        cur = root;
        while (cur.right != null) {
            finalRight++;
            cur = cur.right;
        }
        // 此时 cur.right == null
        if (cur.left == null) {
            res = Math.min(res, finalRight);
        }
        return res;
    }
    // ==================================================================================================




    // 什么时候用传统的遍历，什么时候用morris遍历？ 当来到某个结点x时如果你需要左子树给你想要的信息，也需要右子树给你想要的
    // 信息后，才能做出决策，那么这种时候只能用传统遍历，因为这要求能三次回到某个节点，而morris遍历只能回到一个结点
    // 最多两次。

    public static void main(String[] args) {
        TreeNode n2 = new TreeNode(2);
        TreeNode n4 = new TreeNode(4);
        TreeNode n7 = new TreeNode(7);
        TreeNode n5 = new TreeNode(3, n2, n4);
        TreeNode n8 = new TreeNode(8, n7, null);
        TreeNode root = new TreeNode(6, n5, n8);
        morrisPreOrder(root);
        System.out.println();
        morrisInOrder(root);
        System.out.println();
        morrisPosOrder(root);
        System.out.println();
        System.out.println(isBST(root));
        System.out.println(minHeightV1(root));
        System.out.println(minHeightV2(root));
    }

}
