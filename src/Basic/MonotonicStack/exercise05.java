package Basic.MonotonicStack;

// leetCode907
// 给定一个数组arr, 返回所有子数组最小值的累加和.

// 思路：利用单调栈的思想，换一种找子数组的标准：按照以谁作为子数组中最小值为标准找出所有子数组。
//      对于数组中每个元素arr[i]，可以用单调栈找出左边界left和右边界right，在[left+1,right+1]中都是大于等于arr[i]的元素，
//      假如i在left和right之间，那么此时以arr[i]作为最小元素的子数组一共有: (i - left + 1) * (right - i + 1)
// 这个规律很容易找，自己举个例子就可以。

// 这里如果有重复值的情况，是需要把相等值弹出的，就是说要保证栈里严格单调。为什么要这样呢？
// 假如有一个arr是这样的:   0   1   2   3   4   5   6   7   8   9   10  11  12  13  14
//                       2   4   5   3   6   7   8   3   6   7   8    3   5   3   2
// 右边不能跨界的情况：
//                       (___(___(__()___)___)___)
//                       (___(___(___(___(___(___(___()__)___)___)
// 右边如果跨界了：
//                       (___(___(__()___)___)___)___)___)___)___)____)___)___)
//                       (___(___(___(___(___(___(___()__)___)___)____)___)___)
// 这样就有重复的子数组了
//
//   arr[3]结算出的信息为：[0, 7]
//   arr[7]结算出的信息为：[0, 11]
//   arr[11]结算出的信息为：[0, 13]
// 这样我们的目的就达到了，就是说让其左边界可以跨过和自己相等的值，而右边界无法跨过和自己相等的值。如果右边也可以跨过
// 和自己相等的值，那么在用上面的通用公式时就会重复计算某些子数组。
//
public class exercise05 {
    // 暴力解法


    // 单调栈解法
    public static int sumV2(int[] arr){
        if (arr == null || arr.length == 0)
            return 0;
        int[][] Areas = boundAreas(arr);
        int N = arr.length;
        int res = 0;
        for (int i = 0; i < N; i++) {
            int left = Areas[0][i] + 1;
            int right = Areas[1][i] == -1 ? N - 1 : Areas[1][i] - 1;
            res += (i - left + 1) * (right - i + 1) * arr[i];
        }
        return res;
    }

    // 该方法返回的是一个2 * arr.length的矩阵，第0行表示所有元素的左边界信息，第1行表示所有元素的右边界信息。
    private static int[][] boundAreas(int[] arr) {
        int N = arr.length;
        int[][] Area = new int[2][N];
        int[] stack = new int[N];
        int pointer = -1, index = 0;
        for (int i = 0; i < N; i++) {
            while (pointer != -1 && arr[stack[pointer]] >= arr[i]){
                int cur = stack[pointer--];
                Area[0][cur] = pointer == -1 ? -1 : stack[pointer];
                Area[1][cur] = i;
            }
            stack[++pointer] = i;
        }
        while (pointer != -1){
            int cur = stack[pointer--];
            Area[0][cur] = pointer == -1 ? -1 : stack[pointer];
            Area[1][cur] = -1;
        }
        return Area;
    }

    public static void main(String[] args) {
        int[] arr = {3, 7, 2, 4};
        System.out.println(sumV2(arr));
    }
}
