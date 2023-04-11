package Hard;

// leetCode564
// 给定一个表示整数的字符串 n，返回与它最近的回文整数（不包括自身）。如果不止一个，返回较小的那个。
// “最近的”定义为两个整数差的绝对值最小。

// 输入: n = "1"  输出: "0"
// 解释: 0 和 2是最近的回文，但我们返回最小的，也就是 0。

// 数据规模
// 1 <= n.length <= 18
// n 只由数字组成
// n 不含前导 0
// n 代表在 [1, 10^18 - 1] 范围内的整数

import java.util.Arrays;

public class ClosestPalindromeNum {

    // 先简单贪心一下：对于数字n，我们需要先求出三个数：粗回文、上界、下界  答案只可能在这三个数之中
    // 比如：n==10023  其粗回文就是找到数字的对称轴，把它直接写成回文数字即可，
    // 所以 n 的粗回文就是 10001  因为对称轴是中间的0；
    // 粗回文的上界：写出粗回文后，将对称轴上的数+1，就是上界，10101，其必然也是回文数字；如果n是偶数位，那就让
    // 最中间的两位同时+1就能求出上界。比如：n==346231  粗回文-->346643  上界-->347743
    // 粗回文的下界：同理，只需要让粗回文最中间的那位-1即可，如果是偶数位，那就让最中间的一起-1即可。
    // 总之，答案必然是这三个中的一个。
    // 但是，该题目的难点在于coding，因为有非常多的边界要考虑。所以下面的代码主要在处理边界。

    public static String nearestPalindromic(String n) {
        long num = Long.parseLong(n);
        long raw = getRawPalindrome(n); // 粗回文
        long upper = raw > num ? raw : getUpperPalindrome(raw);
        long lower = raw < num ? raw : getLowerPalindrome(raw);

        return String.valueOf(upper - num >= num - lower ? lower : upper);
    }


    // 获取n的粗回文
    private static long getRawPalindrome(String n) {
        char[] chs = n.toCharArray();
        int N = chs.length;
        // 直接复制即可
        for (int i = 0; i < (N >> 1); i++) {
            chs[N - 1 - i] = chs[i];
        }
        return Long.parseLong(String.valueOf(chs));
    }


    // 通过粗回文得到其上界
    private static long getUpperPalindrome(long raw) { // raw已经是一个回文数了
        char[] chs = String.valueOf(raw).toCharArray();
        int N = chs.length;
        // 如果raw是 89|98  偶数位是让最里面的两个9都+1，这里就涉及到了进位问题 当左侧的9 +1后，左侧就变成了
        // 90 这时要把左侧的90对称过来 就变成了 9009  这样才是比8998大的且最近的回文数
        // 如果raw是 99|99 这不仅有进位问题，还有数位的变化，左侧的99 +1后变成了100，不能直接将左侧的100对称过去
        // 那样就变成了100001，从4位数直接变成了6位数，实际上应该是10001，只增加一位。
        char[] res = new char[N + 1]; // N+1就是应对上面的增加数位的情况，最多只可能加1位
        res[0] = '0'; // 0位置就留作备用的，如果有新增的数位那再使用它
        System.arraycopy(chs, 0, res, 1, N);
        // i一开始到了应该+1的位上。如果是奇数长度那就在中间，如果是偶数，那i一开始就在左侧的最右的那个数位
        // 切记res中是从1开始的，0位置是保留的，暂时不用。
        for (int i = (N - 1) / 2 + 1; i >= 0; i--) {
            if (++res[i] > '9')
                res[i] = '0';
            else
                break;
        }
        int offset = res[0] == '1' ? 1 : 0;
        N = res.length;
        // 再把这个对称复制过去
        for (int i = N - 1; i >= (N + offset) >> 1; i--) {
            res[i] = res[N - i - offset];
        }
        return Long.parseLong(String.valueOf(res));
    }


    // 通过粗回文得到其下界  找下界-1的过程中会有借位的问题
    private static long getLowerPalindrome(long raw) {
        char[] chs = String.valueOf(raw).toCharArray();
        int N = chs.length;
        // 找下界就是要 -1 ，所以不可能有新增数位，开辟同样大的空间绝对够用
        char[] res = new char[N];
        System.arraycopy(chs, 0, res, 0, N);
        for (int i = (N - 1) >> 1; i >= 0; i--) {
            if (--res[i] < '0')
                res[i] = '9';
            else
                break;
        }
        if (res[0] == '0') { // 那就要减一个数位了，最高位为0就无效
            res = new char[N - 1];
            Arrays.fill(res, '9');
            return N == 1 ? 0 : Long.parseLong(String.valueOf(res));
        }
        // 如果没有造成数位丢失,执行到这里，该-1的部分都已经减完了
        for (int i = 0; i < N >> 1; i++) {
            res[N - 1 - i] = res[i];
        }
        return Long.parseLong(String.valueOf(res));
    }
}
