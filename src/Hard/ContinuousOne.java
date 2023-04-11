package Hard;

// leetCode600
// 给定一个正整数 n ，请你统计在 [0, n] 范围的非负整数中，有多少个整数的二进制表示中不存在连续的 1 。

// 1 <= n <= 10^9

public class ContinuousOne {

    // 首先n的上限是10^9，说明算法的时间复杂度不可能再和n的数值有关系了，可以猜想是和n的数位有关。
    // 这是一道数位dp的题目。先尝试写出暴力递归尝试

    // 我们递归的整体思路是：以n的二进制形式作为尝试的载体。
    // 我们尝试每位设置成0、1，尝试所有的组合，但是最终的数值不能超过n，然后计算所有符合情况的数量。
    //
    //       5 4 3 2 1 0
    // n     1 0 1 1 1 0    默认从最低位开始编号
    // 但是我们尝试的数位是从最高位到最低位的
    // 当前来到了第i位做决定， pre表示第 i+1 位的决定是0还是1   因为是从高位往右尝试的  所以上一位是i+1位置的
    // alreadyLess 表示：已经做完决定的N-1～i+1位的数值是否 < 对应n的N-1～i+1位
    // 如果我们之前做的决定是 110，当前来到了2位置， 那么110 > n对应的101  此时alreadyLess==false
    // 为什么要有这个参数？因为来到i位置做决定时不是随心所欲的，我们的决定要保证不能超过上限n，这样才有意义。

    private static int f(int pre, boolean alreadyLess, int i, int n) {
        if (i == -1) // 做完全部决定了，说明当前的尝试可行，返回1
            return 1;
        if (pre == 1) { // 前一位的决定是1，那么当前位别无选择只能设置为0，因为不能有连续的1
            // 因为又做了i位置的决定，去往i-1位置时，要重新定义 已经做过决定的N-1～i 和 对应的n的N-1～i
            // 的大小关系
            // 1.如果N-1～i+1的决定了已经 < n的N-1～i+1了，那么后面不管做什么决定，都是小于的关系
            //   比如n的高三位是 101  我们做的决定是 100  后面做什么决定，最终组成的数都不可能超过n了
            // 2.如果alreadyLess==false，有两种情况，要不就是相等，要么就大于，大于是不可能的，如果大于了，
            //   就可以不用继续往下尝试了，已经超过了n。所以只是相等。相等的情况，
            //   n & (1 << i)) != 0  是为了判断n的i位置是否为1，如果为1，而我们做的决定必然是0，
            //   做过的决定是相等  正在做的决定是 0 < 1  那么整体从N-1～i就是对应 < n的。
            boolean relation = alreadyLess || ((n & (1 << i)) != 0);
            // 到i-1位置，pre就是i位置做的决定0
            return f(0, relation, i - 1, n);
        } else { // pre是0  那么我们就需要分析当前位置设为0和1的两种情况
            // 可能性1  继续做0的决定
            // 如果之前已经 < 了，那依然保持同样的关系
            // 如果 == 了，那么判断n的i位是否为1，如果为1，而我又做的0的决定，所以设为true
            // 如果n的i位为0，我也是0，那依然保持false
            boolean relation = alreadyLess || ((n & (1 << i)) != 0);
            int p1 = f(0, relation, i - 1, n);

            // 可能性2  做1的决定
            int p2 = 0;
            // 为什么p2的设置还有条件限制？
            // 1.alreadyLess==true 说明之前已经小了，那么i位置做什么决定都无所谓，肯定不会超过n
            // 2.alreadyLess==false  说明之前的相等，那么就要分析第i位的情况了，因为我做的决定是1
            //   所以n的i位如果是0的话，那么就直接超过n了，说明我此时i位置做的1决定根本不可行，所以这种情况
            //   下是不能设置p2的，于是将p2初始值设为0，就是为了这种情况。
            //   只有n的i位置是1的情况下，我才能继续做1的决定，因为这样，N-1～i做的决定顶多是和n相等，后面还有希望
            // 所以，通过上面分析发现，alreadyLess==true时，i位置做什么决定后，关系依然是true
            // 如果alreadyLess==false，那就是相等，然后i位置的决定和n的i位置都是1，依然维持了相等，
            // 所以下一步传入时只需要将上一步的alreadyLess原样传入即可
            if (alreadyLess || ((n & (1 << i)) != 0))
                p2 = f(1, alreadyLess, i - 1, n);

            return p1 + p2;
        }
    }


    // 主方法
    public static int findIntegers(int n) {
        // 查找n最高位的1在哪，这样接下来尝试的时候就可以从该位置往右尝试了
        int i = 31; // int的最高位
        for (; i >= 0; i--) {
            if ((n & (1 << i)) != 0)
                break;
        }
        // 这里参数的设置就有讲究了
        // pre设置为0，你可以理解成我们找到n最高位的1之前是0  也可以理解成 只有设置为0，我们第一个尝试的位置
        // 才可以任意设置0或1，没有限制，因为你想想第一个数有什么限制呢，作为开头，是没有包袱的。
        // alreadyLess == false   可以看作前面都是0，表示相等，所以并不小于
        return f(0, false, i, n);
    }
    // 时间复杂度：O(2 * 2 * logn)  pre只有两种值 alreadyLess是布尔型  i是n的二进制有效位数是 log2(n)
    // ===========================================================================================





    // 上面的尝试是正确的，只是超时了，所以需要改成记忆化搜索
    public static int findIntegers2(int n) {
        int i = 31;
        for (; i >= 0; i--) {
            if ((n & (1 << i)) != 0)
                break;
        }
        // for循环出来之后，i表示，n最高位的1，在哪？
        // 从这个位置，往右边低位上走！
        // 这里有一个问题就是三个可变参数，两个是int，一个是布尔，没办法直接生成三维int表
        // 所以我们用数字0表示false，1表示true
        int[][][] dp = new int[2][2][i + 1];
        // 因为范围是[0, n]  给的数>=1，所以说最小的1，也会有答案2个，所以dp生成好后，就用默认值0
        // 就可以表示该递归没算过，而不用重新全部填充成别的值来表示没计算过
        return g(0, 0, i, n, dp);
    }

    private static int g(int pre, int alreadyLess, int i, int n, int[][][] dp) {
        if (i == -1)
            return 1;
        if (dp[pre][alreadyLess][i] != 0)
            return dp[pre][alreadyLess][i];
        int res = 0;
        int relation = Math.max(alreadyLess, (n & (1 << i)) != 0 ? 1 : 0);
        if (pre == 1) { // 前一位的决定是1，那么当前位别无选择只能设置为0，因为不能有连续的1
            // 到i-1位置，pre就是i位置做的决定0
            res = g(0, relation, i - 1, n, dp);
        } else { // pre是0  那么我们就需要分析当前位置设为0和1的两种情况
            int p1 = g(0, relation, i - 1, n, dp);
            // 可能性2  做1的决定
            int p2 = 0;
            if (alreadyLess == 1 || ((n & (1 << i)) != 0))
                p2 = g(1, alreadyLess, i - 1, n, dp);

            res = p1 + p2;
        }
        dp[pre][alreadyLess][i] = res;
        return res;
    }


    // 在写上面代码的时候犯了一个不起眼的错误：就是 (n & (1 << i)) != 0
    // 该表达式是用来判断 n的二进制形式从低位到高位的第i位上是否为1
    // 但是我之前的错误写法是: (n & (1 << i)) == 1  来判断
    //
    //    4  3  2  1  0
    //    0  1  0  0  1
    // &  0  1  0  0  0
    //    0  1  0  0  0  != 1     用1000来判断1001的第3位是否为1，其结果并不是想当然的1，对应位&完之后
    //                            如果得到了1，那它依然会保持在原有数位上。

}