package Basic.MonotonicStack;

import java.util.Stack;

// leetCode1504
// 给定一个二维数组matrix，其中的值不是0就是1, 返回全部由1组成的子矩形数量.

// 思路：有了exercise03的经验，这一题依然是要构造出help直方图数组，不过这题是要求全部由1组成的子矩形数量。
//      比如有一个直方图数组help=[3, 2, 6, 5, 4]，那么其全部由1组成的子矩形数量有多少？
//         __
//        |__|__
//        |__|__|__
//   __   |__|__|__|
//  |__|__|__|__|__|
//  |__|__|__|__|__|
//  |__|__|__|__|__|
//
// 对直方图中的每个元素。我们依然还是用单调栈找出左右边界，比如3的左右边界就是[0,0]，它左边是没有比它小的值的，所以只用算
// 而右边离他最近且比自己小的值为2，所以就要算所有高度为3的矩形区域有多少。这个规定是这样决定的：arr[i] - max(左边小值，右边小值)
// 因为当前元素为3，左边又没有小值，所以只需要算1个，那就是高度为3的矩形区域。
// 当前元素为5时，arr[i] - max(左边小值，右边小值) = 5 - max(2, 4) = 1  所以只需要算1个高度为5的矩形区域。
// 当前元素为4时，arr[i] - max(左边小值，右边小值) = 4 - max(2, -1) = 2  所以需要算2个高度，就是高度为4、3的矩形区域。
// 这样就能把所有高度的矩形区域全部算完。
// 当确定一个区域宽度[left,right]后，并且知道要算哪些高度的矩形区域时，应该如何计算呢？
// 还拿4举例，他需要算两个高度4和3的矩形区域。4的宽度为[2, 4]，高度为4的矩形区域就是，(2,3,4)列、(2,3)列、(2)列、
// (3,4)列、(3)列、(4)列 就是6个  高度为3的矩形区域也是6个。所以可以推出一般公式：
// 如果宽度为N，要算的高度为M，那所有的矩形区域为： (N+1)*N/2 * M

// 这里需要注意一点：单调栈的出入栈规则有一点小变化，当栈顶元素和即将要入栈的元素相等时，就弹出，并且此时不记录被弹出元素的
// 左右边界，等到和自己相等的那个元素被弹出时再结算这个的信息，而之前那个被弹出的就不用管了。为什么会出现即将入栈的元素和栈顶
// 元素相等呢？假设他们俩的值都为x，首先这两个元素之间是不可能有比x更大的值进来的，如果有，那就必然会落在栈顶，不会出现现在的
// 局面；也不可能有比x更小的值进来，如果有，那肯定把现在栈顶那个元素已经弹出去了，不会留到现在。所以，这就说明，只有可能这两个
// 相等的值是紧挨着的，或者中间的值都是相等的，所以只需要留最后一个相等的即可。

public class exercise04 {

    public static int findOne(int[][] matrix){
        if (matrix == null)
            return 0;
        int res = 0;
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] help = new int[cols];
        System.arraycopy(matrix[0], 0, help, 0, cols);
        res += calculateV2(help);
        for (int row = 1; row < rows; row++) {
            for (int col = 0; col < cols; col++)
                help[col] = matrix[row][col] == 0 ? 0 : help[col] + 1;
            res += calculateV2(help);
        }
        return res;
    }

    private static int calculateV1(int[] help) {
        if (help == null || help.length == 0)
            return 0;
        int N = help.length;
        int res = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < N; i++) {
            // num表示要算多少个高度，这里不用管areas中的元素是否为-1，因为我们是求最大值，-1是所有值里最小的了
            // 如果碰到-1说明没边界，在max中相当于没用
            while (!stack.isEmpty() && help[stack.peek()] >= help[i]){
                int cur = stack.pop();
                if (help[cur] > help[i]){
                    int left = stack.isEmpty() ? -1 : stack.peek();
                    int width = i - left - 1;
                    int num = help[cur] - Math.max(left == -1 ? 0 : help[left], help[i]);
                    res += ((width * (width + 1)) >> 1) * num;
                }
            }
            stack.push(i);
        }
        while (!stack.isEmpty()){
            int cur = stack.pop();
            int left = stack.isEmpty() ? -1 : stack.peek();
            int width = N - left - 1;
            int num = help[cur] - (left == -1 ? 0 : help[left]);
            res += ((width * (width + 1)) >> 1) * num;
        }
        return res;
    }


    // 有一个通用的优化常数项时间复杂度的技巧，就是用自己写的栈替换系统提供的栈.
    public static int calculateV2(int[] help){
        if (help == null || help.length == 0)
            return 0;
        int N = help.length;
        int res = 0, pointer = -1;
        int[] stack = new int[N];
        for (int i = 0; i < N; i++) {
            // num表示要算多少个高度，这里不用管areas中的元素是否为-1，因为我们是求最大值，-1是所有值里最小的了
            // 如果碰到-1说明没边界，在max中相当于没用
            while (pointer != -1 && help[stack[pointer]] >= help[i]){
                int cur = stack[pointer--];
                if (help[cur] > help[i]){
                    int left = pointer == -1 ? -1 : stack[pointer];
                    int width = i - left - 1;
                    int num = help[cur] - Math.max(left == -1 ? 0 : help[left], help[i]);
                    res += ((width * (width + 1)) >> 1) * num;
                }
            }
            stack[++pointer] = i;
        }
        while (pointer != -1){
            int cur = stack[pointer--];
            int left = pointer == -1 ? -1 : stack[pointer];
            int width = N - left - 1;
            int num = help[cur] - (left == -1 ? 0 : help[left]);
            res += ((width * (width + 1)) >> 1) * num;
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 1, 0, 1},
                          {1, 0, 0, 0},
                          {0, 1, 0, 1}};
        System.out.println(findOne(matrix));
    }
}
