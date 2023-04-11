package Basic.DynamicProgramming;

public class _0070_Code {

    public int climbStairs(int n) {
        if (n <= 0)
            return 0;
        int[] f = new int[n + 1];
        f[0] = 0;
        f[1] = 1;
        if (n > 1) {
            f[2] = 2;
            for (int i = 3; i < f.length; i++) {
                f[i] = f[i - 2] + f[i - 1];
            }
        }
        return f[n];
    }
}
