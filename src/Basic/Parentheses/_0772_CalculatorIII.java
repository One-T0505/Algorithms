package Basic.Parentheses;

import java.util.LinkedList;

// leetCode772
// leetCode224   224题和这道题目很像，但是没有乘除法   学习完这道题的思想后去尝试解决 leetCode224题

// 给定一个字符串str，str表示一个公式，公式里可能有整数、加减乘除符号和左右括号，返回公式的计算结果。
// 难点在于括号可能嵌套很多层：str="48*((70-65)-43)+8*1"，返回-1816。str="3+1*4"，返回7。
// str="3+(1*4)"，返回7。说明：
//  1.可以认为给定的字符串一定是正确的公式，即不需要对str做公式有效性检查
//  2.如果是负数，就需要用括号括起来，比如“4*(-3)” 但如果负数作为公式的开头或括号部分的开头，则可以没有
//    括号，比如" 3*4"和"(-3*4)"都是合法的。
//  3.不用考虑计算过程中会发生溢出的情况。

public class _0772_CalculatorIII {


    public static int calculate(String str) {
        return f(str.toCharArray(), 0)[0];
    }


    // 请从str[i...]往下算，遇到字符串终止位置或者右括号，就停止
    // 返回两个值，长度为2的数组
    // 0) 负责的这一段的结果是多少
    // 1) 负责的这一段计算到了哪个位置
    // 每次碰到左括号时就递归调用该f函数，让它去算一对括号内的结果
    private static int[] f(char[] str, int i) {
        LinkedList<String> queue = new LinkedList<>();
        int cur = 0;
        int[] res = null;
        while (i < str.length && str[i] != ')') {
            if (str[i] >= '0' && str[i] <= '9')  // 如果碰到的是数字
                cur = cur * 10 + str[i++] - '0';
            else if (str[i] != '(') {  // 碰到了运算符号
                add(queue, cur);   // 将收集到的数放入容器中，并调整运算顺序
                queue.addLast(String.valueOf(str[i++]));   // 将符号添加至容器
                cur = 0; // 只要碰到了运算符号，就说明一个运算数结束了，所以要重置
            } else {  // 遇到左括号了就交给递归去弄
                res = f(str, i + 1);
                // 碰到左括号的前一个字符必然不可能是数字，一定是运算符号，那就说明上面的cur必然被重置为0了，所以下面可以
                // 赋值，其效果等同累加。
                cur = res[0];
                i = res[1] + 1;
            }
        }
        add(queue, cur);
        return new int[]{getNum(queue), i};
    }


    private static void add(LinkedList<String> queue, int num) {
        if (!queue.isEmpty()) {
            int cur = 0;
            String top = queue.pollLast();
            if (top.equals("+") || top.equals("-"))
                queue.addLast(top);
            else {  // 如果最上面的符号是乘除
                cur = Integer.parseInt(queue.pollLast());
                num = top.equals("*") ? (cur * num) : (cur / num);
            }
        }
        queue.addLast(String.valueOf(num));
    }


    // 现在只有加减号和数
    private static int getNum(LinkedList<String> queue) {
        int res = 0, num = 0;
        boolean add = true;
        String cur = null;
        // 下面会处理万一最开头是符号应该怎么操作
        while (!queue.isEmpty()) {
            cur = queue.pollFirst();
            if (cur.equals("+"))
                add = true;
            else if (cur.equals("-"))
                add = false;
            else {
                num = Integer.parseInt(cur);
                res += add ? num : (-num);
            }
        }
        return res;
    }


    // 这个递归套路非常有用，可以解决几乎所有的括号嵌套问题。这里的递归精妙在于设计了两个返回值。其他的类似问题都可以通过
    // 这样的递归稍作改动即可。
    // ====================================================================================================


    public static void main(String[] args) {
        System.out.println(calculate("1+1"));
    }
}
