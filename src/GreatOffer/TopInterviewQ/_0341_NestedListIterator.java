package GreatOffer.TopInterviewQ;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

// 给你一个嵌套的整数列表 nestedList 。每个元素要么是一个整数，要么是一个列表；该列表的元素也可能是
// 整数或者是其他列表。请你实现一个迭代器将其扁平化，使之能够遍历这个列表中的所有整数。
// 实现扁平迭代器类 NestedIterator ：
//   1.NestedIterator(List<NestedInteger> nestedList) 用嵌套列表 nestedList 初始化迭代器。
//   2.int next() 返回嵌套列表的下一个整数。
//   3.boolean hasNext() 如果仍然存在待迭代的整数，返回 true ；否则，返回 false 。
// 你的代码将会用下述伪代码检测：
//   initialize iterator with nestedList
//   res = []
//   while iterator.hasNext()
//      append iterator.next() to the end of res
//   return res
// 如果 res 与预期的扁平化列表匹配，那么你的代码将会被判为正确。

public class _0341_NestedListIterator {

    // 该接口是题目提供的，不用管具体实现
    public interface NestedInteger {

        // @return true if this NestedInteger holds a single integer, rather than a nested list.
        public boolean isInteger();

        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger();

        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return empty list if this NestedInteger holds a single integer
        public List<NestedInteger> getList();
    }


    // 题目的意思就是说，有可能给的List是这样的：[4, [2, 5, 3], 6, [2], [9, [2, [7, 1]]]]
    // 是这样一层一层嵌套的，我们现在需要实现下面 NestedIterator 这个类，就是屏蔽这些嵌套的关系，直接对外
    // 提供迭代器的方法。让用户使用起来的感觉就像是：[4, 2, 5, 3, 6, 2, 9, 2, 7, 1] 一个一个地拿元素
    // 这道题目的考点就是深度优先遍历。
    public static class NestedIterator implements Iterator<Integer> {

        private List<NestedInteger> list;  // 记录传入的参数

        // 这个栈就是实现DFS的关键，用它来保存沿途的位置，比如上面举例用的list中的7元素：
        // 那么在栈中从栈顶到栈底依次存储的信息是：4  1  0  表示7是最外层list的第4个元素中的第1个元素中的第0个元素
        private Stack<Integer> stack;
        private boolean used;  // 当前元素是否使用过

        public NestedIterator(List<NestedInteger> nestedList) {
            list = nestedList;
            stack = new Stack<>();
            stack.push(-1);
            used = true;
            hasNext();
        }

        @Override
        public Integer next() {
            Integer res = null;
            if (!used) {
                res = get(list, stack);
                used = true;
                hasNext();
            }
            return res;
        }


        @Override
        public boolean hasNext() {  // 只有该函数会修改 used 的值，其余所有函数都不会修改
            if (stack.isEmpty())
                return false;
            // 执行到这里说明栈不为空
            if (!used)
                return true;
            // 执行到这里说明，栈里的这一串元素代表的路径的元素是使用过了
            // 所以，我们需要根据当前栈里元素指向的元素的下一个元素
            if (findNext(list, stack))
                used = false;
            return true;
        }


        // ======================================================================================


        // 该方法只会根据此时stack中存储的位置逐个嵌套下去，找到真实的单一元素，该方法执行完后，栈中的东西
        // 不会改变。
        private int get(List<NestedInteger> list, Stack<Integer> stack) {
            int index = stack.pop();
            Integer res = null;
            if (!stack.isEmpty())  // 说明，最外层list的index处的元素不是单一整数，而是有嵌套的list
                res = get(list.get(index).getList(), stack);
            else
                res = list.get(index).getInteger();

            stack.push(index);
            return res;

        }



        // 根据栈中存储的路径去找到下一个元素的路径，并将其保存在栈里。
        private boolean findNext(List<NestedInteger> list, Stack<Integer> stack) {
            int index = stack.pop();
            // 如果栈顶元素的位置index在最外层list处不是嵌套的，那么将它弹出后，栈就为空了
            // 栈不为空，就说明当前index在最外层list处是嵌套的
            if (!stack.isEmpty() && findNext(list.get(index).getList(), stack)) {
                stack.push(index);
                return true;
            }
            // 如果此时的index就是当前列表的最后一个元素，那么i==index+1，根本不会进入for循环
            // 会直接到最后的return
            for (int i = index + 1; i < list.size(); i++) {
                if (pickFirst(list.get(i), i, stack))
                    return true;
            }
            return false;
        }


        // 正确加入最外层list的pos处元素的第一个元素，因为pos处可能是单一整数也可能是嵌套列表，
        // 所以要判断，并且将正确的元素加入栈中
        private boolean pickFirst(NestedInteger integer, int pos, Stack<Integer> stack) {
            if (integer.isInteger()) {  // 如果最外层list的pos处是单一整数
                stack.add(pos);
                return true;
            } else { // 如果最外层list的pos处是嵌套列表
                List<NestedInteger> innerList = integer.getList();
                for (int i = 0; i < innerList.size(); i++) {
                    if (pickFirst(innerList.get(i), i, stack)) {
                        stack.add(pos);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
