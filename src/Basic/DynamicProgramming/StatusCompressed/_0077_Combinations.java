package Basic.DynamicProgramming.StatusCompressed;

import java.util.ArrayList;
import java.util.List;

/**
 * ymy
 * 2023/3/19 - 17 : 04
 **/

// 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
// 你可以按任何顺序返回答案。

// 1 <= n <= 20
// 1 <= k <= n

public class _0077_Combinations {

    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (k < 1 || n < k)
            return res;
        // 因为 n <= 20  所以可以用int型数据二进制形式下的第20位来表达某个数字是否被选过了。
        // 比如：... 00100  表示3已经被选过了，我们用最低位表示数字1
        // pre表示上一次选择的数字，主方法调用时，传入0，这样就会从数字1开始选择。因为题目要求的是：(2, 3) (3, 2)
        // 是一样的，所以我们设置了不往回选。
        f(n, 0, 0, k, new ArrayList<>(), res);
        return res;
    }

    private static void f(int n, int status, int pre, int rest, ArrayList<Integer> path,
                          List<List<Integer>> res) {
        if (rest == 0){
            res.add(new ArrayList<>(path));
        } else {
            for (int move = pre; move < n; move++) {
                if ((status & (1 << move)) == 0){
                    status |= 1 << move;
                    path.add(move + 1);
                    f(n, status, move + 1, rest - 1, path, res);
                    path.remove(path.size() - 1);
                    status ^= 1 << move;
                }
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(combine(4, 2));
    }
}
