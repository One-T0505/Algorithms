package Basic.DynamicProgramming;

// 一些恶魔抓住了公主（P）并将她关在了地下城的右下角。地下城是由 M x N 个房间组成的二维网格。我们英勇的骑士（K）
// 最初被安置在左上角的房间里，他必须穿过地下城并通过对抗恶魔来拯救公主。
// 骑士的初始健康点数为一个正整数。如果他的健康点数在某一时刻降至 0 或以下，他会立即死亡。
// 有些房间由恶魔守卫，因此骑士在进入这些房间时会失去健康点数（若房间里的值为负整数，则表示骑士将损失健康点数）；
// 其他房间要么是空的（房间里的值为 0），要么包含增加骑士健康点数的魔法球（若房间里的值为正整数，则表示骑士将增
// 加健康点数）。为了尽快到达公主，骑士决定每次只向右或向下移动一步。
// 编写一个函数来计算确保骑士能够拯救到公主所需的最低初始健康点数。

import java.util.Arrays;

public class _0174_Code {

    // 看如下一个例子：
    // -2   -3    3       如果骑士遵循最佳路径 右 -> 右 -> 下 -> 下，则骑士的初始健康点数至少为 7。
    // -5   -10   1       为什么 右->右->下->下 是最佳路径？  如果是 下 -> 下 -> 右 -> 右  不是最后的血量
    // 10   30    -5      还是正数吗？这是因为，如果走这条路，往下走一步，就要扣去7点血，如果你还能继续往下走，就
    //
    // 要求你的初始血量得>=8才有可能继续下去，所以这不是最佳路径。
    // 经过上面的例子可以发现，其实我们要找的路径是其最小值要尽可能大，负数的情况下。


    public static int calculateMinimumHP(int[][] dungeon) {
        int n = dungeon.length, m = dungeon[0].length;
        // dp[i][j]表示从[i][j]这个位置到达终点所需的最小初始值
        int[][] dp = new int[n][m];
        // 终点到终点，如果终点的值>=0，那初始值只需要1就可以，如果终点的值<0，那就需要1 - dungeon[n - 1][m - 1]
        dp[n - 1][m - 1] = dungeon[n - 1][m - 1] >= 0 ? 1 : 1 - dungeon[n - 1][m - 1];
        for (int i = n - 2; i >= 0; i--) {
            dp[i][m - 1] = Math.max(dp[i + 1][m - 1] - dungeon[i][m - 1], 1);
        }
        for (int j = m - 2; j >= 0; j--) {
            dp[n - 1][j] = Math.max(dp[n - 1][j + 1] - dungeon[n - 1][j], 1);
        }
        for (int i = n - 2; i >= 0; --i) {
            for (int j = m - 2; j >= 0; --j) {
                int minn = Math.min(dp[i + 1][j], dp[i][j + 1]);
                dp[i][j] = Math.max(minn - dungeon[i][j], 1);
            }
        }
        return dp[0][0];
    }


    public static void main(String[] args) {
        int[][] g = {{0, -3}};
        System.out.println(calculateMinimumHP(g));
    }

}
