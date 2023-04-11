package GreatOffer.TopInterviewQ;


// 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]。
// 请你合并所有重叠的区间，并返回 一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。

import java.util.Arrays;

public class _0056_MergeIntervals {

    public static int[][] merge(int[][] intervals) {
        int N = intervals.length;
        Arrays.sort(intervals, (a, b) -> (a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]));
        int start = intervals[0][0];
        int end = intervals[0][1];
        int size = 0;
        for (int i = 1; i < N; i++) {
            if (intervals[i][0] > end) {
                intervals[size][0] = start;
                intervals[size++][1] = end;
                start = intervals[i][0];
                end = intervals[i][1];
            } else
                end = Math.max(end, intervals[i][1]);
        }
        intervals[size][0] = start;
        intervals[size++][1] = end;
        return Arrays.copyOf(intervals, size);
    }
}
