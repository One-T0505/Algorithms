package Basic.Tree.OrderedTree;

import utils.arrays;
import Basic.Sort.Sort;

// 有一个滑动窗口(讲过的，之前是用堆来实现的) :
//  1) L是滑动窗口最左位置，R是滑动窗口最右位置，一开始L、R都在数组左侧
//  2) 任何一步都可能R往右动，表示某个数进了窗口
//  3) 任何一步都可能L往右动，表示某个数出了窗口
// 窗口大小固定为k，想知道每一个窗口状态的中位数。返回的是数学意义上的中位数：窗口内可能是无序的数组元素。

// 如果用最暴力的方法就是O(N2)，之前也用滑动窗口优化过，可以到O(N)，现在再用有序表改写可以到O(logN)
// 思路很简单，我们用SizeBalancedTree改写，每次R往右移动就把新数加入树中，L往右移动就从树中删除一个记录；
// 每时刻下树中所有记录就是当前窗口的状态，我们需要新增一个接口：查找树中第index大的数就可以完成该题。

public class _0480_Code {

    // 有序表结点类定义
    public static class SBTNode<K extends Comparable<K>> {
        public K key;
        public SBTNode<K> left;
        public SBTNode<K> right;
        public int size;   // 记录的是不同key的个数

        public SBTNode(K key) {
            this.key = key;
            size = 1;
        }
    }
    // ======================================================================================================


    // 有序表类封装
    public static class SBETree<K extends Comparable<K>> {
        SBTNode<K> root;

        // ============================================================================================
        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            SBTNode<K> lastNode = lastIndex(key);
            return lastNode != null && key.compareTo(lastNode.key) == 0;
        }

        // 往树中放结点
        public void put(K key){
            if (key == null) {
                return;
            }
            SBTNode<K> lastNode = lastIndex(key);
            if (lastNode == null || key.compareTo(lastNode.key) != 0) {
                root = add(root, key);
            }

        }


        // 删除结点
        public void remove(K key){
            if (key == null)
                return;
            if (containsKey(key))
                root = delete(root, key);
        }


        // 获取树中第index大的数，从0开始
        public K getIndexKey(int index){
            if (index < 0 || index >= root.size)
                return null;
            return getIndex(root, index + 1).key;
        }

        public int size(){
            return root == null ? 0 : root.size;
        }

        // ============================================================================================
        // 如果树中存在key则返回，若不存在则需要看树中是否结点的key全部大于key，如果全部大于key，则返回最小的那个
        // 如果树中存在比key小的结点，那么返回离key最近且小于key 的。
        // 所以，这个方法的会返回值可能比key大，也可能比key小。
        private SBTNode<K> lastIndex(K key) {
            SBTNode<K> pre = root;
            SBTNode<K> cur = root;
            while (cur != null){
                pre = cur;
                if (key.compareTo(cur.key) == 0)
                    break;
                else if (key.compareTo(cur.key) < 0)
                    cur = cur.left;
                else
                    cur = cur.right;
            }
            return pre;
        }

        // 此时传入的index已经是从1开始计算的索引了
        private SBTNode<K> getIndex(SBTNode<K> cur, int index) {
            if (cur == null)
                return null;
            int leftS = cur.left == null ? 0 : cur.left.size;
            if (index == (leftS) + 1)
                return cur;
            else if (index <= (cur.left != null ? cur.left.size : 0)) {
                return getIndex(cur.left, index);
            } else {
                return getIndex(cur.right, index - 1 - (leftS));
            }
        }

        private SBTNode<K> add(SBTNode<K> cur, K key) {
            if (cur == null)
                return new SBTNode<>(key);
            else {
                cur.size++;
                // 这李只有if，else两种情况，因为我们封装的结点不可能重复，所以不可能相等
                if (cur.key.compareTo(key) > 0)
                    cur.left = add(cur.left, key);
                else
                    cur.right = add(cur.right, key);
                return keepBalanced(cur);
            }
        }

        private SBTNode<K> delete(SBTNode<K> cur, K key) {
            cur.size--;
            if (key.compareTo(cur.key) > 0)
                cur.right = delete(cur.right, key);
            else if (key.compareTo(cur.key) < 0)
                cur.left = delete(cur.left, key);
            else {
                // 左右子树都为空，直接删
                if (cur.left == null && cur.right == null)
                    cur = null;
                // 仅有左子树
                else if (cur.left != null && cur.right == null)
                    cur = cur.left;
                // 仅有右子树
                else if (cur.left == null)
                    cur = cur.right;
                else {
                    // 找到右子树上最左的结点
                    SBTNode<K> pre = null;
                    SBTNode<K> des = cur.right;
                    des.size--;
                    while (des.left != null){
                        pre = des;
                        des = des.left;
                        des.size--;
                    }
                    if (pre != null){
                        pre.left = des.right;
                        des.right = cur.right;
                    }
                    des.left = cur.left;
                    des.size = des.left.size + (des.right == null ? 0 : des.right.size) + 1;
                    cur = des;
                }
            }
            return cur;
        }

        private SBTNode<K> keepBalanced(SBTNode<K> cur) {
            if (cur == null)
                return null;
            int leftS = cur.left == null ? 0 : cur.left.size;
            int leftLeftS = cur.left != null && cur.left.left != null ? cur.left.left.size : 0;
            int leftRightS = cur.left != null && cur.left.right != null ? cur.left.right.size : 0;
            int rightS = cur.right == null ? 0 : cur.right.size;
            int rightLeftS = cur.right != null && cur.right.left != null ? cur.right.left.size : 0;
            int rightRightS = cur.right != null && cur.right.right != null ? cur.right.right.size : 0;
            if (leftLeftS > rightS) {  // LL
                cur = rightRotate(cur);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (leftRightS > rightS) {  // LR
                cur.left = leftRotate(cur.left);
                cur = rightRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (rightRightS > leftS) {  // RR
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur = keepBalanced(cur);
            } else if (rightLeftS > leftS) {  // RL
                cur.right = rightRotate(cur.right);
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            }
            return cur;
        }

        private SBTNode<K> leftRotate(SBTNode<K> cur) {
            SBTNode<K> right = cur.right;
            cur.right = right.left;
            right.left = cur;
            // 修改size属性
            right.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) + (cur.right == null ? 0 : cur.right.size) + 1;
            return right;
        }

        private SBTNode<K> rightRotate(SBTNode<K> cur) {
            SBTNode<K> left = cur.left;
            cur.left = left.right;
            left.right = cur;
            // 修改size属性，先后顺序不能错
            left.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) + (cur.right == null ? 0 : cur.right.size) + 1;
            return left;
        }

    }
    // ======================================================================================================

    // 该结点就是我们最后需要的结点. 最后有序表的key就是这里的结点Node。Node是优先按照值排序的，
    // 如果值相等，才按照位置排序。也就是说数组中每个元素都是独一无二的结点。
    public static class Node implements Comparable<Node> {
        public int index;    // 这个表示在数组中的索引
        public int val;      // 这个表示元素值

        public Node(int index, int val) {
            this.index = index;
            this.val = val;
        }


        @Override
        public int compareTo(Node o) {
            return val == o.val ? Integer.valueOf(index).compareTo(o.index) :
                    Integer.valueOf(val).compareTo(o.val);
        }
    }

    // 主方法
    public static double[] getMedianV1(int[] arr, int k){
        if (arr == null || arr.length < k)
            return null;
        int N = arr.length;
        SBETree<Node> tree = new SBETree<>();
        for (int i = 0; i < k - 1; i++)
            tree.put(new Node(i, arr[i]));
        double[] res = new double[N - k + 1];
        int index = 0;
        for (int i = k - 1; i < N; i++) {
            tree.put(new Node(i, arr[i]));
            // 偶数个
            if ((tree.size() & 1) == 0) {
                Node former = tree.getIndexKey(tree.size() >> 1);
                Node latter = tree.getIndexKey((tree.size() >> 1) - 1);
                res[i - k + 1] = ((double) former.val + (double) latter.val) / 2;
            } else {  // 奇数个
                res[index++] = tree.getIndexKey(tree.size() >> 1).val;
            }
            tree.remove(new Node(i - k + 1, arr[i - k + 1]));
        }
        return res;
    }


    // 暴力法
    public static double[] getMedianV2(int[] arr, int k){
        if (arr == null || arr.length < k)
            return null;
        int[] help = new int[k];
        double[] res = new double[arr.length - k + 1];
        int L = 0, R = 0, index = 0;
        while (R <= arr.length - 1){
            R++;
            if (R - L == k){
                System.arraycopy(arr, L, help, 0, k);
                Sort.insertSort(help, 0, k - 1);
                // 奇数个
                if ((k & 1) == 1){
                    res[index++] = help[k >> 1];
                } else { // 偶数个
                    int former = help[(k >> 1) - 1];
                    int latter = help[k >> 1];
                    res[index++] = ((double) former + (double) latter) / 2;
                }
                L++;
            }
        }
        return res;
    }


    public static void main(String[] args) {
        int[] arr = {3, 5, 2, 1, 7, 4};
        int k = 4;
        double[] res1 = getMedianV1(arr, k);
        double[] res2 = getMedianV2(arr, k);
        arrays.printArray(res1);
        arrays.printArray(res2);
    }
}
