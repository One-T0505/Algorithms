package Hard;

// leetCode568
// 力扣想让一个最优秀的员工在 N 个城市间旅行来收集算法问题。 但只工作不玩耍，聪明的孩子也会变傻，所以
// 您可以在某些特定的城市和星期休假。您的工作就是安排旅行使得最大化你可以休假的天数，但是您需要遵守一些规则和限制。
// 规则和限制：
//  1.您只能在 N 个城市之间旅行，用 0 到 n-1 的索引表示。一开始，您在索引为 0 的城市，并且那天是星期一。
//  2.这些城市通过航班相连。这些航班用 n * n 矩阵 flights（不一定是对称的）表示，flights[i][j] 代表城
//    市 i 到城市 j 的航空状态。如果没有城市 i 到城市 j 的航班，flights[i][j] = 0；否则，flights[i][j] = 1。
//    同时，对于所有的 i ，flights[i][i] = 0。
//  3.您总共有 k周（每周7天）的时间旅行。您每周最多只能乘坐一次航班，并且只能在每周的星期一上午乘坐航班。由于飞行
//    时间很短，我们不考虑飞行时间的影响。
//  4.对于每个城市，不同的星期您休假天数是不同的，给定一个 N*K 矩阵 days 代表这种限制，days[i][j] 代表您在第j
//    个星期在城市i能休假的最长天数。
//  5.如果您从 A 市飞往 B 市，并在当天休假，扣除的假期天数将计入 B 市当周的休假天数。
//  6.我们不考虑飞行时数对休假天数计算的影响。
// 给定 flights 矩阵和 days 矩阵，您需要输出 k 周内可以休假的最长天数。

public class Vacation {

    // 创建一个二维表dp，dp[i][j]表示0～i-1周已经过完了，但是当前第i周必须在j号城市度过，0～i周累计能休假的
    // 最多天数。所以，最后只需要返回dp最后一行的最大值即可。
    // 转移方程还挺好想：第i周必须在j号城市度过，那么第i-1周只能在有航班到j号城的城市度过。

    public static int maxVacationDays(int[][] flights, int[][] days) {
        int cities = flights.length; // 城市数量
        int weeks = days[0].length;  // 星期数
        // 先制作一张新的航班表map[][]，map[1]=={0, 2, 4} 表示0，2，4这些城市能飞到城市1
        int[][] map = new int[cities][];
        for (int i = 0; i < cities; i++) {
            int len = 0;
            // 统计所有能到i城市的数量
            for (int j = 0; j < cities; j++) {
                if (flights[j][i] == 1)
                    len++;
            }
            map[i] = new int[len];
            for (int c = cities - 1; c >= 0; c--) {
                if (flights[c][i] == 1)
                    map[i][--len] = c;
            }
        }

        // 如果dp[i][j]==-1  说明第i周根本不可能到j号城市
        int[][] dp = new int[weeks][cities];
        // 先处理第0行
        dp[0][0] = days[0][0]; // 默认的城市是在0号，如果选择留在这里，那么能休假的天数是多少
        // 也可以直接从0号称飞到别的城市度过第0周
        for (int j = 1; j < cities; j++) {
            dp[0][j] = flights[0][j] == 1 ? days[j][0] : -1;
        }

        for (int w = 1; w < weeks; w++) { // 第w周
            for (int c = 0; c < cities; c++) { // 在c号城市度过
                // 先把答案设置为dp[w-1][c]  表示我下周依然可以在这个城市度过，不用换地方
                int res = dp[w - 1][c];
                // pre表示上一周待的城市
                for (int pre : map[c]) { // 枚举能到c城市的城市
                    res = Math.max(res, dp[w - 1][pre]);
                }
                dp[w][c] = res == -1 ? -1 : res + days[c][w];
            }
        }
        int res = 0;
        for (int i = 0; i < cities; i++)
            res = Math.max(res, dp[weeks - 1][i]);

        return res;
    }
}
