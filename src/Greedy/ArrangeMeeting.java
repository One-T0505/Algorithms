package Greedy;

import java.util.Arrays;
import java.util.Comparator;

// 每一个会议都由这样的形式表示：[start, end] 如何安排才能安排最多的会议
public class ArrangeMeeting {

    static class Conference {
        public int start;
        public int end;

        public Conference(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    // 贪心策略：按照会议结束的时间来排. 贪心策略不要去证明对不对，再写一种暴力解法当对数器做实验对比
    public static int arrangeMeeting(Conference[] conferences) {
        Arrays.sort(conferences, (o1, o2) -> o1.end - o2.end);
        int timeStamp = 0, res = 0;
        for (Conference c : conferences){
            if (timeStamp <= c.start){
                timeStamp = c.end;
                res++;
            }
        }
        return res;
    }

    public static int verify(Conference[] conferences){
        if (conferences == null || conferences.length == 0)
            return 0;
        return process(conferences, 0, 0);
    }

    // conferences里面是剩下的还没安排的会议，done表示已经成功安排的会议数量，timeStamp表示当前来到的时间点
    // 该方法返回能安排的最多会议数量
    private static int process(Conference[] conferences, int done, int timeStamp) {
        if (conferences.length == 0)
            return done;
        int max = done;
        for (int i = 0; i < conferences.length; i++){
            if (timeStamp <= conferences[i].start){
                Conference[] rest = copyButRemove(conferences, i);
                max = Math.max(max, process(rest, done + 1, conferences[i].end));
            }
        }
        return max;
    }

    private static Conference[] copyButRemove(Conference[] conferences, int i) {
        Conference[] rest = new Conference[conferences.length - 1];
        int index = 0;
        for (int j = 0; j < conferences.length; j++) {
            if (j != i)
                rest[index++] = conferences[j];
        }
        return rest;
    }

    public static Conference[] generateConferences(int conferenceNum, int timeLimit){
        Conference[] conferences = new Conference[(int) (Math.random() * (conferenceNum + 1))];
        for (int i = 0; i < conferences.length; i++) {
            int start = (int) (Math.random() * (timeLimit + 1));
            int end = (int) (Math.random() * (timeLimit + 1));
            if (start == end)
                conferences[i] = new Conference(start, end + 1);
            else
                conferences[i] = new Conference(Math.min(start, end), Math.max(start, end));
        }
        return conferences;
    }

    public static void main(String[] args) {
        int conferenceNum = 12;
        int timeLimit = 30;
        int testTime = 100000;
        for (int i = 0; i < testTime; i++) {
            Conference[] conferences = generateConferences(conferenceNum, timeLimit);
            if (arrangeMeeting(conferences) != verify(conferences)){
                System.out.println("Failed!");
                return;
            }
        }
        System.out.println("AC");
    }
}
