package LeetCode;

import java.util.*;

/**
 * ymy
 * 2023/3/29 - 13 : 27
 **/


// 给定两个以 升序排列 的整数数组 nums1 和 nums2 , 以及一个整数 k 。
// 定义一对值 (u,v)，其中第一个元素来自 nums1，第二个元素来自 nums2 。
// 请找到和最小的 k 个数对 (u1,v1),  (u2,v2)  ...  (uk,vk) 。

public class TopKSmallestPairSum {

    public static class Node {
        public int sum;
        public int i;
        public int j;

        public Node(int sum, int i, int j) {
            this.sum = sum;
            this.i = i;
            this.j = j;
        }
    }


    // 最经典的方法就是用一个大根堆
    public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0)
            return null;
        LinkedList<List<Integer>> res = new LinkedList<>();
        int N = nums1.length;
        int M = nums2.length;
        // 仅根据和的大小来排序
        k = Math.min(k, N * M);
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> b.sum - a.sum);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int sum = nums1[i] + nums2[j];
                if (heap.size() == k && sum >= heap.peek().sum)
                    continue;
                heap.add(new Node(sum, i, j));
                if (heap.size() > k)
                    heap.poll();
            }
        }
        while (!heap.isEmpty()){
            Node poll = heap.poll();
            ArrayList<Integer> pair = new ArrayList<>();
            pair.add(nums1[poll.i]);
            pair.add(nums2[poll.j]);
            res.addFirst(pair);
        }
        return res;
    }




    // 在本题中，用堆的方法会超时，所以还要继续优化。注意题目说的是升序序列，每个数组里面不存在相等的数.
    public static List<List<Integer>> kSmallestPairs2(int[] nums1, int[] nums2, int k) {
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0)
            return null;
        List<List<Integer>> res = new ArrayList<>();
        int N = nums1.length;
        int M = nums2.length;
        // 堆中存放的是长度为2的数组，表示一个数堆，(i,j) 表示nums1[i] 和 nums2[j]
        // 总体思路是这样的：最小的是数对和肯定是(0,0), 那么第二小的数对可能是谁呢？是 (0,1) 或者 (1,0)
        // 假如第二小的是(1,0)  那么现在已经选择了 (0,0) (1,0)  那么第三小的数对可能是谁呢？
        // 是 (0,1) (1,1) (2,0)   其中 (0,1) 是选择了(0,0)后产生的候选；其余两个是选择了(1,0)后产生的候选
        // 所以，概括来说就是，如果我们已经选出了前n对最小的数对和：(a1,b1) (a2,b2) ... (an,bn)
        // 那么第n+1小的数对和的候选有：(a1+1, b1) (a1, b1+1) (a2+1, b2) (a2, b2+1) ...
        // (an+1, bn) (an, bn+1)
        // 每次从备选中弹出最小的数对和加入总答案中，然后由该数对和又产生了两个备选，将其加入堆中，
        // 但是要注意一个问题，就是重复加入备选的问题。比如选择了(0,0)后，我们会把(1,0) (0,1) 加入堆中，
        // 如果最小的是(1, 0) 那么我们会把 (1, 1) (2, 0) 加入堆中，此时堆里就是 (0, 1) (1, 1) (2, 0)
        // 如果此时最小的是(0,1)  那么我们又会把 (1, 1)  (0,2) 加入，导致了重复。
        // 为此，我们设计了一个方法，就是先把 (i,0) 加入堆中，就是nums1中每个元素和nums2中的第0个元素的数对加入
        // 堆中。每次弹出后，我们只需要将其1位置的索引+1，所以仅仅产生了一个备选。
        // 所以堆中最开始是 (0,0) (1,0) (2,0) ... (n,0)
        // 最小的是(0,0) 弹出没有问题，然后我们只需要添加(0,1)这个备选即可。
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> nums1[a[0]] + nums2[a[1]] -
                (nums1[b[0]] + nums2[b[1]]));
        // 把 nums1 的所有索引入队，nums2 的索引初始时都是 0
        // 优化：最多入队 k 个就可以了，因为提示中 k 的范围较小，这样可以提高效率
        for (int i = 0; i < Math.min(k, N); i++) {
            heap.add(new int[] {i, 0});
        }

        while (k-- > 0 && !heap.isEmpty()){
            int[] pos = heap.poll();
            res.add(Arrays.asList(nums1[pos[0]], nums2[pos[1]]));

            if (++pos[1] < M)
                heap.add(pos);
        }
        return res;
    }



    public static void main(String[] args) {
        int[] n1 = {1, 1, 2};
        int[] n2 = {1, 2, 3};
        int k = 10;
        System.out.println(kSmallestPairs(n1, n2, k));
    }
}
