package Basic.Hash;

import java.util.HashMap;

public class Hash<K> {
    private HashMap<K, Integer> KeyIntMap;
    private HashMap<Integer, K> IntKeyMap;
    private int size;

    public Hash(){
        IntKeyMap = new HashMap<>();
        KeyIntMap = new HashMap<>();
        size = 0;
    }

    public void insert(K key){
        if (!KeyIntMap.containsKey(key)){
            KeyIntMap.put(key, size);
            IntKeyMap.put(size++, key);
        }
    }

    // 删除后index就不连续了，那么随机的时候有很多数都不是连着的了。所以每次用最后一个填补要删除的那个
    public void delete(K key){
        if (KeyIntMap.containsKey(key)){
            // 获得要删除的key的index
            int index = KeyIntMap.get(key);
            // 获得当前表中最后一个index
            int lastIndex = --size;
            // 获得当前表中最后一个index对应的key
            K tail = IntKeyMap.get(lastIndex);

            // 填补
            KeyIntMap.put(tail, index);
            IntKeyMap.put(index, tail);

            // 删除最后的
            IntKeyMap.remove(lastIndex);
            KeyIntMap.remove(key);
        }
    }

    public K getRandom(){
        if (size == 0)
            return null;
        int randomIndex = (int) (Math.random() * size);
        return IntKeyMap.get(randomIndex);
    }
}
