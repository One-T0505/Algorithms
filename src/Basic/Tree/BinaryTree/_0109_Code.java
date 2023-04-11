package Basic.Tree.BinaryTree;


// 给定一个单链表的头节点 head ，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
// 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差不超过 1。

import utils.SingleNode;
import utils.TreeNode;

import java.util.ArrayList;

public class _0109_Code {

    // 常规做法，将链表的值先存储在数组中，然后二分+递归建树
    public TreeNode sortedListToBST(SingleNode head) {
        if (head == null)
            return null;
        if (head.next == null)
            return new TreeNode(head.val);
        ArrayList<Integer> list = new ArrayList<>();
        while (head != null){
            list.add(head.val);
            head = head.next;
        }
        return f(list, 0, list.size() - 1);
    }


    // L~mid-1作为左子树  R～结尾作为右子树  mid作为根
    private TreeNode f(ArrayList<Integer> list, int L, int R) {
        if (L > R)
            return null;
        if (L == R)
            return new TreeNode(list.get(L));
        int mid = L + ((R - L) >> 1);
        TreeNode root = new TreeNode(list.get(mid));
        root.left = f(list, L, mid - 1);
        root.right = f(list, mid + 1, R);
        return root;
    }
    // =====================================================================================



    // 最优解，就在链表上玩递归建树
    public TreeNode sortedListToBST2(SingleNode head) {
        return g(head, null);
    }

    private TreeNode g(SingleNode L, SingleNode R) {
        if (L == R)
            return null;
        SingleNode mid = midOrLatter(L, R);
        TreeNode root = new TreeNode(mid.val);
        root.left = g(L, mid);
        root.right = g(mid.next, R);
        return root;
    }


    private SingleNode midOrLatter(SingleNode head, SingleNode tail) {
        if (head == tail || head.next == tail)
            return head;
        SingleNode slow = head, fast = head;
        while (fast.next != tail && fast.next.next != tail){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
