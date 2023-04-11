package GreatOffer.class24;

// 字节面试题
// 正常的里程表会依次显示自然数表示里程
// 吉祥的里程表会忽略含有4的数字而跳到下一个完全不含有4的数
// 正常:1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
// 吉祥:1 2 3 5 6 7 8 9 10 11 12 13 15 16 17...38 39 50 51 52 53 55
// 给定一个吉祥里程表的数字num(当然这个数字中不含有4)  返回这个数字代表的真实里程Y
// 比如：给了一个吉祥里程数5，那么实际上他表示真实里程4

public class _02_Code {

    // 只用1位数表示吉祥里程，0也算，就能表示9个；如果只用两位数那么可以表示72个数；最多用2位数来表示吉祥里程
    // 就可以表示9+72=81个；最多用3位数表示可以有729个。。。这是公比为9的等差数列。所以我们来生成一个数组arr，
    // 让arr[i]表示：最多使用i位数能表示多少个吉祥里程。题目的意思就是给定一个数，找出她在吉祥里程中是第几个。
    // 这个arr可以使用下面比较奇特的方法生成：

    // arr[i]
    public static long[] arr =
            {1L, 9L, 81L, 729L, 6561L, 59049L, 531441L, 4782969L, 43046721L, 387420489L,
                    3486784401L, 31381059609L, 282429536481L, 2541865828329L, 22876792454961L,
                    205891132094649L, 1853020188851841L, 16677181699666569L, 150094635296999121L};

    private static void printArr() { // 在终端打印出来后复制一下就生成了arr
        System.out.println("long类型最大长度为：" + decimalLength(Long.MAX_VALUE));
        System.out.print("public static long[] arr = {");
        long v = 1L;
        for (int i = 0; i < 19; i++) {
            System.out.print(v + "L, ");
            v *= 9L;
        }
        System.out.println("}");
    }


    // 随便给一个数num，先求出num有几位数，比如k位数，那么答案的第一部分就是arr[k-1]。假如num==567329，我们现在
    // 想求的是从1..567329，有多少个数，注意：我们求的是有多少个吉祥里程数，567329这个数实际上代表的里程数就代表
    // 1..567329有多少个吉祥里程数。
    //  1.答案的第一部分就是arr[5]，这个表示只用1、2、3、4、5位数一共能表示多少个吉祥里程数。因为arr[5]的值已经表示：
    //    1～99999有多少个数了。
    //  2.答案的第二部分就是：最高位从1～5-1，最高位为5时要单独算。
    //    此时有个条件需要判断：如果最高位h< 4 那么就有h-1个(1 2..h-1) 如果h>4 那么就有h-2个
    //    对于这每一个 x_____,剩下的5位数都可以从0～99999算，所以这就是arr[5]的含义，所以明白为什么要算0了吗？
    //    x00000，是5位数中的第一个吉祥数。答案的第二部分就是：(h-1或者h-2)*arr[5]
    //  3.答案的第三部分就是：最高位固定为5，那也就是求67329是第几个吉祥数，这样就递归起来了。

    public static long paces(long num) {
        if (num <= 0)
            return 0;
        int len = decimalLength(num);
        long offset = offset(len);
        // 得到num的最高位数值
        long highest = num / offset;

        // 答案的第一部分
        // 为什么要单独-1？因为我们在构建数组时为了方便，在只有1位数的情况下多算了一个0，而实际上是没有0的
        // 在实际的吉祥里程中两位数可以表示80个数，包括一位数的8和两位数的72。在多位数的情况下，x0000是他们
        // 的第一个吉祥数，但这个数是在吉祥里程中真是会出现的；只有一位数的情况下，0是不会出现的。
        long p1 = arr[len - 1] - 1;
        // 答案的第二部分
        long p2 = (highest - (highest < 4 ? 1 : 2)) * arr[len - 1];
        // 答案的第三部分
        // 如果用户给的数是567329，那么调用f方法穿的参数就是：f(67329, 10000, 5);
        // 第三部分算的就是：500000~567329有多少个吉祥数
        long p3 = f(num % offset, offset / 10, len - 1);

        return p1 + p2 + p3;
    }


    // 此时传入的num是可以算0的，因为此时传入的num前面已经有数字了，上游调用时已经把最高位抹除了
    private static long f(long num, long offset, int len) {
        if (len == 0)  // 那就是被隐藏的最高位自己单独一个
            return 1;
        long highest = num / offset;
        // 主函数中第二部分是highest- (1 : 2)，因为那会是在最高位上判断，最高位前面是没有数的，不能从0开始
        // 但是递归方法中num前面是有固定的数的，是可以从0开始算的
        return (highest - (highest < 4 ? 0 : 1)) * arr[len - 1] +
                f(num % offset, offset / 10, len - 1);
    }


    // 求一个数的位数
    private static int decimalLength(long val) {
        int len = 0;
        while (val != 0) {
            len++;
            val /= 10;
        }
        return len;
    }


    // 如果给的len==6，那么返回 100000
    private static long offset(int len) {
        long res = 1L;
        for (int i = 0; i < len - 1; i++)
            res *= 10L;
        return res;
    }
    // ===============================================================================================


    // 暴力方法
    public static long solution(long num) {
        if (num < 1)
            return 0;
        long count = 0;
        for (long i = 1; i <= num; i++) {
            if (!contains4(i))
                count++;
        }
        return count;
    }

    private static boolean contains4(long num) {
        while (num != 0) {
            if (num % 10 == 4) {
                return true;
            }
            num /= 10;
        }
        return false;
    }
    // =============================================================================================


    // for test
    public static void test() {
        // 功能测试
        long max = 8888L;
        System.out.println("功能测试开始，验证 0 ~ " + max + " 以内所有的结果");
        for (long i = 1; i <= max; i++) {
            // 测试的时候，输入的数字i里不能含有4，这是题目的规定！
            if (!contains4(i)) {
                long res1 = paces(i);
                long res2 = solution(i);
                if (res1 != res2) {
                    System.out.println("Failed");
                    System.out.println("要判断的是：" + i);
                    System.out.println("暴力方法：" + res2);
                    System.out.println("优化方法：" + res1);
                    return;
                }
            }
        }
        System.out.println("AC");

        // 性能测试
        long num = 8173528635L;
        long start;
        long end;
        System.out.println("性能测试开始，计算 num = " + num + " 的答案");
        start = System.currentTimeMillis();
        long res1 = solution(num);
        end = System.currentTimeMillis();
        System.out.println("暴力方法答案 : " + res1 + ", 运行时间 : " + (end - start) + " ms");

        start = System.currentTimeMillis();
        long res2 = paces(num);
        end = System.currentTimeMillis();
        System.out.println("方法三答案 : " + res2 + ", 运行时间 : " + (end - start) + " ms");
    }


    public static void main(String[] args) {
        test();
    }
}
