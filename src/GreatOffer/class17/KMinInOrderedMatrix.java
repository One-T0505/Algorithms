package class17;

// leetCode378
// 给定一个每一行有序、每一列也有序，整体可能无序的二维数组
// 再给定一个正数k，返回二维数组中，最小的第k个数

public class KMinInOrderedMatrix {

    // 我们需要先完成一个方法f，我们传入一个数t，方法f返回矩阵中<=t的数有多少个，并返回<=t且离t最近的那个数是
    // 什么。为什么这么设计待会再讲。先思考如何完成该方法f。同样地，起点还是从右上角出发，如果当前值<=t，那么
    // 当前位置及其所在行的左侧都符合；如果当前值>t，那就往左移动。

    // 返回一个长度为2的数组res，res[0]表示<=t的元素个数；res[1]表示<=t中最大的元素
    private static int[] f(int[][] m, int t) {
        int N = m.length;
        int M = m[0].length;
        int i = 0, j = M - 1;  // 起点
        int[] res = new int[2];
        res[1] = Integer.MIN_VALUE;
        while (i < N && j > -1) {
            if (m[i][j] <= t) {
                res[0] += j + 1;
                res[1] = Math.max(res[1], m[i++][j]);
            } else  // m[i][j] > t
                j--;
        }
        return res;
    }


    // 有了上面这个方法后，该题目就容易多了。首先要明白，矩阵中最小值min是在左上角，最大值max是在右下角.
    // 采用二分法来寻找全局第k小的数。将要求的信息换一种说法：其实就是要找一个数r，使得在矩阵中<=r的数刚好
    // 有k个，然后返回<=r中最大的那个数，这个数其实就是矩阵中第k小的数。每一次从min～max求中点mid，调用f方法，
    // 看<=mid的数是不是刚好有k个，如果是就返回<=mid中最大的数，这就是为什么方法f要记录<=t中最大数，因为实际二分
    // 求出来的mid在矩阵中可能不存在。

    public static int topK(int[][] m, int k) {
        if (m == null || m.length == 0 || m[0].length == 0 || k < 1)
            return Integer.MAX_VALUE;
        int L = m[0][0];
        int R = m[m.length - 1][m[0].length - 1];
        int[] dp = null;
        int res = 0;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            dp = f(m, mid);
            if (dp[0] < k)
                L = mid + 1;
            else {
                res = dp[1];
                R = mid - 1;
            }
        }
        return res;
    }


    public static void main(String[] args) {
        int[][] m = {{1, 2}, {1, 3}};
        int k = 1;
        System.out.println(topK(m, k));
    }
}
