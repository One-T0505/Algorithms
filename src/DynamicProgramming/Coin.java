package DynamicProgramming;

public class Coin {
    // 给一个数组arr，每个元素表示一种面值，不可能为负数，并且面值不重复，每种面值的货币使用次数不限制。
    // 输入一个总金额N，仅用arr提供的面值可以有多少种方法组出金额N？
    public static int v1(int[] arr, int N){
        if (arr == null || arr.length == 0 || N < 0)
            return 0;
        return process(arr, 0, N);
    }

    // 可以自由使用arr[ index...]所有的面值，每一种面值都可以使用任意张，
    // 组成rest有多少种方法
    private static int process(int[] arr, int index, int rest) {
        if (rest < 0)
            return 0;
        if (index == arr.length)
            return rest == 0 ? 1 : 0;
        int res = 0;
        for (int nums = 0; nums * arr[index] <= rest; nums++) {
            res += process(arr, index + 1, rest - nums * arr[index]);
        }
        return res;
    }
}
