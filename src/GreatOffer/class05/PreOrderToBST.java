package class05;


import utils.TreeNode;

import java.util.Stack;

// leetCode1008
// 给定一个数组，该数组的顺序是先序遍历一棵BST的结果，现在要求将其还原成BST，并返回根结点。该数组无重复值。

public class PreOrderToBST {

    // 在数组中第一个元素毫无疑问就是BST的根结点，其在数组中，后面离他最近且比他小的元素就是在树中他的左子树结点；
    // 数组中离他最近且比他大的元素就是树中的右子树结点。听到这样的描述是不是很自然想到了单调栈！！！！

    public static TreeNode generateBST(int[] arr) {
        if (arr == null || arr.length == 0)
            return null;
        int N = arr.length;
        // greater[i] 表示右侧比arr[i]大的最近元素的位置
        // 如果greater某个元素值为-1，就表示不存在
        int[] greater = new int[N];
        // 栈里存放的都是数组下标
        Stack<Integer> stack = new Stack<>();
        // 找每个元素右侧离自己最近且比自己大的元素的位置
        for (int i = 0; i < N; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] < arr[i])
                greater[stack.pop()] = i;
            stack.push(i);
        }
        while (!stack.isEmpty())
            greater[stack.pop()] = -1;

        return process(arr, 0, N - 1, greater);
    }

    // 在[L,R]上建立建立一棵树，并返回根结点
    private static TreeNode process(int[] arr, int L, int R, int[] greater) {
        if (L > R)
            return null;
        TreeNode root = new TreeNode(arr[L]);
        root.left = process(arr, L + 1, greater[L] == -1 ? R : greater[L] - 1, greater);
        root.right = greater[L] == -1 ? null : process(arr, greater[L], R, greater);
        return root;
    }
    // ==============================================================================================


    // 如果在上面的方法上继续优化，那就只能优化常数时间。可以用数组替换系统栈。
    public static TreeNode generateBSTV2(int[] arr) {
        if (arr == null || arr.length == 0)
            return null;
        int N = arr.length;
        // greater[i] 表示右侧比arr[i]大的最近元素的位置
        // 如果greater某个元素值为-1，就表示不存在
        int[] greater = new int[N];
        // 栈里存放的都是数组下标
        int[] stack = new int[N];
        int top = -1;
        // 找每个元素右侧离自己最近且比自己大的元素的位置
        for (int i = 0; i < N; i++) {
            while (top != -1 && arr[stack[top]] < arr[i])
                greater[stack[top--]] = i;
            stack[++top] = i;
        }
        while (top != -1)
            greater[stack[top--]] = -1;

        return process(arr, 0, N - 1, greater);
    }


    public static void main(String[] args) {
        int[] arr = {9, 7, 2, 4, 8, 14, 12, 11, 13};
        TreeNode root = generateBST(arr);
    }

}
