package class08;


// 给定一个char[][] matrix，也就是char类型的二维数组，再给定一个字符串word，可以从任何一个某个位置出发，
// 可以走上下左右能不能找到word?
// char[][] m = {{'a', 'b', 'z'},
//               {'c', 'd', 'o'},
//               {'f', 'e', 'o'}}
//
// 问题1: 可以走重复路的情况下，能不能找到？
//       比如，word = "z000z"， 是可以找到的，z->0->0->0->z，因为允许走一条路径中已经走过的字符
// 问题2 :不可以走重复路的情况下，返回能不能找到
//       比如，word = "z000z"， 是不可以找到的，因为已经走过的字符不能重复走

public class FindWordInMatrix {

    // 先解决问题1，可以重复的情况下    这两个问题都可用这个主函数，唯一的区别只是递归时的区别
    public static boolean wander(char[][] m, String word) {
        if (m == null || m.length == 0 || m[0].length == 0 ||
                word == null || word.length() == 0)
            return false;
        int N = m.length;
        int M = m[0].length;
        char[] words = word.toCharArray();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (f(m, i, j, 0, words))
                    return true;
            }
        }
        return false;
    }


    // 定义一个递归函数 f(i, j, k)  (i,j) 表示从矩阵中出发的位置，k表示word的第k个字符，
    // f返回从(i, j)出发能不能搞定word[k..]。也就是说我们要对矩阵中的每个位置(i, j)调用：
    // f(i, j, 0)，只要有一次返回true，那最后的结果就是true
    private static boolean f(char[][] m, int i, int j, int k, char[] words) {
        if (k == words.length)
            return true;
        if (i < 0 || i >= m.length || j < 0 || j >= m[0].length || m[i][j] != words[k])
            return false;
        // 上一个if的所有无效条件都不成立
        return f(m, i - 1, j, k + 1, words) || f(m, i + 1, j, k + 1, words) ||
                f(m, i, j - 1, k + 1, words) || f(m, i, j + 1, k + 1, words);
    }


    // 用于实现不走回头路的情况。这里采取的思路是，将走过的地方的字符值设为0。该方法不能改动态规划，因为m中的内容会发生改变
    private static boolean g(char[][] m, int i, int j, int k, char[] words) {
        if (k == words.length)
            return true;
        // 这里新增了一个条件：m[i][j] == 0  说明当前走的路已经来到了走过的地方
        if (i < 0 || i >= m.length || j < 0 || j >= m[0].length || m[i][j] == 0 || m[i][j] != words[k])
            return false;

        // 执行到这里了，说明上面的无效条件都不满足，即：m[i][j] == words[k] && m[i][j] != 0
        m[i][j] = 0;
        boolean res = false;
        if (g(m, i - 1, j, k, words) || g(m, i + 1, j, k, words) ||
                g(m, i, j - 1, k, words) || g(m, i, j + 1, k, words))
            res = true;
        m[i][j] = words[k];    // 还原现场，这是深度递归，所以需要还原现场
        return res;
    }
}
