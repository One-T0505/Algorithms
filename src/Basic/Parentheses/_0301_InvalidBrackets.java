package Basic.Parentheses;

// leetCode301
// 给你一个由若干括号和字母组成的字符串 s ，删除最小数量的无效括号，使得输入的字符串有效。
// 返回所有可能的结果。答案可以按任意顺序返回。

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class _0301_InvalidBrackets {


    private static List<String> res = new ArrayList<String>();

    public static List<String> removeInvalidParentheses(String s) {
        List<String> ans = new ArrayList<>();
        remove(s, ans, 0, 0, new char[]{'(', ')'});
        return ans;
    }
    // 上面这个方法是民间大神写的，非常地好，但是我读不懂。下面的方法是官方给出的解，易于理解。
    // --------------------------------------------------------------------------------------------------


    // 首先我们利用括号匹配的规则求出该字符串 s 中最少需要去掉的左括号的数目 lRemove 和右括号的数目 rRemove，
    // 然后我们尝试在原字符串 s 中去掉 lRemove 个左括号和 rRemove 个右括号，然后检测剩余的字符串是否合法匹配，
    // 如果合法匹配则我们则认为该字符串为可能的结果，我们利用回溯算法来尝试搜索所有可能的去除括号的方案。

    // 在进行回溯时可以利用以下的剪枝技巧来增加搜索的效率:
    //  1.我们从字符串中每去掉一个括号，则更新 lRemove 或者 rRemove，当我们发现剩余未尝试的字符串的长度小于
    //    lRemove + rRemove 时，则停止本次搜索。
    //  2.当 lRemove 和 rRemove 同时为 0 时，则我们检测当前的字符串是否合法匹配，如果合法匹配则我们将其记录下来。

    // 由于记录的字符串可能存在重复，因此需要对重复的结果进行去重，去重的办法有如下两种：
    //  1.利用哈希表对最终生成的字符串去重
    //  2.我们在每次进行搜索时，如果遇到连续相同的括号我们只需要搜索一次即可，比如当前遇到的字符串为 "(((())"，
    //    去掉前四个左括号中的任意一个，生成的字符串是一样的，均为 "((())"，因此我们在尝试搜索时，只需去掉一个
    //    左括号进行下一轮搜索，不需要将前四个左括号都尝试一遍.

    // modifyIndex <= checkIndex
    // 只查s[checkIndex....]的部分，因为之前的一定已经调整对了
    // 但是之前的部分是怎么调整对的，调整到了哪？就是modifyIndex
    // 比如：
    // ( ( ) ( ) ) ) ...
    // 0 1 2 3 4 5 6
    // 一开始当然checkIndex = 0，modifyIndex = 0
    // 当查到6的时候，发现不对了，
    // 然后可以去掉2位置、4位置的 )，都可以
    // 如果去掉2位置的 ), 那么下一步就是
    // ( ( ( ) ) ) ...
    // 0 1 2 3 4 5 6
    // checkIndex = 6 ，modifyIndex = 2
    // 如果去掉4位置的 ), 那么下一步就是
    // ( ( ) ( ) ) ...
    // 0 1 2 3 4 5 6
    // checkIndex = 6 ，modifyIndex = 4
    // 也就是说，
    // checkIndex和modifyIndex，分别表示查的开始 和 调的开始，之前的都不用管了  par  (  )
    public static void remove(String s, List<String> ans, int checkIndex, int deleteIndex, char[] par) {
        for (int count = 0, i = checkIndex; i < s.length(); i++) {
            if (s.charAt(i) == par[0]) {
                count++;
            }
            if (s.charAt(i) == par[1]) {
                count--;
            }
            // i check计数<0的第一个位置
            if (count < 0) {
                for (int j = deleteIndex; j <= i; j++) {
                    if (s.charAt(j) == par[1] && (j == deleteIndex || s.charAt(j - 1) != par[1])) {
                        remove(s.substring(0, j) + s.substring(j + 1), ans, i, j, par);
                    }
                }
                return;
            }
        }
        String reversed = new StringBuilder(s).reverse().toString();
        if (par[0] == '(') {
            remove(reversed, ans, 0, 0, new char[]{')', '('});
        } else {
            ans.add(reversed);
        }
    }

    public static List<String> removeInvalidParentheses2(String s) {
        int lremove = 0;
        int rremove = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                lremove++;
            } else if (s.charAt(i) == ')') {
                if (lremove == 0) {
                    rremove++;
                } else {
                    lremove--;
                }
            }
        }
        helper(s, 0, lremove, rremove);

        return res;
    }

    private static void helper(String str, int start, int lremove, int rremove) {
        if (lremove == 0 && rremove == 0) {
            if (isValid(str)) {
                res.add(str);
            }
            return;
        }

        for (int i = start; i < str.length(); i++) {
            if (i != start && str.charAt(i) == str.charAt(i - 1)) {
                continue;
            }
            // 如果剩余的字符无法满足去掉的数量要求，直接返回
            if (lremove + rremove > str.length() - i) {
                return;
            }
            // 尝试去掉一个左括号
            if (lremove > 0 && str.charAt(i) == '(') {
                helper(str.substring(0, i) + str.substring(i + 1), i, lremove - 1, rremove);
            }
            // 尝试去掉一个右括号
            if (rremove > 0 && str.charAt(i) == ')') {
                helper(str.substring(0, i) + str.substring(i + 1), i, lremove, rremove - 1);
            }
        }
    }

    private static boolean isValid(String str) {
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                cnt++;
            } else if (str.charAt(i) == ')') {
                cnt--;
                if (cnt < 0) {
                    return false;
                }
            }
        }

        return cnt == 0;
    }
    // ------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        String s = ")(";
        System.out.println(removeInvalidParentheses2(s));
    }
    // -----------------------------------------------------------------------------------------------

    // 采用BFS
    // 注意到题目中要求最少删除，这样的描述正是广度优先搜索算法应用的场景，并且题目也要求我们输出所有的结果。我们
    // 在进行广度优先搜索时每一轮删除字符串中的 1 个括号，直到出现合法匹配的字符串为止，此时进行轮转的次数即为最少
    // 需要删除括号的个数。我们进行广度优先搜索时，每次保存上一轮搜索的结果，然后对上一轮已经保存的结果中的每一个字
    // 符串尝试所有可能的删除一个括号的方法，然后将保存的结果进行下一轮搜索。在保存结果时，我们可以利用哈希表对上一
    // 轮生成的结果去重，从而提高效率。
    public List<String> removeInvalidParentheses3(String s) {
        List<String> ans = new ArrayList<String>();
        Set<String> currSet = new HashSet<>();

        currSet.add(s);
        while (true) {
            for (String str : currSet) {
                if (isValid(str)) {
                    ans.add(str);
                }
            }
            if (ans.size() > 0) {
                return ans;
            }
            Set<String> nextSet = new HashSet<String>();
            for (String str : currSet) {
                for (int i = 0; i < str.length(); i++) {
                    if (i > 0 && str.charAt(i) == str.charAt(i - 1)) {
                        continue;
                    }
                    if (str.charAt(i) == '(' || str.charAt(i) == ')') {
                        nextSet.add(str.substring(0, i) + str.substring(i + 1));
                    }
                }
            }
            currSet = nextSet;
        }
    }

}
