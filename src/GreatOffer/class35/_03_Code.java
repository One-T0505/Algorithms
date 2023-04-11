package GreatOffer.class35;

import java.util.Arrays;

// 小红书
// 一场电影开始和结束时间可以用一个小数组来表示["07:30","12:00"]
// 已知有2000场电影开始和结束都在同一天，这一天从00: 00开始到23: 59结束
// 一定要选3场完全不冲突的电影来观看， 返回最大的观影时间
// 如果无法选出3场完全不冲突的电影来观看，返回-1  如果一场电影的结束时间刚好是另一场电影的开始，那么这样也可以安排

public class _03_Code {

    // 因为这个时间是没有秒的，所以每一场电影的开始和结束都可以转化成int型数据，然后对其进行排列
    // 外层是电影开始时间递增排序，开始时间相同的，按照结束时间递增排序。
    // 设计这样一个递归函数f(minute, index, rest) minute是当前来到的时间（分钟），rest表示还剩几场电影要安排，
    // index表示现在要对index号的电影进行决策了
    // 因为分钟数最大就是1440，rest最大为3，index最大为2000，所以10^8以内能搞定

    public static int arrange(int[][] movies) {
        Arrays.sort(movies, (a, b) -> (a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]));
        return f(movies, 0, 0, 3);
    }


    // 该方法返回：当前时间为time，来到了i号电影做决策，还需要看rest场，返回能找到的最长观看时间
    private static int f(int[][] movies, int time, int i, int rest) {
        if (i == movies.length)
            return rest == 0 ? 0 : -1;
        // 不考虑当前场次电影
        int p1 = f(movies, time, i + 1, rest);
        int next = movies[i][0] >= time && rest > 0 ? f(movies, movies[i][1], i + 1, rest - 1)
                : -1;
        int p2 = next != -1 ? (movies[i][1] - movies[i][0] + next) : -1;
        return Math.max(p1, p2);
    }
    // ========================================================================================



    // 记忆化搜索 三个可变参数，其中time是可以优化的，并不一定要到1440，我们遍历所有电影，找到最晚的那场电影的结束时间
    public static int arrange2(int[][] movies) {
        int N = movies.length;
        Arrays.sort(movies, (a, b) -> (a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]));
        int latest = 0;
        for (int[] movie : movies)
            latest = Math.max(latest, movie[1]);
        int[][][] dp = new int[latest + 1][N][4];
        for (int[][] m : dp) {
            for (int[] a : m)
                Arrays.fill(a, -2);  // 用-2表示没算过
        }
        return g(movies, 0, 0, 3, dp);
    }


    // 该方法返回：当前时间为time，来到了i号电影做决策，还需要看rest场，返回能找到的最长观看时间
    private static int g(int[][] movies, int time, int i, int rest, int[][][] dp) {
        if (dp[time][i][rest] != -2)
            return dp[time][i][rest];
        if (i == movies.length)
            return rest == 0 ? 0 : -1;
        // 不考虑当前场次电影
        int p1 = g(movies, time, i + 1, rest, dp);
        int next = movies[i][0] >= time && rest > 0 ? g(movies, movies[i][1], i + 1, rest - 1, dp)
                : -1;
        int p2 = next != -1 ? (movies[i][1] - movies[i][0] + next) : -1;
        dp[time][i][rest] = Math.max(p1, p2);
        return dp[time][i][rest];
    }
    // ---------------------------------------------------------------------------------------------





    // 动态规划
    public static int arrangeMovies(int[][] movies) {
        int N = movies.length;
        Arrays.sort(movies, (a, b) -> (a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]));
        int latest = 0;
        for (int[] movie : movies)
            latest = Math.max(latest, movie[1]);
        int[][][] dp = new int[latest + 1][N + 1][4];
        // 先规定 i 用于遍历 latest；j 用于遍历 N；k 用于遍历 rest
        // 把 i、j、k 三个维度和三维坐标轴一一对应就比较好想象。
        for (int i = 0; i <= latest; i++){
            for (int k = 1; k < 4; k++)
                dp[i][N][k] = -1;
        }

        for (int j = N - 1; j >= 0; j--){
            for (int k = 0; k < 4; k++){
                for (int i = latest; i >= 0; i--){
                    dp[i][j][k] = dp[i][j + 1][k];
                    int next = movies[j][0] >= i && k > 0 ? dp[movies[j][1]][j + 1][k - 1] : -1;
                    int p2 = next != -1 ? (movies[j][1] - movies[j][0] + next) : -1;
                    dp[i][j][k] = Math.max(dp[i][j][k], p2);
                }
            }
        }
        return dp[0][0][3];
    }
}
