package GreatOffer.class19;

import java.util.HashMap;

// leetCode146
// 请你设计并实现一个满足  LRU (最近最少使用) 缓存约束的数据结构。
// 实现 LRUCache 类：
//  1. LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存。
//  2. int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
//  3. void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value ；如果不存在，
//    则向缓存中插入该组 key-value。如果插入操作导致关键字数量超过 capacity ，则应该逐出最久未使用的关键字。
// 函数 get 和 put 必须以 O(1) 的平均时间复杂度运行。

public class LRUCache<K, V> {

    // 该结构需要用到双向链表和哈希表来实现。双向链表中我们让离头部最近的表示最长时间没使用的缓存；也就是说如果要
    // 逐出一个缓存，那就是头部。所以每次对链表中现有的缓存的访问或者修改，都应该让当前结点移动至链表尾部，表示最新
    // 修改的结点。
    // 手动实现双向链表
    public static class Node<K, V> {
        public K key;
        public V val;
        public Node<K, V> pre;
        public Node<K, V> next;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }


    public static class DoubleLinkedList<K, V> {
        private Node<K, V> head;
        private Node<K, V> tail;

        public DoubleLinkedList() {
            head = null;
            tail = null;
        }


        // 现在来了一个新的结点，需要把它挂到尾巴上去
        public void addLast(Node<K, V> node) {
            if (node == null)
                return;
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                node.pre = tail;
                tail = node;
            }
        }


        // 现在要将链表上的一个结点移动到尾部，并且要让所有的链接信息都正确
        // 给的node必然在链表上
        public void moveToLast(Node<K, V> node) {
            if (node == tail)
                return;
            if (node == head) {
                head = node.next;
                head.pre = null;
            } else {
                node.pre.next = node.next;
                node.next.pre = node.pre;
            }
            // 上面的操作只是用来分离node，下面是将node并入尾部的操作
            node.pre = tail;
            node.next = null;
            tail.next = node;
            tail = node;
        }


        // 删除链表头部结点
        public Node<K, V> pollFirst() {
            if (head == null)
                return null;
            Node<K, V> res = head;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.next;
                head.pre = null;
                res.next = null;
            }
            return res;
        }
    }
    // 以上是对自定义双向链表结构的封装，下面才是LRUCache的设计
    // ==============================================================================================

    private final int capacity;
    private HashMap<K, Node<K, V>> map;
    private DoubleLinkedList<K, V> list;


    public LRUCache(int capacity) {
        map = new HashMap<>();
        list = new DoubleLinkedList<>();
        this.capacity = capacity;
    }


    public V get(K key) {
        if (map.containsKey(key)) {
            Node<K, V> res = map.get(key);
            list.moveToLast(res);
            return res.val;
        }
        return null;
    }

    public void put(K key, V val) {
        if (map.containsKey(key)) {
            Node<K, V> res = map.get(key);
            res.val = val;
            list.moveToLast(res);
        } else {
            Node<K, V> res = new Node<>(key, val);
            map.put(key, res);
            list.addLast(res);
            if (map.size() == capacity + 1) // 说明超出限制了
                removeMostUnused();
        }
    }

    private void removeMostUnused() {
        Node<K, V> res = list.pollFirst();
        map.remove(res.key);
    }

}
