package GreatOffer.class40;

import java.util.Arrays;
import java.util.PriorityQueue;

// 给定int[][] meetings, 比如
// {66，70} : 0号会议截止时间66，获得收益70
// {25，90} : 1号会议截止时间25，获得收益90
// {50，30} : 2号会议截止时间50,获得收益30
// 一开始的时间是0，任何会议都持续10的时间，但是一个会议一定要在该会议截止时间之前开始
// 只有一个会议室，任何会议不能共用会议室，一旦一个会议被正确安排，将获得这个会议的收益 请返回最大的收益

public class _03_Code {

    // 先将数组排序，让截止时间从小到大排序，截止时间相同的，收益大小随便排序，申请一个小根堆，用于做门槛
    // 假如排完序的数组是：[6, 20]  [9, 50]  [13, 42]  [16, 39]  [25, 40]  [25, 39]  [25, 69]
    // 时间一开始初始化为0，遍历到第一个数组，发现当前时间<截止时间，说明当前会议可安排，安排的开始时间就是
    // 最早的当前时间，所以让当前时间直接到 之前的当前时间+ 10，然后把该会议的收益加到堆中。来到下一个数组发现其
    // 截止时间<当前时间，说明无法接着安排这个会议，但是发现其收益比堆顶元素收益大，那就让当前会议的收益替换堆顶元素，
    // 当前时间不变。如果说本来的堆顶元素的安排时间是0，那就让当前新的会议也在同一时间安排，只不过换成了这个会议，而不是
    // 堆顶会议了。

    public static int maxProfit(int[][] meetings) {
        int N = meetings.length;
        Arrays.sort(meetings, (a, b) -> a[0] - b[0]);
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int time = 0;
        int profit = 0;
        for (int i = 0; i < N; i++) {
            if (meetings[i][0] >= time) {
                heap.add(meetings[i][1]);
                profit += meetings[i][1];
                time += 10;
            } else {
                if (!heap.isEmpty() && meetings[i][1] > heap.peek()) {
                    profit += meetings[i][1] - heap.peek();
                    heap.poll();
                    heap.add(meetings[i][1]);
                }
            }
        }
        return profit;
    }


    public static void main(String[] args) {
        int[][] m = {{6, 20}, {9, 50}, {13, 42}, {16, 39}, {25, 40}, {25, 39}, {25, 69}};
        System.out.println(maxProfit(m));
    }
}
