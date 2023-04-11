package Basic.Parentheses;


// 给定一个经过编码的字符串，返回它解码后的字符串。
// 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
// 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
// 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
// 3[a]2[bc] --> aaabcbc   3[a2[c]] --> accaccacc   2[abc]3[cd]ef --> abcabccdcdcdef
public class _0394_DecodeString {


    // 本题也要用到括号嵌套模型中的经典递归模版：让递归函数返回两个值。


    // 递归函数需要返回两个值：一个是当前递归函数已经解码好的字符串，一个是结束的位置，便于告诉别人下次要从哪里
    // 开始算，因为两个返回值类型不同，所以需要用类包装。
    public static class Info {
        public String res;
        public int end;

        public Info(String res, int end) {
            this.res = res;
            this.end = end;
        }
    }



    public String decodeString(String s) {
        char[] chs = s.toCharArray();
        return f(chs, 0).res;
    }


    // 从s[i]开始往后解码，遇到']'或者遍历完字符串了停止
    private static Info f(char[] chs, int i) {
        StringBuilder sb = new StringBuilder();
        int times = 0;
        while (i < chs.length && chs[i] != ']') {
            if ((chs[i] >= 'a' && chs[i] <= 'z') || (chs[i] >= 'A' && chs[i] <= 'Z'))
                sb.append(chs[i++]);
            else if (chs[i] >= '0' && chs[i] <= '9')
                times = times * 10 + chs[i++] - '0';
            else {  // 碰到'['了
                Info next = f(chs, i + 1);
                sb.append(next.res.repeat(times));
                times = 0;
                i = next.end + 1;
            }
        }
        return new Info(sb.toString(), i);
    }


}
