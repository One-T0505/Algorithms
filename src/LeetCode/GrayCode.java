package LeetCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * ymy
 * 2023/3/23 - 20 : 22
 **/

// n 位格雷码序列 是一个由 2^n 个整数组成的序列，其中：
//  1.每个整数都在范围 [0, 2^n - 1] 内（含 0 和 2^n - 1）
//  2.第一个整数是 0
//  3.一个整数在序列中出现不超过一次
//  4.每对相邻整数的二进制表示恰好一位不同 ，且第一个和最后一个整数的二进制表示恰好一位不同
//  5.给你一个整数 n ，返回任一有效的 n 位格雷码序列 。

public class GrayCode {

    // 暴力解法
    public static List<Integer> grayCode(int n) {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(0);
        HashSet<Integer> visited = new HashSet<>();
        visited.add(0);
        // 将1～2^n - 1 放入集合中，表示可以用的数字
        dfs(res, 1, n, visited);
        return res;
    }

    private static void dfs(ArrayList<Integer> path, int i, int n, HashSet<Integer> visited) {
        if (i == (1 << n))
            return;
        int pre = path.get(i - 1);
        // 说明填到了最后一个位置了
        // 填最后一个位置的数时，有两个约束条件：要和前一个  第0个数 都只有1位数不同
        // 也就是说，异或后的结果都只有1位上是1。
        for (int j = 0; j < n; j++) {
            int cur = pre ^ (1 << j);
            if (!visited.contains(cur)){
                if (i == (1 << n) - 1){
                    cur ^= path.get(0);
                    if ((cur & (cur - 1)) != 0)
                        continue;
                }
                // 还没到最后，只用考虑和前一个数不同即可
                visited.add(cur);
                path.add(cur);
                dfs(path, i + 1, n, visited);
            }
        }
    }
    // ----------------------------------------------------------------------------------------------






    public static void main(String[] args) {
        int n = 1;
        System.out.println(grayCode(n));
    }
}
