package Basic.BitOperation.Exercise;

// leetCode1707
// 给你一个由非负整数组成的数组arr，另有一个查询数组queries，其中queries[i] = [xi, mi]。
// 第i个查询的答案是xi和任何arr数组中不超过mi的元素按位异或XOR得到的最大值。换句话说，答案是 max(nums[j] XOR xi) ，
// 其中所有 j 均满足 arr[j] <= mi。如果 arr 中的所有元素都大于 mi，最终答案就是 -1 。
// 返回一个整数数组 answer 作为查询的答案，其 中 answer.length == queries.length 且 answer[i] 是第i个查询的答案。


public class LimitedXorSum {

    // 思路和前两题大思路差不多，只需要对前缀树稍作修改。我们需要对结点新加一个属性，就是以该结点为根的树上包含的最小值。
    // 让根结点时刻记录着全局最小值。并且提供的方法maxXor需要变成：maxXor(num, m) 给一个num，该方法返回树上小于等于m的数中，
    // 和num异或的最大结果。

    // 主方法
    public static int[] maximizeXor(int[] nums, int[][] queries) {
        if (nums == null || nums.length == 0 || queries == null ||
                queries.length == 0 || queries[0].length != 2)
            return null;
        int M = queries.length;
        int[] res = new int[M];
        PrefixTree tree = new PrefixTree();
        for (int num : nums) tree.add(num);
        for (int i = 0; i < M; i++)
            res[i] = tree.maxXor(queries[i][0], queries[i][1]);
        return res;
    }

    public static class Node {
        public int min;
        public Node[] nexts;  // 为什么长度为2？因为它的后继止只可能是0或1，刚好用索引0表示0

        public Node() {
            min = Integer.MAX_VALUE;
            nexts = new Node[2];
        }
    }

    public static class PrefixTree {

        public Node root = new Node();

        // 将一个数的二进制形式加入到树中
        public void add(int num) {
            Node cur = root;
            cur.min = Math.min(cur.min, num);  // 更新最小值
            // 因为题目说了所有元素非负，所以符号位不可能为1，全为0，那就可以不用记最高位了，所以从30开始
            for (int move = 30; move >= 0; move--) {
                int path = (num >> move) & 1;   // 拿到每位上的值，这个值也刚好反映了应该去往哪里
                if (cur.nexts[path] == null)
                    cur.nexts[path] = new Node();
                cur = cur.nexts[path];
                cur.min = Math.min(cur.min, num);  // 更新最小值

            }
        }
        // 所以该树上每个记录的长度都固定为31,那么该方法的时间复杂度就是：O(1)

        // 该结构之前收集了一票数字，并且建好了前缀树
        // 树上不超过m的数中，num和谁 ^ 有最大的结果（把结果返回）
        public int maxXor(int num, int m) {
            if (root.min > m)
                return -1;    // 按题意返回
            Node cur = root;
            int res = 0;
            for (int move = 30; move >= 0; move--) { // 取出num中第move位的状态，path只有两种值0就1，整数
                int path = (num >> move) & 1;
                // expected表示当前位的值希望遇到的值：不需要考虑符号位；
                // 实际遇到的值。如若想去的地方存在且该去向有不超过m的值才能去，否则去另一边
                // 其实这里有个思维难点，就是说只要下面的两个条件不同时满足，就可以去path 这条路吗？如果 path ^ 1 这条路存在，只是说这条路通过的所有值都 > m
                // 在这样的情况下，凭什么就可以直接选择 path 这条路呢？万一通过 path 的所有值的最小值也都 > m 呢？
                // 仔细想就知道不可能。假设当前结点来到了c，每个结点记录的是通过自己的所有值中的最小值，而c就两条路，如果都>m，那么从前缀树的根结点
                int expected = (cur.nexts[path ^ 1] != null && cur.nexts[path ^ 1].min <= m) ?
                        path ^ 1 : path;
                // expected ^ path 就得到了第move位真实的值，现在要把该位置上的值添加到res上
                res |= (expected ^ path) << move;
                cur = cur.nexts[expected];
            }
            return res;
        } // 该方法的for循环固定32次，所以时间复杂度为：O(1)
    }
}
