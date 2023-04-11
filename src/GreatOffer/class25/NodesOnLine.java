package GreatOffer.class25;

import java.util.HashMap;
import java.util.Map;

// 给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。求最多有多少个点在同一条直线上。

public class NodesOnLine {

    // 总体流程：遍历每个点，让他作为直线一定要经过的点，然后逐一与后面的点算斜率，相当于套两层循环；当来到第3个点的
    // 时候，不用往前再去算了，只用从4..N-1去算斜率就好了。我们用哈希表来记录对应斜率下的点有多少个。
    // 如果直接用double来表示斜率，会有偏差，所以我们就用实际的分数来表示斜率，对于两个点之间的斜率，我们要先求出
    // 最大公约数，然后将斜率化成最简形式，比如：3/9 和 1/3 是同样的斜率，所以这里就需要用到gcd方法。
    // 哈希表的key：最简化斜率的分子；value：哈希表，该哈希表的key就是外层哈希表key这个分子下的分母，而value
    // 则是该斜率下有多少个点； 因为最简化的情况下，一个分子可能有不同的分母。

    // 下面再来讲讲两个点之间的位置关系。在这道题目里，我们将位置关系分成了4种：
    //  1.完全重合的点   2.处于同一水平线上   3.处于同一竖直线上   4.正常的存在斜率的关系
    //
    // 当我们选定一个点作为必经点时，要将它和其余所有点的位置都分析一遍，最后情况2，情况3情况4要选出最大值，再加上情况1
    // 这个就是选定一个点作为必经点能找到的最多共线的结果；然后枚举每个必经点，求出总答案。

    public static int maxPoints(int[][] points) {
        if (points == null || points.length == 0)
            return 0;
        if (points.length <= 2)
            return points.length;
        int N = points.length;
        int res = 0;
        HashMap<Integer, Map<Integer, Integer>> map = new HashMap<>();
        // 遍历每一个点，让它作为直线必经过的点
        for (int i = 0; i < N; i++) {
            int same = 1;   // 记录有多少个点完全一样，包含当前点
            int sameX = 0;  // 有几个点和当前点处于同一条竖直线上，不包含当前点
            int sameY = 0;  // 有几个点和当前点处于同一条水平线上，不包含当前点
            int line = 0;   // 和当前必经点，以正常斜率共线的点数，不包含当前点
            for (int j = i + 1; j < N; j++) {  // 讨论i号点和j号点的斜率关系
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                if (dx == 0 && dy == 0) // 说明位置完全一样
                    same++;
                else if (dx == 0)
                    sameX++;
                else if (dy == 0)
                    sameY++;
                else {  // 正常情况
                    int gcd = gcd(dx, dy);   // 找出最大公约数
                    // 算出斜率的最简化模式
                    dx /= gcd;
                    dy /= gcd;
                    // 不存在分子
                    if (!map.containsKey(dx))
                        map.put(dx, new HashMap<>());
                    map.get(dx).put(dy, map.get(dx).getOrDefault(dy, 0) + 1);

                    line = Math.max(line, map.get(dx).get(dy));
                }
            }
            // 此时已经枚举了当前必经点和其余所有点的情况
            // 从上面的代码里可以看出，后三种情况的位置关系的点的数量我们都没有将这个必经点算进去，
            // 这个必经点我们是放在了same中的
            res = Math.max(res, Math.max(Math.max(sameX, sameY), line) + same);
            // 清空当前必经点存入的所有信息，换新的必经点来使用这个map
            map.clear();
        }
        return res;
    }


    // 求a、b的最大公约数  要求传入的a、b不为0
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }


    public static void main(String[] args) {
        System.out.println(gcd(-4, -2));
    }
}
