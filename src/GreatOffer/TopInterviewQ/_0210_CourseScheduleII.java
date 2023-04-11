package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// 现在你总共有 numCourses 门课需要选，记为 0 到 numCourses - 1。给你一个数组 prerequisites ，
// 其中 prerequisites[i] = [ai, bi] ，表示在选修课程 ai 前 必须 先选修 bi 。
// 例如，想要学习课程 0 ，你需要先完成课程 1 ，我们用一个匹配来表示：[0,1] 。
// 返回你为了学完所有课程所安排的学习顺序。可能会有多个正确的顺序，你只要返回任意一种就可以了。如果不可能
// 完成所有课程，返回一个空数组。

public class _0210_CourseScheduleII {

    // 和207题思路一样，只需要在队列中弹出时记录顺序即可。
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] res = new int[numCourses];
        for (int i = 0; i < numCourses; i++)
            res[i] = i;
        if (prerequisites == null || prerequisites.length == 0)
            return res;
        HashMap<Integer, Course> map = new HashMap<>();
        for (int[] arr : prerequisites) {
            int to = arr[0];
            int from = arr[1];
            if (!map.containsKey(from))
                map.put(from, new Course(from));
            if (!map.containsKey(to))
                map.put(to, new Course(to));
            map.get(from).nexts.add(map.get(to));
            map.get(to).in++;
        } // 上面的循环是把给定的二维数组转换成了图的样式
        int index = 0;
        // 准备一个队列，将初始时所有入度为0的点加入队列
        Queue<Course> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (!map.containsKey(i))
                res[index++] = i;
            else {
                if (map.get(i).in == 0)
                    queue.add(map.get(i));
            }
        }
        int N = map.size();
        int count = 0;
        while (!queue.isEmpty()) {
            Course cur = queue.poll();
            res[index++] = cur.num;
            count++;
            for (Course c : cur.nexts) {
                if (--c.in == 0)
                    queue.add(c);
            }
        }
        return count == N ? res : new int[0];
    }

    public static class Course {
        public int num;  // 课程编号
        public int in;   // 入度
        public ArrayList<Course> nexts;   // 该课程的后继课程

        public Course(int num) {
            this.num = num;
            in = 0;
            nexts = new ArrayList<>();
        }
    }
}
