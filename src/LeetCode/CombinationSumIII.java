package LeetCode;

import java.util.ArrayList;
import java.util.List;

/**
 * ymy
 * 2023/4/10 - 09 : 41
 **/


// leetCode216
// 找出所有相加之和为 n 的 k 个数的组合，且满足下列条件：
//  1.只使用数字1到9
//  2.每个数字 最多使用一次
// 返回 所有可能的有效组合的列表 。该列表不能包含相同的组合两次，组合可以以任何顺序返回。

// 2 <= k <= 9
// 1 <= n <= 60

public class CombinationSumIII {

    // 初版暴力递归
    // 因为只能使用 1～9，所以可以用int型数据的低10位表示0～9，每个二进制位上如果是1就表示对应的数值用过了，如果是0没用过
    // 因为0不可用，所以我们默认初始情况下就让int的最低位为1，所以下面的代码里 status 初始化为1
    public static List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        if (k < 1 || n < k)
            return res;
        dfs(1, n, k, new ArrayList<>(), res);
        return res;
    }


    // 比如给定n==9，k==3，那么 [1, 3, 5] 就是一种合理情况，因为是dfs，所以还会产生 [3, 1, 5] 这种情况，
    // 但是这样两种状态我们都只算作一个答案，所以我们需要一个哈希集合，到最后的时候判断status是否存在，因为上面
    // 两种情况的 status 是一样的，只是产生每个数字的先后不同
    private static void dfs(int i, int rest, int k, ArrayList<Integer> path, List<List<Integer>> res) {
        if (rest == 0 && path.size() == k){
            res.add(new ArrayList<>(path));
            return;
        }
        for (int begin = i; begin < 10; begin++) {
            // 剪枝
            if (rest - begin < 0)
                return;
            path.add(begin);
            dfs(begin + 1, rest - begin, k, path, res);
            path.remove(path.size() - 1);
        }
    }



    public static void main(String[] args) {
        System.out.println(combinationSum3(4, 11));
    }
}
