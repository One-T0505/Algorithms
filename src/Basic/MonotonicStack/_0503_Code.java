package Basic.MonotonicStack;

// 给定一个循环数组 nums （ nums[nums.length - 1] 的下一个元素是 nums[0] ），返回 nums 中每个元素的下一个更大元素。
// 数字 x 的 下一个更大的元素是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数
// 如果不存在，则输出 -1。

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class _0503_Code {

    public int[] nextGreaterElements(int[] nums) {
        if (nums == null || nums.length == 0)
            return null;
        int N = nums.length;
        int[] res = new int[N];
        Arrays.fill(res, -1);
        Stack<List<Integer>> stack = new Stack<>();
        for (int i = 0; i < (N << 1) - 1; i++) {
            while (!stack.isEmpty() && nums[stack.peek().get(0)] < nums[i % N]){
                List<Integer> pop = stack.pop();
                for (int pos : pop)
                    res[pos] = nums[i % N];
            }
            if (!stack.isEmpty() && nums[stack.peek().get(0)] == nums[i % N])
                stack.peek().add(i % N);
            else {
                List<Integer> list = new ArrayList<>();
                list.add(i % N);
                stack.push(list);
            }
        }
        return res;
    }
}
