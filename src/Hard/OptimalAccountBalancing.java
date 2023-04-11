package Hard;

// leetCode465
// 给你一个表示交易的数组 transactions ，其中 transactions[i] = [fromi, toi, amounti] 表示
// ID = fromi 的人给 ID = toi 的人共计 amounti 。
// 请你计算并返回还清所有债务的最小交易笔数。

// 输入：transactions = [[0,1,10],[2,0,5]]
// 输出：2
// 解释：#0 给 #1 $10。 #2 给 #0 $5 。需要进行两笔交易。一种结清债务的方式是 #1 给 #0 和 #2 各 $5 。

// 输入：transactions = [[0,1,10],[1,0,1],[1,2,5],[2,0,5]]
// 输出：1
// 解释：#0 给 #1 $10 。#1 给 #0 $1 。#1 给 #2 $5 。#2 给 #0 $5 。
//      因此，#1 只需要给 #0 $4 ，所有的债务即可还清。


import java.util.Arrays;
import java.util.HashMap;

public class OptimalAccountBalancing {

    // 首先这个数组给的就很让人头晕，我们需要给的数组做出一种清晰的预处理结构。
    // 如果在数组中。0号人一共借出去了20，那么我们就会给0号人-20的tag，表示他的账还需要20块才能填平
    // 如果3号人的tag是30，就说明3从其他人手里一共借了30，需要分还出去30才能平账。如果一个人的tag是0，
    // 说明他从别人借的钱和给出去的钱相等，本来他的账就是平的，所以这样的人不用管，我们只需要把那些账不平的人
    // 先筛选出来。并且还应该知道，这些所有账不平的人的tag的累加和必然为0！！！
    // debts方法就是做数据预处理的。

    public static int minTransfers(int[][] transactions) {
        int[] debts = debts(transactions);
        int N = debts.length;
        // debts数组中不可能有元素为0。看完了下面的递归讲解后，再回来看这个公式。
        // 因为debts是所有不为0的数组成的，并且累加和为0，所以将他们分成最多的集合时，没有剩余元素没进集合。
        // 假设 f(debts, (1 << N) - 1, 0, N) == x  即x个集合
        // A11, A12, ... , A1t  第1个集合 有t个元素
        // A21, A22, ... , A2m  第2个集合 有m个元素
        // Ax1, Ax2, ... , Axn  第x个集合 有n个元素
        // t + m + ... + n == N  并且每个集合需要元素数-1次交易就可以平账
        // 所以总的交易次数就是 (t - 1) + (m - 1) + ... + (n - 1) == N - x
        return N - f(debts, (1 << N) - 1, 0, N);
    }


    // 这个递归函数的功能很复杂  先搞清楚，debts里记录的是每个账不平的人的钱财情况
    // 我们如何才能解决题目的问题，即让总的交易笔数最少呢？
    // 我们希望在debts中划分出尽量多的集合，每个集合里的人的tag的累加和都为0，就说明这个集合内部就可以自行
    // 解决完平账的问题。比如：[10, -1, -9, 3, -3] 就可以分成一个集合，这样他们内部就可以把问题解决。
    // 我们想用递归，那就必须找到一个途径将问题规模缩小，这样才可能递归。
    // 我们想到的方法是：将最后一个元素拉出来，让剩下的元素去拼集合，看最多能拼凑出几个。
    // 主方法调用该函数时，传入的是 f(debts, (1 << N) - 1, 0, N) 来解释下
    // N是debts的长度，(1 << N) - 1 其实就是让低位的N位数全部为1，这就表示这N个人都在的情况，用0表示某个位置的
    // 人不在。比如：11101110  这就说明debts[0]和debts[4]的数据不可用，因为我们采取的是不考虑最后一个人的清况下，
    // 所以，势必在递归越来越深的时候0越来越多。
    // 当我们确定了用不考虑最后一个人的情况下讨论可能性的时候，就说明了我们需要枚举最后一个人是谁，这样才能考虑全面
    // sum表示debts中所有可用的数据的累加和，如果全部可用，那sum就是0。 最后一个N是固定参数。
    // 方法的返回值就是：在debts中所有可用的数据里，能划分出的最大集合数量。
    // 比如：[3, -1, -2, -3, 1, 2, -3]  可以划分成 [3, -3] [1, -1] [2, -2] 3
    //      也可以划分成 [3, -1, -2] [-3, 1, 2] -3
    // 我们会选第一种，因为可以划分出3个集合
    private static int f(int[] debts, int set, int sum, int N) {
        // 这个表达式就是想问 set是否只有1个1了，如果是，就说明可用的数据只有1个了，又因为debts中的数据不可能
        // 为0，所以只有1个数的情况下是划分不出集合的
        if ((set & (set - 1)) == 0)
            return 0;
        int val = 0;
        int max = 0;
        // 枚举最后一个人是谁
        for (int i = 0; i < N; i++) {
            val = debts[i];
            // 这说明i号位置的那个人是可用的，那么我们才会考虑他作为最后一个人
            if ((set & (1 << i)) != 0) {
                // set ^ (1 << i) 就是将i位置设置为0，那么可用的数据的累加和就要减去这个val
                max = Math.max(max, f(debts, set ^ (1 << i), sum - val, N));
            }
        }
        // 此时max已经是不考虑某位的情况下能划分的最大集合数了，那么我们还要考虑把最后一个人加进去是否
        // 会对结果有影响？如果sum是0，而此时去掉了一个人之后，能凑出的集合数不管是多少，必然都会有人进不了集合
        // 这些进不了集合的人和这个没被考虑进去的最后一个人必然可以凑出一个新的集合，所以sum==0的时候，max要+1
        // 比如：[3, -1, -2, -3, 4, -1]  sum==0  如果把-1当作最后一个暂且不考虑，那么就会分成：
        // [3, -1, -2]  -3  4   这没进集合的元素和没被考虑的必然可以再凑出一个集合。
        return sum == 0 ? max + 1 : max;
    }


    private static int[] debts(int[][] trans) {
        HashMap<Integer, Integer> dp = new HashMap<>();
        for (int[] trade : trans) {
            dp.put(trade[0], dp.getOrDefault(trade[0], 0) - trade[2]);
            dp.put(trade[1], dp.getOrDefault(trade[1], 0) + trade[2]);
        }
        // 筛选出账不平的人
        int N = 0;
        for (int val : dp.values()) {
            if (val != 0)
                N++;
        }
        int[] res = new int[N];
        int i = 0;
        for (int val : dp.values()) {
            if (val != 0)
                res[i++] = val;
        }
        return res;
    }
    // ============================================================================================



    // 分析上面的递归会发现，可变参数只有1个，f(int[] debts, int set, int sum, int N)
    // 只有set，因为sum完全是由set来决定的，并且，有了set和debts、，sum我们就能知道是多少了，不需要sum
    // 记忆化搜索
    public static int minTransfers2(int[][] transactions) {
        int[] debts = debts(transactions);
        int N = debts.length;
        int[] dp = new int[1 << N];
        Arrays.fill(dp, -1);
        return N - g(debts, (1 << N) - 1, 0, N, dp);
    }


    private static int g(int[] debts, int set, int sum, int N, int[] dp) {
        if (dp[set] != -1)
            return dp[set];
        if ((set & (set - 1)) == 0)
            dp[set] = 0;
        else {
            int max = 0;
            // 枚举最后一个人是谁
            for (int i = 0; i < N; i++) {
                // 这说明i号位置的那个人是可用的，那么我们才会考虑他作为最后一个人
                if ((set & (1 << i)) != 0) {
                    // set ^ (1 << i) 就是将i位置设置为0，那么可用的数据的累加和就要减去这个val
                    max = Math.max(max, g(debts, set ^ (1 << i), sum - debts[i], N, dp));
                }
            }
            dp[set] = sum == 0 ? max + 1 : max;
        }
        return dp[set];
    }
}
