package DynamicProgramming;

public class NumberToLetter {
    // 规定1和A对应、2和B对应、3和C对应...
    // 那么一个数字字符串比如"111”就可以转化为: "AAA"、 "KA"和"AK"
    // 给定一个只有数字字符组成的字符串str，返回有多少种转化结果
    public static int numberToLetterV1(String s){
        if (s == null || s.length() == 0)
            return 0;
        return process1(s.toCharArray(), 0);
    }

    // chars是整个字符数组，index表示从哪里开始转换，也就是说从0～index-1已经做好选择了，并且让当前方法
    // 不得不从chars[index]处做选择
    private static int process1(char[] chars, int index) {
        if (index == chars.length)  // 0～chars.length-1的安排就是一种可行结果
            return 1;
        if (chars[index] == '0') // 如果之前的决策让当前方法直接从0开始做选择，那就行不通，因为0没有对应的字母
            return 0;
        if (chars[index] == '1'){ // 当前字符为1，那就必然有两种选择，将当前字符转化为A；或者将当前字符与后一个字符一起转换
            int res = process1(chars, index + 1); // 单独转换自己
            if (index + 1 < chars.length)
                res += process1(chars, index + 2);
            return res;
        }
        // 需要分情况，如果下一位是0～6，那就可以有两种情况；如果下一位是7～9，那当前字符只能单独转换
        if (chars[index] == '2'){
            int res = process1(chars, index + 1);
            if (index + 1 < chars.length && (chars[index + 1] >= '0' && chars[index + 1] <= '6'))
                res += process1(chars, index + 2);
            return res;
        }
        // 表示chars[index] == '3' ~ '9'，那就只有一种选择，只能单独转换自己
        return process1(chars, index + 1);
    }
    // =================================================================================================


    // 暴力递归还有一种实现递归的方式，该方式相比于process1会比较简单.但是主过程是一模一样的
    public static int numberToLetterV2(String s){
        if (s == null || s.length() == 0)
            return 0;
        return process2(s.toCharArray(), 0);
    }

    private static int process2(char[] chars, int index) {
        if (index == chars.length)
            return 1;
        if (chars[index] == '0')
            return 0;
        // 单独转换自己
        int res = process2(chars, index + 1);
        // 表示自己和下一个字符转出的数值在0~26范围内就可以转换
        if (index + 1 < chars.length && (chars[index] - '0') * 10 + (chars[index + 1] - '0') < 27)
            res += process2(chars, index + 2);
        return res;
    }
    // =================================================================================================


    // 动态规划的方法完全就是根据递归的步骤改写的,这个动态规划方法是根据第一个暴力递归改写的
    public static int dpV1(String s){
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
    // =================================================================================================


    // 这个动态规划方法是根据第二个暴力递归改写的
    public static int dpV2(String s){
        if (s == null || s.length() == 0)
            return 0;
        char[] chars = s.toCharArray();
        int N = chars.length;
        int[] cache = new int[N + 1];
        cache[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            if (chars[i] != '0'){
                cache[i] = cache[i + 1];
                // 表示自己和下一个字符转出的数值在0~26范围内就可以转换
                if (i + 1 < chars.length && (chars[i] - '0') * 10 + (chars[i + 1] - '0') < 27)
                    cache[i] += cache[i + 2];
            }
        }
        return cache[0];
    }

    public static void main(String[] args) {
        System.out.println(numberToLetterV1("15121134192111"));
        System.out.println(numberToLetterV2("15121134192111"));
        System.out.println(dpV1("15121134192111"));
        System.out.println(dpV2("15121134192111"));
    }
}
