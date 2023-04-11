package GreatOffer.Interview;

// 这里有 n 门不同的在线课程，按从 1 到 n 编号。给你一个数组 courses ，其中
// courses[i] = [durationi, lastDayi] 表示第 i 门课将会持续上 durationi 天课，并且必须在
// 不晚于 lastDayi 的时候完成。注意是完成不是开始
// 你的学期从第 1 天开始。且不能同时修读两门及两门以上的课程。
// 返回你最多可以修读的课程数目。

import java.util.Arrays;
import java.util.PriorityQueue;

public class ArrangeCourse {

    public static int scheduleCourse(int[][] courses) {
        // 先把数组排序：按照课程的截止时间从小到大排序  截止早的在前面
        Arrays.sort(courses, (a, b) -> (a[1] - b[1]));
        // 准备一个大根堆  里面存放的是数值是课程的持续时间
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
        // 起始时间点
        int time = 0;
        for (int[] c : courses) {
            // 如果当前课程能在截止前完成，那么就说明该课程可以成功修完，就把其加在堆里
            // 并且把时间带到完成该门课的时间
            if (time + c[0] <= c[1]) {
                heap.add(c[0]);
                time += c[0];
            } else { // 如果当前课程不能修  那就和堆顶元素比较
                // 堆顶元素是已经修过的课程里持续时间最长的，换句话说就是最没有性价比的，反正都是1门课，
                // 当然是持续时间越短的性价比越高。所以选择大根堆，让持续时间长的放在堆顶，优先被替换。
                // 如果堆顶课程的持续时间 > 当前课程，并且由于这个位置关系，当前课程的截止时间必然 >= 堆顶课程
                // 结合这两点的优势，所以用当前课程替换堆顶课程，当然了，对应的时间也要修改
                // time - 堆顶课程的持续时间 + 当前课程的持续时间   才是正确的时间节点。
                if (!heap.isEmpty() && heap.peek() > c[0]) {
                    heap.add(c[0]);
                    time += c[0] - heap.poll();
                }
            }
        }
        return heap.size();
    }
}
