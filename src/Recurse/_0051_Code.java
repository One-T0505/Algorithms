package Recurse;

// N皇后问题  先去看Nqueens文件之后再来
// 按照国际象棋的规则，皇后可以攻击与之处在同一行或同一列或同一斜线上的棋子。
// n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
// 给你一个整数 n ，返回所有不同的 n 皇后问题的解决方案。
// 每一种解法包含一个不同的 n 皇后问题的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class _0051_Code {

    // 预处理数组  其作用等到看懂下面的代码后就知道了
    public HashMap<Integer, Integer> map;


    public List<List<String>> solveNQueens(int n) {
        ArrayList<List<String>> res = new ArrayList<>();
        ArrayList<String> path = new ArrayList<>();
        if(n == 1){
            path.add("Q");
            res.add(path);
            return res;
        }
        int offset = 1;
        for (int i = 0; i <= n; i++) {
            map.put(offset, i);
            offset <<= 1;
        }
        int limit = (1 << n) - 1;
        dfs(limit, n, 0, 0, 0, path, res);
        return res;
    }

    private void dfs(int limit, int n, int colLim, int leftLim, int rightLim,
                     ArrayList<String> path, ArrayList<List<String>> res) {
        if (colLim == limit){
            res.add(new ArrayList<>(path));
            return;
        }
        int choices = limit & (~(colLim | leftLim | rightLim));
        int pos = 0;
        char[] chars = new char[n];
        Arrays.fill(chars, '.');
        while (choices != 0){
            pos = choices & (-choices);
            choices -= pos;
            int fact = map.get(pos);
            chars[n - fact - 1] = 'Q';
            path.add(String.valueOf(chars));
            dfs(limit, n, colLim | pos, (leftLim | pos) << 1, (rightLim | pos) >> 1,
                    path, res);
            path.remove(path.size() - 1);
            chars[n - fact - 1] = '.';
        }
    }


    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("123");
        System.out.println(sb.replace(1, 3, "Q"));
        System.out.println(Integer.bitCount(8));
    }
}
