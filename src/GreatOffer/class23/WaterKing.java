package GreatOffer.class23;


// 超级水王问题
// 给定一个数组，如果数组中有某个值出现的次数超过了数组个数的一半，返回。要求：时间复杂度O(N) 空间复杂度O(1)

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WaterKing {

    // 空间复杂度要求O(1)，那就封死了使用哈希表的方法。正确解法是这样的：
    // 在原数组中，每次删除两个不同的数，如果有水王数，那么剩下来的就是水王数；但是如果数组中本来就没有水王数，那么
    // 用这种方法剩下来的数就不是水王数。比如：arr=[1,2,3,4,5]，删除1，2，再删除3，4，剩下来的5不是水王数。
    // 所以最后剩下来的那个数，还需要再遍历一遍数组，看看它出现的次数。
    // 现在的问题就是如何在不修改arr的基础上，空间复杂度为O(1)，每次删除两个不同的数。
    // 这个方法是整个问题最精华的部分，只需要使用两个临时变量就可以做到。
    // 申明两个变量candidate和times，一开始两个变量都初始化为0，当times==0的时候，candidate无论是多少都没意义。
    // 现在开始遍历数组：
    //  1.如果此时times==0，那么将candidate设置为当前元素的值，并将times设置为1；
    //  2.如果times!=0，那么candidate是有效的，比较当前元素是否和candidate相等
    //    1.相等：那么candidate不变，times++
    //    2.不等：那么times--
    // 遍历完数组后，如果times不等于0，那么对应的candidate就是剩下来的值，如果times==0，那就说明没剩下值
    // 其实candidate就相当于靶子，times就相当于还有几个靶子需要消灭。

    public static void solution1(int[] arr) {
        if (arr == null || arr.length < 2) {
            System.out.println("不存在水王数");
            return;
        }
        if (arr.length == 2) {
            System.out.println(arr[0] == arr[1] ? "水王数：" + arr[0] : "不存在水王数");
            return;
        }
        int N = arr.length;
        int candidate = 0, times = 0;
        for (int elem : arr) {
            if (times == 0) {
                candidate = elem;
                times = 1;
            } else if (candidate == elem)
                times++;
            else
                times--;
        }
        if (times == 0) {
            System.out.println("不存在水王数");
            return;
        }
        // 用times当作计数器
        times = 0;
        for (int elem : arr) {
            if (elem == candidate)
                times++;
        }
        if (times > (N >> 1))
            System.out.println("水王数是：" + candidate);
        else
            System.out.println("不存在水王数");
    }
    // ===========================================================================================


    // 扩展1 :给定一个正数k，返回所有出现次数 > N/k的数   k==2的时候就是水王数问题
    // 出现次数>N/k的数最多只有k-1个，如果k个数出现次数都大于N/k，那总次数就超过N了。
    // 思路是一样的，只不过这次需要每次删除k个不相同的数。找一张表，里面只能存放k-1个记录，当空间固定后，
    // 空间复杂度仍然是O(1).

    public static List<Integer> solution2(int[] arr, int k) {
        if (arr == null || k < 2 || arr.length < 2)
            return null;
        int N = arr.length;
        // key 数值  value 次数
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int elem : arr) {
            if (map.containsKey(elem))
                map.put(elem, map.get(elem) + 1);
                // 不包含当前元素，但是还有容量可以放
            else if (map.size() < k - 1)
                map.put(elem, 1);
            else {  // 说明此时map中有k-1个不同值了，加上当前元素，就是k个不同值
                // 那就让当前map的每个key的次数都减1，如果减完之后为0，就移除；那么当前元素也不用进map了
                // 不可以像下面这样，在迭代器中一边遍历，一边删除
                // for (int key : map.keySet()){
                //    if (map.get(key) == 1)
                //        map.remove(key);
                //    else
                //        map.put(key, map.get(key) - 1);
                // }
                ArrayList<Integer> removeList = new ArrayList<>();
                for (int key : map.keySet()) {
                    if (map.get(key) == 1)
                        removeList.add(key);
                    else
                        map.put(key, map.get(key) - 1);
                }
                for (int key : removeList)
                    map.remove(key);
            }
        }
        // 先将所有的value设为0，用作计数器
        map.replaceAll((key, v) -> 0);
        // 将map中剩余的key每个都拿去原始数组中验证真实出现的次数
        for (int elem : arr) {
            if (map.containsKey(elem))
                map.put(elem, map.get(elem) + 1);
        }
        ArrayList<Integer> res = new ArrayList<>();
        for (int key : map.keySet()) {
            if (map.get(key) > (N / k))
                res.add(key);
        }
        return res;
    }

}
