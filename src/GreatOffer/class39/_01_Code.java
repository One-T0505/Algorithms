package GreatOffer.class39;


// 来自腾讯
// 给定一个只由0和1组成的字符串s,假设下标从1开始，规定i位置的字符价值v[i]计算方式如下:
//  1) i == 1 时，V[i]=1
//  2) i > 1 时，如果S[i] != S[i-1], V[i] = 1
//  3) i > 1时，如果S[i]== S[i-1], V[i] = V[i-1] + 1
// 你可以随意删除S中的字符，返回整个S的最大价值
// 字符串长度<= 5000

public class _01_Code {

    // 0 0 0 0 1 1 0 0 0  如果是这样的数组，那么将两个1删除能使整个的价值变高
    // 0 0 1 1 1 1 1 0 0  此时不删除中间的1价值更高

    public static int maxVal(String s) {
        if (s == null || s.length() == 0)
            return 0;
        int[] arr = new int[s.length()];
        int i = 0;
        for (char c : s.toCharArray())
            arr[i++] = c - '0';
        // 不管0位置是0还是1，下面的curValue都可以正确计算出，所以这样调用没问题
        return f(arr, 0, 0, 0);
    }

    // 设计一个递归函数f
    // 目前在arr[i...]上做选择，arr[i]的左侧最近的没被删除的数字是lastNum
    // 并且lastNum代表的价值是baseValue
    // 返回在arr[i...]上做选择，最终获得的最大价值
    // index -> 0 ~ 4999  lastNum -> 0 or 1  baseValue -> 1 ~ 5000
    // 5000*2*5000->5*10^7(过!)
    private static int f(int[] arr, int i, int lastNum, int baseValue) {
        if (i == arr.length)
            return 0;
        int curValue = lastNum == arr[i] ? baseValue + 1 : 1;
        // 保留当前位置的值
        int p1 = f(arr, i + 1, arr[i], curValue);
        // 删除当前位置的值
        int p2 = f(arr, i + 1, lastNum, baseValue);

        return Math.max(curValue + p1, p2);
    }


    // 改成动态规划
    public static int maxVal2(String s) {
        if (s == null || s.length() == 0)
            return 0;
        int N = s.length();
        int[] arr = new int[N];
        int i = 0;
        for (char c : s.toCharArray())
            arr[i++] = c - '0';
        // 2表示可变参数lastNum，lastNum只可能是0、1；baseValue最大值就为N
        int[][][] dp = new int[N + 1][2][N];
        // dp[N] 这个二维矩阵全部要设置为0，初始化本来就为0，所以不用管了
        for (int k = N - 1; k >= 0; k--) {

        }


        return dp[0][0][0];
    }
}
