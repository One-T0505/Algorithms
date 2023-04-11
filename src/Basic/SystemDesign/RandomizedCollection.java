package Basic.SystemDesign;

import java.util.*;

/**
 * ymy
 * 2023/3/30 - 21 : 24
 **/

// leetCode381  这道题目是 RandomizedSet 的进阶版。本题允许重复值
// RandomizedCollection 是一种包含数字集合(可能是重复的)的数据结构。它应该支持插入和删除特定元素，以及删除随机元素。
// 实现 RandomizedCollection 类:
//  1.RandomizedCollection() 初始化空的 RandomizedCollection 对象。
//  2.bool insert(int val) 将一个 val 项插入到集合中，即使该项已经存在。如果该项不存在，则返回 true，
//    否则返回 false 。
//  3.bool remove(int val) 如果存在，从集合中移除一个 val 项。如果该项存在，则返回 true ，否则返回 false。
//    注意，如果 val 在集合中出现多次，我们只删除其中一个。
//  4.int getRandom() 从当前的多个元素集合中返回一个随机元素。每个元素被返回的概率与集合中包含的相同值的数量线性相关。
// 您必须实现类的函数，使每个函数的 平均 时间复杂度为 O(1) 。
// 注意：生成测试用例时，只有在 RandomizedCollection 中 至少有一项 时，才会调用 getRandom 。


public class RandomizedCollection {

    private ArrayList<Integer> indexToVal;
    private HashMap<Integer, HashSet<Integer>> valToIndex;


    public RandomizedCollection() {
        indexToVal = new ArrayList<>();
        valToIndex = new HashMap<>();
    }


    public boolean insert(int val) {
        indexToVal.add(val);
        boolean res = !valToIndex.containsKey(val);
        // 即 不存在 val
        if (res)
            valToIndex.put(val, new HashSet<>());
        valToIndex.get(val).add(indexToVal.size() - 1);
        return res;
    }


    public boolean remove(int val) {
        if (valToIndex.containsKey(val)){
            Iterator<Integer> it = valToIndex.get(val).iterator();
            // pos 就是我们决定的要删除的val的位置
            int pos = it.next();
            // lastNum 就是最后一个加入的元素
            int lastNum = indexToVal.get(indexToVal.size() - 1);
            // 下面这三步是必然会执行的
            indexToVal.set(pos, lastNum); // 让最后一个元素去覆盖即将要被删除的位置的值
            valToIndex.get(val).remove(pos); // 删除val出现的位置pos
            // lastNum本来是在最后一个位置的，现在lastNum已经移到pos处了，所以lastNum所有出现的位置里应该删除这个位置
            valToIndex.get(lastNum).remove(indexToVal.size() - 1);

            // 如果我们要删除的位置不是最后一个元素，因为最后一个元素很特殊，要用它去填补。如果我们删除的本身不是最后的
            // 这样才可以在lastNum出现的位置里添加pos，因为lastNum移动到了pos处。但是如果 pos就是最后的位置
            // 那么这个地方最后是切实地应该会被删除的，所以不能添加新位置。
            if (pos < indexToVal.size() - 1)
                valToIndex.get(lastNum).add(pos);
            if (valToIndex.get(val).size() == 0)
                valToIndex.remove(val);
            indexToVal.remove(indexToVal.size() - 1);
            return true;
        }
        return false;
    }



    public int getRandom() {
        return indexToVal.get((int) (Math.random() * indexToVal.size()));
    }


    public static void main(String[] args) {
        RandomizedCollection rc = new RandomizedCollection();
        System.out.println(rc.insert(1));
        System.out.println(rc.remove(1));
        System.out.println(rc.insert(1));
        System.out.println(rc.getRandom());
    }
}
