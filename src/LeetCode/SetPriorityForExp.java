package LeetCode;

import Hard.SplitThreeEqualSum;

import java.util.ArrayList;
import java.util.List;

/**
 * ymy
 * 2023/3/26 - 14 : 38
 **/

// leetCode241
// 给你一个由数字和运算符组成的字符串 expression ，按不同优先级组合数字和运算符，计算并返回所有可能组合的结果。
// 你可以按任意顺序返回答案。生成的测试用例满足其对应输出值符合 32 位整数范围，不同结果的数量不超过 10^4 。

// 输入：expression = "2*3-4*5"
// 输出：[-34,-14,-10,-10,10]
// 解释：
//   (2*(3-(4*5))) = -34
//   ((2*3)-(4*5)) = -14
//   ((2*(3-4))*5) = -10
//   (2*((3-4)*5)) = -10
//   (((2*3)-4)*5) = 10


// 1 <= expression.length <= 20
// expression 由数字和算符 '+'、'-' 和 '*' 组成。
// 输入表达式中的所有整数值在范围 [0, 99]

public class SetPriorityForExp {

    // 这个题目的思路就是每个每个富豪作为最后一个结合的部分
    public static List<Integer> diffWaysToCompute(String expression) {
        List<Integer> res = new ArrayList<>();
        if (expression == null || expression.length() == 0)
            return res;
        char[] exp = expression.toCharArray();
        int N = exp.length;
        return dfs(exp, 0, N - 1);
    }


    // L、R可以保证肯定是一个数字，给的表达式必然是数字、符号。。。 这样的顺序。 一定要注意有可能数字不止一位
    // 比如 92，占两位  根据数据规模可知，数字最多就两位
    private static List<Integer> dfs(char[] exp, int L, int R) {
        List<Integer> res = new ArrayList<>();
        if (L == R || L == R - 1){
            int cur = 0;
            for (int i = L; i <= R; i++) {
                cur = cur * 10 + (exp[i] - '0');
            }
            res.add(cur);
            return res;
        }
        for (int i = L + 1; i < R; i++) {
            if (!(exp[i] >= '0' && exp[i] <= '9')){
                List<Integer> left = dfs(exp, L, i - 1);
                List<Integer> right = dfs(exp, i + 1, R);
                for (int l : left){
                    for (int r : right){
                        switch (exp[i]){
                            case '+' -> res.add(l + r);
                            case '-' -> res.add(l - r);
                            case '*' -> res.add(l * r);
                        }
                    }
                }
            }
        }
        return res;
    }


    public static void main(String[] args) {
        String exp = "10+4";
        System.out.println(diffWaysToCompute(exp));
    }
}
