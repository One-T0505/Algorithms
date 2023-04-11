package class03;

// leetCode863
// 给定三个参数：二叉树的头节点head；树上某个节点src；正数k；
// 从src开始，可以向上走或者向下走，返回与src的距离是k的所有节点。

import utils.TreeNode;

import java.util.*;

public class _01_DistanceNodes {
    // 为什么要给头结点呢？如果不给头结点就没有全局视野，怎么往上走.
    public static List<Integer> distanceNodes(TreeNode root, TreeNode target, int k) {
        // 首先要建立一个表，记录每个结点的父结点
        HashMap<TreeNode, TreeNode> parents = new HashMap<>();
        parents.put(root, null);
        build(root, parents);
        // 避免重复进入
        HashSet<TreeNode> set = new HashSet<>();
        set.add(target);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(target);
        ArrayList<Integer> res = new ArrayList<>();
        int curDistance = 0;
        // 这里用到了一个技巧：如何处理同一批高度的结点。在BFS中，如何使队列中的结点都是同一层次的。可以用size这个属性，
        // 最开始队列只有根结点，size=1，那么就把要做的操作做size遍，弹出size遍后，队列中当前层的结点都走完了，都是下一层结点。
        // 这样可以方便地控制队列中保持的结点都是同一层次的。
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                TreeNode cur = queue.poll();
                if (curDistance == k)
                    res.add(cur.val);
                if (cur.left != null && !set.contains(cur.left)) {
                    queue.add(cur.left);
                    set.add(cur.left);
                }
                if (cur.right != null && !set.contains(cur.right)) {
                    queue.add(cur.right);
                    set.add(cur.right);
                }
                if (parents.get(cur) != null && !set.contains(parents.get(cur))) {
                    queue.add(parents.get(cur));
                    set.add(parents.get(cur));
                }
            }
            curDistance++;
            if (curDistance > k)
                break;
        }
        return res;
    }

    private static void build(TreeNode cur, HashMap<TreeNode, TreeNode> parents) {
        if (cur == null)
            return;
        if (cur.left != null) {
            parents.put(cur.left, cur);
            build(cur.left, parents);
        }
        if (cur.right != null) {
            parents.put(cur.right, cur);
            build(cur.right, parents);
        }
    }
    // -----------------------------------------------------------------------------------------------


    // 尝试使用DFS来做
    public static List<Integer> distanceNodes2(TreeNode root, TreeNode target, int k) {
        // 首先要建立一个表，记录每个结点的父结点
        HashMap<TreeNode, TreeNode> parents = new HashMap<>();
        parents.put(root, null);
        build(root, parents);
        // 避免重复进入
        HashSet<TreeNode> set = new HashSet<>();
        set.add(target);
        ArrayList<Integer> res = new ArrayList<>();
        dfs(target, 0, parents, set, res, k);
        return res;
    }

    private static void dfs(TreeNode cur, int dis, HashMap<TreeNode, TreeNode> parents,
                            HashSet<TreeNode> set, ArrayList<Integer> res, int k) {
        if (dis == k) {
            res.add(cur.val);
            return;
        }
        if (cur.left != null && !set.contains(cur.left)) {
            set.add(cur.left);
            dfs(cur.left, dis + 1, parents, set, res, k);
            set.remove(cur.left);
        }
        if (cur.right != null && !set.contains(cur.right)) {
            set.add(cur.right);
            dfs(cur.right, dis + 1, parents, set, res, k);
            set.remove(cur.right);
        }
        TreeNode father = parents.get(cur);
        if (father != null && !set.contains(father)) {
            set.add(father);
            dfs(father, dis + 1, parents, set, res, k);
            set.remove(father);
        }
    }
}
