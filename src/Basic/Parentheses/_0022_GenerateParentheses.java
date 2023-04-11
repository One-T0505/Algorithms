package Basic.Parentheses;

import java.util.ArrayList;
import java.util.List;

// 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且有效的括号组合。
// 比如n==3，说明给你3个左括号和3个右括号，返回所有合法的组合形式

public class _0022_GenerateParentheses {


    public List<String> generateParenthesis(int n) {
        if (n < 1)
            return null;
        char[] path = new char[n << 1];
        ArrayList<String> res = new ArrayList<>();
        f(0, 0, n, path, res);
        return res;
    }


    // 在0..index-1这些已经填好的位置中，左括号数量-右括号数量就是leftMinusRight的含义
    // leftRest表示还能用几个左括号
    // 本题就是训练剪枝的能力，少跑一些没必要的路
    // path的长度是固定的，最暴力的递归就是跑遍所有可能的情况：每个位置填左括号或右括号，然后填完之后再进行
    // 有效性检查
    private void f(int index, int leftMinusRight, int leftRest, char[] path, ArrayList<String> res) {
        // 为什么敢把path的结果直接加进res里而不用担心合法性问题？
        // 因为下面剪枝剪得好，保证每一个能填完path的情况必定是合法的
        if (index == path.length)
            res.add(String.valueOf(path));
        else {
            if (leftRest > 0) {
                path[index] = '(';
                f(index + 1, leftMinusRight + 1, leftRest - 1, path, res);
            }
            if (leftMinusRight > 0) {
                path[index] = ')';
                f(index + 1, leftMinusRight - 1, leftRest, path, res);
            }
        }
    }
}
