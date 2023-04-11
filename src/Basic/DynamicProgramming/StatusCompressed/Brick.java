package Basic.DynamicProgramming.StatusCompressed;

// 你有无限的1*2的砖块，要铺满M*N的区域 不同的铺法有多少种?

import java.util.Arrays;

public class Brick {

    // 这个题目给我们，我们很容易一头雾水，所以我们要自己加些限制才能有思路。我们规定每块砖只向右或者向上摆放。
    // 向右摆放就是1*2，向上摆放就是2*1。
    public static int brick1(int N, int M){
        // 依然把N、M想象成一个矩阵，下标索引从0开始的，所以在下面的处理中，N-1就是最后一行，M-1就是最后一列。
        // 怎么处理无所谓，你把它看成下标从1开始的也可以。
        if (N < 1 || M < 1 || ((N * M) & 1) != 0)
            return 0;
        if (N == 1 || M == 1)
            return 1;
        return f(0, (1 << M) - 1, N, M);
    }


    // 这里的递归函数非常复杂，我们需要详细了解其各个参数的作用以及返回值的含义。
    // i表示来到第i行来处理了，并且潜台词是第i-2行～第0行这片区域都被瓷砖填满了，没有空隙。
    // 而pre就是表示第 i-1 行瓷砖的情况，我们使用位信息来存储一整行的状态，0表示无瓷砖，1表示有瓷砖。
    // 比如 10110  这次我们用最高位对应第0列的做法来对应，这些比较符合自然直觉。
    // 当pre有某位有0时，那我们在第i行对应列的位置必须将瓷砖向上摆放，这样才能将pre中的空位填补上，如果我们在
    // 第i行都不去填补pre中的这个空位，当我们来到i+1行的时候，就再也来不及补了，因为瓷砖的高度最多为2，已经补不到了。
    // 也就是说，pre中为0的位置，在当前行的地方必须向上摆放一块瓷砖，只有pre中为1的位置，才是允许我们自由发挥的地方,
    // 但是自由发挥的限度也仅仅在于：能否在此列向右摆放一块瓷砖。不可能是向上摆，因为上方已经有了，所以仅仅能决定
    // 是否向右摆。
    private static int f(int i, int pre, int N, int M) {
        if (i == N){ // 结束了
            // 如果pre是全1，那就说明最后一行也全部填满了，并让自己走到了终止位置，那么之前的摆法就是ok的，返回1
            return pre == (1 << M) - 1 ? 1 : 0;
        }
        // 还没到终止行
        // 这个位运算，是算出当前行的状态，pre中为0的位置cur中必须为1；但要注意有效位，一共有32位，我们只需要
        // 低位的M位。 比如 pre == 1001011  那么 cur == 0110100  并且还有保持高位(32-M)个全为0
        // 当我们做好了cur之后，为0的位置才是我们可以决定当前行是否要摆瓷砖的位置
        int cur = (~pre) & ((1 << M) - 1);
        // 这里就变成了从左到右的尝试模型，尝试当前行的各种摆法，并且返回一共能有多少种方法
        // 记住：这个dfs的返回值是尝试完了当前行之后，还要处理后续所有行，返回总结果。
        // 这里为什么传M-1呢？因为我们尝试的列的方向自然是第0列～第M-1列，而第0列对应的是cur的第M-1位
        // 所以我们传入的是M-1，标记的是cur中的哪一位的信息。
        return dfs(cur, M - 1, i, N, M);
    }


    private static int dfs(int cur, int j, int row, int N, int M) {
        // 神奇吗？该递归函数有调用了父级递归函数！！！  来解释下为什么
        // 我们是从M-1向第0列处理的，因为这样的顺序对应的是位信息从低到高位。所以当j==-1时，说明尝试做完了
        // 并且cur中的状态已经更新好了，里面存放的就是第cur行我们的摆放策略，然后进入下一行，此时的cur
        // 就是第 cur+1 行的pre参数了，回忆一下f函数的含义，就能理解了。
        if (j == -1)
            return f(row + 1, cur, N, M);
        // 还没处理完
        // 这个res的值就是当前列不摆放砖块。因为我至少可以在这里不摆放砖块吧，所以这个答案肯定是有的。
        int res = dfs(cur, j -1, row, N, M);
        // 在当前位置摆放砖块是有条件的。并且只能向右摆放。
        // 下面的条件其实就是说，如果cur里j和j-1位置都是0，那么我们才可以在j位置向右横躺摆放一块瓷砖
        if (j >= 1 && (cur & (3 << (j - 1))) == 0)
            // cur | (3 << (j - 1))  这个就是在cur中将第j-1，和第j位上一起设置为1，然后跳到j-2列去处理
            res += dfs(cur | (3 << (j - 1)), j - 2, row, N, M);

        return res;
    }
    // 这里有个小问题，我们是把整个列的状态用int型数据的二进制形式来表示的，如果列数超过了32，int表示不下了怎么办？
    // 所以在一开始时我们需要先判断一下行数和列数，让较小值作为列，题目是不会让较小值超过32的。
    // ===============================================================================================



    // 改成记忆化搜索
    public static int brick2(int N, int M){
        // 依然把N、M想象成一个矩阵，下标索引从0开始的，所以在下面的处理中，N-1就是最后一行，M-1就是最后一列。
        // 怎么处理无所谓，你把它看成下标从1开始的也可以。
        if (N < 1 || M < 1 || ((N * M) & 1) != 0)
            return 0;
        if (N == 1 || M == 1)
            return 1;
        int min = Math.min(N, M);
        int max = Math.max(N, M);
        int[][] dp = new int[1 << min][max + 1];
        for (int[] arr : dp)
            Arrays.fill(arr, -1);
        return g(0, (1 << M) - 1, max, min, dp);
    }


    private static int g(int i, int pre, int N, int M, int[][] dp) {
        if (dp[pre][i] != -1)
            return dp[pre][i];
        int res = 0;
        if (i == N){ // 结束了
            res = pre == (1 << M) - 1 ? 1 : 0;
        } else {
            int cur = (~pre) & ((1 << M) - 1);
            res = dfs2(cur, M - 1, i, N, M, dp);
        }
        dp[pre][i] = res;
        return res;
    }


    private static int dfs2(int cur, int j, int row, int N, int M, int[][] dp) {
        if (j == -1)
            return g(row + 1, cur, N, M, dp);
        int res = dfs2(cur, j -1, row, N, M, dp);
        if (j >= 1 && (cur & (3 << (j - 1))) == 0)
            res += dfs2(cur | (3 << (j - 1)), j - 2, row, N, M, dp);
        return res;
    }
    // ==========================================================================================


    public static void main(String[] args) {
        int N = 7;
        int M = 6;
        System.out.println(brick1(N, M));
        System.out.println(brick2(N, M));

    }

}
