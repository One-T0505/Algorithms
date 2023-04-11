package Basic.DynamicProgramming;

import java.util.ArrayList;
import java.util.List;

/**
 * ymy
 * 2023/3/19 - 17 : 24
 **/

// 有效 IP 地址正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔。
// 例如："0.1.2.201" 和 "192.168.1.1" 是有效 IP 地址，但是 "0.011.255.245"、"192.168.1.312" 和
// "192.168@1.1" 是无效 IP 地址。
// 给定一个只包含数字的字符串 s ，用以表示一个 IP 地址，返回所有可能的有效 IP 地址，这些地址可以通过在 s 中
// 插入 '.' 来形成。你不能重新排序或删除 s 中的任何数字。你可以按任何顺序返回答案。

// 1 <= s.length <= 20
// s 仅由数字组成

public class _0093_RecoverIP {

    public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        if (s == null || s.length() < 4 || s.length() > 12)
            return res;
        char[] chs = s.toCharArray();
        int N = chs.length;
        f(chs, 0, "", 4, res);
        return res;
    }


    // 现在来到i位置做决策  pre表示在0～i-1位置已经做好的决策
    // rest表示还可以分成几部分，因为一个ip地址就是4部分，不能让 "." 的数量超过3个；
    // 所以主方法调用时，传入的是4
    private static void f(char[] chs, int i, String pre, int rest, List<String> res) {
        if (rest < 0)
            return;
        if (i == chs.length && rest == 0)
            // 这个添加的方式看个人喜欢，因为我是每次先添加数，再添加 "."  所以等到完成的时候，最后还会多一个 "."
            // 要去除
            res.add(pre.substring(0, pre.length() - 1));
        else if (i < chs.length && rest > 0){
            StringBuilder sb = new StringBuilder(pre);
            // 如果碰到了0，必须直接将其分割出来
            if (chs[i] == '0'){
                sb.append(0).append(".");
                f(chs, i + 1, sb.toString(), rest - 1, res);
            } else { // 如果不是0，才可以枚举，但是枚举的范围不能超过255
                int cur = 0;
                while (i < chs.length){
                    int last = sb.length();
                    cur = cur * 10 + chs[i] - '0';
                    if (cur > 255)
                        break;
                    sb.append(cur).append(".");
                    int now = sb.length();
                    f(chs, i + 1, sb.toString(), rest - 1, res);
                    sb.delete(last, now);
                    i++;
                }
            }
        }
    }


    public static void main(String[] args) {
        String s = "108220";
        System.out.println(restoreIpAddresses(s));
    }
}
