package Hard;

import java.util.TreeSet;

// leetCode363
// 给你一个 m x n 的矩阵 matrix 和一个整数 k ，找出并返回矩阵内部矩形区域的不超过 k 的最大数值和。
// 题目数据保证总会存在一个数值和不超过 k 的矩形区域。


public class MaxSumSubMatrix {

    // 主方法
    // 下面的子问题模型是先要搞清楚的，当我们明白如何在一个数组中找出一个子数组累加和不超过k且最大的值时，我们才可以
    // 将该问题扩展成二维模型。解决二维模型的总流程：
    // 先找只包含第1行的区域中不超过k的矩形区域的最大数值    这就是一维模型
    // 然后再找只包含1～2行的矩阵中，不超过k的最大数值   当包含的行数>=2时，就可以把同列不同行的数值加在一起压缩成一个数组例如：
    //   4  2  -3  7
    //   5  9  6   -1   -->   9  11  3  6
    // 为什么可以合并？因为矩形区域，如果某一列被选中，则该列上所有元素都被选中，所以可以把一列的数看成整体
    // 然后是只包含1～3行 .... 1~N行   2～2行  2～3行 .. 2～N行
    // 这个处理流程是N^2的复杂度，每次处理有都是一个一维数组的长度，所以总的时间复杂度是:O(N^2*M*logM)
    // 当行数N远大于M时，就把原矩阵转置一下，让M去做行，这样能让时间复杂度低一些

    public static int maxSumSubMatrix(int[][] matrix, int k) {
        if (matrix == null || matrix[0] == null)
            return Integer.MAX_VALUE;
        if (matrix.length > matrix[0].length)
            matrix = rotate(matrix);
        int N = matrix.length;
        int M = matrix[0].length;
        int res = Integer.MIN_VALUE;
        // 和一维模型中的作用一样，当我们压缩成一个数组后，还是要找到满足要求的子数组，求法和一维的一样，也是通过找前缀
        // 间接求答案
        TreeSet<Integer> set = new TreeSet<>();
        // s表示起始行号  e表示终止行号
        for (int s = 0; s < N; s++) {
            // 每次起始行号换了之后，就要更新这个数组。这个数组是不断压缩数组的，每一行新加进来之后，就要在cur数组上加上
            // 对应的数值
            int[] cur = new int[M];
            for (int e = s; e < N; e++) {
                set.add(0);
                int rowSum = 0;  // 实时记录压缩成一维数组的元素的整体和
                for (int i = 0; i < M; i++) {
                    cur[i] += matrix[e][i];
                    rowSum += cur[i];
                    Integer find = set.ceiling(rowSum - k);
                    if (find != null)
                        res = Math.max(res, rowSum - find);
                    set.add(rowSum);
                }
                set.clear(); // 每次都要完全清空
            }
        }
        return res;
    }

    private static int[][] rotate(int[][] m) {
        int N = m[0].length;
        int M = m.length;
        int[][] r = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                r[i][j] = m[j][i];
            }
        }
        return r;
    }


    // 要想解决这个问题，得先完成一个子问题模型。我们先把二维问题转换成一维问题。
    // 在一个数组中找出子数组累加和不超过 k 的最大和是多少。
    // 思路是之前讲过的：以每个元素作为子数组结尾，看向左能扩充的最远处使得子数组累加和最接近k且不超过k。
    // 这个时间复杂度就是O(N^2)  我们可以使用有序表来优化
    // 假设来到i位置，现在是要找以arr[i]为子数组结尾的最接近k的子数组累加和是多少。如果我们知道0～i的整体累加和是sum，
    // 其实我们就是要找一个从0～j的前缀和a使得a>=sum-k且是最小的，这样一来，j+1～i的累加和就是不超过k的最大值了。
    // 时间复杂度：O(NlogN)
    private static int floorK(int[] arr, int k) {
        if (arr == null || arr.length == 0)
            return -1;  // 表示不存在
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);
        int sum = 0;
        int N = arr.length;
        int res = Integer.MIN_VALUE;
        for (int e : arr) {
            sum += e;  // sum实时记录从0～当前元素的整体累加和
            Integer find = set.ceiling(sum - k);  // find就是上面的sum-k
            if (find != null) { // 如果存在的话，就更新答案
                res = Math.max(res, sum - find);
            }
            set.add(sum);  // 这样有序表里就记录了所有的前缀和
        }
        return res;
    }
}
