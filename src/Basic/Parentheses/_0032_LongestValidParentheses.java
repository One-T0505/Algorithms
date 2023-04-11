package Basic.Parentheses;

// leetCode32
// 给定一个只由左括号和右括号的字符串，返回最长的有效括号子串的长度。

// 0 <= s.length <= 3 * 10^4
// s[i] 为 '(' 或 ')'

public class _0032_LongestValidParentheses {

    // 看到子串，就要想起划分子串的一种思维定式：以每个元素作为结尾去找子串。
    // 遍历字符串，同样构造一张等长的缓存表。dp[i]表示以s[i]字符为结尾，向左能找到的最长有效长度。
    // 如果s[i]=='('，则dp[i]直接为0，因为'('作为结尾向左扩充不可能找到有效的匹配。只有s[i]==')'时，
    // 才有必要讨论。
    // 1.如果s[i]==')'，那么他应该去找哪个位置匹配呢？应该去找：pre == i - dp[i - 1] - 1；dp[i-1]表示的是以i-1
    //   作为结尾向左能找到的最长的长度，那么这部分我们就不用管了，只需要看pre是否为'('，如果是，则
    //   dp[i] = dp[i-1] + 2，相当于在外面又包了一层。但是又例外情况，比如：
    //   0  1
    //   (  )
    //   0  ?    此时dp[0]=0，dp[1]，用上面的公式，它应该找-1位置去匹配，所以，如果pre<0，那么也是无效，直接dp[1]=0
    //
    // 2.并且还要看看pre-1这个位置能找到多长的长度。比如：
    //    0  1  2  3  4  5
    //    (  )  (  (  )  )
    //    0  2  0  0  ?  ?
    // dp[4]先去找pre==3位置匹配，可以得到dp[4]=2；然后是dp[5]可以按照之前的步骤得到dp[5]=4，但是就结束了吗？
    // 应该再去pre-1这个地方去看看还有没有合法匹配.所以dp[5]=6，是最后结果。因为合法括号的位置关系可以是：
    // 假设A、B是一段合法匹配的括号   (A) 也可以是  BA

    public static int longestValidParentheses(String s) {
        if (s == null || s.length() < 2)
            return 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int[] dp = new int[N];
        int res = 0;
        int pre = 0;
        // dp[0] = 0
        for (int i = 1; i < N; i++) {
            if (chs[i] == ')') {
                pre = i - dp[i - 1] - 1;
                if (pre >= 0 && chs[pre] == '(')
                    dp[i] = dp[i - 1] + 2 + (pre > 0 ? dp[pre - 1] : 0);
            }
            res = Math.max(res, dp[i]);
        }
        return res;
    }



    // 方法2   最优解    时间复杂度O(N)  空间复杂度O(1)
    public static int longestValidParentheses2(String s) {
        if (s == null || s.length() < 2)
            return 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        // 分别记录左右括号的数量
        int left = 0;
        int right = 0;
        int res = 0;
        for (char c : chs) {
            if (c == '(')
                left++;
            else
                right++;
            if (left == right)
                res = Math.max(res, left << 1);
            else if (left < right) {
                left = 0;
                right = 0;
            }
        }
        left = 0;
        right = 0;

        for (int i = N - 1; i >= 0; i--){
            if (chs[i] == '(')
                left++;
            else
                right++;
            if (left == right)
                res = Math.max(res, left << 1);
            else if (left > right) {
                left = 0;
                right = 0;
            }
        }

        return res;
    }


    public static void main(String[] args) {
        String s = "(()";
        System.out.println(longestValidParentheses2(s));
    }
}
