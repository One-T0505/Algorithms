package GreatOffer.class09;

import utils.arrays;

// leetCode300
// 最长递增子序列
// 给一个数组，找出最长的递增子序列。eg：arr=[4, 1, 3, 2, 3, 9, 5, 6]  那么最长递增子序列是：[1  2  3  5  6]
// 长度为5

public class IncrementalSubSeq {

    // 暴力解法：O(N^2)  用动态规划。定义等长数组cahce，cache[i]表示：以arr[i]作为结尾能找到的最长递增子序列
    public static int incrementalSubSeq(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length == 1)
            return 1;
        int res = 1;
        int N = arr.length;
        int[] cache = new int[N];
        cache[0] = 1;
        // 对于每个格子，我们要去寻找它之前所有比他小的元素的cache值
        for (int i = 1; i < N; i++) {
            int max = 0;
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i])
                    max = Math.max(max, cache[j]);
            }
            cache[i] = max == 0 ? 1 : max + 1;   // +1表示自己
            res = Math.max(res, cache[i]);
        }
        return res;
    }
    // =====================================================================================================


    // 优化的方法能达到O(NlogN)，我们获得每个元素之前能找到的最大递增子序列，不能是再通过遍历的方式获得了。
    // 这时我们需要定义一个辅助数组end，和原数组等长。end[i]的含义：目前已经找到的i+1长度的递增子序列的最小
    // 结尾元素。"目前"的意思是说在原数组中已经遍历过的。end初识时都为0，表示没有有效数据。
    // 这个概念一开始很难懂。举个例子：arr[3, 2 ,1, 4, 7, 6]
    // 来到3时，直接让end[0]=3，end[0]的含义是：目前能找到的长度为1的递增子序列的最小结尾。因为3是第一个元素，所以
    // 可以直接填写；下一个来到2，在end数组所有有效数据中用二分查找是否有小于2的，发现此时end中只有end[0]=3是有效
    // 数据，并且大于2，所以2没办法当作别人的结尾，于是只能和end[0]比较，发现2<3,于是将end[0]=2，表示目前能找到的
    // 长度为1的递增子序列的最小结尾是2。下一个来到1，同样地，最后更新end[0]=1;又来到4，发现end[0]<4，于是4可以作
    // 为结尾，于是我们将end[1]=4，这是我们找到的长度为2的递增子序列的最小结尾；来到7时，end中有两个有效数据，找到
    // 小于7的最右侧，发现是end[1]=4，于是7又可以作为结尾了，所以让end[2]=7;又来到6，同样用二分找到小于6的最右侧，
    // 于是来到end[1]=4，发现小于6，6也可以作为结尾，并且end[2]=7，所以可以将其更新为end[2]=6。遍历完整个数组后，
    // end中最后一个有效数据的索引+1就是结果。可以保证的是，ends中的所有有效区域肯定是排序好的，不然没办法用二分查找。
    // 因为ends[i]==k，表示当前能找到的i+1长度的递增子序列中最小结尾为k，这个序列必然是从长度为i的序列中升上来的，为
    // 什么可以升上来？当然是因为k>ends[i-1]的值了

    public static int incrementalSubSeq2(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length == 1)
            return 1;
        int N = arr.length;
        int[] ends = new int[N];
        ends[0] = arr[0];
        int R = 0;    // 记录ends中的有效区域
        int res = 1;
        for (int i = 1; i < N; i++) {
            int pos = mostLeftEag(ends, arr[i], 0, R);
            if (pos == -1) { // 说明ends中所有有效元素都比arr[i]小
                ends[++R] = arr[i];
                res = Math.max(res, R + 1);
            } else // 说明end[pos]>=arr[i] end[pos-1]<arr[i]  此时我们就是要讲arr[i]作为这个end[pos-1]的新结尾
                // 加上arr[i]后，新的长度就应该填在end[pos]处，并且原先这里的值必然>=arr[i] 所以直接覆盖就行，不用比大小
                ends[pos] = arr[i];
        }
        return res;
    }


    // 找到ends中>=target的最左的位置
    private static int mostLeftEag(int[] ends, int target, int l, int r) {
        int index = -1;
        while (l <= r) {
            int mid = l + ((r - l) >> 1);
            if (ends[mid] >= target) {
                index = mid;
                r = mid - 1;
            } else
                l = mid + 1;
        }
        return index;
    }

    // leetCode354  俄罗斯套娃
    // 一定要重视这个问题原型：最长递增子序列问题，他是很多很多问题的原始模型。现在在讲一个扩展问题，俄罗斯套娃问题。
    // 给定一个套娃数组，每个元素都由两个数据长度和高度组成，
    // 比如：[(2, 3), (4, 7), (3, 6), (6, 9), (4, 5), (3, 8), (1, 4), (2, 4), (5, 5)]。
    // 规定任意一个套娃必须高度和长度严格小于另一个套娃才可以被套进去。请找出最多能套几个娃？
    //
    // 思路：首先对其进行二维排序：总体按照长度递增排序，长度相等的娃按照高度递减排序。按照这样的规则整体排完序后，单独把高度
    // 那一维的数据拿出来组成一个数组，该数组中最长递增子序列长度就是最多能套的娃。为什么？
    // 比如上面的数组排完序后是这样的：[(1, 4), (2, 4), (2, 3), (3, 8), (3, 6), (4, 7), (4, 5), (5, 5), (6, 9)]
    // 单独将高度拿出来：[4, 4, 3, 8, 6, 7, 5, 5, 9]，比如说这个8，它的长度是多少已经不重要了，但是我知道的事，长度和我一样
    // 的但是高度不如我的必然在我后面，那么以8作为结尾形成的最长子序列根本不会考虑到6，这样就不会干扰，所以8的左侧只要高度小于
    // 8的必然可以被套进去。所以，长度相同下，高度按照递减排序就是为了不影响形成最长递增子序列。如果6在8的前面，那么8再算
    // 递增子序列时会把6考虑进去，但是6的长度是和8的一样的，是不能被套进去的。
    // ====================================================================================================


    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            int[] arr = arrays.randomNoNegativeArr(20, 50);
            int res1 = incrementalSubSeq(arr);
            int res2 = incrementalSubSeq2(arr);
            if (res1 != res2) {
                System.out.println("Failed");
                System.out.println("暴力解法：" + res1);
                System.out.println("优化解法：" + res2);
                arrays.printArray(arr);
                return;
            }
        }
        System.out.println("AC");
    }
}
