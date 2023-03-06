package DynamicProgramming;

// 给你一个无重复元素的整数数组 candidates 和一个目标整数 target ，找出 candidates 中可以使数字和为目标数 target
// 的所有不同组合，并以列表形式返回。你可以按任意顺序返回这些组合。
// candidates 中的同一个数字可以无限制重复被选取。如果至少一个数字的被选数量不同，则两种组合是不同的。

// 1 <= candidates.length <= 30
// 2 <= candidates[i] <= 40

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _0039_Code {

    // 这非常像货币模型 从左到右的尝试模型  并且题目说了为正整数数组
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        Arrays.sort(candidates);
        f(candidates, 0, target, path, res);
        return res;
    }


    private static void f(int[] candidates, int i, int rest, List<Integer> path, List<List<Integer>> res) {
        if (rest == 0)
            res.add(new ArrayList<>(path));
        else if (i < candidates.length && candidates[i] <= rest){
            // 枚举可以使用几个candidates[i]
            for (int j = 0; j * candidates[i] <= rest; j++) {
                for (int k = 0; k < j; k++) {
                    path.add(candidates[i]);
                }
                f(candidates, i + 1, rest - j * candidates[i], path, res);
                path.subList(path.size() - j, path.size()).clear();
            }
        }
    }



    public static void main(String[] args) {
        int[] arr = {2, 3, 6, 7};
        int target = 7;
        System.out.println(combinationSum(arr, target));
    }
}
