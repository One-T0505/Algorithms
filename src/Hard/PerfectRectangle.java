package Hard;

// leetCode391
// 给你一个数组 rectangles ，其中 rectangles[i] = [xi, yi, ai, bi] 表示一个坐标轴平行的矩形。这个矩形的
// 左下顶点是 (xi, yi) ，右上顶点是 (ai, bi) 。
// 如果所有矩形一起精确覆盖了某个矩形区域，则返回 true ；否则，返回 false 。

import java.util.HashMap;

public class PerfectRectangle {

    // 这个题要用到的结论并没有通用性，只适合于本题。思路：根据所给的所有小矩形，我们可以找出他们所界定的外围的最大矩形，
    // 确定出上下左右边界。这个边界矩形需满足两个条件就能返回true：
    //   1. 边界矩形面积 == 所有小矩形面积的累加和    这个条件是必要条件，满足这个不一定就是true，但是不满足这个必然是false
    //       ____________
    //      |_____A______|     A、B、C确定的边界矩形的面由A、B、C三部分，外加B、C中间那部分组成，如果来了第4个矩形D，其
    //      | B|  |   C  |     面积刚好等于B、C之间那部分，且D完全在C内部，这样的话， A+B+C+D==边界矩形的面积
    //      |__|__|______|     但是A、B、C、D却不是严丝合缝的，这就是条件1的反例。所以还需要条件2
    //
    //
    //   2. 边界矩形的4个顶点在所有小矩形中都只出现1次，边界矩形内部其余所有小矩形矩的点出现次数都为偶数次

    public static boolean isRectangleCover(int[][] rectangles) {
        if (rectangles == null || rectangles.length == 0 || rectangles[0] == null || rectangles[0].length == 0)
            return false;
        int T = Integer.MIN_VALUE;  // 上边界
        int B = Integer.MAX_VALUE;  // 下边界
        int L = Integer.MAX_VALUE;  // 左边界
        int R = Integer.MIN_VALUE;  // 右边界
        // <x, <y, t>>  (x,y)这个点出现了t次
        HashMap<Integer, HashMap<Integer, Integer>> times = new HashMap<>();
        int area = 0;  // 记录所有小矩形的面积累加和
        for (int[] rec : rectangles) {
            // 每一个小矩形都有4个顶点，所以第一步就是统计新来的矩形的4个顶点的词频
            add(times, rec[0], rec[1]);
            add(times, rec[2], rec[1]);
            add(times, rec[2], rec[3]);
            add(times, rec[0], rec[3]);
            // 第二步 统计总面积
            area += (rec[2] - rec[0]) * (rec[3] - rec[1]);
            // 第三步 更新边界矩形信息
            L = Math.min(L, rec[0]);
            B = Math.min(B, rec[1]);
            R = Math.max(R, rec[2]);
            T = Math.max(T, rec[3]);
        }
        // 检查 条件1 和 条件2  是否都成立
        return area == (T - B) * (R - L) && (checkPoints(times, L, B, R, T));
    }


    private static void add(HashMap<Integer, HashMap<Integer, Integer>> dp, int x, int y) {
        if (!dp.containsKey(x))
            dp.put(x, new HashMap<>());
        dp.get(x).put(y, dp.get(x).getOrDefault(y, 0) + 1);
    }


    // 该方法用于检查是否满足条件2，即点的出现次数是否符合要求
    // (L, T)  (L, B)  (R, B)  (R, T)  4个点只能分别出现一次，其余点的次数都得是偶数次
    private static boolean checkPoints(HashMap<Integer, HashMap<Integer, Integer>> dp,
                                       int L, int B, int R, int T) {
        // 先检查4个最外界的点是否只出现1次
        if (dp.get(L).getOrDefault(T, 0) != 1 ||
                dp.get(L).getOrDefault(B, 0) != 1 ||
                dp.get(R).getOrDefault(B, 0) != 1 ||
                dp.get(R).getOrDefault(T, 0) != 1)
            return false;
        // 执行到这里说明4个边界点确实都只出现了1次
        dp.get(L).remove(T);
        dp.get(L).remove(B);
        dp.get(R).remove(T);
        dp.get(R).remove(B);
        for (int x : dp.keySet()) {
            for (int times : dp.get(x).values()) {
                if ((times & 1) == 1)
                    return false;
            }
        }
        return true;
    }


}
