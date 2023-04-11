package Basic.SystemDesign;

// leetCode0380
// 实现RandomizedSet 类：
//  1.RandomizedSet() 初始化 RandomizedSet 对象
//  2.bool insert(int val) 当元素 val 不存在时，向集合中插入该项，并返回 true ；否则，返回 false 。
//  3.bool remove(int val) 当元素 val 存在时，从集合中移除该项，并返回 true ；否则，返回 false 。
//  4.int getRandom() 随机返回现有集合中的一项（测试用例保证调用此方法时集合中至少存在一个元素）。每个元素
//    应该有相同的概率被返回。
// 你必须实现类的所有函数，并满足每个函数的 平均 时间复杂度为 O(1) 。

import java.util.HashMap;

public class RandomizedSet {

    // 索引其实就是表示时刻，人为给添加进来的元素添加一个索引，这样在随即返回时
    // 就可以通过该索引来决定随机返回谁
    // 所以，每次添加一个从来没有的新元素时，就会产生两条记录  (v, index)  (index, v)
    private HashMap<Integer, Integer> vToIndex;  // 值->索引
    private HashMap<Integer, Integer> indexToV;  // 索引->值

    public RandomizedSet() {
        vToIndex = new HashMap<>();
        indexToV = new HashMap<>();
    }

    public boolean insert(int val) {
        if (!vToIndex.containsKey(val)) {
            int index = vToIndex.size();
            vToIndex.put(val, index);
            indexToV.put(index, val);
            return true;
        }
        return false;
    }

    // 假如此时，两张哈希表的记录如下：
    //  vToIndex  (3, 0)      indexToV  (0, 3)
    //            (6, 1)                (1, 6)
    //            (4, 2)                (2, 4)
    //            (8, 3)                (3, 8)
    // 此时要删除6这个值，那么每张表里都要删除跟6相关的记录，这样很方便；但是如果在哈希表里直接删除记录，
    // 那么下次随机返回一个数的时候，我们利用索引来随机时，会发现1这个位置是空的，随着删除的元素越多，
    // 空的索引也会越来越多，随机返回失败的概率就越来越高。所以我们要填洞，删除(1, 6)时，直接让最后一个位置的
    // 值8去覆盖1位置的6，然后删除最后一个位置的记录即可。这样，每次删除都只需要删除最后一条记录。
    public boolean remove(int val) {
        if (!vToIndex.containsKey(val))
            return false;
        int N = vToIndex.size();
        int pos = vToIndex.get(val);
        indexToV.put(pos, indexToV.get(N - 1));
        vToIndex.put(indexToV.get(N - 1), pos);
        vToIndex.remove(val);
        indexToV.remove(N - 1);
        return true;
    }

    public int getRandom() {
        int pos = (int) (Math.random() * vToIndex.size());
        return indexToV.get(pos);
    }
}
