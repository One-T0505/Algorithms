package class05;


// leetCode72
// 编辑代价问题。  非常重要
// 对于字符串中字符的处理方式，定义三种操作以及相应代价：1>添加add a;  2>删除delete d;  3>替换replace r;
// 现在给两个任意的字符串s1和s2，请问s1编辑成s2的最小代价是多少？

public class EditDistance {

    // 这道题目是非常经典的动态规划中的样本尝试模型。定义一张二维缓存表cahce，cache[i][j]表示：s1中前i个字符
    // 编辑成s2中前j个字符的最小代价。
    public static int editDistance(String s1, String s2, int a, int d, int r) {
        // 只要不是两个都不为空，返回无效值
        if (!(s1 != null && s2 != null))
            return -1;
        // 两个都不为空
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        int N = c1.length;
        int M = c2.length;
        int[][] cache = new int[N + 1][M + 1];
        // cache[0][0] = 0 可以省略 因为初始值就是0
        for (int i = 1; i <= M; i++)
            cache[0][i] = a * i;      // s1前0个字符就是空串""，只能添加
        for (int i = 1; i <= N; i++)
            cache[i][0] = d * i;      // s1取前i个编辑成s2的前0个字符，只能删除

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                // 情况1：用s1的前i-1个字符搞定s2的前j个字符，然后删除s1的第i个字符
                int p1 = cache[i - 1][j] + d;
                // 情况2：用s1的前i个字符搞定s2的前j-1个字符，然后添加s2的第j个字符
                int p2 = cache[i][j - 1] + a;
                // 情况3：用s1的前i-1个字符搞定s2的前j-1个字符，然后需要判断s1的第i个字符和s2的第j个字符是否相等
                //       相等则没有替换代价，不想等需要额外加一个替换代价
                int p3 = cache[i - 1][j - 1] + (c1[i - 1] == c2[j - 1] ? 0 : r);
                cache[i][j] = Math.min(p1, Math.min(p2, p3));
            }
        }
        return cache[N][M];
    }
    // ====================================================================================================


    // 空间压缩.只用单行缓存
    public static int editCostV2(String s1, String s2, int a, int d, int r) {
        // 只要不是两个都不为空，返回无效值
        if (!(s1 != null && s2 != null))
            return -1;
        // 两个都不为空
        int N = s1.length();
        int M = s2.length();
        int[] cache = new int[M + 1];
        // cache[0] = 0 可以省略 因为初始值就是0
        for (int i = 1; i <= M; i++)
            cache[i] = a * i;      // s1前0个字符就是空串""，只能添加

        for (int i = 1; i <= N; i++) {
            int pre = cache[0];        // 就用了pre和cur就完成了之前信息的保存
            cache[0] = d * i; // s1取前i个编辑成s2的前0个字符，只能删除
            for (int j = 1; j <= M; j++) {
                int cur = cache[j];    // 就用了pre和cur就完成了之前信息的保存
                // 情况1：用s1的前i-1个字符搞定s2的前j个字符，然后删除s1的第i个字符
                int p1 = cache[j] + d;
                // 情况2：用s1的前i个字符搞定s2的前j-1个字符，然后添加s2的第j个字符
                int p2 = cache[j - 1] + a;
                // 情况3：用s1的前i-1个字符搞定s2的前j-1个字符，然后需要判断s1的第i个字符和s2的第j个字符是否相等
                //       相等则没有替换代价，不想等需要额外加一个替换代价
                int p3 = pre + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : r);
                cache[j] = Math.min(p1, Math.min(p2, p3));
                pre = cur;  // 就用了pre和cur就完成了之前信息的保存
            }
        }
        return cache[M];
    }

    public static void main(String[] args) {
        String s1 = "abcd";
        String s2 = "abcfef";
        System.out.println(editDistance(s1, s2, 1, 2, 4));
        System.out.println(editCostV2(s1, s2, 1, 2, 4));
    }
}
