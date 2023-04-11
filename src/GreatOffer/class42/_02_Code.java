package GreatOffer.class42;

import utils.arrays;

import java.util.Arrays;
import java.util.HashMap;

// 给你一个有序的原始数组ori = [1, 2, 4]
// 将原始数据保留，并将每个原始数据扩大2倍加入新数组就变成了：[1, 2, 4, 2, 4, 8]
// 再将其乱序后就是加工数组post  比如：[4, 4, 2, 1, 8, 2]
// 现在给你一个加工数组post，请返回其原始数组。
// 如果给的是:[4, 4, 2, 1, 8, 2]   那么应该返回 [1, 2, 4]

public class _02_Code {

    // 先将加工数组排序，然后从右往左遍历。准备一个哈希表。来到当前元素arr[i]，如果哈希表不存在arr[i]/2，
    // 那么就在哈希表存一条记录：(arr[i]/2, 1)  表示之后要找到1个arr[i]来配arr[i]，arr[i]必然是原数组中
    // 的某个数*2。
    // 1 2 2 7 7 8   2 4 4 14 14 16     加工数组排序后的结果：1 2 2 2 4 4 7 7 8 14 14 16
    // 来到9位置的14时，哈希表的记录是：(8, 1个)  (7, 1个)  发现表里没14，说明14不是需要的原数组元素，而是被扩充
    // 的元素，且表里有7，那么直接将数量+1   哈希表的记录是：(8, 1个)  (7, 2个)
    // 来到8时，发现表里直接有8，就是说找到了我们需要的原数组元素，所以就把把填入原数组元素中，当然也是从原数组最后
    // 开始填。并将表里记录的数量-1，如果减为0了，就删除记录。

    public static int[] restore(int[] post) {
        if (post == null || post.length < 2 || (post.length & 1) == 1)
            return null;
        int N = post.length >> 1;
        int[] ori = new int[N];
        Arrays.sort(post);
        int index = N - 1;
        HashMap<Integer, Integer> dp = new HashMap<>();
        for (int i = post.length - 1; i >= 0; i--) {
            if (dp.isEmpty() || !dp.containsKey(post[i]))
                dp.put(post[i] >> 1, dp.getOrDefault(post[i] >> 1, 0) + 1);
            else {
                ori[index--] = post[i];
                if (dp.get(post[i]) == 1)
                    dp.remove(post[i]);
                else
                    dp.put(post[i], dp.get(post[i]) - 1);
            }
        }
        return ori;
    }


    public static void main(String[] args) {
        int[] arr = {14, 7, 2, 8, 16, 4, 1, 14, 2, 7, 2, 4};
        int[] ori = restore(arr);
        arrays.printArray(ori);
    }

}
