package GreatOffer.TopHotQ;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// 给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer ，其中 answer[i] 是指对于第 i 天，
// 下一个更高温度出现在几天后。如果气温在这之后都不会升高，请在该位置用0来代替。

public class _0739_DailyTemperatures {

    // 这很明显地是用单调栈来做。题目就是要找当前元素右侧离自己最近且比自己大的元素
    public int[] dailyTemperatures(int[] temperatures) {
        if (temperatures == null || temperatures.length == 0)
            return new int[0];
        int N = temperatures.length;
        int[] res = new int[N];
        // 让栈里存储下标，不要存储具体的值，存下标不仅能知道值是多少，还能直到位置在哪
        Stack<List<Integer>> stack = new Stack<>();
        for (int i = 0; i < N; i++) {
            while (!stack.isEmpty() && temperatures[stack.peek().get(0)] < temperatures[i]) {
                List<Integer> pop = stack.pop();
                for (int pos : pop)
                    res[pos] = i - pos;
            }
            // 从while出来后，有三种情况：1.栈空  2.栈不空且栈顶元素==当前元素  3.栈不空且栈顶元素>当前元素
            // 情况1和情况3都需要往栈里添加新的列表   情况2只需要在栈顶的列表里添加当前元素
            if (!stack.isEmpty() && temperatures[stack.peek().get(0)] == temperatures[i])
                stack.peek().add(i);
            else {  // 情况1和情况3
                ArrayList<Integer> cur = new ArrayList<>();
                cur.add(i);
                stack.push(cur);
            }
        }
        return res;
    }
}
