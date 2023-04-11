package class12;


// 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
// 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
// 比如：nums = [100,4,200,1,3,2]  输出：4      nums = [0,3,7,2,5,8,4,6,0,1]  输出：9

import java.util.HashMap;

public class ContinuousSubSeq {

    // 经典解法是用两张哈希表，一张记录连续区间头信息的head，一张记录连续区间尾信息的tail。接下来举个例子来说明
    // 两张表的用法。假如：nums = [3, 5, 2, 6, 1, 4]。首先来了一个3，在两张表都记录下，并且value设为1，因为以
    // 3作为开头的连续区间的长度只有3自己，同样，以3作为结尾的连续区间的长度只有3自己。下一个5来到时一样。
    // 2来到时，先按照之前的一样，长度都先填为1。然后在head中找是否有以3开头的元素，发现有，于是这两个区间可以合并。
    //
    //     head             tail                     发现以3开头的区间的长度为1，这时就可以把这个长度加到自己身上，
    //  2       2        3        2                  在head中以2开头的连续区间长度就变成了2。并且由head中的
    //  5       1        5        1                  3->1，就说明这个区间的结尾也是3，所以由此找到tail中的
    //                                               3->1，并将其修改成3->2，于是head中的3->1和tail中的
    //                                               2->1就成了无效记录，删除。再去tail中查找是否有以1结尾
    //                                               的区间，发现没有，于是2这个元素就顺利加进来了。后续操作类似。


    // 只用一张哈希表的解法
    public static int longestConsecutiveV2(int[] nums) {
        if (nums == null)
            return 0;
        if (nums.length < 2)
            return nums.length;
        HashMap<Integer, Integer> map = new HashMap<>();
        int res = 0;
        for (int elem : nums) {
            if (!map.containsKey(elem)) {
                map.put(elem, 1);
                // 如果存在elem-1，则返回对应的value，否则返回0
                int preLen = map.getOrDefault(elem - 1, 0);
                int posLen = map.getOrDefault(elem + 1, 0);
                int all = preLen + 1 + posLen;
                map.put(elem - preLen, all);
                map.put(elem + posLen, all);
                res = Math.max(res, all);
            }
        }
        return res;
    }


    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2};
        System.out.println(longestConsecutiveV2(nums));
    }
}
