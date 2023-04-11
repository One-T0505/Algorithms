package class04;

// leetCode97
// 给两个字符串s1、s2，作为原始串。再给一个目标串all，判断all是不是s1和s2的交错串。
// eg：假设s1="abcd"   s2="hjk"   all="ahjbkcd"  all是str1和str2的交错串。因为all中s1的相对次序保持不变，
// str2的相对次序也不变。

public class IntersectString {

    // 错误示范。该方法为什么不行，因为只适用于s1和s2中没有重复字符。比如s1="aaabf"   s2="aacce"
    // all="aaaabccfe" 这样指针走向就有问题了。
    public static boolean isIntersected(String s1, String s2, String all) {
        if (s1 == null || s1.length() == 0 || s2 == null || s2.length() == 0 ||
                all == null || all.length() == 0 || all.length() != s1.length() + s2.length())
            return false;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        char[] chars = all.toCharArray();
        int p1 = 0, p2 = 0;
        for (int i = 0; i < chars.length; i++) {
            if (p1 < chars1.length && chars[i] == chars1[p1])
                p1++;
            else if (p2 < chars2.length && chars[i] == chars2[p2])
                p2++;
            else
                return false;
        }
        return true;
    }


    // 动态规划。定义一张二维缓存表cache，行数表示s1的长度，列数表示s2的长度。cache[i][j]表示：从s1的0位置开始拿出i个，
    // 从s2的0位置开始拿出j个字符能不能以交错形式搞定all字符串中前i+j个字符。
    public static boolean dp(String s1, String s2, String all) {
        if (s1 == null || s2 == null || all == null || all.length() != s1.length() + s2.length())
            return false;
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        char[] c3 = all.toCharArray();
        int L1 = c1.length;
        int L2 = c2.length;
        boolean[][] cache = new boolean[L1 + 1][L2 + 1];
        cache[0][0] = true;
        for (int i = 1; i <= L2; i++) {   // 先处理第0行
            if (c2[i - 1] != c3[i - 1])
                break;
            cache[0][i] = true;
        }
        for (int i = 1; i <= L1; i++) { // 再处理第0列
            if (c1[i - 1] != c3[i - 1])
                break;
            cache[i][0] = true;
        }
        for (int i = 1; i <= L1; i++) {
            for (int j = 1; j <= L2; j++) {
                // all中前i+j个字符中，最后一个不是s1[i-1]就是s2[j-1]，从这样的角度去分析
                // 情况1：总字符串最后一个位置i+j-1的字符如果和s1[i-1]相等，那么就相当于是在问：从s1中拿出i-1个字符，
                //       从s2中依然拿出j个字符能否以交错形式搞定all中前i+j-1个字符，对应的就是cache[i-1][j]的值
                // 情况2：总字符串最后一个位置i+j-1的字符如果和s2[j-1]相等，那么就相当于是在问：从s1中依然拿出i个字符，
                //       从s2中依然拿出j-1个字符能否以交错形式搞定all中前i+j-1个字符，对应的就是cache[i][j-1]的值
                // 如果两种情况中一种，cache[i][j]才能设为true
                if ((c3[i + j - 1] == c1[i - 1] && cache[i - 1][j]) ||
                        (c3[i + j - 1] == c2[j - 1] && cache[i][j - 1]))
                    cache[i][j] = true;
            }
        }
        return cache[L1][L2];
    }

    public static void main(String[] args) {
        String s1 = "aabcc";
        String s2 = "dbbca";
        String s3 = "aadbbcbcac";
        System.out.println(dp(s1, s2, s3));
    }
}
