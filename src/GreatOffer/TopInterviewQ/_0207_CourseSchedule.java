package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1。
// 在选修某些课程之前需要一些先修课程。 先修课程按数组 prerequisites 给出，其中 prerequisites[i] = [ai, bi]，
// 表示如果要学习课程 ai 则必须先学习课程 bi 。
// 例如，先修课程对 [0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。
// 请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。

public class _0207_CourseSchedule {


    // 该题目考察的是图的拓扑算法
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        if (prerequisites == null || prerequisites.length == 0)
            return true;
        if (prerequisites[0].length != 2)
            return false;
        // 一个编号对应一个Course实例
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
        int N = map.size();
        // 准备一个队列，将初始时所有入度为0的点加入队列
        Queue<Course> queue = new LinkedList<>();
        for (Course c : map.values()) {
            if (c.in == 0)
                queue.add(c);
        }
        int count = 0;
        while (!queue.isEmpty()) {
            Course cur = queue.poll();
            count++;
            for (Course c : cur.nexts) {
                if (--c.in == 0)
                    queue.add(c);
            }
        }
        return count == N;
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
