package Tree;

public class AVLNode<K, V> {
    public K key;
    public V val;
    public AVLNode<K, V> left;
    public AVLNode<K, V> right;
    public int h; // 以当前结点为根的树的高度

    public AVLNode(K key, V val) {
        this.key = key;
        this.val = val;
    }
}
