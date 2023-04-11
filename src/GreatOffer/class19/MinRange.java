package GreatOffer.class19;

import java.util.List;
import java.util.TreeSet;

// leetCode632
// 你有 k 个非递减排列的整数数组。找到一个最小区间，使得 k 个数组中的每个数组至少有一个数包含在其中。
// 我们定义如果 b-a < d-c 或者在 b-a == d-c 时 a < c，则区间 [a,b] 比 [c,d] 小。

public class MinRange {

    // 用有序表实现
    // 首先先把每个数组的第一个元素放进有序表，然后记录当前最小值和最大值，然后弹出最小值，找到该最小值所在
    // 数组，并加入该数组的下一个元素，然后再记录最小最大值，看是否能缩小上一次记录的范围。有序表的容量始终维持再
    // N，假如有N个数组。直到有序表的容量不是N的时候，结束。

    public int[] smallestRange(List<List<Integer>> nums) {
        if (nums == null)
            return null;
        int N = nums.size();
        TreeSet<Node> tree = new TreeSet<>(((o1, o2) -> o1.val != o2.val ? o1.val - o2.val :
                o1.arrId - o2.arrId));
        for (int i = 0; i < N; i++)
            tree.add(new Node(nums.get(i).get(0), i, 0));
        // 用来辨别第一次的，因为 b - a == 0，比任何两个 max - min 都小，所以按照正常逻辑是无法更新的，所以
        // 用这个变量来单独区分第一次更新 a、b
        boolean set = false;
        int a = 0;
        int b = 0;
        while (tree.size() == N) {
            Node min = tree.first();
            Node max = tree.last();
            if (!set || (max.val - min.val < b - a)) {
                set = true;
                a = min.val;
                b = max.val;
            }
            min = tree.pollFirst();
            int arrId = min.arrId;
            int index = min.index + 1;
            if (index != nums.get(arrId).size()) {
                tree.add(new Node(nums.get(arrId).get(index), arrId, index));
            }
        }
        return new int[]{a, b};
    }

    public static class Node {
        public int val;
        public int arrId;  // 位于第几个数组
        public int index;  // 位于当前数组的第几个元素

        public Node(int val, int arrid, int index) {
            this.val = val;
            this.arrId = arrid;
            this.index = index;
        }
    }
}
