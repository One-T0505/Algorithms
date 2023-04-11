package LeetCode;

/**
 * ymy
 * 2023/3/22 - 14 : 02
 **/

// leetCode223
// 给你二维平面上两个由直线构成且边与坐标轴 平行/垂直 的矩形，请你计算并返回两个矩形覆盖的总面积。
// 每个矩形由其 左下 顶点和 右上 顶点坐标表示：
//  第一个矩形由其左下顶点 (ax1, ay1) 和右上顶点 (ax2, ay2) 定义。
//  第二个矩形由其左下顶点 (bx1, by1) 和右上顶点 (bx2, by2) 定义。


public class MatrixArea {

    public static int computeArea(int ax1, int ay1, int ax2, int ay2,
                                  int bx1, int by1, int bx2, int by2) {
        int s1 = (ax2 - ax1) * (ay2 - ay1);
        int s2 = (bx2 - bx1) * (by2 - by1);

        // 矩形 s1 完全罩住 s2
        if ((ax1 <= bx1 && ay1 <= by1) && (ax2 >= bx2 && ay2 >= by2))
            return s1;
        // 矩形 s2 完全罩住 s1
        if ((bx1 <= ax1 && by1 <= ay1) && (bx2 >= ax2 && by2 >= ay2))
            return s2;
        // 两个矩形没有交集
        if (ax2 <= bx1 || ax1 >= bx2 || ay1 >= by2 || ay2 <= by1)
            return s1 + s2;
        // 有交集
        int overlap = Math.min(ay2, by2) - Math.max(ay1, by1);
        // s2 在 s1 的右侧部分
        if (bx1 > ax1)
            overlap *= (Math.min(ax2, bx2) - bx1);
        // s2 在 s1 的左侧部分 以及 s2的左边和s1的左边重合的情况
        else
            overlap *= (Math.min(ax2, bx2) - ax1);
        return s1 + s2 - overlap;
    }

    // 这道题目不难，就是有很多情况需要讨论，上面的代码是我自己写完之后精简的结果。一开始写的代码非常冗长，
    // 最后将情况合并了，才成这样的。
}
