package Basic.MonotonicStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// 现在来介绍一种新的数据结构：单调栈。该结构通常用于求一个数组中某个元素左边和右边离自己最近且自己小的值。
// 当然也可以是左右边离自己最近且比自己大的值。用最暴力的遍历也可以做，但是时间复杂度为：O(N^2)，而单调栈可以做到：O(N).
// 下面就来介绍用单调栈寻找数组中每个元素左边和右边离自己最近且自己小的值。

// 假设该数组中没有重复值。arr=[4, 2, 5, 9, 6, 1, 3]   流程： 申请一个栈，从栈底到栈顶必须严格递增。
// 现在遍历这个数组:
//                                                         左         右
//   top   ｜     |                                  0     -1        1->2
//         ｜     |                                  1     -1        5->1
//         ｜6->3 |                                  2    1->2       5->1
// bottom  ｜5->1 |                                  3    2->5       4->6
//                                                   4    2->5       5->1
//                                                   5    -1          -1
//                                                   6    5->1         -1
// 栈为空，4直接入栈；2入栈时，要一直把栈里把自己大的元素弹出，来保持栈的单调性。栈中每一个被弹出的元素就开始结算自己的信息：
// arr[0]=4被弹出时，就可以知道自己右边离自己最近且比自己小的元素就是让自己被迫出栈的那个元素：arr[1]=2，而左边离自己最近
// 且比自己小的元素就是栈中自己下面的元素，如果下面没有那就说明不存在，用-1表示。
// 5入栈时比栈顶大可以直接入栈，9也直接入栈。6入栈时，要弹出9，所以arr[3]=9就结算，右边离自己最近且比自己小的元素是arr[4]=6,
// 左边离自己最近且比自己小的元素是arr[2]=5。1入栈时需要把栈中所有元素都弹出，于是依次做结算。最后6->3直接入栈。
// 当遍历完数组时，栈中还有元素时，这些元素的右边肯定没有比自己小的了，因为数组遍历完了，而左边离自己最近且比自己小的元素就是
// 压在自己下面的。

public class MonotonicStack {

    // 解释下返回值为什么是个矩阵？对于每个数组中的元素，都返回两个值，一个表示左边离自己最近且比自己小的元素的下标，
    // 如果不存在用-1代替；右边也同理。所以返回的矩阵形状为：N*2
    // 该方法是数组没有重复值的情况
    public static int[][] monotonicStackNoRepeat(int[] arr){
        if (arr == null)
            return null;
        int N = arr.length;
        int[][] res = new int[N][2];
        char[] a = new char[5];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < N; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]){
                int cur = stack.pop();
                res[cur][1] = i;
                res[cur][0] = stack.isEmpty() ? -1 : stack.peek();
            }
            // 此时栈顶元素 < arr[i]
            stack.push(i);
        }
        // 处理栈中还剩下的元素
        while (!stack.isEmpty()){
            int cur = stack.pop();
            res[cur][1] = -1;
            res[cur][0] = stack.isEmpty() ? -1 : stack.peek();
        }
        return res;
    }

    // 现在来讨论数组中有重复值的情况，arr={3, 7, 4, 3, 2, 4, 8}，当遍历到arr[3]=3时，栈中情况如下图：
    //
    //
    //    |         |
    //    |         |                               此时arr[3]=3要入栈，按正常情况弹出2->4，但是相同的值不弹，
    //    |         |                               而是记录到一起.当这两个元素被弹出时肯定是一起弹出的，就分别对他们两个
    //    |         |                               结算。比如arr[4]=2要入栈，那么就要把{0,3}->3结算：
    //    |{0,3}->3 |
    //                                                   左        右
    //                                        arr[0]=3   -1       4->2
    //                                        arr[3]=3   -1       4->2
    public static int[][] monotonicStackRepeat(int[] arr){
        if (arr == null)
            return null;
        int N = arr.length;
        int[][] res = new int[N][2];
        Stack<List<Integer>> stack = new Stack<>();
        for (int i = 0; i < N; i++) {
            while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]){
                List<Integer> cur = stack.pop(); // 该数组中存放的位置，虽然位置不同，但是在原数组中都是值相等的
                int left = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
                for (int pos : cur){
                    res[pos][1] = i;
                    res[pos][0] = left;
                }
            }
            // 此时栈顶元素>=arr[i] 或者 栈已为空
            if (!stack.isEmpty() && arr[i] == arr[stack.peek().get(0)]) // 如果是相等
                stack.peek().add(i);
            else { // 要么栈为空，要么已经不等
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.push(list);
            }
        }
        // 处理遍历完数组之后还剩余在栈中的元素
        while (!stack.isEmpty()){
            List<Integer> pop = stack.pop();
            int left = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
            for (int pos : pop){
                res[pos][0] = left;
                res[pos][1] = -1;
            }
        }
        return res;
    }
}
