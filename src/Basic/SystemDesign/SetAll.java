package Basic.SystemDesign;

// 传统的哈希表有put(key, value)，get(key) 方法，时间复杂度是O(1).
// 现在想新加一个方法setAll(value1),将表中所有的key对应的value全部设置为value1。
// 如何保证put、get、setAll 这三个方法的时间复杂度依然是O(1)

import java.util.HashMap;

public class SetAll {

    // 设计一种结构，在HashMap外面再包一层，并且还要有个记录时间点的变量time,初始为0，并且还有个变量all。
    // 当前时刻存放一个记录(3,6)，就会变成 3->(6, 0)  3就是原来的key，6就是原来的value，0表示在哪个时刻存放的，接下来
    // 再存放几个，5->(2, 1)  4->(1, 2)  7->(5, 3)，此时来到4时刻，想用setAll(9)，想让已经存在的key对应的value
    // 全部设置为9，此时让setAllTime=4，这个就表示何时进行了setAll操作，然后让all=9；这样就隐式地处理好了。比如我要
    // get(5)，发现添加的时刻2 < setAllTime，就说明setAll操作是会影响这个记录的，于是直接返回all；如果根据时间发现
    // 不会受到影响，那就返回真实值。

    public static class Node<V> {
        public V val;
        public long time;

        public Node(V val, long time) {
            this.val = val;
            this.time = time;
        }
    }

    public static class MyMap<K, V> {
        public HashMap<K, Node<V>> map;
        public long time;
        public Node<V> setAll;

        public MyMap() {
            map = new HashMap<>();
            time = 0;
            setAll = new Node<>(null, -1);
        }

        public void put(K key, V val){
            map.put(key, new Node<>(val, time++));
        }

        public V get(K key){
            if (!map.containsKey(key))
                return null;
            if (map.get(key).time > setAll.time)
                return map.get(key).val;
            else
                return setAll.val;
        }

        public void setAll(V val){
            setAll.time = time++;
            setAll.val = val;
        }
    }


    public static void main(String[] args) {
        MyMap<Integer, Integer> map = new MyMap<>();
        map.put(2, 4);
        map.put(5, 1);
        map.put(8, 3);
        System.out.println(map.get(5));
        map.setAll(9);
        System.out.println(map.get(5));

    }
}
