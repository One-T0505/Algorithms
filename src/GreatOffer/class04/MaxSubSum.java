package class04;


public class MaxSubSum {
    // leetCode53
    // 1.返回一个数组中，子数组最大累加和.(既然这么问了，那必然是说数组中会有负数)
    //   思路是：找出一每个元素作为子数组的结尾，看能找到最大子数组的累加和是多少，整个全局的答案必然在其中
    public static int maxSubArray(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        int pre = arr[0];
        int res = arr[0];
        for (int i = 1; i < arr.length; i++) {
            int cur = Math.max(arr[i], arr[i] + pre);
            res = Math.max(res, cur);
            pre = cur;
        }
        return res;
    }
    // =========================================================================================


    // 2.给一个二维数组matrix，返回matrix中最大累加和的子矩阵。
    // 这个是上面问题的推广，先明白上面那个问题之后再看这个。假设矩阵有N行M列，
    // 我们这样去搜寻一个矩阵：
    // 先看仅包含0～0行中最大的子矩阵，再看仅包含0～1行的最大子矩阵，0～2，0～3，... , 0～N-1；
    // 然后再看1～1，1～2，... ,1～N-1；按照这样一种顺序去找最大子矩阵，结果必然存在于其中，我们这里的思路是确定每行
    // 作为子矩阵的开头能找到的局部最大解。确定了搜寻范围后，
    // 如何在范围中找出最大子矩阵？比如仅包含0～0行，那不就是在数组中找出最大累加和子数组问题吗？当仅包含0～1行时，
    // 就让0行的元素对应加上1行元素的值，此时再对这个数组找出最大累加和子数组，找的就是仅包含这两行的最大子矩阵。
    // 所以时间复杂度为：O(行^2 * 列)    该方法灵活的地方在于可根据数据规模调整。如果 行数 << 列数，就按刚才的做法来；
    // 如果 列数 << 行数，那就让矩阵转置一下就可以了，让小的那个作平方项。

    public static int maxSubMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0)
            return 0;
        int N = matrix.length;
        int M = matrix[0].length;
        int res = matrix[0][0];
        for (int i = 0; i < N; i++) {
            int[] help = new int[M];
            for (int j = i; j < N; j++) {
                for (int k = 0; k < M; k++)
                    help[k] += matrix[j][k];
                res = Math.max(res, maxSubArray(help));
            }
        }
        return res;
    }

}
