package Basic.BitOperation;

// leetCode421
// 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。

// 1 <= nums.length <= 2 * 10^5
// 0 <= nums[i] <= 2^31 - 1

public class TwoNumMaxXor {

    // 这个题就是上个题目的阉割版。还是同样的一棵前缀树，一开始把arr[0]加进树里，然后新来的数在树中找结果，然后添加进树里。
    // 注意：添加进树里的是数组元素本身，不是前缀异或和了！！！


    public int findMaximumXOR(int[] arr) {
        if (arr == null || arr.length == 0)
            return Integer.MIN_VALUE;
        if (arr.length == 1) // 只能和自己异或
            return 0;
        int res = 0; // 全都为非负数，所以最小值不可能低于0
        PrefixTree tree = new PrefixTree();
        tree.add(arr[0]);
        // 注意：题目说的限制是 i <= j  也就是说每个数都可以和自己异或，但是和自己结果只能为0，我们已经提前放在res初始化了
        // 所以只需要考虑在自己前面的数即可
        for (int i = 1; i < arr.length; i++) {
            res = Math.max(res, tree.maxXor(arr[i]));
            tree.add(arr[i]);
        }
        return res;
    }

    public static class Node {
        public Node[] nexts = new Node[2];  // 为什么长度为2？因为它的后继止只可能是0或1，刚好用索引0表示0
    }

    public static class PrefixTree {

        public Node root = new Node();

        // 将一个数的二进制形式加入到树中
        // 注意：题目说了数值为非负数，所以最高符号位就不用考虑了，这点必须优化，否则题目就会卡你超出内存限制
        public void add(int num) {
            Node cur = root;
            for (int move = 30; move >= 0; move--) { // 这里已经少了符号位的考虑
                int path = (num >> move) & 1;   // 拿到每位上的值，这个值也刚好反映了应该去往哪里
                if (cur.nexts[path] == null)
                    cur.nexts[path] = new Node();
                cur = cur.nexts[path];
            }
        }
        // 所以该树上每个记录的长度都固定为31,那么该方法的时间复杂度就是：O(1)

        // 该结构之前收集了一票数字，并且建好了前缀树
        // num和 树上的谁 ^ 有最大的结果（把结果返回）
        public int maxXor(int num) {
            Node cur = root;
            int res = 0;
            for (int move = 30; move >= 0; move--) { // 取出num中第move位的状态，path只有两种值0就1，整数
                int path = (num >> move) & 1;
                // 不用在考虑符号位了
                int expected = cur.nexts[path ^ 1] == null ? path : path ^ 1;
                // expected ^ path 就得到了第move位真实的值，现在要把该位置上的值添加到res上
                res |= (expected ^ path) << move;
                cur = cur.nexts[expected];
            }
            return res;
        } // 该方法的for循环固定32次，所以时间复杂度为：O(1)

    }
}
