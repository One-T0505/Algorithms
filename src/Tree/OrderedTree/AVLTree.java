package Tree.OrderedTree;

// 平衡二叉树
// 有序表的key必然得是可排序的，所以要继承Comparable
public class AVLTree<K extends Comparable<K>, V> {

    static class AVLTNode<K, V> {
        public K key;
        public V val;
        public AVLTNode<K, V> left;
        public AVLTNode<K, V> right;
        public int h; // 以当前结点为根的树的高度

        public AVLTNode(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public AVLTNode<K, V> root;
    public int size;

    public AVLTree() {
        root = null;
        size = 0;
    }

    // 公开方法
    public int size(){
        return size;
    }

    public boolean containsKey(K key){
        if (key == null)
            return false;
        AVLTNode<K, V> node = findLastIndex(key);
        return node != null && key.compareTo(node.key) == 0;
    }

    public void put(K key, V val){
        if (key == null)
            return;
        AVLTNode<K, V> node = findLastIndex(key);
        // 如果树中已经有了key，那就覆盖原来的值即可
        if (node != null && key.compareTo(node.key) == 0)
            node.val = val;
        else { // 如果树中没有key
            size++;
            root = add(root, key, val);
        }
    }

    public void remove(K key){
        if (key == null)
            return;
        if (containsKey(key)){
            size--;
            root = delete(root, key);
        }
    }

    public V get(K key){
        if (key == null)
            return null;
        AVLTNode<K, V> node = findLastIndex(key);
        return node != null && key.compareTo(node.key) == 0 ? node.val : null;
    }

    public K firstKey(){
        if (root == null)
            return null;
        AVLTNode<K, V> cur = root;
        while (cur.left != null)
            cur = cur.left;
        return cur.key;
    }

    public K lastKey(){
        if (root == null)
            return null;
        AVLTNode<K, V> cur = root;
        while (cur.right != null)
            cur = cur.right;
        return cur.key;
    }

    public K floorKey(K key) {
        if (key == null)
            return null;
        AVLTNode<K, V> node = findLastNoGreater(key);
        return node == null ? null : node.key;
    }

    public K ceilingKey(K key){
        if (key == null)
            return null;
        AVLTNode<K, V> node = findFirstNoLesser(key);
        return node == null ? null : node.key;

    }

    // ====================================================================================================


    // 私有方法
    // 右旋，并返回新的根结点
    private AVLTNode<K, V> rightRotate(AVLTNode<K, V> cur){
        AVLTNode<K, V> left = cur.left;
        cur.left = left.right;
        left.right = cur;
        cur.h = Math.max(cur.left == null ? 0 : cur.left.h, cur.right == null ? 0 : cur.right.h) + 1;
        left.h = Math.max(left.left.h, left.right.h) + 1;
        return left;
    }

    // 左旋，并返回新的根结点
    private AVLTNode<K, V> leftRotate(AVLTNode<K, V> cur){
        AVLTNode<K, V> right = cur.right;
        cur.right = right.left;
        right.left = cur;
        cur.h = Math.max(cur.left == null ? 0 : cur.left.h, cur.right == null ? 0 : cur.right.h) + 1;
        right.h = Math.max(right.left.h, right.right.h) + 1;
        return right;
    }

    // 在平衡二叉树中以cur为根结点的树中插入新结点，并且插入后检查平衡性，如果违规则需要调整并返回新的头部结点，同时要更新
    // 调整后受影响结点的高度
    private AVLTNode<K, V> add(AVLTNode<K, V> cur, K key, V val){
        if (cur == null)
            return new AVLTNode<>(key, val);
        else {
            if (key.compareTo(cur.key) < 0)
                cur.left = add(cur.left, key, val);
            else
                cur.right = add(cur.right, key, val);
            cur.h = Math.max(cur.left == null ? 0 : cur.left.h, cur.right == null ? 0 : cur.right.h) + 1;
            // 新加结点后，要判断是否还能维持平衡，若平衡被打破了，那是哪种原因造成的，并做相应处理使其维持平衡
            // 在调整过程中可能头部会换成新的结点
            return keepBalanced(cur);
        }
    }

    // 在以cur为根结点的树上，删除key代表的结点，并在删除后检查平衡性，返回新头部
    // 默认：key是存在于这棵树中的
    private AVLTNode<K, V> delete(AVLTNode<K, V> cur, K key){
        if (key.compareTo(cur.key) > 0)
            cur.right = delete(cur.right, key);
        else if (key.compareTo(cur.key) < 0)
            cur.left = delete(cur.left, key);
        else {
            if (cur.left == null && cur.right == null)  // 无左右子树就直接删除
                cur = null;
            else if (cur.left == null) // 仅有右子树，直接让右子树接替
                cur = cur.right;
            else if (cur.right == null) // 仅有左子树，直接让左子树接替
                cur = cur.left;
            else { // 有左右子树，那就用右子树中最小的结点替换.此时就变成了删除右子树中最小结点了
                AVLTNode<K, V> des = cur.right;
                while (des.left != null)
                    des = des.left;
                // 此时des已经来到cur右子树中最小的结点
                cur.right = delete(cur.right, des.key);
                des.left = cur.left;
                des.right = cur.right;
                cur = des;
            }
        }
        if (cur != null)
            cur.h = Math.max(cur.left == null ? 0 : cur.left.h, cur.right == null ? 0 : cur.right.h) + 1;
        return keepBalanced(cur);
    }


    // 引起不平衡的原因有四种：LL、LR、RR、RL。
    // 1.LL型不平衡需要在最小不平衡树上做一次右旋
    // 2.LR型不平衡需要在最小不平衡树的左子树上先做一次左旋，再在最小不平衡树上做一次右旋
    // 3.RR型不平衡需要在最小不平衡树上做一次左旋
    // 4.RL型不平衡需要在最小不平衡树的右子树上先做一次右旋，再在最小不平衡树上做一次左旋
    private AVLTNode<K, V> keepBalanced(AVLTNode<K, V> cur) {
        if (cur == null)
            return null;
        int leftH = cur.left == null ? 0 : cur.left.h;
        int rightH = cur.right == null ? 0 : cur.right.h;
        if (Math.abs(leftH - rightH) > 1){ // 如果不平衡
            if (leftH > rightH){ // 左子树引起的不平衡
                int leftLeftH = cur.left != null && cur.left.left != null ? cur.left.left.h : 0;
                int leftRightH = cur.left != null && cur.left.right != null ? cur.left.right.h : 0;
                if (leftLeftH >= leftRightH) // 如果有一种不平衡，既可以看作是LL型，也可以是LR型，那就按LL型来处理，这是一种技巧
                    cur = rightRotate(cur); // LL型只需要右旋一次即可
                else { // LR型
                    cur.left = leftRotate(cur.left);
                    cur = rightRotate(cur);
                }
            } else { // 右子树引起的不平衡
                int rightLeftH = cur.right != null && cur.right.left != null ? cur.right.left.h : 0;
                int rightRightH = cur.right != null && cur.right.right != null ? cur.right.right.h : 0;
                if (rightRightH >= rightLeftH) // 如果有一种不平衡，既可以看作是RR型，也可以是RL型，那就按RR型来处理，这是一种技巧
                    cur = leftRotate(cur);
                else {
                    cur.right = rightRotate(cur.right);
                    cur = leftRotate(cur);
                }
            }
        }
        return cur;
    }


    // 如果树中有key则返回，如果没有则返回树中离key最近的结点。
    // 如果树中全部大于key，那就返回树中最小的；如果树中有小于key的，那就返回小于key且最近的
    private AVLTNode<K, V> findLastIndex(K key){
        AVLTNode<K, V> pre = root;
        AVLTNode<K, V> cur = root;
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

    // 找到最接近key但是不超过key的结点
    private AVLTNode<K, V> findLastNoGreater(K key){
        if (key == null)
            return null;
        AVLTNode<K, V> res = null;
        AVLTNode<K, V> cur = root;
        while (cur != null){
            if (key.compareTo(cur.key) == 0){
                res = cur;
                break;
            } else if (key.compareTo(cur.key) < 0)
                cur = cur.left;
            else {
                res = cur;
                cur = cur.right;
            }
        }
        return res;
    }

    // 找出最接近key但是不小于key的结点
    private AVLTNode<K, V> findFirstNoLesser(K key){
        if (key == null)
            return null;
        AVLTNode<K, V> res = null;
        AVLTNode<K, V> cur = root;
        while (cur != null){
            if (key.compareTo(cur.key) == 0){
                res = cur;
                break;
            } else if (key.compareTo(cur.key) < 0) {
                res = cur;
                cur = cur.left;
            } else
                cur = cur.right;
        }
        return res;
    }

}

