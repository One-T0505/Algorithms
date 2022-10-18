package Tree.OrderedTree;

// SizeBalanceTree 也是一种平衡二叉树，不过它的平衡性的定义和AVL树不一样。
// SBTree的不平衡性是这样定义的：
//        A
//      /   \
//     B     C
//    / \   / \
//   D   E F   G
// 判断A这棵树是否满足SBTree的平衡性，实际上是对A的所有孩子B、C逐一判断(也有可能只有一个孩子)。
// B是F、G的叔叔，只要以B为根的树的结点总数 >= 以F为根的树 && >= 以G为根的树(即叔叔的结点数要不小于侄子)
// C也要不少于自己的侄子D、E的结点数，这样以A为根的树就符合SBTree定义的平衡性。
// 若A存在LL型违规，说的是：D的结点数 > C的结点数    则做一次右旋，该操作和AVL树一模一样，并返回新头
// 若A存在LR型违规，说的是：E的结点数 > C的结点数    则先做一次左旋，再做一次右旋，该操作和AVL树一模一样，并返回新头
// 若A存在RR型违规，说的是：G的结点数 > B的结点数    则做一次左旋，该操作和AVL树一模一样，并返回新头
// 若A存在RL型违规，说的是：F的结点数 > B的结点数    则先做一次右旋，再做一次左旋，该操作和AVL树一模一样，并返回新头
// 做完调整后，以新头为根，遍历树，看有哪个结点的左右孩子是在调整完之后发生改变了，那就对这个结点调用和A一样的判断是否不平衡的方法
// SBTree在删除结点时是不会检查树的平衡性的，只会在加结点的时候考虑
public class SBTree {
    public static class SBTNode<K extends Comparable<K>, V> {
        public K key;
        public V val;
        public SBTNode<K, V> left;
        public SBTNode<K, V> right;
        public int size;  // 以该结点为根有多少个结点

        public SBTNode(K key, V val) {
            this.key = key;
            this.val = val;
            size = 1;
        }
    }

    public static class SizeBalancedTree<K extends Comparable<K>, V>{
        private SBTNode<K, V> root;

        private SBTNode<K, V> rightRotate(SBTNode<K, V> cur){
            SBTNode<K, V> left = cur.left;
            cur.left = left.right;
            left.right = cur;
            // 调整完后，结点数量也要重新调整
            left.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) + (cur.right == null ? 0 : cur.right.size) + 1;
            return left;
        }

        private SBTNode<K, V> leftRotate(SBTNode<K, V> cur){
            SBTNode<K, V> right = cur.right;
            cur.right = right.left;
            right.left = cur;
            right.size = cur.size;
            cur.size = (cur.left == null ? 0 : cur.left.size) + (cur.right == null ? 0 : cur.right.size) + 1;
            return right;
        }

        private SBTNode<K, V> keepBalanced(SBTNode<K, V> cur){
            if (cur == null)
                return null;
            int leftS = cur.left == null ? 0 : cur.left.size;
            int rightS = cur.right == null ? 0 : cur.right.size;
            int leftLeftS = cur.left != null && cur.left.left != null ? cur.left.left.size : 0;
            int leftRightS = cur.left != null && cur.left.right != null ? cur.left.right.size : 0;
            int rightLeftS = cur.right != null && cur.right.left != null ? cur.right.left.size : 0;
            int rightRightS = cur.right != null && cur.right.right != null ? cur.right.right.size : 0;
            // LL型不平衡
            if (leftLeftS > rightS){
                cur = rightRotate(cur);
                // 右旋过后，其左右孩子有变化的只有可能是cur和cur.right
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            } else if (leftRightS > rightS) { // LR型不平衡
                cur.left = leftRotate(cur.left);
                cur = rightRotate(cur);
                // LR型不平衡经调整后，其左右孩子有变化的只有可能是cur和cur.right、cur.left
                cur.right = keepBalanced(cur.right);
                cur.left = keepBalanced(cur.left);
                cur = keepBalanced(cur);
            } else if (rightRightS > leftS) { // RR型不平衡
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur = keepBalanced(cur);
            } else if (rightLeftS > leftS) {  // RL型不平衡
                cur.right = rightRotate(cur.right);
                cur = leftRotate(cur);
                cur.left = keepBalanced(cur.left);
                cur.right = keepBalanced(cur.right);
                cur = keepBalanced(cur);
            }
            return cur;
        }

        // 现在，以cur为头的树上，新增加(key, value )这样的记录，加完之后，会对cur做检查,该调整调整
        // 返回，调整完之后，整棵树的新头部
        private SBTNode<K, V> add(SBTNode<K, V> cur, K key, V val){
            if (cur == null)
                return new SBTNode<>(key, val);
            cur.size++;
            if (key.compareTo(cur.key) < 0)
                cur.left = add(cur.left, key, val);
            else
                cur.right = add(cur.right, key, val);
            return keepBalanced(cur);
        }

        // 现在，以cur为头的树上要删除key这个结点，删除后并返回新的头
        private SBTNode<K, V> delete(SBTNode<K, V> cur, K key){
            cur.size--;
            if (key.compareTo(cur.key) < 0)
                cur.left = delete(cur.left, key);
            else if (key.compareTo(cur.key) > 0)
                cur.right = delete(cur.right, key);
            else { // 说明要删除的就是cur结点
                if (cur.left == null && cur.right == null)
                    cur = null;
                else if (cur.left != null && cur.right == null)
                    cur = cur.left;
                else if (cur.left == null && cur.right != null)
                    cur =cur.right;
                else {  // 左右子树都有，选择用右子树中最小的值代替cur
                    SBTNode<K, V> des = cur.right, pre = null;
                    des.size--;
                    while (des.left != null){
                        pre = des;
                        des = des.left;
                        des.size--;
                    }
                    // 此时des来到了右子树最左的结点，pre是des的父结点
                    if (pre != null){
                        pre.left = des.right;
                        des.right = cur.right;
                    }
                    des.left = cur.left;
                    des.size = des.left.size + (des.right == null ? 0 : des.right.size) + 1;
                    cur =des;
                }
            }
            // cur = keepBalanced(cur)  这句可以不要，因为SBTree默认是在删除时不调整的
            return cur;
        }
    }
}
