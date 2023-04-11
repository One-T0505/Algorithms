package GreatOffer.TopHotQ;

import utils.TreeNode;

// 小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为 root 。
// 除了 root 之外，每栋房子有且只有一个“父“房子与之相连。一番侦察之后，聪明的小偷意识到
// “这个地方的所有房屋的排列类似于一棵二叉树”。 如果两个直接相连的房子在同一天晚上被打劫 ，房屋将自动报警。
// 给定二叉树的 root 。返回在不触动警报的情况下 ，小偷能够盗取的最高金额 。

public class _0337_HouseRobberIII {

    // 问题原型是员工最大快乐值问题
    public int rob(TreeNode root) {
        Info info = f(root);
        return Math.max(info.no, info.yes);
    }

    private Info f(TreeNode x) {
        if (x == null)
            return new Info(0, 0);
        Info left = f(x.left);
        Info right = f(x.right);

        int no = Math.max(left.no, left.yes) + Math.max(right.no, right.yes);
        int yes = x.val + left.no + right.no;

        return new Info(yes, no);
    }


    public static class Info {
        public int yes; // 偷当前这个房子能获得最大多少财富
        public int no; // 不偷当前这个房子能获得最大多少财富

        public Info(int yes, int no) {
            this.yes = yes;
            this.no = no;
        }
    }


}
