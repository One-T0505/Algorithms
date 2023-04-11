package GreatOffer.class18;


// 给定两个有序数组A、B，从A中任意选择一个数，从B中任意选择一个数的和作为目标值。求前k个大的和。

import java.util.HashSet;
import java.util.PriorityQueue;

public class TopKSum {

    public static int[] topKMax(int[] A, int[] B, int k) {
        if (A == null || A.length == 0 || B == null || B.length == 0 || k < 1)
            return null;
        // 最多有 N * M 种组合，如果 k > N * M, 就让 k == N * M
        int N = A.length;
        int M = B.length;
        k = Math.min(N * M, k);

        // 大根堆，仅根据sum值的大小排序
        PriorityQueue<Node> heap = new PriorityQueue<>(((o1, o2) -> o2.sum - o1.sum));
        // 用于记录哪个位置的元素进过堆，(i,j) 表示：A[i] + B[j] 这个组合
        // 那么存入set的形式就是：i_j
        // 形式不是固定的，最终的目的就是记录哪些组合使用过
        HashSet<String> set = new HashSet<>();

        // 先存入初始结点
        int posA = N - 1;
        int posB = M - 1;
        heap.add(new Node(posA, posB, A[posA] + B[posB]));
        set.add(posA + "_" + posB);

        int[] res = new int[k];
        int index = 0;

        while (index != k) {
            Node cur = heap.poll();
            res[index++] = cur.sum;
            posA = cur.posA;
            posB = cur.posB;
            // 如果上面的元素没进过堆
            if (posA - 1 >= 0 && !set.contains((posA - 1) + "_" + posB)) {
                heap.add(new Node(posA - 1, posB, A[posA - 1] + B[posB]));
                set.add((posA - 1) + "_" + posB);
            }
            // 如果左侧的元素没进过堆
            if (posB - 1 >= 0 && !set.contains(posA + "_" + (posB - 1))) {
                heap.add(new Node(posA, posB - 1, A[posA] + B[posB - 1]));
                set.add(posA + "_" + (posB - 1));
            }
        }
        return res;
    }

    // 该题用到的结构是大根堆，堆上的元素是一个Node类型的结点，它有三个域：
    // 来自数组A的哪个位置，来自数组B的哪个位置，和
    // 最开始放入的结点一定是(A最后一个元素，B最后一个元素，和)
    // 可以想象一张矩阵m，行代表A数组，列代表B数组，m[3][4] 表示的含义是：A[3] + B[4]这种组合。
    // 所以最开始入堆的一定是矩阵中最右下角的元素，然后让其左侧和上侧的元素入堆。
    public static class Node {
        public int posA;
        public int posB;
        public int sum;

        public Node(int posA, int posB, int sum) {
            this.posA = posA;
            this.posB = posB;
            this.sum = sum;
        }
    }

}
