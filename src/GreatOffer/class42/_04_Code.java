package GreatOffer.class42;

import utils.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

// leetCode272
// 给你一棵二叉排序树的根结点root，该树有N个结点，给你一个目标值t，和一个数量k，要求返回树上
// 最接近t的k个结点。
// 要求时间复杂度低于O(N)

public class _04_Code {

    // 二叉排序树中是没有重复值的
    // 首先先要明白几个点：二叉排序树的某个节点cur的后继
    //  1.如果cur有右子树，则cur的后继是其右子树的最左结点
    //  2.如果cur没有右子树，则cur的后继就是一直要从cur往上找到某个结点a，a是其父结点的左孩子，则cur的后继就是a的父
    // 假设t==8，k==5，算法的总体流程是：
    // 先在bst上找到<=8且最大的结点x，再在bst上找到>=8且最小的结点y   如果x==y，就说明bst中存在8
    // 如果x==y，那就让x找一下前驱，这样x和y就能错开了，并且x是<8的最大值，y是>=8的最小值
    // 我们的目的是从这两边找出最接近t的k个元素。如果x一开始就不等于y，就不需要让x往前移动了
    // x此时已经来到了正确位置，然后比较 t-x 和 y-t 的大小，如果t-x比较小，说明x比现在的y更接近t，所以收集答案x，
    // 并让x往前移动；如果y-t比较小，就收集答案y并让y往后移动。直到收集到k个答案。
    // 在收集过程中，如果x或者y有一个已经到头了，那就直接从另一个开始继续收集

    // 所以，我们需要一种结构，能快速让y找到其后继，让x找到其前驱  因为题目要求的时间复杂度不能超过O(N)

    // 主方法
    public static List<Integer> closedKValues(TreeNode root, double t, int k) {
        List<Integer> res = new LinkedList<>();
        Stack<TreeNode> more = new Stack<>(); // 方便y找后继的结构
        Stack<TreeNode> less = new Stack<>(); // 方便x找前驱的结构
        getPost(more, t, root);
        getPre(less, t, root);
        // 如果找到的x==y，那么就让x往前驱跳一步
        if (!more.isEmpty() && !less.isEmpty() && more.peek().val == less.peek().val)
            getPredecessor(less);
        // 还没凑够
        while (k-- > 0) {
            if (more.isEmpty())
                res.add(getPredecessor(less));
            else if (less.isEmpty())
                res.add(getSuccessor(more));
            else {  // 如果两个都不为空，那就比大小
                double diffU = Math.abs((more.peek().val - t));
                double diffD = Math.abs((t - less.peek().val));
                if (diffD < diffU)
                    res.add(getPredecessor(less));
                else
                    res.add(getSuccessor(more));
            }
        }
        return res;
    }


    // 在root为头的树上 找到>=t且最小的结点
    // 并且找的过程中，只要某个节点c往左走了，就把c放入more里，因为c为啥要往左走，就说明c>t
    // 这是将y及其后继都压栈，以后只需要从栈中弹出元素就能让y到下一个后继
    // 比如bst是这样的：
    //                 12
    //                /
    //               6                       getPost方法遵循，如果当前值 > t 直接压栈，然后滑向左孩子
    //                \                      当前值 < t 不压栈  直接滑向右孩子   如果当前值==t 则压栈后
    //                10                     直接停止算法。如果给的是左边的树，运行一次算法后，栈里是这样的：
    //                /                      12  10  8
    //               7                       其实这个算法并没有将所有>=t的值全部由大到小压栈，所以这个栈只是
    //                \                      局部的，后面的方法会不断调整栈，getPost方法做的栈只是目前够用了。
    //                 8                     8的后继就是10，10的后继就是12。
    //                  \
    //                   9
    //                  /
    //                 8.5
    private static void getPost(Stack<TreeNode> more, double t, TreeNode root) {
        while (root != null) {
            if (root.val == t) {
                more.push(root);
                break;
            } else if (root.val > t) {
                more.push(root);
                root = root.left;
            } else
                root = root.right;
        }

    }


    // 在root为头的树上找到<=t，且最接近t的结点
    // 并且找的过程中，只要某个节点c往右走了，就把c放入less里
    private static void getPre(Stack<TreeNode> less, double t, TreeNode root) {
        while (root != null) {
            if (root.val == t) {
                less.push(root);
                break;
            } else if (root.val < t) {
                less.push(root);
                root = root.right;
            } else
                root = root.left;
        }
    }


    // 返回more的头部的值
    // 并且调整more : 为了以后能很快的找到返回结点的后继节点
    // 这个就是补充getPost方法的方法，只要栈里弹出一个元素，在返回前就会对栈修改好再弹出
    // 像刚才的树，栈里一开始是压栈 12  10  8   当8被弹出后，栈里就变成了 12  10  9  8.5
    // 每个元素被弹出后就会将其右子树的一整条左边界加入栈中。
    //
    // 你想想看，getPost方法填入栈中的元素是每一个即将要向左划的元素被加入栈的，比如当前结点c>t那么接下来就会
    // 滑向c的左孩子，而c的右子树全部被忽略了；如果c的左孩子>t那么在栈里c和c的左孩子就是紧邻的，但其实如果c有
    // 右孩子，那c的右子树上的左边界就是可以夹在c和c的左孩子之间的。所以在栈里每弹出一个元素时，就要检查其是否有右孩子，
    // 如果有就要把其右子树的左边界加入，因为这些元素刚好比被弹出的元素大，比被弹出元素在栈中的下一个元素小。
    private static int getSuccessor(Stack<TreeNode> more) {
        TreeNode pop = more.pop();
        int ret = pop.val;
        pop = pop.right;
        while (pop != null) {
            more.push(pop);
            pop = pop.left;
        }
        return ret;
    }


    // 返回less头部的值
    // 并且调整less : 为了以后能很快的找到返回节点的前驱节点
    private static int getPredecessor(Stack<TreeNode> less) {
        TreeNode pop = less.pop();
        int ret = pop.val;
        pop = pop.left;
        while (pop != null) {
            less.push(pop);
            pop = pop.right;
        }
        return ret;
    }

}
