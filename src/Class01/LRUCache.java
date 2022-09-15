package Class01;

import java.util.HashMap;


// 设计LRU缓存结构，该结构在构造时确定大小，假设大小为K，并有如下两个功能。
//  set (key, va lue) :将记录(key, value)插入该结构。
//  get (key) :返回key对应的va lue值。
// [要求]
//  1. set和get方法的时间复杂度为0(1)
//  2.某个key的set或get操作一旦发生， 认为这个key的记录成了最常使用的
//  3.当缓存的大小超过K时，移除最不经常使用的记录，即set或get最久远的
// [举例]
//  假设缓存结构的实例是cache，大小为3，并依次发生如下行为
//  1. cache. set("A", 1)。最常使用的记录为("A", 1)
//  2. cache. set("B", 2)。最常使用的记录为("B",2)，("A", 1)变为最不常使用的
//  3. cache. set("C", 3)。最常使用的记录为("C", 2)，("A", 1)还是最不常使用的
//  4. cache. get("A")。最常使用的记录为("A",1)，("B", 2)变为最不常使用的
//  5. cache. set("D", 4)。大小超过了3，所以移除此时最不常使用的记录("B", 2)，加入记录
//     ("D",4)，并且为最常使用的记录，然后("C",2)变为最不常使用的记录。

public class LRUCache<K, V> {
    // [思路]
    // LRU中封装两个数据结构：HashMap、双向链表，双向链表取代了时间这个概念，每次操作了某个结点后就让它移动到链表尾部，所以当
    // 需要替换时只需要固定替换头部元素即可

    private HashMap<K, Node<K, V>> map;
    private DoubleLinkedList<K, V> list;
    private int capacity;

    public LRUCache(int capacity) {
        if (capacity < 1)
            throw new RuntimeException("should be more than 0.");
        this.map = new HashMap<>();
        this.list = new DoubleLinkedList<>();
        this.capacity = capacity;
    }

    // 访问一下，就将该结点移到链表最后
    public V get(K key){
        if (this.map.containsKey(key)){
            Node<K, V> node = this.map.get(key);
            this.list.toTail(node);
            return node.value;
        }
        return null;
    }

    public void set(K key, V value){
        if (this.map.containsKey(key)){
            Node<K, V> node = this.map.get(key);
            node.value = value;  // 更新后就不用map.put来更新了,因为本来就是内存地址直接修改了
            this.list.toTail(node);
        }else {
            Node<K, V> item = new Node<>(key, value);
            this.map.put(key, item);
            this.list.add(item);
            if (this.map.size() == this.capacity + 1){
                this.removeMostUnusedCache();
            }
        }
    }

    private void removeMostUnusedCache() {
        Node<K, V> item = this.list.remove();
        this.map.remove(item.key);
    }
}

class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> pre;
    public Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

class DoubleLinkedList<K, V> {
    private Node<K, V> head;
    private Node<K, V> tail;

    public DoubleLinkedList() {
        this.head = null;
        this.tail = null;
    }

    // 新加入一个结点，放到尾部
    public void add(Node<K, V> item){
        if (item == null)
            return;
        if (this.head == null){ // 说明是第一个加入的结点
            this.head = item;
            this.tail = item;
        }else {
            this.tail.next = item;
            item.pre = this.tail;
            this.tail = item;
        }
    }

    // 从头部删除一个结点
    public Node<K, V> remove(){
        if (this.head == null)
            return null;
        Node<K, V> res = this.head;
        if (this.head == this.tail){ // 链表中只有一个结点
            this.head = null;
            this.tail = null;
        }else {
            this.head = res.next;
            res.next = null;
            this.head.pre = null;
        }
        return res;
    }

    // 将这个node移动到链表的尾部。说明链表中一定存在这个node，将node分理出，所以要重建前后环境
    public void toTail(Node<K, V> node){
        if (this.tail == node)
            return;
        if (this.head == node){ // 如果node是头部结点
            this.head = node.next;
            this.head.pre = null;
        }else {
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        node.pre = this.tail;
        node.next = null;
        this.tail.next = node;
        this.tail = node;
    }
}
