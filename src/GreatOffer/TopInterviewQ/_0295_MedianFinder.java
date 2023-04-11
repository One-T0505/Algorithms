package GreatOffer.TopInterviewQ;

import java.util.PriorityQueue;

// 中位数是有序整数列表中的中间值。如果列表的大小是偶数，则没有中间值，中位数是两个中间值的平均值。
// 例如 arr = [2,3,4] 的中位数是 3。例如 arr = [2,3] 的中位数是 (2 + 3) / 2 = 2.5 。
// 实现 MedianFinder 类:
//  MedianFinder() 初始化 MedianFinder 对象。
//  void addNum(int num) 将数据流中的整数 num 添加到数据结构中。
//  double findMedian() 返回到目前为止所有元素的中位数。与实际答案相差 10^-5 以内的答案将被接受。
//  要求时间复杂度O(logN)

public class _0295_MedianFinder {

    // 要实现这个类，需要准备两个堆，一个大根堆max，一个小根堆min。每次数据流给一个数字a，如果
    // a > max的堆顶，那么a进入小根堆；否则，a进入大根堆。当前元素入堆后，判断两个堆元素数量的差值，
    // 如果达到了2，那么就让多的那个堆弹出栈顶元素进入另一个堆。总的流程就是这样的。

    // 其实，大根堆维护的是已经给出的数据流中较小的那一半，并且让其从大到小排序，所以大根堆的堆顶元素必然是当前
    // 数据流中位数的参与者，有可能直接是，有可能是两个需要计算平均值的那个。小根堆维护的是当前数据流中较大的那一半。
    // 如果差值来到了2，那么就不平衡了，所以需要继续调整两边元素个数，保证每次利用两个堆的堆顶一定能求得中位数。

    public static class MedianFinder {

        private PriorityQueue<Integer> maxH;
        private PriorityQueue<Integer> minH;

        public MedianFinder() {
            maxH = new PriorityQueue<>(((o1, o2) -> o2 - o1));
            minH = new PriorityQueue<>();  // 默认就是小根堆，所以不用传入排序规则
        }

        public void addNum(int num) {
            if (maxH.isEmpty() || maxH.peek() >= num)
                maxH.add(num);
            else
                minH.add(num);
            // 调整平衡
            if (Math.abs(maxH.size() - minH.size()) == 2) {
                PriorityQueue<Integer> more = maxH.size() > minH.size() ? maxH : minH;
                PriorityQueue<Integer> less = more == minH ? maxH : minH;
                less.add(more.poll());
            }
        }

        public double findMedian() {
            if (minH.isEmpty() && maxH.isEmpty())
                return 0;
            if (minH.size() == maxH.size())
                return ((double) minH.peek() + (double) maxH.peek()) / 2;
            // 如果两个堆的元素数量不等，那也只会相差一个
            return minH.size() > maxH.size() ? minH.peek() : maxH.peek();
        }
    }
}
