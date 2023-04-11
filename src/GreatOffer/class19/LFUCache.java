package GreatOffer.class19;

import java.util.HashMap;

// leetCode460
// 请你为最不经常使用（LFU）缓存算法设计并实现数据结构。
// 实现 LFUCache 类：
//  1. LFUCache(int capacity) 用数据结构的容量 capacity 初始化对象
//  2. int get(int key) - 如果键 key 存在于缓存中，则获取键的值，否则返回 -1 。
//  3. void put(int key, int value) - 如果键 key 已存在，则变更其值；如果键不存在，请插入键值对。
//     当缓存达到其容量 capacity 时，则应该在插入新项之前，移除最不经常使用的项。在此问题中，当存在平局
//     （即两个或更多个键具有相同使用频率）时，应该去除最近最久未使用的键。
//     为了确定最不常使用的键，可以为缓存中的每个键维护一个频率计数器。使用计数最小的键是最久未使用的键。
// 当一个键首次插入到缓存中时，它的使用计数器被设置为 1 (由于 put 操作)。对缓存中的键执行 get 或 put 操作，
// 频率计数器的值将会递增。
// 函数 get 和 put 必须以 O(1) 的平均时间复杂度运行。

public class LFUCache {

    // 思路：LFU缓存的思想就是：当要替换记录时应选择使用次数最少的记录，如果有多条记录都是使用次数最少的，
    //      那就选择最长时间没使用的，即最早使用的。新加入的结点频次为1，即便桶里记录的最低频次大于1，那也必须
    //      将桶里的结点踢出去，这是规定。
    //      因为时间复杂度要求O(1)，所以需要用到二维双向链表来实现。宏观上来看是一个双向链表，并且有head和
    //      tail指向头和尾。每一个结点也是一个双向链表，可以把它当作一个桶。该结点里存放的都是使用次数相同的
    //      记录，按照最后一次使用的时间从头到尾相连，也就是说顶层双向链表每个结点内部的头结点就是使用次数相同
    //      的一组记录里最久没使用的。所以顶层双向链表的第一个结点内部的第一个结点就是每次被替换的对象。
    //
    // 操作上需要注意：每次使用一个记录后，其频率值就会+1，那么就应该把该记录移动到下一个桶中，如果下一个桶不存在还需要
    // 新生成一个桶；桶内需要断链，并且如果该桶已经空了，那还需要在顶层链表中断链。


    // 最小的数据结构，用于封装记录的结点
    public static class Node {
        public int key;
        public int val;
        public int F;   // 使用次数
        public Node pre;
        public Node next;

        public Node(int key, int val, int f) {
            this.key = key;
            this.val = val;
            F = f;
        }
    }
    // ===============================================================================================


    // 桶结构
    // 一个桶内都是频率相同的结点，并且头的使用是离当前时刻最近的，也就是说如果要从当前桶中删除一个结点，那应该删除尾部
    public static class LinkedList {
        public Node head;
        public Node tail;
        public LinkedList pre; // 上一个桶
        public LinkedList next; // 下一个桶

        // 当需要构造这个桶时，那说明一定是这个桶有存在的必要了，那必然是至少有一个结点需要存到当前桶里
        public LinkedList(Node n) {
            head = n;
            tail = n;
        }


        // 把一个新结点插入到桶的头结点
        public void addFirst(Node node) {
            // 只要这个桶存在，那最少有一个结点
            node.next = head;
            head.pre = node;
            head = node;
        }

        // 判断当前桶是否为空
        public boolean isEmpty() {
            return head == null;
        }


        // 删除node节点并保证node的上下环境重新连接
        public void remove(Node node) {
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                if (head == node) {
                    head = node.next;
                    head.pre = null;
                } else if (tail == node) {
                    tail = tail.pre;
                    tail.next = null;
                } else {
                    node.pre.next = node.next;
                    node.next.pre = node.pre;
                }
            }
            node.pre = null;
            node.next = null;
        }
    }
    // ==============================================================================================


    private final int capacity;
    private HashMap<Integer, Node> map;
    private HashMap<Node, LinkedList> heads;   // 记录某个频率值是由哪个内部链表记录的
    private LinkedList headList;   // 指向最左边的桶
    private int size; // 缓存目前存了多少个记录

    public LFUCache(int capacity) {
        map = new HashMap<>();
        heads = new HashMap<>();
        this.capacity = capacity;
        size = 0;
        headList = null;
    }


    public int get(int key) {
        if (!map.containsKey(key))
            return -1;
        Node res = map.get(key);
        res.F++;
        LinkedList curList = heads.get(res);
        move(res, curList);
        return res.val;
    }


    public void put(int key, int value) {
        if (capacity == 0)
            return;
        if (map.containsKey(key)) {
            Node tar = map.get(key);
            tar.val = value;
            tar.F++;
            LinkedList curList = heads.get(tar);
            move(tar, curList);
        } else {
            if (size == capacity) {
                Node node = headList.tail;
                headList.remove(node);
                isNull(headList);
                map.remove(node.key);
                heads.remove(node);
                size--;
            }
            Node cur = new Node(key, value, 1);
            if (headList == null)
                headList = new LinkedList(cur);
            else {
                if (headList.head.F == cur.F)
                    headList.addFirst(cur);
                    // 最左边的桶是现存所有桶中频率最低的，如果cur.F != headList.F，那么后面的桶的频率必然
                    // 都比cur.F大，所以需要申请一个新的桶放在headList的左侧，让他做新的headList
                else {
                    LinkedList newList = new LinkedList(cur);
                    newList.next = headList;
                    headList.pre = newList;
                    headList = newList;
                }
            }
            map.put(key, cur);
            heads.put(cur, headList);
            size++;
        }
    }


    // node当前在oldList中，并且node.F已经+1了，不再适合这个桶了，需要把它移动到下一个桶中
    // 把node从oldList删掉，然后放到次数+1的桶中  这个桶有可能不存在，还需要建立
    // 整个过程既要保证桶之间仍然是双向链表，也要保证节点之间仍然是双向链表
    private void move(Node node, LinkedList oldList) {
        oldList.remove(node);
        // preList表示次数+1的桶的前一个桶是谁
        // 如果oldList删掉node之后还有节点，oldList就是次数+1的桶的前一个桶
        // 如果oldList删掉node之后空了，oldList是需要删除的，所以次数+1的桶的前一个桶，是oldList的前一个

        // isNull 方法判断当前链表 oldList 如果为空，他就会做出相应的调整，它会让重新调整其他的链表的指向，让他们
        // 都正常连接，但是没有修改 oldList 的指向。也就是说，其他的链表已经找不到 oldList 了，但 oldList 还能
        // 找到他们
        LinkedList preList = isNull(oldList) ? oldList.pre : oldList;
        // nextList表示次数+1的桶的后一个桶是谁
        LinkedList nextList = oldList.next;
        if (nextList == null) {
            LinkedList newList = new LinkedList(node);
            if (preList != null)
                preList.next = newList;
            newList.pre = preList;
            if (headList == null)
                headList = newList;
            heads.put(node, newList);
        } else {
            if (nextList.head.F == node.F) {
                nextList.addFirst(node);
                heads.put(node, nextList);
            } else {
                LinkedList newList = new LinkedList(node);
                if (preList != null)
                    preList.next = newList;
                newList.pre = preList;
                newList.next = nextList;
                nextList.pre = newList;
                if (headList == nextList)
                    headList = newList;
                heads.put(node, newList);
            }
        }
    }


    // removeList：刚刚减少了一个节点的桶
    // 这个函数的功能是，判断刚刚减少了一个节点的桶是不是已经空了。
    // 1）如果不空，什么也不做
    //
    // 2)如果空了，removeList还是整个缓存结构最左的桶(headList)。
    // 删掉这个桶的同时也要让最左的桶变成removeList的下一个。
    //
    // 3)如果空了，removeList不是整个缓存结构最左的桶(headList)。
    // 把这个桶删除，并保证上一个的桶和下一个桶之间还是双向链表的连接方式
    //
    // 函数的返回值表示刚刚减少了一个节点的桶是不是已经空了，空了返回true；不空返回false
    private boolean isNull(LinkedList removeList) {
        if (removeList.isEmpty()) {
            if (headList == removeList) {
                headList = removeList.next;
                if (headList != null)
                    headList.pre = null;
            } else {
                removeList.pre.next = removeList.next;
                if (removeList.next != null)
                    removeList.next.pre = removeList.pre;
            }
            return true;
        }
        return false;
    }

}
