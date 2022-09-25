public class printTable {
    // 定义一种数:可以表示成若干(数量>1)连续正数和的数
    // 比如: 5 = 2+3；12 = 3+4+5
    // 反例：1不是这样的数，因为要求数量大于1个、连续正数和；2= 1 + 1，2也不是，因为等号右边不是连续正数
    // 给定一个参数N，返回是不是可以表示成若干连续正数和的数
    public static boolean v1(int N){
        if (N <= 2)
            return false;
        for (int i = 1; i < N; i++) {
            int sum = i;
            for (int j = i + 1; j < N; j++) {
                sum += j;
                if (sum > N)
                    break;
                if (sum == N)
                    return true;
            }
        }
        return false;
    }

    public static boolean v2(int N){
        if (N <= 2)
            return false;
        // 这个位运算用来求N是不是的2的指数。假如N是2的指数，那么写成2进制就是这样的形式：
        // 10，100，1000，1000，10000。。。 除了最高位其余全为0；N-1就成了01，011，0111，01111。。。
        // N-1就是除了最高位其余全为1，刚好和N完全相反。此时做与操作结果必然为0。
        // 所以， N & (N-1) == 0 则N为2的幂，否则不是
        return (N & (N - 1)) != 0;
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 200; i++) {
            if (v1(i) != v2(i)){
                System.out.println(i);
                break;
            }
        }
    }
}
