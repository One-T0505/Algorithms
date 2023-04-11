package Basic.Parentheses;


// 来自美团
// ()分值为2  (()) 分值为3  ((()))分值为4
// 也就是说，每包裹一层，分数就是里面的分值+1
// ()() 分值为 2 * 2    (())() 分值为 3 * 2
// 也就是说，每连接一段，分数就是各部分相乘，以下是一个结合起来的例子
// (()())()(()) -> (2 * 2 + 1) * 2 * (2 + 1) --> 30
// 给定一个括号字符串str，已知str一定是正确的括号结合，不会有违规嵌套 返回分数


public class CalculateScore {

    // 该题目属于括号嵌套系列问题，之前有建议过，遇到括号嵌套问题推荐的一种递归模型
    // 建议复习下大厂刷题班第8节的模型

    public static int scores(String s) {
        return compute(s.toCharArray(), 0)[0];
    }


    // i表示现在要从chs[i]开始往后走，直到 遇到 ')' 或者 终止位置  停！
    // 返回值：int[] res 长度就是2
    // res[0] ：从i到停止的位置计算的得分
    // res[1] : 在哪个位置停下的！
    private static int[] compute(char[] chs, int i) {
        if (chs[i] == ')')
            return new int[]{1, i};
        int res = 1;
        while (i < chs.length && chs[i] != ')') {
            int[] info = compute(chs, i + 1);
            res *= info[0] + 1;
            i = info[1] + 1;
        }
        return new int[]{res, i};
    }



    public static void main(String[] args) {
        String s = "((())())";
        System.out.println(scores(s));
    }
}
