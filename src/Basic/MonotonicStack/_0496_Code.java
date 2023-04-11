package Basic.MonotonicStack;

// nums1 中数字 x 的 下一个更大元素是指 x 在 nums2 中对应位置右侧的第一个比 x 大的元素。
// 给你两个没有重复元素的数组 nums1 和 nums2 ，下标从 0 开始计数，其中nums1 是 nums2 的子集。
// 对于每个 0 <= i < nums1.length ，找出满足 nums1[i] == nums2[j] 的下标 j，并且在 nums2
// 确定 nums2[j] 的下一个更大元素。如果不存在下一个更大元素，那么本次查询的答案是 -1。
// 返回一个长度为 nums1.length 的数组 ans 作为答案，满足 ans[i] 是如上所述的下一个更大元素。

// 1 <= nums1.length <= nums2.length <= 1000
// 0 <= nums1[i], nums2[i] <= 10^4
// nums1和nums2中所有整数互不相同
// nums1 中的所有整数同样出现在 nums2 中


import java.util.HashMap;
import java.util.Stack;

public class _0496_Code {

    // 哈希表 + 单调栈      常数时间优化：用数组代替系统栈
    public static int[] nextGreaterElement(int[] nums1, int[] nums2){
        int M = nums1.length;
        int N = nums2.length;
        int[] res = new int[M];
        // 让栈底到栈顶维持递减的顺序
        Stack<Integer> stack = new Stack<>();
        // key: 某个数   value: nums2中该数的右侧第一个比key大的数是什么
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = N- 1; i >= 0; i--){
            while (!stack.isEmpty() && stack.peek() < nums2[i]){
                Integer cur = stack.pop();
                // 每一个被弹出的元素就可以结算了  它对应的结果就是栈中在它下面的元素
                // 这里不用判断map中是否已经存在cur这个key了，因为nums2中的元素都不相同，所以不可能存在了
                map.put(cur, stack.isEmpty() ? -1 : stack.peek());
            }
            stack.push(nums2[i]);
        }
        while (!stack.isEmpty()){
            Integer cur = stack.pop();
            map.put(cur, stack.isEmpty() ? -1 : stack.peek());
        }
        for (int i = 0; i < M; i++) {
            res[i] = map.get(nums1[i]);
        }
        return res;
    }



    public static int[] nextGreaterElement2(int[] nums1, int[] nums2){
        int M = nums1.length;
        int N = nums2.length;
        int[] res = new int[M];
        int[] stack = new int[N];
        int top = 0;
        // key: 某个数   value: nums2中该数的右侧第一个比key大的数是什么
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = N- 1; i >= 0; i--){
            while (top != 0 && stack[top - 1] < nums2[i]){
                Integer cur = stack[--top];
                // 每一个被弹出的元素就可以结算了  它对应的结果就是栈中在它下面的元素
                // 这里不用判断map中是否已经存在cur这个key了，因为nums2中的元素都不相同，所以不可能存在了
                map.put(cur, top == 0 ? -1 : stack[top - 1]);
            }
            stack[top++] = nums2[i];
        }
        while (top != 0){
            Integer cur = stack[--top];
            map.put(cur, top == 0 ? -1 : stack[top - 1]);
        }
        for (int i = 0; i < M; i++) {
            res[i] = map.get(nums1[i]);
        }
        return res;
    }
}
