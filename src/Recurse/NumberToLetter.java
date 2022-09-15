package Recurse;

public class NumberToString {
    // 规定1和A对应、2和B对应、3和C对应...
    // 那么一个数字字符串比如"111”就可以转化为: "AAA"、 "KA"和"AK"
    // 给定一个只有数字字符组成的字符串str，返回有多少种转化结果
    public static int numberToString(String s){
        if (s == null || s.length() == 0)
            return 0;
        char[] chars = s.toCharArray();
        int len = chars.length;
        return 0;
    }
}
