package Basic.DynamicProgramming;

// 司机调度时间限制: 3000MS 内存限制: 589824KB 题目描述:
// 正值下班高峰时期，现有可载客司机数2N人，调度中心将调度相关司机服务A、B两个出行高峰区域。第i个司机前往服务A区域
// 可得收入为 income[i][0]，前往服务B区域可得收入为income[i][1]。返回将每位司机调度完成服务后，所有司机总可得
// 的最高收入金额，要求每个区域都有N位司机服务。

public class ScheduleDriver {


    // 暴力递归尝试
    public static int bestStrategy(int[][] income){
        if (income == null || income.length == 0 || (income.length & 1) != 0)
            return 0;
        int all = income.length;
        int half = all >> 1;
        return f(income, 0, half);
    }

    private static int f(int[][] income, int i, int rest) {
        if (i == income.length)
            return 0;
        if (rest == 0)  // 只能去B
            return f(income, i + 1, rest) + income[i][1];
        // 只能去A    i - ((income.length >> 1) - rest) == income.length >> 1
        // income.length >> 1 这个是应该去A的人数，  (income.length >> 1) - rest  是已经去A的人数
        // i是已经分配司机的数量  i - ((income.length >> 1)   就是已经分配去B的司机数量
        // 如果等于总数量的一半，说明B已经满了，只能去A    表达式还可以简化
        if (i + rest == income.length)
            return f(income, i + 1, rest - 1) + income[i][0];
        // 选当前司机去A
        int p1 = f(income, i + 1, rest - 1) + income[i][0];
        // 选当前司机去B
        int p2 = f(income, i + 1, rest) + income[i][1];
        return Math.max(p1, p2);
    }



    // 改成动态规划
    public static int bestStrategy2(int[][] income){
        if (income == null || income.length == 0 || (income.length & 1) != 0)
            return 0;
        int all = income.length;
        int half = all >> 1;
        int[][] dp = new int[all + 1][half + 1];
        // dp[all] 这一行都设置为0
        for (int i = all - 1; i >= 0; i--){
            dp[i][0] = dp[i + 1][0] + income[i][1];
            for (int j = 1; j <= half; j++){
                dp[i][j] = dp[i + 1][j - 1] + income[i][0];
                if (i + j != all)
                    dp[i][j] = Math.max(dp[i][j], dp[i + 1][j] + income[i][1]);
            }
        }
        return dp[0][half];
    }




    // 状态压缩的动态规划，缓存不需要一张矩阵，只需要一个数组即可
    public static int bestStrategy3(int[][] income){
        if (income == null || income.length == 0 || (income.length & 1) != 0)
            return 0;
        int all = income.length;
        int half = all >> 1;
        int[] dp = new int[half + 1];
        for (int i = all - 1; i >= 0; i--){
            int pre = dp[0];
            dp[0] = dp[0] + income[i][1];
            for (int j = 1; j <= half; j++){
                int tmp = dp[j];
                dp[j] = pre + income[i][0];
                pre = tmp;
                if (i + j != all)
                    dp[j] = Math.max(dp[j], pre + income[i][1]);
            }
        }
        return dp[half];
    }


    public static void main(String[] args) {
        int[][] income = {{4, 9}, {1, 3}, {5, 1}, {3, 4}, {3, 9}, {6, 8}, {10, 5}, {3, 3}};
        System.out.println(bestStrategy2(income));
        System.out.println(bestStrategy(income));
        System.out.println(bestStrategy3(income));
    }
}
