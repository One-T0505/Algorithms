package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.TreeMap;

// 给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数。
// 注意 这个子序列必须是严格递增的。

public class _0673_LongestIncrementalSubSeq {

    // 这个题目是大厂刷题班第9节的最长递增子序列问题原型的改版。建议先去复习下问题原型。
    // 该方法时间复杂度为O(NlogN)  是最优解
    // 现在回忆一下最长递增子序列问题里使用的ends数组, ends[i]表示遍历过的元素里能找到的，长度为i+1的递增子序列里
    // 最小的结尾元素是多少。并且应该知道：在遍历过程中，可能会对ends[i]处的元素值修改多次，唯一不变的是，每次修改后的
    // 值都比修改前的小。ends[i]可能一开始是7，后来被修改成了5，又被修改成了2，每次修改后的值都比修改前小。
    // 这是这个辅助数组的性质决定的，如果当前元素为8，而ends[i]==6，那么8就会去修改ends[i+1]处的值，不会来改变
    // ends[i].
    //
    // 这道题比问题原型更复杂一些，但是核心思想都差不多。我们需要的辅助元素不再是简单的数组了，而是一个list，
    // 并且该列表里每个元素是一个有序表。这个列表就是增强版的ends数组，list[i]表示长度为i+1的子序列的记录。
    // 原先ends[i]只能记录当前找到的长度为i+1的递增子序列里最小的结尾元素，但是没有办法记录变化过程，而在list中，
    // 每个有序表都会记录其变化过程，通俗来说就是：ends[i]只能记录2，而list[i]可以记录 7->5->2 这个过程。
    // list[3](5, 2) 表示：长度为4的递增子序列中，以>=5为结尾元素的递增子序列有2个。list[i]中每个新加入进去的key
    // 一定是递减的，这和上面讲的ends的性质是一样的。为什么要让value的意义变成>=呢？ 是这样的：
    // 当一个新的数假如是8要加入到list中时，我们需要先在list中每个有序表的最小的key排成的数组里找到>=8的最左边的位置p
    // 那么list[p-1]就是那个firstkey<8的位置，我们需要在该有序表里查找<8的子序列结尾元素一共有多少个，所以我们需要在
    // list[p-1]中将所有<8的key对应的value累加，找<8的，我们可以间接求，知道该有序表所有value的和，然后减去 >=8的，
    // 就是<8的数量了。举例：
    //
    //   list     2      按理说我们应该这样记录，让每条记录表示以当前元素结尾的递增子序列有多少个，那么如果现在来了
    //          1   4    7，我们就应该在list[3]位置新建一个表，因为7的到来可以让1，5这两个记录扩增到长度为4，所以
    //          5   2    需要每次在有序表里遍历，把所有<7的数量都加起来，这样比较慢。
    //          8   5
    //
    // 如果有序表的存储变成这样了：
    //
    //   list    2           这样就变成了累加，(1,11)表示，长度为3的递增子序列中以>=1的元素结尾的有11个，
    //         1   11        所以想找到>7的，只需要找到当前有序表里最小的key对应的value，因为该value表示当前有序表
    //         5    7        的总和，减去>=7的数量即可，而>=7的数量，在有序表里只需调用ceilingKey即可，比上面的遍历
    //         8    5        快很多。
    //
    // 这里还要注意一点：就是在新的表里添加记录时，其value的含义要保持一致，所以我们找到list[2]中<7的数量为6后，
    // 需要把把该信息填入list[3]中，如果list[3]已经有了如下记录的话：
    //
    //   list    3           那么就应该插入一条记录：(7, 6+5) 因为其含义是 >=7的元素有多少个，所以还要加上
    //         8   5         此前有序表里>=7的数量。进入一个有序表里的key必然是比已经存在的key小的，如果当前元素
    //                       是9，那么就应该填入list[4]里了。
    //
    // 经过上面的分析可知：当前元素num如果想要进列表里，需要搞定这个公式：a - b + c
    // a ： 算出当前元素需要插入到list的目标位置的前一个list[pre]有序表中的总数量
    // b ： list[pre]中 >= num 的总数量
    // c ： list[pre + 1]中已经存在的记录的总数量

    public static int findNumberOfLIS(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        int N = nums.length;
        ArrayList<TreeMap<Integer, Integer>> dp = new ArrayList<>();
        int cur = 0;
        int count = 0;  // count就是表示 a - b 的值
        for (int num : nums) {
            // len就是找出的当前元素应该插入到list的哪个元素里
            cur = search(dp, num);
            // 说明当前记录只能在列表的第一个有序表里添加，那么pre就是空的，所以只有自己了，所以设置为1
            // 因为在cur表里，要填入>=num的数量，自己肯定算一个
            if (cur == 0)
                count = 1;
            else {
                TreeMap<Integer, Integer> pre = dp.get(cur - 1);  // cur-1处的有序表才是<num的
                count = pre.firstEntry().getValue() - pre.getOrDefault(pre.ceilingKey(num), 0);
            }
            // 此时已经完成了 count 即：a - b 的值，还剩个 c
            // c 有可能是0，有可能不是0，如果当前有序表是空的，那么c就是0
            if (cur == dp.size()) { // 当前要插入的有序表还没建出来，所以c==0
                dp.add(new TreeMap<>());
                dp.get(cur).put(num, count); // 直接存入count，即：a-b
            } else  // 要插入的位置的有序表已经存在，说明有记录
                dp.get(cur).put(num, dp.get(cur).firstEntry().getValue() + count);
        }
        return dp.get(dp.size() - 1).firstEntry().getValue();
    }


    // 二分查找，查找dp中>=num的最左的位置
    // 假如 num==7，而dp中每个treeMap的最小的key排成的数组是：[2, 3, 5, 7, 8] 那么返回的值应该是3
    // 当dp中每个treeMap的最小的key排成的数组全部比num小的时候，该方法直接返回的是数组长度，这也直接表示
    // 当前元素应该插在长度为多少的记录中
    // 当数组所有元素都>=num时，该方法返回0
    // 所以，该方法的返回结果适用于所有情况，结果直接表示当前元素新繁衍的记录应该放在ArrayList的哪个位置里
    private static int search(ArrayList<TreeMap<Integer, Integer>> dp, int num) {
        int L = 0, R = dp.size() - 1;
        int pos = dp.size();
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (dp.get(mid).firstKey() >= num) {
                pos = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return pos;
    }
}
