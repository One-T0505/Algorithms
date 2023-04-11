package class13;

// leetCode87
// 使用下面描述的算法可以扰乱字符串 s 得到字符串 t ：
//  如果字符串的长度为 1 ，算法停止
//  如果字符串的长度 > 1 ，执行下述步骤：
//   1.在一个随机下标处将字符串分割成两个非空的子字符串。即，如果已知字符串 s ，则可以将其分成两个子字符串 x 和 y ，
//     且满足 s = x + y 。
//   2.随机决定是要「交换两个子字符串」还是要「保持这两个子字符串的顺序不变」。即，在执行这一步骤之后，s 可能是
//     s = x + y 或者 s = y + x 。
//   3.在 x 和 y 这两个子字符串上继续从步骤 1 开始递归执行此算法。
// 给你两个长度相等的字符串 s1 和 s2，判断 s2 是否是 s1 的扰乱字符串。如果是，返回 true ；否则，返回 false 。


public class ScrambleString {

    // 思路：给了两个字符串，所以要很快想到动态规划中的样本对应模型。定义一个递归函数f(s1,l1,r1,s2,l2,r2)，
    // 该方法的作用是判断s1[l1..r1]和s2[l2..r2]是否为扰乱串。
    // 一定保证l1～r1 和 l2～r2等长
    // 可能性如何划分？  情况1：
    //   s1      l1   |       r1      假设s1的切割方式如图所示
    //   s2      l2   |       r2
    //
    // 第一种可能就是s1的左部分和s2的左部分是扰乱串 && s1的右部分和s2的右部分是扰乱串，这样s1[l1..r1]才和
    // s2[l2..r2]是扰乱串
    //
    //  情况2：
    //   s1      l1   |       r1      假设s1的切割方式如图所示
    //   s2      l2       |   r2
    //
    // 第二种可能是：s1的左部分和s2的右部分是扰乱串 && s1的右部分和s2的左部分是扰乱串，这样整体才是
    //
    // 在s1的l1..r1上要枚举第一刀的位置。


    // 主方法
    public static boolean isScramble(String s1, String s2) {
        // 去除无效参数
        if ((s1 == null) ^ (s2 == null))  // 一个为空，一个不为空
            return false;
        if (s1 == null)  // 两个都为空
            return true;
        // 执行到这里说明两个都不为空
        if (s1.length() != s2.length()) // 长度不等
            return false;
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        if (!sameComponents(c1, c2))  // 长度相等，但是组成部分不同
            return false;
        if (s1.equals(s2))  // 长度相等，并且一一对应相等
            return true;
        return f(c1, 0, c1.length - 1, c2, 0, c2.length - 1);
    }


    // 传入的参数必然是两个长度相等的字符串
    private static boolean sameComponents(char[] s1, char[] s2) {
        int[] component = new int[256];
        for (char c : s1)
            component[c]++;
        for (char c : s2) {
            if (--component[c] < 0)  // s1中不存在的字符，在component中对应位置都为0
                return false;
        }
        return true;
    }

    private static boolean f(char[] s1, int l1, int r1, char[] s2, int l2, int r2) {
        if (l1 == r1)
            return s1[l1] == s2[l2];
        // leftEnd 表示s1切割后左部分的结尾位置；因为左右部分都至少有1个字符，所以leftEnd<r1
        for (int leftEnd = l1; leftEnd < r1; leftEnd++) {
            // 可能性1：左对左 右对右
            boolean p1 = f(s1, l1, leftEnd, s2, l2, l2 + leftEnd - l1) &&
                    f(s1, leftEnd + 1, r1, s2, l2 + leftEnd - l1 + 1, r2);
            // 可能性2：左对右 右对左
            boolean p2 = f(s1, l1, leftEnd, s2, r2 - leftEnd + l1, r2) &&
                    f(s1, leftEnd + 1, r1, s2, l2, r2 - leftEnd + l1 - 1);
            // 只要两种可能性有一个对了，整体就是扰乱串
            if (p1 || p2)
                return true;
        }
        return false;
    }
    // ==============================================================================================


    // 改成记忆化搜索版本。发现有4个可变参数，如果申请4维缓存也可以，但是有点浪费空间。看看能不能优化下。
    // l1、r1、l2、r2四个变量说的就是s1和s2上一段等长的位置，我们可以用l1、l2、N这三个变量描述同样一件事情

    public static boolean isScrambleV2(String s1, String s2) {
        // 去除无效参数
        if ((s1 == null) ^ (s2 == null))  // 一个为空，一个不为空
            return false;
        if (s1 == null)  // 两个都为空
            return true;
        // 执行到这里说明两个都不为空
        if (s1.length() != s2.length()) // 长度不等
            return false;
        char[] c1 = s1.toCharArray();
        int N = c1.length;  // c1、c2长度相等，只用一个即可
        char[] c2 = s2.toCharArray();
        if (!sameComponents(c1, c2))  // 长度相等，但是组成部分不同
            return false;
        if (s1.equals(s2))  // 长度相等，并且一一对应相等
            return true;
        // dp[i][j][k] = 0 g(i,j,k)状态之前没有算过的
        // dp[i][j][k] = -1 g(i,j,k)状态之前算过的,返回值是false
        // dp[i][j][k] = 1 g(i,j,k)状态之前算过的,返回值是true
        int[][][] dp = new int[N][N][N + 1];
        return g(c1, 0, c2, 0, N, dp);
    }

    private static boolean g(char[] s1, int l1, char[] s2, int l2, int N, int[][][] dp) {
        if (dp[l1][l2][N] != 0)
            return dp[l1][l2][N] == 1;
        boolean res = false;
        if (N == 1) {
            res = s1[l1] == s2[l2];
        } else { // leftEnd 表示s1切割后左部分的结尾位置；因为左右部分都至少有1个字符，所以leftEnd<r1
            for (int leftEnd = l1; leftEnd < l1 + N - 1; leftEnd++) {
                int leftNum = leftEnd - l1 + 1;
                int rightNum = N - leftNum;
                // 可能性1：左对左 右对右
                boolean p1 = g(s1, l1, s2, l2, leftNum, dp) &&
                        g(s1, leftEnd + 1, s2, l2 + leftEnd - l1 + 1, rightNum, dp);
                // 可能性2：左对右 右对左
                boolean p2 = g(s1, l1, s2, l2 + N + l1 - leftEnd - 1, leftNum, dp) &&
                        g(s1, leftEnd + 1, s2, l2, rightNum, dp);
                // 只要两种可能性有一个对了，整体就是扰乱串
                if (p1 || p2) {
                    res = true;
                    break;
                }
            }
        }
        dp[l1][l2][N] = res ? 1 : -1;
        return res;
    }
    // ============================================================================================


    // 动态规划
    public static boolean isScrambleV3(String s1, String s2) {
        // 去除无效参数
        if ((s1 == null) ^ (s2 == null))  // 一个为空，一个不为空
            return false;
        if (s1 == null)  // 两个都为空
            return true;
        // 执行到这里说明两个都不为空
        if (s1.length() != s2.length()) // 长度不等
            return false;
        char[] c1 = s1.toCharArray();
        int N = c1.length;  // c1、c2长度相等，只用一个即可
        char[] c2 = s2.toCharArray();
        if (!sameComponents(c1, c2))  // 长度相等，但是组成部分不同
            return false;
        if (s1.equals(s2))  // 长度相等，并且一一对应相等
            return true;
        boolean[][][] dp = new boolean[N][N][N + 1];
        for (int l1 = 0; l1 < N; l1++) {
            for (int l2 = 0; l2 < N; l2++)
                dp[l1][l2][1] = c1[l1] == c2[l2];
        }
        for (int size = 2; size <= N; size++) {
            for (int l1 = 0; l1 <= N - size; l1++) {
                for (int l2 = 0; l2 <= N - size; l2++) {
                    for (int leftEnd = l1; leftEnd < l1 + size - 1; leftEnd++) {
                        int leftNum = leftEnd - l1 + 1;
                        int rightNum = size - leftNum;
                        // 可能性1：左对左 右对右
                        boolean p1 = dp[l1][l2][leftNum] &&
                                dp[leftEnd + 1][l2 + leftEnd - l1 + 1][rightNum];
                        // 可能性2：左对右 右对左
                        boolean p2 = dp[l1][l2 + size + l1 - leftEnd - 1][leftNum] &&
                                dp[leftEnd + 1][l2][rightNum];
                        // 只要两种可能性有一个对了，整体就是扰乱串
                        if (p1 || p2) {
                            dp[l1][l2][size] = true;
                            break;
                        }
                    }
                }
            }
        }
        return dp[0][0][N];
    }

    public static void main(String[] args) {
        String s1 = "great";
        String s2 = "rgeat";
        System.out.println(isScrambleV2(s1, s2));
    }
}
