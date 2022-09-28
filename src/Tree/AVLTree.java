package Tree;

// 平衡二叉树
public class AVLTree<K extends Comparable<K>, V> {
    private AVLTNode<K, V> root;
    private int size;

    public AVLTree() {
        root = null;
        size = 0;
    }

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

    // 在平衡二叉树中插入新结点，cur表示当前到的结点
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
            return keepBalance(cur);
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
            else if (cur.left == null && cur.right != null) // 仅有右子树，直接让右子树接替
                cur = cur.left;
            else if (cur.left != null && cur.right == null) // 仅有左子树，直接让左子树接替
                cur = cur.right;
            else {
                // 有左右子树，那就用右子树中最小的结点替换.此时就变成了删除右子树中最小结点了
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
        return keepBalance(cur);
    }


    // 引起不平衡的原因有四种：LL、LR、RR、RL。
    // 1.LL型不平衡需要在最小不平衡树上做一次右旋
    // 2.LR型不平衡需要在最小不平衡树的左子树上先做一次左旋，再在最小不平衡树上做一次右旋
    // 3.RR型不平衡需要在最小不平衡树上做一次左旋
    // 4.RL型不平衡需要在最小不平衡树的右子树上先做一次右旋，再在最小不平衡树上做一次左旋
    private AVLTNode<K, V> keepBalance(AVLTNode<K, V> cur) {
        if (cur == null)
            return null;
        int leftH = cur.left == null ? 0 : cur.left.h;
        int rightH = cur.right == null ? 0 : cur.right.h;
        if (Math.abs(leftH - rightH) > 1){ // 如果不平衡
            if (leftH > rightH){ // 左子树引起的不平衡
                int leftLeftH = cur.left != null && cur.left.left != null ? cur.left.left.h : 0;
                int leftRightH = cur.left != null && cur.left.right != null ? cur.left.right.h : 0;
                if (leftLeftH >= leftRightH) // 如果有一种不平衡，既可以看作是LL型，也可以是LR型，那就按LL型来处理，这是一种技巧
                    cur =rightRotate(cur); // LL型只需要右旋一次即可
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
}

class AVLTNode<K, V> {
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
