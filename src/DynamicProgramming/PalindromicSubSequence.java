package DynamicProgramming;

// 给定一个字符串str， 返回这个字符串的最长回文子序列长度
// 比如:str = “a12b3c43def2ghi1kpm"
// 最长回文子序列是"1234321” 或者“123c321” 返回长度7
public class PalindromicSubSequence {

    // 暴力递归寻求尝试，看得出这是一种范围尝试模型。范围尝试模型非常注重开头和结尾这两个边界的情况
    public static int pssV1(String ori){
        if (ori == null || ori.length() == 0)
            return 0;
        char[] chars = ori.toCharArray();
        return process1(chars, 0, chars.length - 1);
    }

    private static int process1(char[] chars, int L, int R) {
        if (L == R) // 只有一个字符
            return 1;
        else if (L == R - 1) { // 只有两个字符
            return chars[L] == chars[R] ? 2 : 1;
        } else { // 普遍情况
            // 最长回文子序列不包括左边界L和右边界R
            int p1 = process1(chars, L + 1, R - 1);
            int p2 = process1(chars, L, R - 1);  // 最长回文子序列包括左边界L，不包括右边界R
            int p3 = process1(chars, L + 1, R);  // 最长回文子序列不包括左边界L，包括右边界R
            // 最长回文子序列既包括左边界L也包括右边界R
            int p4 = chars[L] == chars[R] ? 2 + p1 : 0; // 本来是：2 + process1(chars, L + 1, R - 1)
            return Math.max(Math.max(p1, p2), Math.max(p3, p4));
        }
    }
    // ================================================================================================


    // 动态规划法  分析暴力递归中的可变参数只有L和R 范围都是从0~str.length-1
    public static int dpV1(String ori){
        if (ori == null || ori.length() == 0)
            return 0;
        char[] chars = ori.toCharArray();
        int N = chars.length;
        int[][] cache = new int[N][N];   // 矩阵对角线以下的部分都不用，因为L不可能大于R
        // 根据暴力递归的调用关系，可以发现填缓存表的顺序是按照对角线的顺序填
        for (int startCol = 0; startCol < N; startCol++){
            int L = 0, R = startCol;
            while (R < N){
                if (L == R)
                    cache[L][L] = 1;
                else if (L == R - 1)
                    cache[L][R] = chars[L] == chars[R] ? 2 : 1;
                else {
                    int p1 = cache[L + 1][R - 1];
                    int p2 = cache[L][R - 1];
                    int p3 = cache[L + 1][R];
                    int p4 = chars[L] == chars[R] ? 2 + p1 : 0;
                    cache[L][R] = Math.max(Math.max(p1, p2), Math.max(p3, p4));
                }
                L++;
                R++;
            }
        }
        return cache[0][N - 1];
    }
    // ================================================================================================


    // dpV1是由暴力递归一成不变改出的位置严格依赖的动态规划。我们对缓存的空间依赖关系有了一定感知后，可以进一步优化
    // 可以发现，缓存中随机一个格子cache[L][R]依赖于：
    // 1.左下 cache[L + 1][R - 1]   2.左 cache[L][R - 1]   3.下 cache[L + 1][R]  4. 左下+2 或者0
    // 所以说，一个格子是由其左、下、左下这三个格子决定的，并且是由最大值来决定
    // 所以一个格子的值必然是 >= 左、下、左下的， 看下面这样的关系图：
    //
    //                (L,R) (L,R+1)              [L,R]一定是大于等于其左、下、左下三个元素的
    //              ○   ●   ●                    [L,R+1] 现在要找出它自己的左、下、左下三个元素最大的
    //              ○   ○   ○                    因为 [L,R+1]的左 经过判断是必然不小于[L,R+1]的左下，
    //                                           所以左下这个地方就不用考虑了
    //
    //
    public static int dpV2(String ori){
        if (ori == null || ori.length() == 0)
            return 0;
        char[] chars = ori.toCharArray();
        int N = chars.length;
        int[][] cache = new int[N][N];   // 矩阵对角线以下的部分都不用，因为L不可能大于R
        // 根据暴力递归的调用关系，可以发现填缓存表的顺序是按照对角线的顺序填
        for (int startCol = 0; startCol < N; startCol++){
            int L = 0, R = startCol;
            while (R < N){
                if (L == R)
                    cache[L][L] = 1;
                else if (L == R - 1)
                    cache[L][R] = chars[L] == chars[R] ? 2 : 1;
                else {
                    cache[L][R] = Math.max(cache[L][R - 1], cache[L + 1][R]);
                    if (chars[L] == chars[R])
                        cache[L][R] = Math.max(cache[L][R], 2 + cache[L + 1][R - 1]);
                }
                L++;
                R++;
            }
        }
        return cache[0][N - 1];
    }

    public static void main(String[] args) {
        System.out.println(pssV1("abwdcebqqa"));
        System.out.println(dpV1("abwdcebqqa"));
        System.out.println(dpV2("abwdcebqqa"));
    }
}
