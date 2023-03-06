package Tree.BinaryTree;

// 给你一棵二叉树的根节点 root ，返回树的最大宽度。树的最大宽度是所有层中最大的宽度 。
// 每一层的宽度被定义为该层最左和最右的非空节点（即，两个端点）之间的长度。将这个二叉树视作
// 与满二叉树结构相同，两端点间会出现一些延伸到这一层的 null 节点，这些 null 节点也计入长度。
// 题目数据保证答案将会在 32 位带符号整数范围内。

import Tree.TreeNode;
import java.util.ArrayList;

public class _0662_Code {

    // dp[i]表示第i层最左边的结点，根结点在第0层
    ArrayList<Integer> dp = new ArrayList<>();
    // dis[i]表示已知的第i层的宽度
    ArrayList<Integer> dis = new ArrayList<>();

    // 记录最大宽度  为什么初始值设置为1呢？因为下面主方法可以返回width的时候，就说明了这棵树至少有两个结点，
    // 不管哪一层，最后的结果都至少是1
    int width = 1;


    public int widthOfBinaryTree(TreeNode root) {
        // 下面两个if的case  是题目要求的定制化  和算法本身没什么关系
        if (root == null)
            return 0;
        if(root.left == null && root.right == null)
            return 1;
        // path 相当于编号  根结点默认为1
        dfs(root, 0, 1);
        return width;
    }

    private void dfs(TreeNode x, int depth, int path) {
        if (x == null)
            return;
        if (depth < dp.size()){ // 说明当前结点所在层的最左结点我们已经记录好了
            int res = path - dp.get(depth) + 1;
            // 这里为什么不是将算出的新值res去和dis中已经存在的结果去比大小呢，再把较大的存进去呢？
            // 因为我们采用的是后序遍历，同一层的结点我们必然是按照从左到右的顺序遍历的，所以当访问到某个结点时
            // 它一定比上一次访问的该层的结点靠右，所以算出的结果必然比之前的大，所以不用比大小
            dis.set(depth, res);
            width = Math.max(width, res);
        } else if (depth == dp.size()) {  // 说明当前是我们访问的depth层的第一个
            // 把depth层的最左结点标记为当前结点，因为某个层第一个访问的结点就是该层最左的，因为我们是后序遍历
            // 当前层我们只找到了一个，所以不用去更新宽度。
            dp.add(path);
            // 因为这层已经找到了我，所以要加入的话，那宽度也已经是1了
            dis.add(1);
        }
        dfs(x.left, depth + 1, path << 1);
        dfs(x.right, depth + 1, (path << 1) | 1);
    }
}
