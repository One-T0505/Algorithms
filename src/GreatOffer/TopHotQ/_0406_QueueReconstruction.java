package GreatOffer.TopHotQ;


// 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序)。每个
// people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面正好有 ki 个身高大于或等于 hi 的人。
// 请你重新构造并返回输入数组 people 所表示的队列。返回的队列应该格式化为数组 queue ，其中
// queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。

import java.util.Arrays;
import java.util.LinkedList;

public class _0406_QueueReconstruction {


    // 这个题目的描述可能会有点看不懂，建议去leetCode看官方给出的两个例子，搞明白题意后再继续。
    // 首先要排序：按照身高从大到小排序，身高一样的，按照需要的人数从小到大排序。每个人的第2个数据其实
    // 就是说，最终排完的队列，希望前面有多少个人的身高大于等于自己。
    // 我们将数组排完序后，比如: [[7, 0], [7, 1], [5, 1]]   那就是说将队列0位置插上7，然后再把1位置插上7，
    // 再把1位置插上5，就变成了：7 5 7   这样就满足了。

    // 这道题目的难点在于如何高效地插入，如果只是使用数组，那么插入的时间复杂度就是O(N)
    // 还记得有序表，最后讲的那个利用有序表实现插入删除都是O(logN)的结构吗？就用这个结构，就是本题的最优解

    public static int[][] reconstructQueue(int[][] people) {
        int N = people.length;
        Arrays.sort(people, (a, b) -> (a[0] == b[0] ? a[1] - b[1] :b[0] - a[0]));
        SBTree sb = new SBTree();
        for (int i = 0; i < N; i++) {
            sb.insert(people[i][1], i);
        }
        LinkedList<Integer> all = sb.getQueue();
        int[][] res = new int[N][2];
        int i = 0;
        for (int pos : all){
            res[i][0] = people[pos][0];
            res[i++][1] = people[pos][1];
        }
        return res;
    }

    public static class SBNode {
        public int value;
        public SBNode left;
        public SBNode right;
        public int size;

        public SBNode(int index) { // 这里保存的是排完序的数组元素的下标，有了下标，不仅能知道身高，还能知道在哪
            value = index;
            size = 1;
        }
    }


    public static class SBTree {
        private SBNode root;

        private SBNode leftRotate(SBNode cur){
            SBNode right = cur.right;
            cur.right = right.left;
            right.left = cur;
            right.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) +
                    (cur.right == null ? 0 : cur.right.size) + 1;
            return right;
        }


        private SBNode rightRotate(SBNode cur){
            SBNode left = cur.left;
            cur.left = left.right;
            left.right = cur;
            left.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) +
                    (cur.right == null ? 0 : cur.right.size) + 1;
            return left;
        }


        private SBNode keepBalanced(SBNode cur){
            if (cur == null)
                return null;
            int leftS = cur.left == null ? 0 : cur.left.size;
            int leftLeftS = cur.left != null && cur.left.left != null ? cur.left.left.size : 0;
            int leftRightS = cur.left != null && cur.left.right != null ? cur.left.right.size : 0;
            int rightS = cur.right == null ? 0 : cur.right.size;
            int rightLeftS = cur.right != null && cur.right.left != null ? cur.right.left.size : 0;
            int rightRightS = cur.right != null && cur.right.right != null ? cur.right.right.size : 0;
            if (leftLeftS > rightS){
                cur = rightRotate(cur);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (leftRightS > rightS) {
                cur.left = leftRotate(cur.left);
                cur = rightRotate(cur);
                cur.right = keepBalanced(cur.right);
                cur.left = keepBalanced(cur.left);
                cur = keepBalanced(cur);
            } else if (rightRightS > leftS) {
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur = keepBalanced(cur);
            } else if (rightLeftS > leftS){
                cur.right = rightRotate(cur.right);
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            }
            return cur;
        }


        private SBNode insert(SBNode root, int index, SBNode cur){
            if (root == null)
                return cur;
            root.size++;
            int leftAndHeadS = (root.left == null ? 0 : root.left.size) + 1;
            if (index < leftAndHeadS)
                root.left = insert(root.left, index, cur);
            else
                root.right = insert(root.right, index - leftAndHeadS, cur);
            root = keepBalanced(root);
            return root;
        }


        // 我们有序表中的元素也是从下标0开始的。
        private SBNode get(SBNode root, int index){
            int leftS = root.left == null ? 0 : root.left.size;
            if (index == leftS)
                return root;
            else if (index < leftS) {
                return get(root.left, index);
            } else
                return get(root.right, index - leftS - 1);
        }



        public void insert(int index, int val){
            SBNode cur = new SBNode(val);
            if (root == null)
                root = cur;
            else {
                if (index <= root.size)
                    root = insert(root, index, cur);
            }
        }



        public int get(int index){
            SBNode res = get(root, index);
            return res.value;
        }


        private void collect(SBNode head, LinkedList<Integer> queue){
            if (head == null)
                return;
            collect(head.left, queue);
            queue.addLast(head.value);
            collect(head.right, queue);
        }


        public LinkedList<Integer> getQueue(){
            LinkedList<Integer> all = new LinkedList<>();
            collect(root, all);
            return all;
        }
    }
}
