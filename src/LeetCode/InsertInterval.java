package LeetCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ymy
 * 2023/3/20 - 16 : 26
 **/

// 给你一个无重叠的 ，按照区间起始端点排序的区间列表。
// 在列表中插入一个新的区间，你需要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）。

public class InsertInterval {

    public static int[][] insert(int[][] intervals, int[] newInterval) {
        if (intervals == null || intervals.length == 0)
            return new int[][] {{newInterval[0], newInterval[1]}};
        int start = newInterval[0], end = newInterval[1];
        List<int[]> list = new ArrayList<>();
        boolean placed = false;
        for (int[] arr : intervals){
            // 说明完全在左侧，没有重叠区域
            if (arr[1] < start) {
                list.add(arr);
            } else if (arr[0] > end){
                // 如果当前遍历到的区间的开始 > end，说明该区间完全在 newInterval 的右侧，没有重叠
                // 第一次碰到的时候，说明后面的区间都是完全没重叠的，那么此时就应该将 newInterval 插入了
                // 因为还要保持顺序
                if (!placed){
                    list.add(new int[] {start, end});
                    placed = true;
                }
                list.add(arr);
            } else { // 有重叠区域
                start = Math.min(start, arr[0]);
                end = Math.max(end, arr[1]);
            }
        }
        if (!placed)
            list.add(new int[] {start, end});
        int[][] res = new int[list.size()][2];
        for (int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }


    public static void main(String[] args) {
        int[][] inter = {{1, 5}};
        int[] newI = {0, 3};
        System.out.println(Arrays.deepToString(insert(inter, newI)));
    }
}
