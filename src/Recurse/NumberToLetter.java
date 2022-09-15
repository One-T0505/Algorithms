package Recurse;

public class NumberToLetter {
    // 规定1和A对应、2和B对应、3和C对应...
    // 那么一个数字字符串比如"111”就可以转化为: "AAA"、 "KA"和"AK"
    // 给定一个只有数字字符组成的字符串str，返回有多少种转化结果
    public static int numberToLetter(String s){
        if (s == null || s.length() == 0)
            return 0;
        return process(s.toCharArray(), 0);
    }

    // chars是整个字符数组，index表示从哪里开始转换，也就是说从0～index-1已经做好选择了，并且让当前方法
    // 不得不从chars[index]处做选择
    private static int process(char[] chars, int index) {
        if (index == chars.length)  // 0～chars.length-1的安排就是一种可行结果
            return 1;
        if (chars[index] == '0') // 如果之前的决策让当前方法直接从0开始做选择，那就行不通，因为0没有对应的字母
            return 0;
        if (chars[index] == '1'){ // 当前字符为1，那就必然有两种选择，将当前字符转化为A；或者将当前字符与后一个字符一起转换
            int res = process(chars, index + 1); // 单独转换自己
            if (index + 1 < chars.length)
                res += process(chars, index + 2);
            return res;
        }
        // 需要分情况，如果下一位是0～5，那就可以有两种情况；如果下一位是6～9，那当前字符只能单独转换
        if (chars[index] == '2'){
            int res = process(chars, index + 1);
            if (index + 1 < chars.length && (chars[index + 1] >= '0' && chars[index + 1] <= '6'))
                res += process(chars, index + 2);
            return res;
        }
        // 表示chars[index] == '3' ~ '9'，那就只有一种选择，只能单独转换自己
        return process(chars, index + 1);
    }

    // 动态规划的方法完全就是根据递归的步骤改写的
    public static int dp(String s){
        if (s == null || s.length() == 0)
            return 0;
        char[] chars = s.toCharArray();
        int N = chars.length;
        int[] cache = new int[N + 1];
        cache[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            if (chars[i] == '0')
                cache[i] = 0;
            else if (chars[i] == '1'){ // 当前字符为1，那就必然有两种选择，将当前字符转化为A；或者将当前字符与后一个字符一起转换
                cache[i] = cache[i + 1]; // 单独转换自己
                if (i + 1 < N)
                    cache[i] += cache[i + 2];
            } else if (chars[i] == '2'){
                cache[i] = cache[i + 1];
                if (i + 1 < N && (chars[i + 1] >= '0' && chars[i + 1] <= '6'))
                    cache[i] += cache[i + 2];
            } else
            // 表示chars[i] == '3' ~ '9'，那就只有一种选择，只能单独转换自己
            cache[i] = cache[i + 1];
        }
        return cache[0];
    }

    public static void main(String[] args) {
        System.out.println(numberToLetter("1111111"));
        System.out.println(dp("1111111"));
    }
}
