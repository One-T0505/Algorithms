package GreatOffer.Interview;

// 给定一个数组 A（下标从 1 开始）包含 N 个整数：A1，A2，……，AN和一个整数 B。你可以从数组 A 中的任何
// 一个位置（下标为 i）跳到下标i+1，i+2，……，i+B的任意一个可以跳到的位置上。如果你在下标为 i 的位置上，
// 你需要支付 Ai 个金币。如果 Ai 是 -1，意味着下标为 i 的位置是不可以跳到的。
// 现在，你希望花费最少的金币从数组 A 的 1 位置跳到 N 位置，你需要输出花费最少的路径，依次输出所有经过的
// 下标（从 1 到 N）。
// 如果有多种花费最少的方案，输出字典顺序最小的路径。
// 如果无法到达 N 位置，请返回一个空数组。

// 1.路径 Pa1，Pa2，……，Pan是字典序小于 Pb1，Pb2，……，Pbm的，当且仅当第一个 Pai 和 Pbi 不同的 i 满足
//   Pai < Pbi，如果不存在这样的 i 那么满足 n < m。
// 2.A1 >= 0。A2, ..., AN（如果存在）的范围是 [-1, 100]。
// 3.A 数组的长度范围 [1, 1000].
// 4.B 的范围[1, 100].

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MinCostPath {

    // 这里有一个很重要的边界，比如给定数组 [0, 0, 0, 0, 0]  B==3
    // 表示每个点的代价都为0，这样我就可以从 1->3->5 到达终点  但是这不是最终结果
    // 而应该是 1->2->3->4->5   虽然最长，但是字典序最小，按照题目给的字典序规则来说。
    // 我们先完成一个方法，即到终点时的最小代价是多少，不管路径，也不管字典序，只用求到终点的最小代价。
    private static int minCost(int[] coins, int maxJump) {
        if (coins == null || coins.length == 0)
            return 0;
        int N = coins.length;
        // 如果起点或终点不可达
        if (coins[0] == -1 || coins[N - 1] == -1)
            return -1;
        int[] dp = new int[N]; // dp[i]表示到i位置的最小代价，如果i位置不可达，则直接填-1
        dp[0] = 0;
        for (int i = 1; i < N; i++) {
            dp[i] = Integer.MAX_VALUE;
            if (coins[i] != -1) {
                // 为什么要用max呢？比如当前在4位置，但是jump很大，为7，总不能去负数位置吧
                for (int pre = Math.max(0, i - maxJump); pre < i; pre++) {
                    if (dp[pre] != -1)
                        dp[i] = Math.min(dp[i], dp[pre] + coins[pre]);
                }
            }
            dp[i] = dp[i] == Integer.MAX_VALUE ? -1 : dp[i];
        }
        return dp[N - 1];
    }


    // 下面就是考虑路径以及字典序的方法，先说两个结论，这两个结论的简单证明写在最下面了。这两个结论的语境是：
    // 起点是0，终点位置为i， 形成的两条路径分别是 (1,...,p, i)  和  (1,...,q, i)
    // 其中 p  q 是路径的倒数第2个点，两条路径的结点数分别为 m  n
    // 其代价分别是 costp==coins[0] + ... + coins[p]   costq==coins[0] + ... + coins[q]
    //
    // 1.如果两个都是最优代价并且两条路径的结点数一样多的时候，倒数第二个点的位置更靠前的路径是整体字典序更小的结果
    //   即: costp == costq && m == n  那么返回 p < q ? 路径1 : 路径2
    // 2.如果两个都是最优代价并且两条路径的结点数不一样多的时候，结点数更多的路径是字典序更小的结果

    public List<Integer> cheapestJump(int[] coins, int maxJump) {
        int N = coins.length;
        int[] best = new int[N]; // best[i] 表示到i位置的最低代价
        int[] last = new int[N]; // last[i] 表示在到i位置选择最优代价的路径下，倒数第2点的位置
        int[] size = new int[N]; // size[i] 表示在到i位置选择最优代价的路径下，路径的结点数量

        Arrays.fill(best, Integer.MAX_VALUE);
        Arrays.fill(last, -1);
        best[0] = 0;
        for (int i = 0; i < N; i++) {
            if (coins[i] != -1) {
                for (int j = Math.max(0, i - maxJump); j < i; j++) {
                    if (coins[j] != -1) {
                        int cur = best[j] + coins[j];
                        // 如果当前代价优于已知代价  或者  代价相等，但是当前发现的路径结点数量更多
                        // 这两种情况下，是要更新为最新的状态的  其他情况下都不更新
                        // 当前找到的代价如果小于已知的，那没什么好说的，必然会更新的，代价是最重要的，只要小了，
                        // 就根本不用管什么字典序了；
                        // 当代价相等时，我们就需要考虑是否需要替换了，如果 size[j] + 1 > size[i]
                        // 表示：从起点到i，且倒数第2个为j的路径的总数量 < 我们目前找到的这条路径的结点数量
                        // 那就换，遵循结论2
                        // 那结论1怎么好像没用过呢？事实上已经用了，只不过比较隐蔽。
                        // 使用结论1的前提必须是：cur == best[i] && size[i] - 1 == size[j]
                        // 然后我们选择，倒数第2个点更靠前的位置；此时我们的倒数第2个点是j，还有一个就是已经
                        // 记录过的路径，试想一下，为什么已经记录过了？那必然是在j之前处理的呀，所以这不就天然
                        // 符合选择更靠前的吗？所以现在是j了，他比已知的我们记录的位置更靠后，所以无需改变。
                        if (cur < best[i] || (cur == best[i] && size[i] - 1 < size[j])) {
                            best[i] = cur;
                            last[i] = j;
                            size[i] = size[j] + 1;
                        }
                    }
                }
            }
        }
        LinkedList<Integer> path = new LinkedList<>();
        for (int cur = N - 1; cur >= 0; cur = last[cur]) {
            path.addFirst(cur + 1); // 因为题目要求下标从1开始
        }
        return path.getFirst() != 1 ? new LinkedList<>() : path;
    }
}
