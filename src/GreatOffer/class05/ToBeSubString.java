package class05;

// 字节面试  难题，但是很好
// 给定两个字符串s1和s2，问s1最少删除多少字符可以成为s2的子串?
// 比如s1 = "abcde"，s2 = "axbc" 答案是2个  s1删除de 就可以变成s2的子串 abc

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToBeSubString {


    // 方法1：生成s1的所有子序列，并按长度递减排序，逐一把子序列和s2玩KMP，若存在结果，则是最优解；因为是第一个有结果并且
    //       子序列长度尽量长。
    public static int toBeSubStringV1(String s1, String s2) {
        if (s1 == null || s2 == null)
            return -1;
        if (s1.equals(s2))
            return 0;
        // 执行到这里说明，s1.len > s2.len
        List<String> sb = new ArrayList<>();
        f(s1, 0, "", sb);
        sb.sort(((o1, o2) -> o2.length() - o1.length()));
        for (String cur : sb) {
            if (s2.contains(cur))   // 系统提供的该方法底层和KMP几乎一致，可能稍有优化
                return s1.length() - cur.length();
        }
        return s1.length();   // 删除整个字符串就成""，必然是s2的子串
    }

    // 该方法返回s的所有子序列
    // index表示现在要决定s的index处字符的去留；pre表示之前做的决定
    private static void f(String s, int index, String pre, List<String> res) {
        if (index == s.length()) {
            res.add(pre);
            return;
        }
        f(s, index + 1, pre + s.charAt(index), res);
        f(s, index + 1, pre, res);
    }
    // 时间复杂度分析：s1长度为N，s2长度为M。在构造s1所有可能子序列时，时间复杂度为O(2^N)，
    // 排序一下又是O(2^N * log(2^N)) == O(N * 2^N)。单次KMP的时间复杂度为O(M)，对每个子序列都要来一次，
    // 时间复杂度 O(2^N * M)。所以时间复杂度由这三部分组成。有指数级时间，所以只适用于 N 很小的时候。
    // ====================================================================================================


    // 方法2：因为了解了编辑距离问题，所以，可以将s2的所有子串找出来；然后让s1和每个子串求编辑距离。不过这里的编辑距离
    //       需要修改下，因为要找的是只通过删除的方式找到，并且要找的是删除的字符数，所以我们需要把添加的替换的代价
    //       改为无穷大，让删除的代价变为1，刚好表示删除的字符数。每一对编辑距离问题都涉及一张二维表的填写，表的大小为
    //       M*N，s2共有M^2个子串，所以时间复杂度为：O(N * M^3)，如果 N 较大，该方法比上面的方法好很多。

    public static int toBeSubStringV2(String s1, String s2) {
        if (s1 == null || s2 == null || s2.length() == 0)
            return -1;
        if (s1.equals(s2))
            return 0;
        int N = s1.length();
        int M = s2.length();
        int res = s1.length();   // 结果不可能比这还大
        for (int i = 0; i < M; i++) {
            for (int j = i; j < M; j++) {
                // 如果选取的子串长度>s2，那s2不可能通过删除得到该子串
                // 这算是提前剪枝了
                if (N < (j - i + 1))
                    break;
                res = Math.min(res, onlyDelete(s1, s2.substring(i, j + 1)));
            }
        }
        return res;
    }


    // 这里就不像之前那样把每个操作的代价也当参数，现在默认只有删除代价为1，其他为无穷，可用系统最大值表示
    // 这里的缓存表行数表示选取s1的前几个字符，列数表示选取s2的前几个字符。所以该缓存表的严格上半区域是没用的，不用填。
    // 该方法返回：s1仅通过删除得到s2的最小代价   注意这里已经不再是题目说的子串了，而是传入的s2整体了。
    // 并且传入时已经保证了 s1.len >= s2.len  所以动态表要不就是正方形，要不就是瘦长型
    private static int onlyDelete(String s1, String s2) { // 这个方法就是根据编辑距离模型改写的
        int N = s1.length();
        int M = s2.length();
        int[][] dp = new int[N + 1][M + 1];
        // 单独处理第一列
        for (int i = 0; i <= N; i++)
            dp[i][0] = i;
        // 单独处理对角线。因为上游调用该方法时已经提前做了判断，所以保证传入的s1的长度是大于等于s2的，所以缓存表
        // 不会是短宽型的，所以下面的for循环边界就不用考虑了
        for (int i = 0; i <= M; i++)
            dp[i][i] = s1.substring(0, i).equals(s2.substring(0, i)) ? 0 : Integer.MAX_VALUE;
        // 处理一般元素.这里的for循环边界也简化了。原来是i <= Math.min(N, M)
        for (int i = 2; i <= N; i++) {
            for (int j = 1; j <= Math.min(M, i - 1); j++) {
                dp[i][j] = Integer.MAX_VALUE;
                // 如果我的前i个字符能搞定前j-1个字符，那就只用删除我的第i个字符
                if (dp[i - 1][j] != Integer.MAX_VALUE)
                    dp[i][j] = dp[i - 1][j] + 1;
                // 如果s1第i-1个字符等于s2的第j-1个字符，并且我的前i-1个字符能搞定你的前j-1个字符，那就刚好不用增加代价
                if (s1.charAt(i - 1) == s2.charAt(j - 1) && dp[i - 1][j - 1] != Integer.MAX_VALUE)
                    dp[i][j] = Math.min(dp[i - 1][j - 1], dp[i][j]);
            }
        }
        return dp[N][M];
    }
    // ====================================================================================================


    // 方法3：根据toBeSubStringV2方法中的实现步骤来看，是先把s2的所有子串中长度不超过s1的子串逐一和s1一起送进
    //       onlyDelete方法中，来判断编辑距离。假设s1长度为8，s2长度为4；传入了s2[0..0] ... s2[0..3]，分别
    //       需要构建一个8行1列、8行2列、8行3列、8行4列的缓存表，然后传回结果；下次调用时传入的参数就是：
    //       s2[1..1] ... s2[1..3]，构建一个8行1列的缓存表，然后重新填表；接着是8行2列的表.....  里面有大量
    //       重复计算，就这三张表里的第一列明明是一样的结果，可是却算了3遍，还不说以后的调用。所以我们一开始就可以构
    //       建一个最大的缓存表，能容纳所有情况。最大的缓存表无非就是：行数 == Math.min(s1的长度, s2的长度)
    //       列数 == s2的长度。因为生成s2的子串是按序的：0..0  0..1  0..2  0..3  所以在这张大表中也可以实现递推。
    //       这张最大的缓存表cache有8行4列，当我们传入s1 s2[0..0] 时，我们只需要填写第0列即可；下次传入
    //       s1 s2[0..1] 时，只需要用完成的第0列行来填写第1列即可。
    //      这样时间复杂度就是：O(N * M^2)

    // 代码实在不会写

    public static int toBeSubStringV3(String s1, String s2) {
        if (s1 == null || s2 == null || s2.length() == 0)
            return -1;
        if (s1.equals(s2))
            return 0;
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        int N = c1.length;
        int M = c2.length;
        int res = N;   // 结果不可能比这还大
        int[][] dp = new int[N + 1][M + 1];  // 最大的dp表
        // 枚举s2的子串范围
        for (int i = 0; i < M; i++) {
            for (int j = i; j < M; j++) {

            }
        }
        return res;
    }

    // ====================================================================================================


    // for test
    // 随机生成一个长度[0,maxLen]的全部由英文小写字母组成的字符串
    public static String randomString(int maxLen) {
        int len = (int) (Math.random() * (maxLen + 1));
        char[] chs = new char[len];
        for (int i = 0; i < len; i++) {
            // 小写英文字母的ASCII码是从 [97，122]
            chs[i] = (char) ((int) (Math.random() * 26) + 97);
        }
        return Arrays.toString(chs);
    }


    public static void test(int testTime, int maxLen) {
        for (int i = 0; i < testTime; i++) {
            String s1 = randomString(maxLen);
            String s2 = randomString(maxLen);
            int res1 = toBeSubStringV1(s1, s2);
            int res2 = toBeSubStringV2(s1, s2);
            if (res1 != res2) {
                System.out.println("failed");
                System.out.println("s1: " + s1);
                System.out.println("s2: " + s2);
                System.out.println("暴力递归：" + res1);
                System.out.println("动态规划：" + res2);
                return;
            }
        }
        System.out.println("AC");
    }


    public static void main(String[] args) {
        test(50, 7);
//        String s1 = "yy";
//        String s2 = "tfv";
//        System.out.println(toBeSubStringV1(s1, s2));
//        System.out.println(toBeSubStringV2(s1, s2));
    }
}
