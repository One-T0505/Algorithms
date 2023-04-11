package class11;

// leetCode647
// 给你一个字符串 s ，请你统计并返回这个字符串中回文子串的数目。

public class PalindromeSubstring {

    // 普通方法是用动态规划做。这里我们用最优解 Manacher 算法来改写.  还是会用到半径数组，和最远回文中心，最远回文边界
    // 但是这里我们不需要找到最长回文子串的长度，所以删除了max变量，新添加了res变量，来得到我们想要的结果。

    public static int countSubstrings(String s) {
        if (s == null || s.length() == 0)
            return 0;
        char[] ori = expand(s);
        int N = ori.length;
        int[] radius = new int[N];      // 回文半径数组
        int R = -1, C = -1;             // 这里的R指的是回文最右边界的再往右一个位置， 中心点C
        int res = 0;
        for (int i = 0; i < N; i++) {
            // 先确定当前元素至少的回文区域。如果R > i，不管落入4种情况的哪一种，都是
            // Math.min(radius[2 * C - i], R - i)
            radius[i] = i <= R ? Math.min(radius[2 * C - i], R - i + 1) : 1;

            // 4种情况中，只有两种需要左右扩充，但是如果将扩充算法写到对应的情况下会代码冗余，所以写成如下这样，4种情况
            // 都会进入该扩充算法，但是原有的不需要扩充的两种情况进入该循环后会直接失败，所以效果等价。
            while (i + radius[i] < N && i - radius[i] > -1) {
                if (ori[i + radius[i]] == ori[i - radius[i]])
                    radius[i]++;
                else
                    break;
            }
            // 判断是否需要更新R，C
            if (i + radius[i] - 1 > R) {
                R = i + radius[i] - 1;
                C = i;
            }
            res += radius[i] >> 1;
        }
        // 经发现：标准流字符串中的回文半径-1，就可以得到原始字符串中的回文直径
        return res;
    }

    // 将字符串扩充为辨准处理串：1221 --> #1#2#2#1#
    private static char[] expand(String s) {
        char[] res = new char[s.length() << 1 | 1];
        char[] ori = s.toCharArray();
        int index = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 1 ? ori[index++] : '#';
        }
        return res;
    }
    // 经过 Manacher 算法的应用，我们可以发现， Manacher 算法只是一种极为高效的流程，在该流程里，我们可以根据需求
    // 求得我们想要的数据。


    public static void main(String[] args) {
        String s = "abc";
        System.out.println(countSubstrings(s));
    }
}
