package GreatOffer.class37;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// 项目规划问题
// 刚入职网易互娱，新人mini项目便如火如荼的开展起来。为了更好的项目协作与管理,
// 小易决定将学到的甘特图知识用于mini项目时间预估。小易先把项目中每一项工作以任务的形式列举出来,
// 每项任务有一个预计花费时间与前置任务表，必须完成了该任务的前置任务才能着手去做该任务。
// 作为经验PM，小易把任务划分得井井有条，保证没有前置任务或者前置任务全数完成的任务，都可以同时进行。
// 小易给出了这样一个任务表，请作为程序的你计算需要至少多长时间才能完成所有任务。
// 输入第一行为一个正整数T，表示数据组数。
// 对于接下来每组数据，第一行为一个正整数N，表示一共有N项任务。
// 接下来N行，每行先有两个整数Di和Ki,表示完成第i个任务的预计花费时间为Di天，该任务有Ki个前置任务。
// 之后为Ki个整数，Mj表示第Mj个任务是第i个任务的前置任务。
// 数据范围:对于所有数据，满足1<=T<=3，1<=N, Mj<=100000， 0<= Di <= 1000，0<=sum(Ki)<=N*2。

public class _01_Code {

    // 该题目用到的是拓扑排序，但需要稍微改动一下
    // 解释下输入参数：
    // ArrayList<Integer>[] nums  每一个元素都是一个列表，nums[i]表示任务i所有后继任务的编号
    // days：days[i]表示任务i需要多少天完成
    // headCount：headCount[i]表示任务i有多少个前驱任务
    // 所以这三个参数的长度都一样
    public static int dayCount(ArrayList<Integer>[] nums, int[] days, int[] headCount) {
        int N = nums.length;
        // 找出所有前驱为0的任务，并将它们都加入到队列中
        Queue<Integer> queue = countHead(headCount);
        int maxDay = 0;
        int[] countDay = new int[N];
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            countDay[cur] += days[cur];
            for (int i = 0; i < nums[cur].size(); i++) {
                int mission = nums[cur].get(i);
                headCount[mission]--;
                if (headCount[mission] == 0)
                    queue.add(mission);
                countDay[mission] = Math.max(countDay[mission], countDay[cur]);
            }
        }
        for (int i = 0; i < N; i++)
            maxDay = Math.max(maxDay, countDay[i]);

        return maxDay;
    }

    private static Queue<Integer> countHead(int[] headCount) {
        LinkedList<Integer> queue = new LinkedList<>();
        for (int i = 0; i < headCount.length; i++) {
            if (headCount[i] == 0)
                queue.add(i);
        }
        return queue;
    }
}
