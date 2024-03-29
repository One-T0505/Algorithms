package Basic.UnionFind;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

// 并查集
// 有若干个样本a、b、c、d... 假设类型都是V，并查集一开始认为每个样本都在单独的集合里，并查集提供两个方法：
// 1> boolean isSameSet(V x, V y) 查询样本 x 和 y 是否属于同一个集合
// 2> void union(V x, V y) 把 x 和 y 各自所在集合的样本全都合并成一个集合
// 要求这两个方法的代价尽可能低
public class UnionFind<V> {
    public static class Node<V> {
        public V val;
        public Node(V val) { this.val = val;}
    }

    public HashMap<V, Node<V>> nodes;  // 值->结点
    public HashMap<Node<V>, Node<V>> parents;  // 结点->其父结点
    public HashMap<Node<V>, Integer> sizes;    // 结点->该结点所在集合的元素个数, 只有各个集合的根结点才会记录

    public UnionFind(List<V> values) {
        nodes = new HashMap<>();
        parents = new HashMap<>();
        sizes = new HashMap<>();
        for (V v : values) {
            Node<V> node = new Node<>(v);
            nodes.put(v, node);
            parents.put(node, node);
            sizes.put(node, 1);
        }
    }

    public boolean isSameSet(V a, V b){
        if (!nodes.containsKey(a) || !nodes.containsKey(b))
            return false;
        return findRoot(nodes.get(a)) == findRoot(nodes.get(b));
    }

    public void union(V a, V b){
        if (!nodes.containsKey(a) || !nodes.containsKey(b))
            return;
        if (!isSameSet(a, b)){
            Node<V> aRoot = findRoot(nodes.get(a));
            Node<V> bRoot = findRoot(nodes.get(b));
            int aSize = sizes.get(aRoot);
            int bSize = sizes.get(bRoot);
            Node<V> big = aSize >= bSize ? aRoot : bRoot;
            Node<V> small = big == aRoot ? bRoot : aRoot;
            parents.put(small, big);
            sizes.put(big, aSize + bSize);
            sizes.remove(small);
        }
    }


    // 从当前node找到该结点所在集合的根，并且沿路将node到root之间各级父结点都直接指向root
    private Node<V> findRoot(Node<V> node){
        Stack<Node<V>> stack = new Stack<>();
        // 只有各个集合的根结点才会是：自己 == 自己的父结点
        while (node != parents.get(node)){
            stack.push(node);
            node = parents.get(node);
        }
        // 当跳出上面的循环后， node就指向了当前集合的根结点
        while (!stack.isEmpty())
            parents.put(stack.pop(), node); // 让沿路上的那些结点的parent直接指向所在集合的根，这样起到了优化的作用
        return node;
    }
}
