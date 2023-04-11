package GreatOffer.class26;

import java.util.LinkedList;
import java.util.List;

// 给定一个仅包含数字 0-9 的字符串 num 和一个目标值整数 target ，在 num 的数字之间添加二元运算符（不是一元）
// +、- 或 * ，返回所有能够得到 target 的表达式。
// 注意，返回表达式中的操作数不应该包含前导零。

public class _03_Code {


    public List<String> addOperators(String num, int target) {
        if (num == null || num.length() == 0)
            return null;
        char[] digits = num.toCharArray();
        int N = digits.length;
        // num中有N个数字，最多只能添加N-1个运算符号，所以表达式最长就是：2N-1
        char[] path = new char[(N << 1) - 1];
        long prefix = 0;
        LinkedList<String> res = new LinkedList<>();
        // 最开始可以让字符串的前面连续若干个数字作为一个整体，下面的循环就是枚举前缀的过程
        // 比如："3452"  那么前缀可以是3  34  345  3452，然后让剩下的数去做后续添加符号的操作
        for (int i = 0; i < N; i++) {
            prefix = prefix * 10 + digits[i] - '0';
            path[i] = digits[i];
            dfs(digits, i + 1, path, i + 1, 0, prefix, target, res);
            // 这说明数字第一个是0，那么只有一种选择，就是数字0之后必须添加符号，不可以和后续的数字作为第一个部分
            // 如果第一个数字是0，那么刚好上面的代码就使得唯一的递归入口可以执行，并且在这里写break，可以妨碍其后面
            // 添加更多数字作为前缀，一举两得。
            if (prefix == 0)
                break;
        }
        return res;
    }


    // char[] digits 固定参数，字符类型数组，等同于num
    // int pos 字符类型数组digits, 使用到了哪，0..pos-1使用过了
    // char[] path 之前做的决定，已经从左往右依次填写的字符在其中，可能含有'0'~'9' 与 * - +
    // int len path[0..len-1]已经填写好，len是终止，也表示下次填写的位置应该从哪里开始
    // fixed -> 前面固定的部分 cur -> 前一块值还不确定的部分
    //   比如：3+2*4-1 ? 5  当前来到了数字5，要决策它前面该怎么加符号，那么已经填写好的表达式里，有哪一段的值
    //   是不会受到5前面填写随便一个符号的影响？就是3+2*4这段，不管5前面填什么符号，都不影响这一段的值，fixed就是
    //   来记录当前哪一部分的值不会受影响的，fixed和cur只是为了加速计算的，这两个参数也可以不要，当全部填完后，再计算
    //   表达式的值，使用这两个变量只是为了加速。所以，fixed==3+2*4==11，cur就表示 -1?5，那么你决定将？填成什么符号，
    //   就可以把cur的值算出来。我们最后只让left+cur，所以cur里是有正负的。这里有一个边界：就是来到第二个数字时，
    //   比如：5 ？4   此时 fixed == 0，cur就是5
    // 默认 left + cur ...
    // int target 目标
    private void dfs(char[] digits, int pos, char[] path, int len, long fixed, long cur,
                     int target, LinkedList<String> res) {
        if (pos == digits.length) {
            if (fixed + cur == target)
                res.add(new String(path, 0, len));
            return;
        }
        // 还有数字可用
        int j = len + 1;  // j 就是下一个数字应该填的位置   path[len]应该填符号
        long n = 0;
        // 当前这个数字也可以选择后面若干的数字作为一个整体，下面的循环就是枚举当前要选择连续几个数作为i一个整体参与计算
        for (int i = pos; i < digits.length; i++) {
            n = n * 10 + digits[i] - '0';
            path[j++] = digits[i];
            // 填运算符号后，fixed和cur如何变换？
            // 3+2-4 ? 5           3+2-4?5     3+2-4+5          3+2-4-5            3+2-4*5
            //             fixed     5        fixed + cur     fixed + cur           fixed
            //              cur      -4         当前数字         -当前数字           cur*当前数字
            path[len] = '+';
            dfs(digits, i + 1, path, j, fixed + cur, n, target, res);
            path[len] = '-';
            dfs(digits, i + 1, path, j, fixed + cur, -n, target, res);
            path[len] = '*';
            dfs(digits, i + 1, path, j, fixed, cur * n, target, res);
            if (digits[pos] == '0') // 跟主方法中是一样的逻辑
                break;
        }
    }
}
