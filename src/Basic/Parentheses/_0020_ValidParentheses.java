package Basic.Parentheses;


// 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
// 有效字符串需满足：
//  左括号必须用相同类型的右括号闭合。
//  左括号必须以正确的顺序闭合。
//  每个右括号都有一个对应的相同类型的左括号。

public class _0020_ValidParentheses {

    // 思路：用栈，只要碰到左括号就压栈；碰到右括号就弹出栈顶，并检查是否和当前右括号匹配。
    // 整个字符串遍历完之后，如果栈中还有元素，那必然无效。

    public boolean isValid(String s) {
        if (s == null || s.length() < 2)
            return false;
        char[] chars = s.toCharArray();
        int N = chars.length;
        // 用数组模拟系统栈
        char[] stack = new char[N];
        int top = -1;
        for (char cur : chars) {
            if (cur == '(' || cur == '{' || cur == '[')
                // 碰到左括号了就压栈，但是压栈要压其对应的右括号，这样在碰到右括号需要弹出栈里的元素时直接判断
                // 两个字符是否相等就可以了；如果压栈的是左括号，当被弹出时和右括号匹配时还要判断。
                stack[++top] = cur == '(' ? ')' : (cur == '{' ? '}' : ']');
            else {  // 碰到右括号了
                // 栈为空  或者  栈不为空但是匹配失败
                if (top == -1 || cur != stack[top])
                    return false;
                else // 栈不为空且匹配成功
                    top--;
            }
        }
        return top == -1;
    }
}
