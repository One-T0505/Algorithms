package Hard;

// leetCode248 中心对称数
// 以字符串形式给出两个数字low和high，请问在low～high之间有多少个中心对称数。
// 中心对称数：0旋转180度后仍然是0，所以0是   69是，因为69旋转180度后仍然是69  是数整体旋转，不是每位上的数字单个旋转
//           181是中心对称数，旋转后仍然是181

public class CentrosymmetricNumIII {

    // 主方法中涉及到了很多小方法，小方法的具体实现现在不重要，最重要的是要在主方法中勾勒出算法的整体流程
    public static int strobogrammaticInRange(String low, String high) {
        char[] l = low.toCharArray();
        char[] h = high.toCharArray();
        // 检查给的两个边界是否合理
        if (!isValid(l, h))
            return 0;
        // 边界合理
        int lowL = l.length;
        int highL = h.length;
        // 如果长度相等  假设low和high当前数位下的上界为up。先算出从low～up有多少中心对称数lowUp
        // 再算出high～up有多少中心对称数highUp  lowUp - highUp 就是从low～h-1 有多少中心对称数再单独判断下high即可
        if (lowL == highL) { // lowL <= highL
            int lowUp = up(l, 0, false, 1);
            int highUp = up(h, 0, false, 1);
            return lowUp - highUp + (isCentrosymmetric(h) ? 1 : 0);
        }
        // 长度不等，但是high必然是>low的  假设low是n位数，high是m位数  n < m
        // 那就拆解成几部分来算：
        //   1.low到当前数位的上界，即up方法
        //   2.n+1位数到m-1位数的全界。比如 n=5  m=8  那就算6位数、7位数全界分别有多少 全界就是all方法，传参就是几位数
        //   3.算high数位下的下界～high的中心对称数  10000000～h  下界10..0必然不是的，所以可以从10..1开始
        int res = 0;
        for (int len = lowL + 1; len < highL; len++)
            res += all(len, true);
        // 第一部分
        res += up(l, 0, false, 1);
        // 第三部分
        res += down(h, 0, false, 1);

        return res;
    }


    // 检查范围是否合理
    private static boolean isValid(char[] low, char[] high) {
        // 长度不等
        if (low.length != high.length)
            return low.length < high.length;
        // 长度相等
        for (int i = 0; i < low.length; i++) {
            if (low[i] != high[i])
                return low[i] < high[i];
        }
        // low == high
        return true;
    }


    // 判断某个数是否为中心对称数    convert(char c, diff)这个方法非常好用
    // 他可以检查当前位置的字符 c 是否有资格期望成为从中心对称，只有 '0' '1'  '6'  '9'  '8' 才有资格期望中心对称
    // 其他数字直接没戏。如果当前位置的字符有戏，那么 convert 方法就会接着去检查和当前位置对称的位置的字符是否符合期待。
    // 比如：当前位置为1，那么期待对称位置也是1；当前位置为6，那么期待对称位置是9 ；
    // convert 方法的返回值就是期待对称位置的字符是什么，如果返回-1，就表示搞不定
    private static boolean isCentrosymmetric(char[] high) {
        int L = 0, R = high.length - 1;
        int convert = 0;
        while (L < R){
            convert = convert(high[L], true);
            if (convert != -1 && high[R] == convert){
                L++;
                R--;
            } else
                return false;
        }
        // 执行到这里说明所有字符都是中心对称了。但是别忘了，数字长度是有奇偶情况的，如果是奇数，那么此时L==R，还剩
        // 这一个数还没判断
        if (L == R) // 说明是奇数长度
            return high[L] == '0' || high[L] == '1' || high[L] == '8';
        else // 说明是偶数长度，如果能执行到这里直接返回true
            return true;
    }



    // cur是当前数，假设cur为6位数 up方法会找出从cur~999999中所有中心对称数的数量
    // low [左边已经做完决定了 L.....R 右边已经做完决定了]
    // 左边已经做完决定的部分，如果大于low对应的原始部分，leftMore = true;
    // 左边已经做完决定的部分，如果不大于low对应的原始部分，那一定是相等，不可能小于，如果小于那后面就不用看了，因为整体
    // 的数必然小于原始的low，那就不在合理范围内了。所以 leftMore = false;
    // 右边已经做完决定的部分，如果小于low对应的原始部分，rs = 0;
    // 右边已经做完决定的部分，如果等于low对应的原始部分，rs = 1;
    // 右边已经做完决定的部分，如果大于low对应的原始部分，rs = 2;
    // rs < = >
    //    0 1 2
    // 返回 ：没做决定的部分，随意变，几个有效的情况？返回！
    private static int up(char[] cur, int L, boolean leftMore, int rs) {
        int N = cur.length;
        // L确定了之后，和其匹配的R的位置可以算出来
        int R = N - 1 - L;
        // base case
        if (L > R) { // 都做完决定了
            // 如果左边做完决定的部分大于原始 或者 如果左边做完决定的部分等于原始 && 右边做完决定的部分不小于原始
            // 有效！ 否则，无效！
            return leftMore || (!leftMore && (rs != 0)) ? 1 : 0;
        }
        // 如果上面没有return，说明决定没做完，就继续做
        if (leftMore)  // 如果左边做完决定的部分大于原始  那剩余的部分随意变化都可以了
            return all(N - (L << 1), false); // L这边做完了多少决定，其对应的右侧也是一样的
        else { // 如果左边做完决定的部分等于原始
            int ways = 0;
            // 当前L做的决定，大于原始的L位置的数
            for (char c = (char) (cur[L] + 1); c <= '9'; c++) {
                int exp = convert(c, L != R);
                if (exp != -1){
                    // 当前L做的决定是变成大于原始L位置的数了，那么其对应的R位置的数也要改，可能会影响rs的结果，但是
                    // leftMore传入true之后，递归进入函数后，会直接执行88行处，所以这里传入的rs没用，不用管
                    ways += up(cur, L + 1, true, rs);
                }
            }
            // 当前L做的决定，等于原始的L位置的数  convert表示如果L想和原始中L位置的数相等，
            // 那么和L对应的R位置应该是多少
            int convert = convert(cur[L], L != R);
            // 左边做完决定的等于原始，当前L位置做的决定也是等于原始，不管R位置是什么，都无法直接断定整体的结果是否小于
            // 原始，所以剩余还没做决定的部分依然有补救机会
            if (convert != -1) {
                if (convert < cur[R])
                    ways += up(cur, L + 1, false, 0);
                else if (convert == cur[R]) {
                    ways += up(cur, L + 1, false, rs);
                } else
                    ways += up(cur, L + 1, false, 2);
            }
            return ways;
        }
    }


    // high所在位数的下界～high有多少个中心对称数
    private static int down(char[] high, int L, boolean leftLess, int rs) {
        int N = high.length;
        int R = N - 1 - L;
        if (L > R) {
            return leftLess || (!leftLess && rs != 2) ? 1 : 0;
        }
        if (leftLess) {
            return all(N - (L << 1), false);
        } else {
            int ways = 0;
            for (char c = (N != 1 && L == 0) ? '1' : '0'; c < high[L]; c++) {
                if (convert(c, L != R) != -1) {
                    ways += down(high, L + 1, true, rs);
                }
            }
            int convert = convert(high[L], L != R);
            if (convert != -1) {
                if (convert < high[R]) {
                    ways += down(high, L + 1, false, 0);
                } else if (convert == high[R]) {
                    ways += down(high, L + 1, false, rs);
                } else {
                    ways += down(high, L + 1, false, 2);
                }
            }
            return ways;
        }
    }


    // count求len位数中有多少个中心对称数的思路是：每次从左右各剪掉一位，然后排列组合
    // 比如要求6位数中有多少个中心对称数：* * * * * *   首尾只有是：1和1  8和8  6和9  9和6 这4种组合，才能保证
    // 旋转后是中心对称的. 然后就是剩下的4位：✅ * * * * ✅  剩下4位左右两边的组合除了上面同样的4种组合外，还有一
    // 个新的组合  0和0。上一层为什么不能有 0和0的组合？因为其包含了最高位，最高位不能为0，这就是init的作用
    // 当处理的不包含最高位时是可以包含 0和0这种组合的。
    private static int count(int len, boolean init) {
        // 为什么len==0，并且不是最开始的位数时，为什么要返回1。
        // 因为上游调用时，再处理完左右两位后会变成 count(len, true) --> 4 * count(len-2, false) -->
        // 4 * 5 * count(len-4, false) .. --> 4 * 5 * .. 5 * count(0, false)
        // 如果count(0, false)返回0，则结果就变无效了，返回1才是对的
        if (len == 0)
            return init ? 0 : 1;
        // 每次剥掉两位，最后要不就剩下0位，要不就是1位
        // 如果只剩下1位，那就三种：0  1  8  不管是不是包含最高位都是3 和init无关 此时6旋转后是9，不是中心对称了
        if (len == 1)
            return 3;
        if (init)
            return count(len - 2, false) << 2;  // << 2 就是 * 4
        else
            return count(len - 2, false) * 5;
    }


    // len位数中，有多少个中心对称数  上面的这个方法count是all方法的初始版本，比较符合直觉。all方法是对其进
    // 行优化后的写法，先看懂count后再来看all
    private static int all(int len, boolean init) {
        // 反向求 每次加2个位 左右各加一位
        if (len == 1)
            return 3;
        boolean isOdd = (len & 1) == 1;
        int res = isOdd ? 3 : 1;  // 最后剩下的
        for (int i = isOdd ? 3 : 2; i < len; i += 2)
            res *= 5;
        return init ? res << 2 : res * 5;
    }


    // L想得到c字符，R配合应该做什么决定，如果L怎么也得不到c字符，返回-1；如果能得到，返回R配合应做什么决定
    // 比如，L != R，即不是同一个位置
    //   L想得到0，那么就R就需要是0    L想得到1，那么就R就需要是1
    //   L想得到6，那么就R就需要是9    L想得到8，那么就R就需要是8     L想得到9，那么就R就需要是6
    // 除此了这些之外，L不能得到别的了。
    // 比如，L == R，即是同一个位置
    //   L想得到0，那么R就需要是0      L想得到1，那么R就需要是1
    //   L想得到8，那么R就需要是8
    // 除此了这些之外，L不能得到别的了，比如：
    //   L想得到6，那么R就需要是9，而L和R是一个位置啊，怎么可能即6又9，返回-1
    private static int convert(char c, boolean diff) {
        return switch (c) {
            case '0' -> '0';
            case '1' -> '1';
            case '6' -> diff ? '9' : -1;
            case '8' -> '8';
            case '9' -> diff ? '6' : -1;
            default -> -1;
        };
    }


    public static void main(String[] args) {
        System.out.println(strobogrammaticInRange("1001", "11111"));
//        for (int i = 1001; i <= 11111; i++) {
//            if (isCentrosymmetric(String.valueOf(i).toCharArray()))
//                System.out.println(i);
//        }
//        System.out.println(all(5));
    }

}
