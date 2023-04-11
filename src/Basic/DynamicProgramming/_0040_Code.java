package Basic.DynamicProgramming;

// 给定一个候选人编号的集合 candidates 和一个目标数 target，找出 candidates 中所有可以使数字和为 target 的组合。
// candidates 中的每个数字在每个组合中只能使用一次。candidates中可以办好重复数字。
// 注意：解集不能包含重复的组合。

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class _0040_Code {


    // 做这题的时候，一定要去官网看看给的例子，因为题目给的设定比较怪，一定要弄清楚什么是不能重复。
    // 自己写的时候发现，如果数组里有两个1，target==8，那么我们产生的答案 {1, 2, 5} {1, 2, 5} 即便
    // 1是不同的1，2和5都是唯一的，那也算同一个答案，因为这两个答案的面值完全一样。
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        // 用于去重  freq.size() 就表示原数组里有几种不同的值   freq每一个元素都是一个长度为2的数组
        // 0位置表示面值  1位置表示数量
        List<int[]> freq = new ArrayList<>();
        Arrays.sort(candidates);
        for (int e : candidates){
            if (freq.isEmpty() || e > freq.get(freq.size() - 1)[0])
                freq.add(new int[] {e, 1});
            else
                freq.get(freq.size() - 1)[1]++;
        }
        f(freq, 0, target, path, res);
        return res;
    }

    private static void f(List<int[]> freq, int i, int rest, List<Integer> path, List<List<Integer>> res) {
        if (rest == 0){
            res.add(new ArrayList<>(path));
            return;
        }
        if (i == freq.size() || freq.get(i)[0] > rest)
            return;
        f(freq, i + 1, rest, path, res);
        int val = freq.get(i)[0];
        int num = Math.min(freq.get(i)[1], rest / val);
        for (int k = 1; k <= num; k++){
            path.add(val);
            f(freq, i + 1, rest - k * val, path, res);
        }
        for (int j = 0; j < num; j++) {
            path.remove(path.size() - 1);
        }
    }


    public static void main(String[] args) {
        int[] arr = {10, 1, 2, 7, 6, 1, 5};
        System.out.println(combinationSum2(arr, 8));
    }
}
