package class03;


// 给定一个只有0和1组成的二维数组，返回边框全是1的最大正方形面积。正方形内部是不是1无所谓。

public class _05_findSquare {

    // 首先在这样一个二维数组中，要确定出所有的正方形就是O(N^3)。遍历每个元素，以该元素为正方形左上角点，然后选取边
    // 长从1到min(长，宽),所以时间复杂度来到O(N^3)；确定了正方形，然后需要对正方形验证，我们优化的地方只能放在这里，
    // 只有让验证操作尽可能低，才能达到最优解。如果想让验证操作尽可能快，那就需要对数组进行预处理来制作一些信息，方便
    // 验证的时候直接取。我们需要对每个点制作两个需要的信息：以该点为起点向下有多少个连续的1；以该点为起点向右有多少
    // 个连续的1。当我们确定了一个正方形边界后，在左上角检查右信息和下信息是否和边长相等；在左下角点仅查看右信息；在右
    // 上角点检查下信息，如果都和边长相等，就符合。

    public static int findSquare(int[][] m) {
        if (m == null || m.length == 0)
            return 0;
        int[][] right = setRight(m);   // O(N^2)
        int[][] down = setDown(m);     // O(N^2)
        int res = 0;
        int N = m.length;
        int M = m[0].length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                // Math.min(N - i, M - j) 要考虑不要越界
                for (int border = 1; border <= Math.min(N - i, M - j); border++) {
                    if (right[i][j] >= border && down[i][j] >= border &&   // 检查左上角点
                            right[i + border - 1][j] >= border &&    // 检查左下角点
                            down[i][j + border - 1] >= border)      // 检查右上角点
                        res = Math.max(res, border * border);
                }
            }
        }
        return res;
    }

    private static int[][] setDown(int[][] m) {
        int N = m.length;
        int M = m[0].length;
        int[][] down = new int[N][M];
        // 先处理最后一行，这一行最好处理，如果是1，那么下信息就填1，因为包含自己，但是下边又没有元素了
        System.arraycopy(m[N - 1], 0, down[N - 1], 0, M);
        for (int i = N - 2; i >= 0; i--) {
            for (int j = M - 1; j >= 0; j--) {
                down[i][j] = m[i][j] == 0 ? 0 : down[i + 1][j] + 1;
            }
        }
        return down;
    }


    private static int[][] setRight(int[][] m) {
        int N = m.length;
        int M = m[0].length;
        int[][] right = new int[N][M];
        // 先处理最后一列，这一列最好处理，如果是1，那么右信息就填1，因为包含自己，但是右边又没有元素了
        for (int i = 0; i < N; i++)
            right[i][M - 1] = m[i][M - 1];
        for (int i = N - 1; i >= 0; i--) {
            for (int j = M - 2; j >= 0; j--) {
                right[i][j] = m[i][j] == 0 ? 0 : right[i][j + 1] + 1;
            }
        }
        return right;
    }

    public static void main(String[] args) {
        int[][] m = {{1, 1, 0},
                {1, 0, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 0},
                {1, 1, 1},
                {1, 1, 0}};
        System.out.println(findSquare(m));
    }
}
