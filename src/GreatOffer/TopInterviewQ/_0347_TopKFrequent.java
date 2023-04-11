package GreatOffer.TopInterviewQ;

import java.util.HashMap;
import java.util.PriorityQueue;

// 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
// 题目给的输入可以保证每种元素出现的次数都不同

public class _0347_TopKFrequent {

    // 思路：首先需要一张哈希表，对每种元素出现的次数进行统计，完成元素词频表。然后申请一个容量为k的小根堆
    // 该堆仅按照词频排序。每次一个元素的词频入堆时都需要和堆顶比较，如果超过了堆顶则可以入堆。
    // 这个堆其实维护的就是目前为止找到的top k 个元素，而堆顶就是门槛，新来的元素的词频超过门槛了，才可以进入。


    public static int[] topKFrequent(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1)
            return null;
        // 制作词频表
        HashMap<Integer, Node> map = new HashMap<>();
        for (int e : nums) {
            if (!map.containsKey(e))
                map.put(e, new Node(e));
            else
                map.get(e).times++;
        }
        // 生成堆
        PriorityQueue<Node> heap = new PriorityQueue<>(((o1, o2) -> o1.times - o2.times));
        for (Node cur : map.values()) {
            if (heap.size() < k || (heap.size() == k && cur.times > heap.peek().times))
                heap.add(cur);
            if (heap.size() > k)
                heap.poll();
        }
        int[] res = new int[k];
        int i = 0;
        while (!heap.isEmpty())
            res[i++] = heap.poll().val;

        return res;
    }

    public static class Node {
        public int val;
        public int times;

        public Node(int val) {
            this.val = val;
            times = 1;
        }
    }
}
