package TopInterviewQuestions;

import java.util.ArrayList;
import java.util.List;

// 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按任意顺序返回。
// 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。

public class _0017_LetterCombinations {

    public static char[][] phone =
            {{'a', 'b', 'c'},        // 电话号码上按键2对应的字符
                    {'d', 'e', 'f'},        // 3
                    {'g', 'h', 'i'},        // 4
                    {'j', 'k', 'l'},        // 5
                    {'m', 'n', 'o'},        // 6
                    {'p', 'q', 'r', 's'},   // 7
                    {'t', 'u', 'v'},        // 8
                    {'w', 'x', 'y', 'z'}};  // 9


    // 该题目就是一个简单的深度优先遍历
    public List<String> letterCombinations(String digits) {
        if (digits == null)
            return null;
        ArrayList<String> res = new ArrayList<>();
        if (digits.length() == 0)
            return res;
        char[] chars = digits.toCharArray();
        int N = chars.length;
        char[] path = new char[N];
        f(chars, 0, path, res);
        return res;
    }


    // index不仅表示该拨到哪个数字了，同样还指示着path中下一个字符应该填的位置
    // 因为每拨一个数字，path中就会填上一个字符
    private void f(char[] digits, int index, char[] path, ArrayList<String> res) {
        if (index == digits.length)
            res.add(String.valueOf(path));
        else {
            // 拿出当前数字对应的按键的字符 数字2对应phone[0]
            for (char c : phone[digits[index] - '2']) {
                path[index] = c;
                f(digits, index + 1, path, res);
                // 这道题目不用还原现场，因为在char数组中可以直接覆盖
            }
        }
    }
}
