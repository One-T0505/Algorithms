package class42;

// leetCode273
// 将非负整数 num 转换为其对应的英文表示。
// 输入：num = 123
// 输出："One Hundred Twenty Three"

// 输入：num = 12345
// 输出："Twelve Thousand Three Hundred Forty Five"

// 输入：num = 1234567
// 输出："One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"

public class _06_Code {

    // 大流程：英文的数字是三个为一个单位。 a, xxx, yyy, zzz   不会超过长度10的数字  a的后面要加billion
    // xxx后面的单位是million，yyy后面的单位是thousand，zzz后面没单位。
    // 所以当我们拿到一个数字num后，要将其拆分成4段，每弄完一段加上相应的单位即可
    // 所以我们只需要完成一个转换1～999的方法即可。

    public static String[] units = {"Billion ", "Million ", "Thousand ", ""};  // 对应每一段


    // 主方法
    public String numberToWords(int num) {
        if (num == 0)
            return "Zero";
        StringBuilder res = new StringBuilder();
        // 如果为负数，题目要求前面加上Negative，后面按照正数来转换
        if (num < 0)
            res = new StringBuilder("Negative ");
        // 有一种特殊情况，num是系统最小值，因为将其转换为正数超出了int的范围
        if (num == Integer.MIN_VALUE) {
            res.append("Two Billion ");
            num %= -2000000000;
        }
        num = Math.abs(num);
        int high = 1000000000; // 这个high就是用来每次分出来一段的数来进行转换
        int index = 0; // 用单位数组中的哪一个单位
        // 如果num是系统最小值，那么其第四段也就是最高位的那段已经被处理掉了；其他情况都是才开始处理
        // 即便是系统最小值，那么进入循环后虽然多跑一次多余的循环，因为high最开始是十亿级，但是无所谓。
        // cur可以取出当前段内的数字
        while (num != 0) {
            int cur = num / high;
            num %= high;
            // cur != 0 说明当前段内三个数字不都是0，如果都是0，那么当前段就无意义，转换的时候会跳过
            if (cur != 0) {
                res.append(num1To999(cur));
                res.append(units[index]);
            }
            high /= 1000;
            index++;
        }
        return res.toString().trim();
    }

    private String num1To999(int num) {
        if (num < 1 || num > 999)
            return "";
        if (num < 100)
            return num1To99(num);
        int high = num / 100;  // 最高位是几个百
        return num1To19(high) + "Hundred " + num1To99(num % 100);
    }

    private String num1To99(int num) {
        if (num < 1 || num > 99)
            return "";
        // 因为英文中1～19的英语比较特殊
        if (num < 20)
            return num1To19(num);
        int high = num / 10; // 最高位有几个十
        String[] ty = {"Twenty ", "Thirty ", "Forty ", "Fifty ", "Sixty ", "Seventy ",
                "Eighty ", "Ninety "};
        return ty[high - 2] + num1To19(num % 10);
    }


    private String num1To19(int num) {
        if (num < 1 || num > 19)
            return "";
        String[] names = {"One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ",
                "Nine ", "Ten ", "Eleven ", "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ",
                "Sixteen ", "Seventeen", "Eighteen ", "Nineteen "};
        return names[num - 1];
    }
}
