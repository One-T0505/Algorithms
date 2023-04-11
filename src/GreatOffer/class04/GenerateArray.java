package class04;

import utils.arrays;

import java.util.Stack;

// 生成长度为size的达标数组arr，什么叫达标?
// 达标:对于任意的i<k<j，满足arr[i] + arr[j] != arr[k] * 2
// 给定一个正数size,返回长度为size的达标数组.

public class GenerateArray {

    // 一般这样的题，就得从小慢慢做大，先生成符合题意的小数组，再逐步整合出较大的结果。先假设有一个达标的长度为3的数组
    // A = [a, b, c]  也就是说：a + c != 2b;  那么 B = [2a, 2b, 2c] 也必然达标；那么 C = [2a+1, 2b+1, 2c+1]
    // 也必然达标；先让 D = [2a, 2b, 2c, 2a+1, 2b+1, 2c+1]，如果只在左半部分选i、k、j那必然达标，如果只在右半部分
    // 选也必然达标；如果在整体上选，从左边选一个偶数，右边选一个奇数，和为奇数，不可能等于某个数的两倍。所以如果我们得到了
    // 一个长度为N的达标数组，那么我们就可以得到一个长度为2N的达标数组。如果要生成一个长度为7的达标数组，只需要找到长度为4
    // 的达标数组，扩充一倍后舍弃最后那个即可；如果要搞定长度为4的达标数组，只需要找到长度为2的达标数组；如果要搞定长度为2
    // 的达标数组，只需要找到长度为1的达标数组，任意一个数都达标。

    public static int[] generateArray(int size) {
        if (size < 1)
            return null;
        int[] res = new int[size];
        if (size == 1)
            return new int[]{1};
        Stack<Integer> stack = new Stack<>();
        while (size != 1) {
            stack.push(size);
            size = (size + 1) >> 1;
        }
        res[0] = 1;    // 随便来一个值
        int pre = 1;
        while (!stack.isEmpty()) {
            int aim = stack.pop();
            int cur = pre << 1;
            for (int i = 0; i < pre; i++)
                res[i] <<= 1;
            for (int i = pre; i < aim; i++)
                res[i] = res[i - pre] << 1 | 1;
        }
        return res;
    }


    public static void main(String[] args) {
        int[] arr = generateArray(6);
        arrays.printArray(arr);
    }
}
