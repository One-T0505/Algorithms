package Tree.OrderedTree;

import java.util.ArrayList;

// 有序表之跳表SkipList
// 有序表一般都是不支持修改当前结点的key的，因为排序就是按key组织的。通常如果想实现修改操作，那就删除之后再添加。
// 跳表SkipList是通过随机这一十分巧妙的方法，来让所有元素的组织方式彻底和输入顺序解耦。并且从第0层开始，上面的每一层都几乎
// 是下一层元素个数的一半，最底层是包含所有元素真身的。
// 跳表的查询就是从最高层找到不能再继续的时候往下一层跳，逐步往下跳的，所以得名跳表。不难发现跳表的缺点在于空间浪费，有很多
// 指向索引。
public class SkipList<K extends Comparable<K>, V> {

    // 结点类定义
    // ====================================================================================================
    public static class SkipListNode<K extends Comparable<K>, V>{
        public K key;
        public V val;
        // 这个数组就是记录当前结点各个高度的下一个结点；比如当前结点在第3层的下一个结点就是successors.get(3)
        // 这是用数组来模拟多个单链表的
        public ArrayList<SkipListNode<K, V>> successors;

        public SkipListNode(K key, V val) {
            this.key = key;
            this.val = val;
            successors = new ArrayList<>();
        }

        // 遍历的时候，如果是往右遍历到的null(next == null), 遍历结束
        // 头(null), 头节点的null，认为最小
        public boolean isLess(K other){
            return other != null && (key == null || key.compareTo(other) < 0);
        }

        public boolean isEqual(K other){
            return (key == null && other == null) ||
                    (key != null && other != null && key.compareTo(other) == 0);
        }
    }
    // ====================================================================================================


    private static final double PROBABILITY = 0.5;
    public SkipListNode<K, V> head;
    public int size;
    public int maxLevel;

    public SkipList() {
        head = new SkipListNode<>(null, null);
        head.successors.add(null);  // 在最底层第0层上加一个null，其实也可以不写
        size = 0;
        maxLevel = 0;  // 最底层高度对应的索引为0，并不是说还没有高度
    }

    // 公开方法
    // ======================================================================================================
    public boolean containsKey(K key){
        if (key == null)
            return false;
        SkipListNode<K, V> less = mostRightLessInList(key);
        SkipListNode<K, V> next = less.successors.get(0);
        return next != null && next.isEqual(key);
    }

    // 最核心方法   如果存在key那就是设置新值，否则就是添加新结点
    public void put(K key, V val){
        if (key == null)
            return;
        SkipListNode<K, V> less = mostRightLessInList(key);
        SkipListNode<K, V> find = less.successors.get(0);
        if (find != null && find.isEqual(key)) { // 已经存在了
            find.val = val;
        } else { // 没添加过这个key
            size++;
            int newNodeLevel = 0;
            while (Math.random() < PROBABILITY)
                newNodeLevel++;
            // 可能新结点的高度会超过当前最高高度，这时就要让head同样升高
            while (maxLevel < newNodeLevel){
                head.successors.add(null);
                maxLevel++;
            }
            SkipListNode<K, V> node = new SkipListNode<>(key, val);
            // 先统一设置为空
            for (int i = 0; i <= newNodeLevel; i++)
                node.successors.add(null);
            int level = maxLevel;
            SkipListNode<K, V> pre = head;
            while (level >= 0){
                pre = mostRightLessInLevel(key, pre, level);
                // 当level > 新结点高度时 是不需要做任何操作的，只需要找到最右且小的结点
                if (level <= newNodeLevel){
                    node.successors.set(level, pre.successors.get(level));  // 将新结点的值指向下一个
                    pre.successors.set(level, node);  // 让前一个指向新结点
                }
                level--;
            }
        }
    }

    // 删除结点
    public void remove(K key){
        if (containsKey(key)){
            size--;
            int level = maxLevel;
            while (level >= 0){
                SkipListNode<K, V> pre = head;
                pre = mostRightLessInLevel(key, pre, level);
                SkipListNode<K, V> next = pre.successors.get(level);
                // 情况1：在这一层中，pre的下一个就是key
                if (next != null && next.isEqual(key)){
                    // 即使next.successors.get(level) 为null，也是一样的
                    pre.successors.set(level, next.successors.get(level));
                }
                // 情况2：在这一层中，pre的下一个结点的key就大于要删除的key了，就不用操作
                // 如果上面的if执行了，有可能在删除了结点后，该层就没有结点了，所以下面的if就是为了甄别删除后当前层为空的情况
                if (level != 0 && pre == head && pre.successors.get(level) == null){
                    head.successors.remove(level);
                    maxLevel--;
                }
                level--;
            }
        }
    }

    public V get(K key){
        if (key == null)
            return null;
        SkipListNode<K, V> mostRightLess = mostRightLessInList(key);
        SkipListNode<K, V> next = mostRightLess.successors.get(0);
        return next != null && next.isEqual(key) ? next.val : null;
    }


    public K firstKey(){
        return head.successors.get(0) == null ? null : head.successors.get(0).key;
    }

    public K lastKey(){
        int level = maxLevel;
        SkipListNode<K, V> cur = head;
        while (level >= 0){
            SkipListNode<K, V> next = cur.successors.get(level);
            while (next != null){
                cur = next;
                next = next.successors.get(level);
            }
            level--;
        }
        return cur.key;
    }

    public K floorKey(K key){
        if (key == null)
            return null;
        SkipListNode<K, V> less = mostRightLessInList(key);
        SkipListNode<K, V> next = less.successors.get(0);
        return next != null && next.isEqual(key) ? next.key : less.key;
    }

    public K ceilingKey(K key){
        if (key == null)
            return null;
        SkipListNode<K, V> less = mostRightLessInList(key);
        SkipListNode<K, V> next = less.successors.get(0);
        return next != null ? next.key : null;
    }
    // ======================================================================================================


    // 私有方法
    // ======================================================================================================
    // 该方法是在全局范围内找出最接近且小于key的结点
    // 从最高层逐步往下找
    private SkipListNode<K, V> mostRightLessInList(K key){
        if (key == null)
            return null;
        int level = maxLevel;
        SkipListNode<K, V> cur = head;
        while (level >= 0)
            cur = mostRightLessInLevel(key, cur, level--);
        return cur;
    }

    // 在第level层中，目前已经来到了cur结点，想找到当前层中最右的且小于key的结点，并返回
    private SkipListNode<K, V> mostRightLessInLevel(K key, SkipListNode<K, V> cur, int level){
        SkipListNode<K, V> next = cur.successors.get(level);
        while (next != null && next.isLess(key)){
            cur = next;
            next = next.successors.get(level);
        }
        return cur;
    }
    // ======================================================================================================



    // for test
    public static void printAll(SkipList<String, String> sk) {
        for (int i = sk.maxLevel; i >= 0; i--) {
            System.out.print("Level " + i + " : ");
            SkipListNode<String, String> cur = sk.head;
            while (cur.successors.get(i) != null) {
                SkipListNode<String, String> next = cur.successors.get(i);
                System.out.print("(" + next.key + " , " + next.val + ") ");
                cur = next;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipList<String, String> test = new SkipList<>();
        printAll(test);
        System.out.println("======================");
        test.put("A", "10");
        printAll(test);
        System.out.println("======================");
        test.remove("A");
        printAll(test);
        System.out.println("======================");
        test.put("E", "E");
        test.put("B", "B");
        test.put("A", "A");
        test.put("F", "F");
        test.put("C", "C");
        test.put("D", "D");
        printAll(test);
        System.out.println("======================");
        System.out.println(test.containsKey("B"));
        System.out.println(test.containsKey("Z"));
        System.out.println(test.firstKey());
        System.out.println(test.lastKey());
        System.out.println(test.floorKey("D"));
        System.out.println(test.ceilingKey("D"));
        System.out.println("======================");
        test.remove("D");
        printAll(test);
        System.out.println("======================");
        System.out.println(test.floorKey("D"));
        System.out.println(test.ceilingKey("D"));

    }
}
