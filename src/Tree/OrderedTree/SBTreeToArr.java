package Tree.OrderedTree;

import java.util.ArrayList;

// 传统的数组和链表，都支持以下三种方法：
//  1> add(index, num)  在某个位置添加元素
//  2> get(index)  获取index位置的元素
//  3> remove(index)  删除index位置的元素
// 数组的add和remove时间复杂度为O(N)，除非是再最后添加或删除；链表的这三个方法都是O(N)，除非在头部添加删除和获得。
// 但是如果用有序表改写，可以将这三种方法都在O(logN)水准下完成。

// 用有序表实现数组时，会默认包含位置信息。第一个元素来时默认是根结点，又添加一个元素时默认是再之前那个元素的后面，所以就往
// 根结点的右侧插；每新来一个结点时都是插到最右侧，当导致不平衡时，就做平衡性处理，这里最关键的一点是，左旋右旋后，
// 其相对位置信息不会被破坏。比如：数组中是这样的：[5, 2, 3]，一开始插入时如下图左侧，当平衡性处理后，如下图右侧。
//
//      5                                             首先要明白，这里不再是按照key排序了，时按照位置信息排序的。
//        \                2                          5在最前，3在最后，调整完后，位置信息依然保持不变。
//         2     -->     /   \                        add操作就是不断往右加，然后调整平衡性。接下来来讲下高级插入
//          \           5     3                       ，即在指定位置插入。
//           3
//  比如：add(0, 6)   在数组0号位置插入6。因为该有序表对外暴露就是一个数组，所以位置信息也需要从0开始。
//  这里的树中每个结点都一个size域，用于记录结点数。当来到2时，发现2.left.size=1，说明其左子树占据着0~0位置，我想插在0号位置，
//  0<2.left.size，所以向左滑动，最终滑到5的左侧，这其中的判定条件的边界问题需要自己在代码中解决。于是，树变成了如下：
//
//       2                      总之，现在的有序表维护的是位置信息，根据域size就可以判断相对位置。这棵树的中序遍历
//     /   \                    就是数组的元素顺序。左子树在根之前，右子树在根之后。
//    5     3
//   /
//  4
//
public class SBTreeToArr {

    public static class SBTNode<V> {
        public V val;
        public SBTNode<V> left;
        public SBTNode<V> right;
        public int size;

        public SBTNode(V val) {
            this.val = val;
            size = 1;
        }
    }

    public static class SBTreeArr<V> {
        public SBTNode<V> root;

        public int size(){
            return root == null ? 0 : root.size;
        }

        public V get(int index){
            if (index < 0 || (root == null || index >= root.size))
                return null;
             return get(root, index).val;
        }

        public void add(int index, V val){
            SBTNode<V> cur = new SBTNode<>(val);
            if (root == null)
                root = cur;
            else {
                if (0 <= index && index <= root.size)
                    root = add(root, index, cur);
            }
        }

        public void remove(int index){
            if (0 <= index && index < root.size)
                root = remove(root, index);
        }
        // ==============================================================================================



        // 从上游调用时已经对index的有效性进行了检查，并对root是否为null也检查了
        // 能执行到这里，说明root不为空，并且 0 <= index < root.size
        private SBTNode<V> get(SBTNode<V> root, int index) {
            int leftS = root.left == null ? 0 : root.left.size;
            if (index == leftS)
                return root;
            else if (index < leftS)
                return get(root.left, index);
            else
                return get(root.right, index - leftS - 1);
        }

        // 可以保证index合法，并且最开始进入时root不为空
        private SBTNode<V> add(SBTNode<V> root, int index, SBTNode<V> cur) {
            if (root == null)
                return cur;
            root.size++;
            int leftS = root.left == null ? 0 : root.left.size;
            if (index >= leftS + 1)
                root.right = add(root.right, index - leftS - 1, cur);
            else
                root.left = add(root.left, index, cur);
            root = keepBalanced(root);
            return root;
        }


        private SBTNode<V> remove(SBTNode<V> cur, int index) {
            cur.size--;
            int leftS = cur.left == null ? 0 : cur.left.size;
            if (index < leftS)
                cur.left = remove(cur.left, index);
            else if (index > leftS)
                cur.right = remove(cur.right, index - leftS - 1);
            else {
                if (cur.left == null && cur.right == null)
                    cur = null;
                else if (cur.left != null && cur.right == null) {
                    cur = cur.left;
                } else if (cur.left == null && cur.right != null) {
                    cur = cur.right;
                } else {
                    SBTNode<V> pre = null;
                    SBTNode<V> des = cur.right;
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
                    des.size = cur.left.size + (des.right == null ? 0 : des.right.size) + 1;
                    cur = des;
                }
            }
            return cur;
        }

        private SBTNode<V> keepBalanced(SBTNode<V> cur) {
            if (cur == null)
                return null;
            int lS = cur.left != null ? cur.left.size : 0;
            int lLS = cur.left != null && cur.left.left != null ? cur.left.left.size : 0;
            int lRS = cur.left != null && cur.left.right != null ? cur.left.right.size : 0;
            int rS = cur.right != null ? cur.right.size : 0;
            int rLS = cur.right != null && cur.right.left != null ? cur.right.left.size : 0;
            int rRS = cur.right != null && cur.right.right != null ? cur.right.right.size : 0;
            if (lLS > rS) {
                cur = rightRotate(cur);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (lRS > rS) {
                cur.left = leftRotate(cur.left);
                cur = rightRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (rRS > lS) {
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur = keepBalanced(cur);
            } else if (rLS > lS) {
                cur.right = rightRotate(cur.right);
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            }
            return cur;
        }

        private SBTNode<V> leftRotate(SBTNode<V> cur) {
            SBTNode<V> right = cur.right;
            cur.right = right.left;
            right.left = cur;
            right.size = cur.size;
            cur.size = (cur.left != null ? cur.left.size : 0) + (cur.right != null ? cur.right.size : 0) + 1;
            return right;
        }

        private SBTNode<V> rightRotate(SBTNode<V> cur) {
            SBTNode<V> left = cur.left;
            cur.left = left.right;
            left.right = cur;
            left.size = cur.size;
            cur.size = (cur.left != null ? cur.left.size : 0) + (cur.right != null ? cur.right.size : 0) + 1;
            return left;
        }
    }

    // for test
    // 通过以下这个测试，
    // 可以很明显的看到ArrayList的插入、删除、get效率不如SbtList
    // ArrayList需要找到index所在的位置之后才能插入或者读取，时间复杂度O(N)
    // SbtList是平衡搜索二叉树，所以插入或者读取时间复杂度都是O(logN)
    // ================================================================================================
    public static void main(String[] args) {
        // 功能测试
        int test = 50000;
        int max = 1000000;
        boolean pass = true;
        ArrayList<Integer> list = new ArrayList<>();
        SBTreeArr<Integer> sbTreeArr = new SBTreeArr<>();
        for (int i = 0; i < test; i++) {
            if (list.size() != sbTreeArr.size()) {
                pass = false;
                break;
            }
            if (list.size() > 1 && Math.random() < 0.5) {
                int removeIndex = (int) (Math.random() * list.size());
                list.remove(removeIndex);
                sbTreeArr.remove(removeIndex);
            } else {
                int randomIndex = (int) (Math.random() * (list.size() + 1));
                int randomValue = (int) (Math.random() * (max + 1));
                list.add(randomIndex, randomValue);
                sbTreeArr.add(randomIndex, randomValue);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(sbTreeArr.get(i))) {
                pass = false;
                break;
            }
        }
        System.out.println("功能测试是否通过 : " + pass);

        // 性能测试
        test = 1000000;
        list = new ArrayList<>();
        sbTreeArr = new SBTreeArr<>();
        long start = 0;
        long end = 0;

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (list.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            list.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList插入总时长(毫秒) ： " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            list.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList读取总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * list.size());
            list.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList删除总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (sbTreeArr.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            sbTreeArr.add(randomIndex, randomValue);
        }
        System.out.println("===============================================================");
        end = System.currentTimeMillis();
        System.out.println("SBTreeArr插入总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            sbTreeArr.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SBTreeArr读取总时长(毫秒) :  " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * sbTreeArr.size());
            sbTreeArr.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SBTreeArr删除总时长(毫秒) :  " + (end - start));

    }

}
